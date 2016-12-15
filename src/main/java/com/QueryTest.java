package com;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.geotest.clients.DymanoManager;
import com.geotest.clients.ElasticManager;
import com.geotest.entities.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cryptobat on 12/15/2016.
 */
public class QueryTest {
  public static void main(String[] args) {
    randomQuery(100);
  }
  public static void randomQuery(int cnt){
    DymanoManager dymanoManager = new DymanoManager();
    ElasticManager elasticManager = new ElasticManager();
    List<List<Map<String, AttributeValue>>> dynamoResults = new LinkedList<>();
    for(int i = 0; i < cnt; i++){
      Location[] rect = getRandomRect();
      dynamoResults.add(dymanoManager.queryRectangle(rect[0], rect[1]));

    }
  }

  public  static Location[] getRandomRect(){
    double longitudeSize = Math.random()*20.0f;
    double latitudeSize = Math.random()*20.0f;
    Location minLoc = new Location(Math.random()*(90.0-latitudeSize), Math.random()*(180-longitudeSize));
    Location maxLoc = new Location(minLoc.getLat()+latitudeSize, minLoc.getLon()+longitudeSize);

    return new Location[]{minLoc, maxLoc};
  }

}
