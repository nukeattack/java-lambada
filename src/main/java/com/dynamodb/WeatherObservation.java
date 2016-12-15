package com.dynamodb;

public class WeatherObservation {

  private String stationId;
  private String stationName;
  private String identifierType;
  private Integer quality;
  private Long observedAt;
  private Float longitude;
  private Float latitude;
  private String timeZoneName;
  private Float airTemperatureInCelsius;
  private String presentWeatherCode;
  private Float windSpeedInMetersPerSecond;
  private Integer windDirectionInDegree;
  private Float visibilityInMeter;
  private Float pressureAtSeaLevelInHectoPascal;
  private Float pressureChangeInHectoPascalForPast3Hours;
  private Integer relativeHumidityInPercent100Based;
  private Float precipitationAmountInMillimeterForPastHour;
  // TODO 'sun24' in FMDecode
  private Float sunshineDurationInHoursForPast24Hours;
  private Float precipitationAmountInMillimeterFor24Hours;
  private Float temperatureMaxInCelcius;
  private Float temperatureMinInCelcius;
  private Float windSpeedMaxGustInMeterPerSecondForLastHour;
  private Float dewPointTemperatureInCelsius;
  // TODO 'ne' in FMDecode
  private Byte cloudCoverEffectiveInOctaNow;
  private Byte pressureTendencyCodeForPast3Hours;
  private Float pressureReducedToSeaLevelInHectoPascal;
  private Float feelsLikeTemperature;

  public WeatherObservation() {
  }

