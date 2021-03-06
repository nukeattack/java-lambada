package com.geotest.clients;

import com.geotest.entities.Buoy;
import com.geotest.entities.Location;
import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import searchbox.BulkProcessingListener;
import searchbox.BulkProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 */
public class ElasticManager {
  public static final String INDEX_NAME = "geo-test";
  public static final String INDEX_TYPE = "buoy";
  public static final int POINT_COUNT = 6312;
  BulkProcessor bulkProcessor;
  ElasticQueryManager queryManager;
  private JestClient client;

  public ElasticManager() {
    queryManager = new ElasticQueryManager();
    try {
      queryManager.addQueryTemplate("search_boundbox", "elastic/bound_box_query.json");
      queryManager.addQueryTemplate("search_radius", "elastic/radius_query.json");
    } catch (IOException e) {
    }
    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(new HttpClientConfig.Builder("http://search-srv-posi-esearch-dev-m3argdm4lmerf7kz46n2cru4pq.eu-west-1.es.amazonaws.com").multiThreaded(true)
                                    .maxTotalConnection(20).build());
    this.client = factory.getObject();

  }

  public void fillDatabase() throws IOException {
    bulkProcessor = new BulkProcessor(client, new Gson(), new BulkProcessingListener() {
      @Override
      public void beforeBulk(Bulk request) {
      }

      @Override
      public void afterBulk(Bulk request, JestResult response) {
      }

      @Override
      public void afterBulk(Bulk request, Throwable failure) {
      }
    }).withConcurrentRequests(3).addFlushByVolume(10 * 1024 * 1024).addFlushByCount(500).addFlushByPeriod(5, TimeUnit
        .SECONDS);
    String indexMapping = readStream(ElasticManager.class.getClassLoader().getResourceAsStream("buoi_mapping.json"));
    PutMapping putMapping = new PutMapping.Builder(INDEX_NAME, INDEX_TYPE, indexMapping).build();
    client.execute(new CreateIndex.Builder(INDEX_NAME).build());
    client.execute(putMapping);

    for (int i = 0; i < POINT_COUNT; i++) {
      Buoy buoy = new Buoy(UUID.randomUUID().toString(), new Location(Math.random() * 90.0f, Math.random() * 180.0f));
      bulkProcessor.execute(new Index.Builder(buoy).index(INDEX_NAME).type(INDEX_TYPE).build());
      //      client.execute(new Index.Builder(buoy).index(INDEX_NAME).type(INDEX_TYPE).build());
    }
    //    bulkProcessor.flush();
    //    bulkProcessor.close();
  }

  private String readStream(InputStream is) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line);
      sb.append('\n');
    }
    return sb.toString();
  }

  public JestResult queryRect(Location maxLoc, Location minLoc) throws IOException {
    String query = queryManager.getQuery("search_boundbox", maxLoc.getLat(), maxLoc.getLon(),
                                         minLoc.getLat(), minLoc.getLon());
    System.out.println(query);
    Search search = new Search.Builder(query).addIndex(INDEX_NAME)
        .addType(INDEX_TYPE)
        .build();

    return client.execute(search);
  }

  public JestResult queryRadius(Location loc, Double radiusKm) throws IOException {
    String query = queryManager.getQuery("search_radius", loc.getLat(), loc.getLon(), radiusKm);
    System.out.println(query);
    Search search = new Search.Builder(query).addIndex(INDEX_NAME)
        .addType(INDEX_TYPE)
        .build();

    return client.execute(search);
  }
}
