package com.dynamodb;

import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meteogroup.observation.shared.DataEvent;
import com.meteogroup.observation.shared.WeatherObservation;

public class TestMessagePusher {
  public static void main(String[] args) {
    AmazonSQS sqs = new AmazonSQSClient();
    sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
    try{
      DataEvent de = generateDataEvent(100);
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(de);
      sqs.sendMessage(new SendMessageRequest("https://sqs.us-west-2.amazonaws.com/261625240164/projection_test", json));
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  private static DataEvent generateDataEvent(int size){
    DataEvent de = new DataEvent("ods");
    for(int i = 0; i < size; i++){
      de.addObservations(generateWeatherObservation());
    }
    return de;
  }
  
  private static WeatherObservation generateWeatherObservation(){
    WeatherObservation result = new WeatherObservation();
    result.setAirTemperatureInCelsius((float)Math.random());
    result.setCloudCoverEffectiveInOctaNow((byte) 0);
    result.setDewPointTemperatureInCelsius((float)Math.random());
    result.setEventId(UUID.randomUUID());
    result.setLatitude((float)Math.random());
    result.setLongitude((float)Math.random());
    result.setPrecipitationAmountInMillimeterFor24Hours((int)(100*Math.random()));
    result.setPrecipitationAmountInMillimeterForPastHour((int)(100*Math.random()));
    result.setPresentWeatherCode(UUID.randomUUID().toString());
    result.setPressureAtSeaLevelInHectoPascal((float)Math.random());
    result.setPressureChangeInHectoPascalForPast3Hours((float)Math.random());
    result.setPressureTendencyCodeForPast3Hours((byte)0);
    result.setRelativeHumidityInPercent(0L);
    result.setSunshineDurationInHoursForPast24Hours(0);
    result.setTemperatureMaxInCelcius(33.6f);
    result.setTemperatureMinInCelcius(0.2f);
    result.setTimeZoneName("das");
    result.setVisibilityInKilometer(0.33f);
    result.setWindDirectionInDegree(2);
    result.setWindSpeedInMetersPerSecond(33.4f);
    result.setWindSpeedMaxGustInKnotsForLastHour(2.2f);
    return result;
  }
}
