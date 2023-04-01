package com.example.skycheck.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    @Expose
    private Integer id; // weather condition id

    @SerializedName("main")
    @Expose
    private String main; // Group of weather parameters (Rain, Snow, Extreme etc.

    @SerializedName("description") // Weather condition within the group. You can get the output in your language.
    @Expose
    private String desc;

    @SerializedName("icon")
    @Expose
    private String icon; // Weather icon id

    public Weather(Integer id, String main, String desc, String icon) {
        this.id = id;
        this.main = main;
        this.desc = desc;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