  public WeatherObservation(WeatherObservation that) {
    this.stationId = that.stationId;
    this.stationName = that.stationName;
    this.identifierType = that.identifierType;
    this.quality = that.quality;
    this.observedAt = that.observedAt;
    this.longitude = that.longitude;
    this.latitude = that.latitude;
    this.timeZoneName = that.timeZoneName;
    this.airTemperatureInCelsius = that.airTemperatureInCelsius;
    this.presentWeatherCode = that.presentWeatherCode;
    this.windSpeedInMetersPerSecond = that.windSpeedInMetersPerSecond;
    this.windDirectionInDegree = that.windDirectionInDegree;
    this.visibilityInMeter = that.visibilityInMeter;
    this.pressureAtSeaLevelInHectoPascal = that.pressureAtSeaLevelInHectoPascal;
    this.pressureChangeInHectoPascalForPast3Hours = that.pressureChangeInHectoPascalForPast3Hours;
    this.relativeHumidityInPercent100Based = that.relativeHumidityInPercent100Based;
    this.precipitationAmountInMillimeterForPastHour = that.precipitationAmountInMillimeterForPastHour;
    this.sunshineDurationInHoursForPast24Hours = that.sunshineDurationInHoursForPast24Hours;
    this.precipitationAmountInMillimeterFor24Hours = that.precipitationAmountInMillimeterFor24Hours;
    this.temperatureMaxInCelcius = that.temperatureMaxInCelcius;
    this.temperatureMinInCelcius = that.temperatureMinInCelcius;
    this.windSpeedMaxGustInMeterPerSecondForLastHour = that.windSpeedMaxGustInMeterPerSecondForLastHour;
    this.dewPointTemperatureInCelsius = that.dewPointTemperatureInCelsius;
    this.cloudCoverEffectiveInOctaNow = that.cloudCoverEffectiveInOctaNow;
    this.pressureTendencyCodeForPast3Hours = that.pressureTendencyCodeForPast3Hours;
    this.pressureReducedToSeaLevelInHectoPascal = that.pressureReducedToSeaLevelInHectoPascal;
    this.feelsLikeTemperature = that.feelsLikeTemperature;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public boolean hasRequired() {
    return this.stationId != null && this.observedAt != null;
  }

  public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public String getIdentifierType() {
    return identifierType;
  }

  public void setIdentifierType(String identifierType) {
    this.identifierType = identifierType;
  }

  public Integer getQuality() {
    return quality;
  }

  public void setQuality(Integer quality) {
    this.quality = quality;
  }

  public Long getObservedAt() {
    return observedAt;
  }

  public void setObservedAt(Long observedAt) {
    this.observedAt = observedAt;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public String getTimeZoneName() {
    return timeZoneName;
  }

  public void setTimeZoneName(String timeZoneName) {
    this.timeZoneName = timeZoneName;
  }

  public Float getAirTemperatureInCelsius() {
    return airTemperatureInCelsius;
  }

  public void setAirTemperatureInCelsius(Float airTemperatureInCelsius) {
    this.airTemperatureInCelsius = airTemperatureInCelsius;
  }

  public String getPresentWeatherCode() {
    return presentWeatherCode;
  }

  public void setPresentWeatherCode(String presentWeatherCode) {
    this.presentWeatherCode = presentWeatherCode;
  }

  public Float getWindSpeedInMetersPerSecond() {
    return windSpeedInMetersPerSecond;
  }

  public Float getPressureReducedToSeaLevelInHectoPascal() {
    return pressureReducedToSeaLevelInHectoPascal;
  }

  public void setPressureReducedToSeaLevelInHectoPascal(Float pressureReducedToSeaLevelInHectoPascal) {
    this.pressureReducedToSeaLevelInHectoPascal = pressureReducedToSeaLevelInHectoPascal;
  }

  public void setWindSpeedInMetersPerSecond(Float windSpeedInMetersPerSecond) {
    this.windSpeedInMetersPerSecond = windSpeedInMetersPerSecond;
  }

  public Integer getWindDirectionInDegree() {
    return windDirectionInDegree;
  }

  public void setWindDirectionInDegree(Integer windDirectionInDegree) {
    this.windDirectionInDegree = windDirectionInDegree;
  }

  public Float getVisibilityInMeter() {
    return visibilityInMeter;
  }

  public void setVisibilityInMeter(Float visibilityInMeter) {
    this.visibilityInMeter = visibilityInMeter;
  }

  public Float getPressureAtSeaLevelInHectoPascal() {
    return pressureAtSeaLevelInHectoPascal;
  }

  public void setPressureAtSeaLevelInHectoPascal(Float pressureAtSeaLevelInHectoPascal) {
    this.pressureAtSeaLevelInHectoPascal = pressureAtSeaLevelInHectoPascal;
  }

  public Float getPressureChangeInHectoPascalForPast3Hours() {
    return pressureChangeInHectoPascalForPast3Hours;
  }

  public void setPressureChangeInHectoPascalForPast3Hours(Float pressureChangeInHectoPascalForPast3Hours) {
    this.pressureChangeInHectoPascalForPast3Hours = pressureChangeInHectoPascalForPast3Hours;
  }

  public Integer getRelativeHumidityInPercent100Based() {
    return relativeHumidityInPercent100Based;
  }

  public void setRelativeHumidityInPercent100Based(Integer relativeHumidityInPercent100Based) {
    this.relativeHumidityInPercent100Based = relativeHumidityInPercent100Based;
  }

  public Float getPrecipitationAmountInMillimeterForPastHour() {
    return precipitationAmountInMillimeterForPastHour;
  }

  public void setPrecipitationAmountInMillimeterForPastHour(Float precipitationAmountInMillimeterForPastHour) {
    this.precipitationAmountInMillimeterForPastHour = precipitationAmountInMillimeterForPastHour;
  }

  public Float getSunshineDurationInHoursForPast24Hours() {
    return sunshineDurationInHoursForPast24Hours;
  }

  public void setSunshineDurationInHoursForPast24Hours(Float sunshineDurationInHoursForPast24Hours) {
    this.sunshineDurationInHoursForPast24Hours = sunshineDurationInHoursForPast24Hours;
  }

  public Float getPrecipitationAmountInMillimeterFor24Hours() {
    return precipitationAmountInMillimeterFor24Hours;
  }

  public void setPrecipitationAmountInMillimeterFor24Hours(Float precipitationAmountInMillimeterFor24Hours) {
    this.precipitationAmountInMillimeterFor24Hours = precipitationAmountInMillimeterFor24Hours;
  }

  public Float getTemperatureMaxInCelcius() {
    return temperatureMaxInCelcius;
  }

  public void setTemperatureMaxInCelcius(Float temperatureMaxInCelcius) {
    this.temperatureMaxInCelcius = temperatureMaxInCelcius;
  }

  public Float getTemperatureMinInCelcius() {
    return temperatureMinInCelcius;
  }

  public void setTemperatureMinInCelcius(Float temperatureMinInCelcius) {
    this.temperatureMinInCelcius = temperatureMinInCelcius;
  }

	public Float getWindSpeedMaxGustInMeterPerSecondForLastHour() {
		return windSpeedMaxGustInMeterPerSecondForLastHour;
	}

	public void setWindSpeedMaxGustInMeterPerSecondForLastHour(Float windSpeedMaxGustInMeterPerSecondForLastHour) {
		this.windSpeedMaxGustInMeterPerSecondForLastHour = windSpeedMaxGustInMeterPerSecondForLastHour;
	}

  public Float getDewPointTemperatureInCelsius() {
    return dewPointTemperatureInCelsius;
  }

  public void setDewPointTemperatureInCelsius(Float dewPointTemperatureInCelsius) {
    this.dewPointTemperatureInCelsius = dewPointTemperatureInCelsius;
  }

  public Byte getCloudCoverEffectiveInOctaNow() {
    return cloudCoverEffectiveInOctaNow;
  }

  public void setCloudCoverEffectiveInOctaNow(Byte cloudCoverEffectiveInOctaNow) {
    this.cloudCoverEffectiveInOctaNow = cloudCoverEffectiveInOctaNow;
  }

  public Byte getPressureTendencyCodeForPast3Hours() {
    return pressureTendencyCodeForPast3Hours;
  }

  public void setPressureTendencyCodeForPast3Hours(Byte pressureTendencyCodeForPast3Hours) {
    this.pressureTendencyCodeForPast3Hours = pressureTendencyCodeForPast3Hours;
  }

  public Float getFeelsLikeTemperature() {
    return feelsLikeTemperature;
  }

  public void setFeelsLikeTemperature(Float feelsLikeTemperature) {
    this.feelsLikeTemperature = feelsLikeTemperature;
  }
}