package com.example.micha.myapplication.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataOutput {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("createdat")
    @Expose
    private String createdat;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

}
