package com.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class TestMessageOver {
  public static void main(String[] args) {
    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_WEST_2);
    DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    Table table = dynamoDB.getTable("multipleWeatherParamsByTime");
    table.putItem(new Item().withNumber("StationId", 11).withString("ObservedAtChunkId", "abc").withNumber("ObservedAt", 123213).withString("newAttr", "new value"));

  }
}
