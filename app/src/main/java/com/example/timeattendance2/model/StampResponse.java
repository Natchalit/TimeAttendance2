package com.example.timeattendance2.model;

public class StampResponse {
    private boolean completed;
    private String error_message;
    private float request_id;

    public StampResponse(boolean completed, String error_message, float request_id) {
        this.completed = completed;
        this.error_message = error_message;
        this.request_id = request_id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getError_message() {
        return error_message;
    }

    public float getRequest_id() {
        return request_id;
    }
}
