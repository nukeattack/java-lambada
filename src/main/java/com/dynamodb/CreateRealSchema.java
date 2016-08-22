package com.dynamodb;

import java.util.Arrays;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class CreateRealSchema {
  public static void main(String[] args) {
    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withEndpoint("http://localhost:8000");
//    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Region.getRegion(Regions.US_WEST_2));
    DynamoDB dynamo = new DynamoDB(dynamoClient);

    String tableName = "multipleWeatherParamsByTime";
    try {
      CreateTableRequest  tableDescription = new CreateTableRequest ();
      tableDescription.withProvisionedThroughput(new ProvisionedThroughput(20L,  20L));
      tableDescription.withTableName(tableName);
      tableDescription.withKeySchema(Arrays.asList(
                                                     new KeySchemaElement("StationId", KeyType.HASH),
                                                     new KeySchemaElement("ObservedAtChunkId", KeyType.RANGE)));
      tableDescription.withAttributeDefinitions(Arrays.asList(
                                                              new AttributeDefinition("StationId", ScalarAttributeType.N),
                                                              new AttributeDefinition("ObservedAtChunkId", ScalarAttributeType.S),
                                                              new AttributeDefinition("ObservedAt", ScalarAttributeType.S)));
      
      LocalSecondaryIndex index = new LocalSecondaryIndex();
      index.withIndexName("ObservedAtLSI");
      index.withKeySchema(Arrays.asList(new KeySchemaElement("StationId", KeyType.HASH), 
                                        new KeySchemaElement("ObservedAt", KeyType.RANGE)));
      index.withProjection(new Projection().withNonKeyAttributes("ObservedAtChunkId").withProjectionType("INCLUDE"));
      tableDescription.setLocalSecondaryIndexes(Arrays.asList(index));
      
      dynamo.createTable(tableDescription);

    } catch (Exception e) {
      System.err.println("Unable to create table ");
      e.printStackTrace();
      return;
    }
  }
}
