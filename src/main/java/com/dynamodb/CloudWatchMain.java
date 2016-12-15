package com.dynamodb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;

import java.util.Arrays;

/**
 *
 */
public class CloudWatchMain {
  public static void main(String[] args) throws InterruptedException {
    AmazonCloudWatchClient client = new AmazonCloudWatchClient().withRegion(Region.getRegion(Regions.EU_WEST_1));
    for(int i = 0; i < 1000; i++){
      PutMetricDataRequest request = new PutMetricDataRequest();
      MetricDatum datum = new MetricDatum();
      datum.setMetricName("test_metric");
      datum.setValue(Math.sin(Math.PI*2*(i/1000.0f)));
      request.setMetricData(Arrays.asList(datum));
      request.setNamespace("test_metrics");
      client.putMetricData(request);
      Thread.sleep(5000);
    }

  }
}
