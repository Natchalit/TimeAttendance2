package com.example.timeattendance2.model;

import java.io.Serializable;

public class Sites implements Serializable {

    private int index;
    private String name;
    private float latitude, longitude;

    public Sites(int index, String name, float latitude, float longitude) {
        this.index = index;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toString(){
        return name;
    }


}
