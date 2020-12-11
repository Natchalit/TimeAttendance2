package com.example.timeattendance2.model;

public class GetImage {
    private boolean completed;
    private String error_message;
    private float request_id;
    private Images[] images;

    public GetImage(boolean completed, String error_message, float request_id, Images[] images) {
        this.completed = completed;
        this.error_message = error_message;
        this.request_id = request_id;
        this.images = images;
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

    public Images[] getImages() {
        return images;
    }
}
