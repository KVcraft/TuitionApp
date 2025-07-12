package com.example.tuitionapp;

public class ViewResultModel {
    private String courseName;
    private String grade;
    private String date;

    public ViewResultModel(String courseName, String grade, String date) {
        this.courseName = courseName;
        this.grade = grade;
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getGrade() {
        return grade;
    }

    public String getDate() {
        return date;
    }
}

