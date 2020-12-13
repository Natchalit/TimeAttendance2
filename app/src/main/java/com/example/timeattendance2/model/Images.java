package com.example.timeattendance2.model;

import java.io.Serializable;

public class Images implements Serializable {
    private int staffId;
    private String staffName,url;
    private float timeStamp;

    public Images(int staffId, String staffName, String url, float timeStamp) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.url = url;
        this.timeStamp = timeStamp;
    }

    public int getStaffid() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getUrl() {
        return url;
    }

    public float getTimeStamp() {
        return timeStamp;
    }
}
