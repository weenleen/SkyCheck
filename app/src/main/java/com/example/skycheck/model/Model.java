package com.example.skycheck.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Model {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("base")
    @Expose
    private String base; // Internal Parameter

    @SerializedName("main")
    @Expose
    private MainModel main;

    @SerializedName("visibility")
    @Expose
    private Long visibility;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("rain")
    @Expose
    private Rain rain;

    @SerializedName("snow")
    @Expose
    private Snow snow;

    @SerializedName("dt")
    @Expose
    private Long timeCalculated; // time of data calculation, UTC

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("timezone")
    @Expose
    private Long timezone; // Shift in seconds from UTC

    @SerializedName("id")
    @Expose
    private Long cityID;

    @SerializedName("name")
    @Expose
    private String cityName;

    @SerializedName("cod")
    @Expose
    private Long cod; // Internal Parameter

    public Model(Coord coord, List<Weather> weather, String base, MainModel main, Long visibility, Wind wind, Clouds clouds, Rain rain, Snow snow, Long timeCalculated, Sys sys, Long timezone, Long cityID, String cityName, Long cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.rain = rain;
        this.snow = snow;
        this.timeCalculated = timeCalculated;
        this.sys = sys;
        this.timezone = timezone;
        this.cityID = cityID;
        this.cityName = cityName;
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainModel getMain() {
        return main;
    }

    public void setMain(MainModel main) {
        this.main = main;
    }

    public Long getVisibility() {
        return visibility;
    }

    public void setVisibility(Long visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public String getTimeCalculated() {
        Date dateTime = new Date(timeCalculated * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        return format.format(dateTime);
    }

    public void setTimeCalculated(Long timeCalculated) {
        this.timeCalculated = timeCalculated;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(Long timezone) {
        this.timezone = timezone;
    }

    public Long getCityID() {
        return cityID;
    }

    public void setCityID(Long cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
