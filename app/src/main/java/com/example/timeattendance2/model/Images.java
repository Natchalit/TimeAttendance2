package com.example.timeattendance2.model;

import java.io.Serializable;

public class Images implements Serializable {
    private int staffid;
    private String staffName,url;
    private float timeStamp;

    public Images(int staffid, String staffName, String url, float timeStamp) {
        this.staffid = staffid;
        this.staffName = staffName;
        this.url = url;
        this.timeStamp = timeStamp;
    }

    public int getStaffid() {
        return staffid;
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
