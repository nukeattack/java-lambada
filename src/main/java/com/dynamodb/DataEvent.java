package com.dynamodb;

import java.util.List;

public final class DataEvent {

  private String supplier;
  private String source;
  private String tag;
  private List<WeatherObservation> observations;

  public DataEvent() {
  }

  public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public List<WeatherObservation> getObservations() {
    return observations;
  }

  public void setObservations(List<WeatherObservation> observations) {
    this.observations = observations;
  }
}