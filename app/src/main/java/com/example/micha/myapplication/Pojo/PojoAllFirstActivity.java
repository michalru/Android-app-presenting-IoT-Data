package com.example.micha.myapplication.Pojo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PojoAllFirstActivity {

    public PojoAllFirstActivity(PojoGroup pojoGroup, PojoSubgroup pojoSubgroup, PojoTime pojoTime, PojoParameter pojoParameter) {
        this.pojoGroup = pojoGroup;
        this.pojoSubgroup = pojoSubgroup;
        this.pojoTime = pojoTime;
        this.pojoParameter = pojoParameter;
    }

    public PojoGroup pojoGroup;
    public PojoSubgroup pojoSubgroup;
    public PojoTime pojoTime;
    public PojoParameter pojoParameter;

    public PojoGroup getPojoGroup() {
        return pojoGroup;
    }

    public void setPojoGroup(PojoGroup pojoGroup) {
        this.pojoGroup = pojoGroup;
    }

    public PojoSubgroup getPojoSubgroup() {
        return pojoSubgroup;
    }

    public void setPojoSubgroup(PojoSubgroup pojoSubgroup) {
        this.pojoSubgroup = pojoSubgroup;
    }

    public PojoTime getPojoTime() {
        return pojoTime;
    }

    public void setPojoTime(PojoTime pojoTime) {
        this.pojoTime = pojoTime;
    }

    public PojoParameter getPojoParameter() {
        return pojoParameter;
    }

    public void setPojoParameter(PojoParameter pojoParameter) {
        this.pojoParameter = pojoParameter;
    }
}
