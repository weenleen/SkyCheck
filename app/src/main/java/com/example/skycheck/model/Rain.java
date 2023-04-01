package com.example.skycheck.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("1h")
    @Expose
    private Double rain1h;

    @SerializedName("3h")
    @Expose
    private Double rain3h;

    public Rain(Double rain1h, Double rain3h) {
        this.rain1h = rain1h;
        this.rain3h = rain3h;
    }

    public Double getRain1h() {
        return rain1h;
    }

    public void setRain1h(Double rain1h) {
        this.rain1h = rain1h;
    }

    public Double getRain3h() {
        return rain3h;
    }

    public void setRain3h(Double rain3h) {
        this.rain3h = rain3h;
    }
}
