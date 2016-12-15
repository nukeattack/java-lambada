package com.geotest;

import com.geotest.clients.ElasticManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 */
public class GeoTestFillDataMain {
  private final static Logger logger = LoggerFactory.getLogger(GeoTestFillDataMain.class);

  public static void main(String[] args) {
//    DymanoManager dymanoManager = new DymanoManager();
//    dymanoManager.fillDatabase();
    try {
      ElasticManager elasticManager = new ElasticManager();
      elasticManager.fillDatabase();
    } catch (IOException e) {
      logger.error("Error ", e);
      throw new RuntimeException(e);
    }
  }
}
