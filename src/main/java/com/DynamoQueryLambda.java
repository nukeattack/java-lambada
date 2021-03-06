package com;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.geotest.clients.DymanoManager;
import com.geotest.clients.ElasticManager;
import com.geotest.entities.Location;
import io.searchbox.client.JestResult;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cryptobat on 12/16/2016.
 */
public class DynamoQueryLambda implements RequestHandler<Object, Object> {
  public static void randomQuery(int cnt) throws IOException {
    DymanoManager dymanoManager = new DymanoManager();
    ElasticManager elasticManager = new ElasticManager();
    List<List<Map<String, AttributeValue>>> dynamoResults = new LinkedList<>();
    List<Long> dynamoTime = new LinkedList<>();
    List<Long> elasticTime = new LinkedList<>();
    for(int i = 0; i < cnt; i++){
      Location[] rect = getRandomRect();
      long time = System.currentTimeMillis();
      List<Map<String, AttributeValue>>  dynamoResult = dymanoManager.queryRectangle(rect[0], rect[1]);
      System.out.println(dynamoResult);
      dynamoTime.add(System.currentTimeMillis() - time);
      time = System.currentTimeMillis();
      JestResult elasticResult = elasticManager.queryRect(rect[1], rect[0]);
      elasticTime.add(System.currentTimeMillis() - time);
      System.out.println("Dynamo stats : " + dynamoTime);
      System.out.println("Elastic stats : " + elasticTime);
    }
  }

  public static Location[] getRandomRect() {
    double longitudeSize = Math.random() * 20.0f;
    double latitudeSize = Math.random() * 20.0f;
    Location minLoc = new Location(Math.random() * (90.0 - latitudeSize), Math.random() * (180 - longitudeSize));
    Location maxLoc = new Location(minLoc.getLat() + latitudeSize, minLoc.getLon() + longitudeSize);

    return new Location[] {minLoc, maxLoc};
  }

  @Override
  public Object handleRequest(Object input, Context context) {
    try {
      randomQuery(100);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
