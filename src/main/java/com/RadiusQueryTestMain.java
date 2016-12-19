package com;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.geotest.clients.DymanoManager;
import com.geotest.clients.ElasticManager;
import com.geotest.entities.Location;
import io.searchbox.client.JestResult;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 */
public class RadiusQueryTestMain {
  public static void main(String[] args) throws IOException {
    DymanoManager dymanoManager = new DymanoManager();
    ElasticManager elasticManager = new ElasticManager();
    List<List<Map<String, AttributeValue>>> dynamoResults = new LinkedList<>();
    List<Long> dynamoTime = new LinkedList<>();
    List<Long> elasticTime = new LinkedList<>();
    for(int i = 0; i < 100; i++){
      long time = System.currentTimeMillis();
      Location rect = getRandomLoc(50);
      List<Map<String, AttributeValue>> dynamoResult = dymanoManager.queryRadius(rect, 100);
      System.out.println(dynamoResult);
      dynamoTime.add(System.currentTimeMillis() - time);
      System.out.println("Dynamo stats : " + dynamoTime);

      time = System.currentTimeMillis();
      elasticManager.queryRadius(rect, 100.0);
      elasticTime.add(System.currentTimeMillis() - time);
      System.out.println("Elastic stats : " + elasticTime);
      
    }
  }

  public static Location getRandomLoc(double radius) {
    Location minLoc = new Location(Math.random() * (90.0 - radius), Math.random() * (180 - radius));

    return minLoc;
  }
}
