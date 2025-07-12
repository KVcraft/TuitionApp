package com.example.tuitionapp;

public class AssignmentSubmission {
    private int id;
    private String studentId;
    private String studentClass;
    private String title;
    private String filePath;

    public AssignmentSubmission(int id, String studentId, String studentClass, String title, String filePath) {
        this.id = id;
        this.studentId = studentId;
        this.studentClass = studentClass;
        this.title = title;
        this.filePath = filePath;
    }

    // Getters
    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getStudentClass() { return studentClass; }
    public String getTitle() { return title; }
    public String getFilePath() { return filePath; }
}
