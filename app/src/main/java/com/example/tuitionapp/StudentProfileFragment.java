package com.example.tuitionapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
public class StudentProfileFragment extends Fragment {

    private TextView textFirstName, textLastName, textEmail, textContact, textAddress, textCourses;
    private DatabaseHelper dbHelper;
    private String loggedInEmail = "student@example.com"; // Replace this with real login data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());

        // Bind views
        textFirstName = view.findViewById(R.id.textFirstName);
        textLastName = view.findViewById(R.id.textLastName);
        textEmail = view.findViewById(R.id.textEmail);
        textContact = view.findViewById(R.id.textContact);
        textAddress = view.findViewById(R.id.textAddress);
        textCourses = view.findViewById(R.id.textCourses);

        // Load profile
        loadStudentProfile();

        return view;
    }

    private void loadStudentProfile() {
        Cursor cursor = dbHelper.getStudentByEmail(loggedInEmail);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                textFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                textLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                textEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                textContact.setText(cursor.getString(cursor.getColumnIndexOrThrow("contact")));
                textAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                textCourses.setText(cursor.getString(cursor.getColumnIndexOrThrow("courses")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        } else {
            Log.e("StudentProfile", "No data found for email: " + loggedInEmail);
        }
    }
}
