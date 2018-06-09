package com.creativitude.jeewa.models;

/**
 * Created by naveen on 09/06/2018.
 */

public class NotificationModel {

    private String message;

    public NotificationModel(String message) {
        this.message = message;
    }

    public NotificationModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
