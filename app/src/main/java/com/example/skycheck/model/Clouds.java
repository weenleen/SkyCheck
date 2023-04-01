package com.example.skycheck.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer cloudinessPercent;

    public Clouds(Integer cloudinessPercent) {
        this.cloudinessPercent = cloudinessPercent;
    }

    public Integer getCloudinessPercent() {
        return cloudinessPercent;
    }

    public void setCloudinessPercent(Integer cloudinessPercent) {
        this.cloudinessPercent = cloudinessPercent;
    }
}
