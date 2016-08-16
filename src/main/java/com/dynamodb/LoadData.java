package com.dynamodb;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LoadData {
  public static void main(String[] args) throws JsonParseException, IOException {
    //AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withEndpoint("http://localhost:8000");
    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_WEST_2);
    DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    Table table = dynamoDB.getTable("SensorData");
    
    System.out.println("Reading movies file");
    JsonParser jsonParser = new JsonFactory().createParser(new File("moviedata.json"));
    JsonNode rootNode = new ObjectMapper().readTree(jsonParser);
    
    Iterator<JsonNode> nodeIterator = rootNode.iterator();
    
    ObjectNode jsonObject;
    
    System.out.println("Writing data into table");
    while(nodeIterator.hasNext()){
      jsonObject = (ObjectNode)nodeIterator.next();
      int year = jsonObject.path("year").asInt();
      String title = jsonObject.path("title").asText();
      try{
        table.putItem(new Item().withPrimaryKey("year", year, "title", title).withJSON("info", jsonObject.path("info").toString()));
        System.out.println("Record added " + jsonObject.toString());
      }catch(Exception e){
        System.err.println("Error, " + e.getMessage());
      }
    }
    jsonParser.close();
    
  }
}
