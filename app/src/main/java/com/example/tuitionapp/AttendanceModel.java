package com.example.tuitionapp;

public class AttendanceModel {
    private String name;
    private boolean isPresent;

    public AttendanceModel(String name, boolean isPresent) {
        this.name = name;
        this.isPresent = isPresent;
    }

    public String getName() {
        return name;
    }

    public boolean isPresent() {
        return isPresent;
    }
}
