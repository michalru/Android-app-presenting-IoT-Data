package com.example.micha.myapplication.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoGroup {

    @SerializedName("output")
    @Expose
    private List<GroupOutput> groupOutputs = null;

    public List<GroupOutput> getGroupOutput() {
        return groupOutputs;
    }

    public void setGroupOutputs(List<GroupOutput> groupOutputs) {
        this.groupOutputs = groupOutputs;
    }

}
