package com;

import com.geotest.clients.ElasticManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 */
public class ElasticTestMain {
  private final static Logger logger = LoggerFactory.getLogger(ElasticTestMain.class);

  public static void main(String[] args) {
    try {
      ElasticManager elasticManager = new ElasticManager();
      elasticManager.fillDatabase();

    } catch (IOException e) {
      logger.error("Error ", e);
      throw new RuntimeException(e);
    }
  }
}
