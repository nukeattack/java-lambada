package com.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.Arrays;

public class CreateTable {
  public static void main(String[] args) {
    AmazonDynamoDBClient dynamoClient =  new AmazonDynamoDBClient().withRegion(Regions.EU_WEST_1);
//    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_WEST_2);
    DynamoDB dynamo = new DynamoDB(dynamoClient);
    
    String tableName = "SensorData";
    try{
      Table table = dynamo.createTable(tableName, 
            Arrays.asList(
            new KeySchemaElement("year", KeyType.HASH), 
            new KeySchemaElement("title", KeyType.RANGE)), 
            Arrays.asList(
                          new AttributeDefinition("year", ScalarAttributeType.N), 
                          new AttributeDefinition("title", ScalarAttributeType.S)), 
            new ProvisionedThroughput(10L,  10L));
    }catch(Exception e){
      System.err.println("Unable to create table ");
      e.printStackTrace();
      return;
    }
    System.out.println("Table created");
  }
}
