package com.example.micha.myapplication.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoParameter {

    @SerializedName("output")
    @Expose
    private List<ParameterOutput> parameterOutputs = null;

    public List<ParameterOutput> getParameterOutputs() {
        return parameterOutputs;
    }

    public void setParameterOutputs(List<ParameterOutput> parameterOutputs) {
        this.parameterOutputs = parameterOutputs;
    }

}