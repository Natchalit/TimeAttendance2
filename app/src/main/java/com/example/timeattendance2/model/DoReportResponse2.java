package com.example.timeattendance2.model;

public class DoReportResponse2 {
    private boolean completed;
    private String error_message;
    private float request_id;
    private String report2_url;

    public DoReportResponse2(boolean completed, String error_message, float request_id, String report2_url) {
        this.completed = completed;
        this.error_message = error_message;
        this.request_id = request_id;
        this.report2_url = report2_url;
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

    public String getReport2_url() {
        return report2_url;
    }
}
