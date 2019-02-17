package com.example.micha.myapplication.Pojo;

import java.util.Arrays;
import java.util.List;

public class PojoAllSecondActivity {
    public List<PojoData> pojoDataList;

    public PojoAllSecondActivity(List<PojoData> pojoDataList) {
        this.pojoDataList = pojoDataList;
    }

    public List<PojoData> getPojoDataList() {
        return pojoDataList;
    }

    public void setPojoDataList(List<PojoData> pojoDataList) {
        this.pojoDataList = pojoDataList;
    }
}
