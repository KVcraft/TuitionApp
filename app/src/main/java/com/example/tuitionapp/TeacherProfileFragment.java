package com.example.tuitionapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class TeacherProfileFragment extends Fragment {

    TextView tvFirstName, tvLastName, tvNIC, tvEmail, tvContactNo, tvAddress, tvCourse;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Bind Views
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvNIC = view.findViewById(R.id.tvNIC);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvContactNo = view.findViewById(R.id.tvContactNo);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvCourse = view.findViewById(R.id.tvCourse);

        // ✅ Get teacher email from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String teacherEmail = prefs.getString("teacher_email", null);

        if (teacherEmail != null) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    DatabaseHelper.TABLE_TEACHERS,
                    null,
                    DatabaseHelper.COLUMN_TEACHER_EMAIL + " = ?",
                    new String[]{teacherEmail},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int teacherId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_LASTNAME));
                String nic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_NIC));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_CONTACT));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ADDRESS));

                // Get courses
                List<String> courseList = dbHelper.getCoursesForTeacher(teacherId);
                String courseString = String.join(", ", courseList);

                // Set data to views
                tvFirstName.setText(firstName);
                tvLastName.setText(lastName);
                tvNIC.setText(nic);
                tvEmail.setText(teacherEmail);
                tvContactNo.setText(contact);
                tvAddress.setText(address);
                tvCourse.setText(courseString);

                Log.d("TeacherProfile", "Loaded teacher: " + teacherEmail);
            } else {
                Log.e("TeacherProfile", "No teacher found with email: " + teacherEmail);
            }

            if (cursor != null) cursor.close();
            db.close();
        } else {
            Log.e("TeacherProfile", "No email found in SharedPreferences");
        }

        return view;
    }
}
