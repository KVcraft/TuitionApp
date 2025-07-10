package com.example.tuitionapp;

public class Assignment {
    private String session;
    private String dateTime;
    private String title;
    private String openDate;
    private String dueDate;

    public Assignment(String session, String dateTime, String title, String openDate, String dueDate) {
        this.session = session;
        this.dateTime = dateTime;
        this.title = title;
        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    // Getters and setters
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOpenDate() { return openDate; }
    public void setOpenDate(String openDate) { this.openDate = openDate; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}
