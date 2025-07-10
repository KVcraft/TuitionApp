package com.example.tuitionapp;

public class Student {
    private String name;
    private String username;
    private String marks;

    public Student(String name, String username) {
        this.name = name;
        this.username = username;
        this.marks = "";
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
