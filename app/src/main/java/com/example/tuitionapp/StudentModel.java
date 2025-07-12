package com.example.tuitionapp;

public class StudentModel {
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String address;
    private String courses;

    public StudentModel(String firstName, String lastName, String email,
                        String contact, String address, String courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.courses = courses;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getCourses() {
        return courses;
    }
}
