package com.example.micha.myapplication.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoTime {

    @SerializedName("output")
    @Expose
    private List<TimeOutput> timeOutputs = null;

    public List<TimeOutput> getTimeOutputs() {
        return timeOutputs;
    }

    public void setTimeOutputs(List<TimeOutput> timeOutputs) {
        this.timeOutputs = timeOutputs;
    }

}