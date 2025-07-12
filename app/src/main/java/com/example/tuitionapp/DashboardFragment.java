package com.example.tuitionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView tvStudentCount, tvTeacherCount, tvAdminCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Initialize TextViews
        tvStudentCount = view.findViewById(R.id.tvStudentCount);
        tvTeacherCount = view.findViewById(R.id.tvTeacherCount);
        tvAdminCount = view.findViewById(R.id.tvAdminCount);

        // Load counts
        loadCounts();

        return view;
    }

    private void loadCounts() {
        int studentCount = dbHelper.getStudentCount();
        int teacherCount = dbHelper.getTeacherCount();
        int adminCount = dbHelper.getAdminCount();

        tvStudentCount.setText(String.valueOf(studentCount));
        tvTeacherCount.setText(String.valueOf(teacherCount));
        tvAdminCount.setText(String.valueOf(adminCount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}