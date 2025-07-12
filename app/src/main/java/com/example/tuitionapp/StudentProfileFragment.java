package com.example.tuitionapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import java.util.List;

public class StudentProfileFragment extends Fragment {

    TextView tvFirstName, tvLastName, tvEmail, tvContactNo, tvAddress, tvCourse;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        // Bind Views
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvContactNo = view.findViewById(R.id.tvContactNo);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvCourse = view.findViewById(R.id.tvCourse);

        // Get stored email from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String studentEmail = prefs.getString("student_email", null);

        if (studentEmail != null) {
            dbHelper = new DatabaseHelper(requireContext());
            Cursor cursor = (Cursor) dbHelper.getStudentByEmail(studentEmail);

            if (cursor != null && cursor.moveToFirst()) {
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LASTNAME));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS));

                List<String> courseList = dbHelper.getCoursesForStudent(studentId);
                String courseString = String.join(", ", courseList);

                tvFirstName.setText(firstName);
                tvLastName.setText(lastName);
                tvEmail.setText(studentEmail);
                tvContactNo.setText(contact);
                tvAddress.setText(address);
                tvCourse.setText(courseString);

                Log.d("StudentProfile", "Loaded: " + studentEmail);
            } else {
                Log.e("StudentProfile", "Student not found");
            }

            if (cursor != null) cursor.close();
        } else {
            Log.e("StudentProfile", "No student email in session");
        }

        return view;
    }
}
