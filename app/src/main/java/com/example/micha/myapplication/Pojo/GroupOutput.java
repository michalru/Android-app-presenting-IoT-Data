package com.example.micha.myapplication.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupOutput {


    @SerializedName("loc")
    @Expose
    private String loc;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("id")
    @Expose
    private String id;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

