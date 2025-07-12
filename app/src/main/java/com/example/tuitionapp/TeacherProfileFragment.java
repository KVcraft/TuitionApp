package com.example.tuitionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TeacherProfileFragment extends Fragment {

    TextView tvFirstName, tvLastName, tvNIC, tvEmail, tvContactNo, tvAddress, tvCourse;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        // Init DB
        dbHelper = new DatabaseHelper(requireContext());

        // Bind Views
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvNIC = view.findViewById(R.id.tvNIC);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvContactNo = view.findViewById(R.id.tvContactNo);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvCourse = view.findViewById(R.id.tvCourse);

        // Fetch Email from login/session
        String teacherEmail = "teacher1@example.com"; // You should get this from login session

        Teacher teacher = dbHelper.getTeacherByEmail(teacherEmail);
        if (teacher != null) {
            tvFirstName.setText(teacher.firstName);
            tvLastName.setText(teacher.lastName);
            tvNIC.setText(teacher.nic);
            tvEmail.setText(teacher.email);
            tvContactNo.setText(teacher.contact);
            tvAddress.setText(teacher.address);
            tvCourse.setText(teacher.course);
        }

        return view;
    }
}
