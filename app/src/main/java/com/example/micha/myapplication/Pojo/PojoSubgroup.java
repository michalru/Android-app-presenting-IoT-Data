package com.example.micha.myapplication.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoSubgroup {

    @SerializedName("output")
    @Expose
    private List<SubgroupOutput> subgroupOutputs = null;

    public List<SubgroupOutput> getSubgroupOutputs() {
        return subgroupOutputs;
    }

    public void setSubgroupOutputs(List<SubgroupOutput> subgroupOutputs) {
        this.subgroupOutputs = subgroupOutputs;
    }

}