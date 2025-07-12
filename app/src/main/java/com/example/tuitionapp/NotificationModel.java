package com.example.tuitionapp;

public class NotificationModel {
    public String title;
    public String message;
    public String date;

    public NotificationModel() {
        // Needed for Firebase
    }

    public NotificationModel(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }
}
