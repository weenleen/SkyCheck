package com.example.skycheck.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("1h")
    @Expose
    private Double snow1h;

    @SerializedName("3h")
    @Expose
    private Double snow3h;

    public Snow(Double snow1h, Double snow3h) {
        this.snow1h = snow1h;
        this.snow3h = snow3h;
    }

    public Double getSnow1h() {
        return snow1h;
    }

    public void setSnow1h(Double snow1h) {
        this.snow1h = snow1h;
    }

    public Double getSnow3h() {
        return snow3h;
    }

    public void setSnow3h(Double snow3h) {
        this.snow3h = snow3h;
    }
}
