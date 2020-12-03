package com.example.timeattendance2.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {
    private boolean completed;
    private String token;
    private String error_message;
    private float request_id;
    private Sites sites[];

    public LoginResponse(boolean completed, String token, String error_message, float request_id, Sites[] sites) {
        this.completed = completed;
        this.token = token;
        this.error_message = error_message;
        this.request_id = request_id;
        this.sites = sites;

    }

    public boolean isCompleted() {
        return completed;
    }

    public String getToken() {
        return token;
    }

    public String getError_message() {
        return error_message;
    }

    public float getRequest_id() {
        return request_id;
    }

    public Sites[] getSites() {
        return sites;
    }
}
