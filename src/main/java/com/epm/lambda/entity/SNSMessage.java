package com.epm.lambda.entity;

import java.util.Map;

public class SNSMessage {
  @Override
  public String toString() {
    return "SNSMessage [id=" + id + ", data=" + data + "]";
  }
  private String id;
  private Map<String, String> data;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Map<String, String> getData() {
    return data;
  }
  public void setData(Map<String, String> data) {
    this.data = data;
  }
}
