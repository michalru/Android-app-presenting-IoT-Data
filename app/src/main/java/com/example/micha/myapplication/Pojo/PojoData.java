package com.example.micha.myapplication.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoData {

    @SerializedName("output")
    @Expose
    private List<DataOutput> dataOutputs = null;

    public List<DataOutput> getDataOutputs() {
        return dataOutputs;
    }

    public void setOutput(List<DataOutput> dataOutputs) {
        this.dataOutputs = dataOutputs;
    }

}