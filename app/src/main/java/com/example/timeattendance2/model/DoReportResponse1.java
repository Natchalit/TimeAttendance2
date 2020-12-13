package com.example.timeattendance2.model;

public class DoReportResponse1 {

    private boolean completed;
    private String error_message;
    private float request_id;
    private String report1_Url;

    public DoReportResponse1(boolean completed, String error_message, float request_id, String report1_Url) {
        this.completed = completed;
        this.error_message = error_message;
        this.request_id = request_id;
        this.report1_Url = report1_Url;
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

    public String getReport1_url() {
        return report1_Url;
    }
}
