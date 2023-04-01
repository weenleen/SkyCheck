package com.example.skycheck.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("country")
    @Expose
    private String countryCode;

    @SerializedName("sunrise")
    @Expose
    private Long sunriseTime;

    @SerializedName("sunset")
    @Expose
    private Long sunsetTime;

    public Sys(Integer type, Long id, String countryCode, Long sunriseTime, Long sunsetTime) {
        this.type = type;
        this.id = id;
        this.countryCode = countryCode;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public Long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }
}
