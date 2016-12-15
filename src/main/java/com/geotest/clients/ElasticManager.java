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
  public static final int POINT_COUNT = 10000;

  private JestClient client;
  BulkProcessor bulkProcessor;
  ElasticQueryManager queryManager;

  public ElasticManager() {
    queryManager = new ElasticQueryManager();
    try {
      queryManager.addQueryTemplate("search_boundbox", "elastic/bound_box_query.json");
    } catch (IOException e) {
    }
    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(new HttpClientConfig
        .Builder("http://localhost:9200")
                                    .multiThreaded(true).maxTotalConnection(20)
                                    .build());
    this.client = factory.getObject();
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
    }).withConcurrentRequests(3).addFlushByVolume(10*1024*1024).addFlushByCount(500).addFlushByPeriod(5, TimeUnit.SECONDS);
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

  public void fillDatabase() throws IOException {
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

  public void queryRect(Location maxLoc, Location minLoc) throws IOException {
    Search search = new Search.Builder(queryManager.getQuery("search_boundbox", maxLoc.getLat(), maxLoc.getLon(), minLoc.getLat(), minLoc.getLon()))
        .addIndex(INDEX_NAME)
        .addType(INDEX_TYPE)
        .build();

    client.execute(search);
  }
}
