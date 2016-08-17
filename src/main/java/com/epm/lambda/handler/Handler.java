package com.epm.lambda.handler;

import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.epm.lambda.entity.Response;
import com.epm.lambda.entity.SNSMessage;

public class Handler implements RequestHandler<SNSMessage, Response> {
  public Response handleRequest(SNSMessage input, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log("*** Request handling function body entered. " + input.toString());
    AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_WEST_2);
    DynamoDB dynamoDB = new DynamoDB(dynamoClient);
    Table table = dynamoDB.getTable("sensorevents");
    try {
      table.putItem(new Item().withPrimaryKey("id", UUID.randomUUID().toString()).withMap("info", input.getData()));
    } catch (Exception e) {
      logger.log("ERROR: " + e.getMessage() + e.toString());
      return new Response("Item failed to add", input);
    }
    System.out.println("Writing data into table");
    return new Response("Item added", input);
  }

}
