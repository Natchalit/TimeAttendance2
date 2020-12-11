package com.example.timeattendance2.model;

public class DoReportResponse3 {
    private boolean completed;
    private String error_message;
    private float request_id;
    private String report3_url;

    public DoReportResponse3(boolean completed, String error_message, float request_id, String report3_url) {
        this.completed = completed;
        this.error_message = error_message;
        this.request_id = request_id;
        this.report3_url = report3_url;
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

    public String getReport3_url() {
        return report3_url;
    }
}
