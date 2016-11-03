package com.dynamodb;

import java.util.Arrays;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class CreateRealSchemaMultiple {
	public static final String HASH_KEY_NAME = "StationId";
	public static final String RANGE_KEY_NAME = "ObservedAtChunkId";
	public static final String LSI_RANGE_KEY_NAME = "ObservedAt";
	public static final String LSI_NAME = "ObservedAtLSI";
    private final static ProvisionedThroughput THRUPUT = new ProvisionedThroughput(1L, 2L);
    private final static Projection PROJECTION = new Projection().withProjectionType(ProjectionType.INCLUDE).withNonKeyAttributes("ObservedAtChunkId");
	
	
	
  public static void main(String[] args) {
    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Region.getRegion(Regions.US_WEST_2));
//    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Region.getRegion(Regions.US_WEST_2));
    DynamoDB dynamo = new DynamoDB(dynamoClient);
    String tableName = "multipleWeatherParamsByTime";
    try {
//      CreateTableRequest  tableDescription = new CreateTableRequest ();
//      tableDescription.withProvisionedThroughput(new ProvisionedThroughput(20L,  20L));
//      tableDescription.withTableName(tableName);
//      tableDescription.withKeySchema(Arrays.asList(
//                                                     new KeySchemaElement("StationId", KeyType.HASH),
//                                                     new KeySchemaElement("ObservedAtChunkId", KeyType.RANGE)));
//      tableDescription.withAttributeDefinitions(Arrays.asList(
//                                                              new AttributeDefinition("StationId", ScalarAttributeType.S),
//                                                              new AttributeDefinition("ObservedAtChunkId", ScalarAttributeType.S),
//                                                              new AttributeDefinition("ObservedAt", ScalarAttributeType.S)));
//      
//      LocalSecondaryIndex index = new LocalSecondaryIndex();
//      index.withIndexName("ObservedAtLSI");
//      index.withKeySchema(Arrays.asList(new KeySchemaElement("StationId", KeyType.HASH), 
//                                        new KeySchemaElement("ObservedAt", KeyType.RANGE)));
//      index.withProjection(new Projection().withNonKeyAttributes("ObservedAtChunkId").withProjectionType("INCLUDE"));
//      tableDescription.setLocalSecondaryIndexes(Arrays.asList(index));
      CreateTableRequest req = new CreateTableRequest()
              .withTableName(tableName)
              .withAttributeDefinitions(
                  new AttributeDefinition(HASH_KEY_NAME, ScalarAttributeType.N),
                  new AttributeDefinition(RANGE_KEY_NAME, ScalarAttributeType.S),
                  new AttributeDefinition(LSI_RANGE_KEY_NAME, ScalarAttributeType.N))
              .withKeySchema(
                  new KeySchemaElement(HASH_KEY_NAME, KeyType.HASH),
                  new KeySchemaElement(RANGE_KEY_NAME, KeyType.RANGE))
              .withProvisionedThroughput(THRUPUT)
              .withLocalSecondaryIndexes(
                  new LocalSecondaryIndex()
                      .withIndexName(LSI_NAME)
                      .withKeySchema(
                          new KeySchemaElement(HASH_KEY_NAME, KeyType.HASH),
                          new KeySchemaElement(LSI_RANGE_KEY_NAME, KeyType.RANGE))
                      .withProjection(PROJECTION))
                      ;
      dynamo.createTable(req).waitForActive();
      

    } catch (Exception e) {
      System.err.println("Unable to create table ");
      e.printStackTrace();
      return;
    }
  }
}
