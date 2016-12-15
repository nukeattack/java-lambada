package com.geotest.clients;

import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.QueryRectangleRequest;
import com.amazonaws.geo.model.QueryRectangleResult;
import com.amazonaws.geo.util.GeoTableUtil;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.geotest.entities.Buoy;
import com.geotest.entities.Location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class DymanoManager {
  public static final String TABLE_NAME = "geo-test";
  private AmazonDynamoDBClient dynamoClient;
  private DynamoDB dynamo;
  private GeoDataManagerConfiguration geoDataManagerConfiguration;
  private GeoDataManager geoDataManager;

  public DymanoManager() {
    this.dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.EU_WEST_1);
    //    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_WEST_2);
    this.dynamo = new DynamoDB(dynamoClient);
    this.geoDataManagerConfiguration = new GeoDataManagerConfiguration(dynamoClient, TABLE_NAME);
    this.geoDataManager = new GeoDataManager(geoDataManagerConfiguration);
  }

  public void fillDatabase() {
    if (isTableCreated()) {
      getTable();
    } else {
      createTable();
    }

    for (int i = 0; i < 1000; i++) {
      Buoy buoy = new Buoy(UUID.randomUUID().toString(), new Location(Math.random() * 90.0f, Math.random() * 180.0f));
      AttributeValue rangeKeyValue = new AttributeValue().withS(buoy.getId());
      GeoPoint geoPoint = new GeoPoint(buoy.getLocation().getLat(), buoy.getLocation().getLon());
      PutPointRequest request = new PutPointRequest(geoPoint, rangeKeyValue);
      geoDataManager.putPoint(request);
    }
  }

  public List<Map<String, AttributeValue>> queryRectangle(Location minLocation, Location maxLocation){
    QueryRectangleRequest query = new QueryRectangleRequest(new GeoPoint(minLocation.getLat(), minLocation.getLon()), new GeoPoint(maxLocation.getLat(), maxLocation.getLon()));
    QueryRectangleResult result = geoDataManager.queryRectangle(query);
    return result.getItem();
  }

  private Table createTable() {
    try {
      CreateTableRequest createTableRequest = GeoTableUtil.getCreateTableRequest(geoDataManagerConfiguration);
      Table table = dynamo.createTable(createTableRequest);
      table.waitForActive();
      return table;
    } catch (Exception e) {
      System.err.println("Unable to create table ");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private Table getTable() {
    return dynamo.getTable(TABLE_NAME);
  }

  private boolean isTableCreated() {
    try {
      dynamoClient.describeTable(TABLE_NAME);
      return true;
    } catch (ResourceNotFoundException e) {
      return false;
    }
  }
}
