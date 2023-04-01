package com.example.skycheck.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainModel {

    @SerializedName("temp")
    @Expose
    private Double temp;

    @SerializedName("feels_like")
    @Expose
    private Double feelsLike;

    @SerializedName("temp_min")
    @Expose
    private Double tempMin;

    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    @SerializedName("pressure")
    @Expose
    private Integer pressure;

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("sea_level")
    @Expose
    private Integer seaLevel;

    @SerializedName("grnd_level")
    @Expose
    private Integer groundLevel;

    public MainModel(Double temp, Double feelsLike, Double tempMin, Double tempMax, Integer pressure, Integer humidity, Integer seaLevel, Integer groundLevel) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Integer getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(Integer groundLevel) {
        this.groundLevel = groundLevel;
    }
}
