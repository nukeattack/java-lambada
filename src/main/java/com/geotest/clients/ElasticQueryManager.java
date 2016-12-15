package com.geotest.clients;

import com.StreamUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticQueryManager {
  Map<String, String> queryTemplates = new HashMap<>();

  public ElasticQueryManager(){
  }

  public void addQueryTemplate(String queryName, String fileName) throws IOException {
    queryTemplates.put(queryName, StreamUtils.readStream(ElasticQueryManager.class.getClassLoader().getResourceAsStream(fileName)));
  }

  public String getQuery(String name, Object...args){
    return String.format(queryTemplates.get(name), args);
  }
}
