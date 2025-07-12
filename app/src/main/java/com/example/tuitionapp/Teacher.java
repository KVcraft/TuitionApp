package com.example.tuitionapp;

public class Teacher {
    public String firstName;
    public String lastName;
    public String nic;
    public String email;
    public String contact;
    public String address;
    public String photoPathOrBase64; // optional
    public String course; // comma-separated

    public Teacher(String firstName, String lastName, String nic,
                   String email, String contact, String address, String course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.course = course;
    }
}
