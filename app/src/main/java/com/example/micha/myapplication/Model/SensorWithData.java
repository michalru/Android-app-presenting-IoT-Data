package com.example.micha.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
//Klasa dla sensora z danymi
public class SensorWithData implements Parcelable {
    private NodeSubgroup nodeSubgroup;
    private ArrayList<Float> values;
    private ArrayList<Long> timestamps;
    private ArrayList<Float> aggregatedValues;
    private ArrayList<Long> aggregatedTimestamps;

    public SensorWithData(NodeSubgroup nodeSubgroup) {
        this.nodeSubgroup = nodeSubgroup;
        this.timestamps = new ArrayList<>();
        this.values = new ArrayList<>();
        this.aggregatedTimestamps = new ArrayList<>();
        this.aggregatedValues = new ArrayList<>();

    }

    public NodeSubgroup getNodeSubgroup() {
        return nodeSubgroup;
    }
    public void addTimestamp(long timestamp){
        timestamps.add(timestamp);
    }
    public void addValue(float value){
        values.add(value);
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    public ArrayList<Long> getTimestamps() {
        return timestamps;
    }

    public ArrayList<Float> getAggregatedValues() {
        return aggregatedValues;
    }

    public ArrayList<Long> getAggregatedTimestamps() {
        return aggregatedTimestamps;
    }





    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(nodeSubgroup,0);
        out.writeList(values);
        out.writeList(timestamps);
        out.writeList(aggregatedValues);
        out.writeList(aggregatedTimestamps);


    }


    public static final Parcelable.Creator<SensorWithData> CREATOR = new Parcelable.Creator<SensorWithData>() {
        public SensorWithData createFromParcel(Parcel in) {
            return new SensorWithData(in);
        }

        public SensorWithData[] newArray(int size) {
            return new SensorWithData[size];
        }
    };


    private SensorWithData(Parcel in) {
        nodeSubgroup = in.readParcelable(NodeSubgroup.class.getClassLoader());
        values=new ArrayList<>();
        in.readList(values,Float.class.getClassLoader());
        timestamps=new ArrayList<>();
        in.readList(timestamps,Long.class.getClassLoader());
        aggregatedValues =new ArrayList<>();
        in.readList(aggregatedValues,Float.class.getClassLoader());
        aggregatedTimestamps =new ArrayList<>();
        in.readList(aggregatedTimestamps,Long.class.getClassLoader());



    }
}
