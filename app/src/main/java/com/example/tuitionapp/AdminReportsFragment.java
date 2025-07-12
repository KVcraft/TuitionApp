package com.example.tuitionapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdminReportsFragment extends Fragment {

    private TextView reportsContent;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_reports, container, false);

        // Initialize views and database helper
        reportsContent = view.findViewById(R.id.reportsContent);
        dbHelper = new DatabaseHelper(getContext());

        // Load and display reports
        loadReports();

        return view;
    }

    private void loadReports() {
        // Get all reports from database
        Cursor cursor = dbHelper.getAllReports();

        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder reportsBuilder = new StringBuilder();

            while (cursor.moveToNext()) {
                // Adjust these column names to match your database schema
                @SuppressLint("Range") String reportType = cursor.getString(cursor.getColumnIndex("report_type"));
                @SuppressLint("Range") String dateRange = cursor.getString(cursor.getColumnIndex("date_range"));
                @SuppressLint("Range") String course = cursor.getString(cursor.getColumnIndex("course"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));

                reportsBuilder.append("Report Type: ").append(reportType).append("\n")
                        .append("Course: ").append(course).append("\n")
                        .append("Date Range: ").append(dateRange).append("\n\n")
                        .append(content).append("\n\n")
                        .append("--------------------------------\n\n");
            }

            reportsContent.setText(reportsBuilder.toString());
            cursor.close();
        } else {
            reportsContent.setText("No reports generated yet.");
        }
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}