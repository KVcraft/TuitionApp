package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class TeacherResultsFragment extends Fragment {

    private EditText datePicker, timePicker;

    public TeacherResultsFragment() {
        // Required empty public constructor
    }

    public static TeacherResultsFragment newInstance(String param1, String param2) {
        TeacherResultsFragment fragment = new TeacherResultsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_results, container, false);

        AutoCompleteTextView courseSpinner = view.findViewById(R.id.courseSpinner);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        LinearLayout resultsContainer = view.findViewById(R.id.resultsContainer);
        Button submitButton = view.findViewById(R.id.submitButton);
        Button addRowButton = view.findViewById(R.id.addRowButton);

        // Setup Date Picker
        datePicker.setFocusable(false);
        datePicker.setClickable(true);
        datePicker.setOnClickListener(v -> showDatePickerDialog());

        // Setup Time Picker
        timePicker.setFocusable(false);
        timePicker.setClickable(true);
        timePicker.setOnClickListener(v -> showTimePickerDialog());

        // Add first result row
        addResultRow(resultsContainer, inflater);

        // Add new rows on button click
        addRowButton.setOnClickListener(v -> addResultRow(resultsContainer, inflater));

        // Handle result submission
        submitButton.setOnClickListener(v -> {
            String course = courseSpinner.getText().toString().trim();
            String date = datePicker.getText().toString().trim();
            String time = timePicker.getText().toString().trim();

            if (course.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all course, date, and time fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            int rowsSaved = 0;

            for (int i = 0; i < resultsContainer.getChildCount(); i++) {
                View row = resultsContainer.getChildAt(i);
                EditText studentIdInput = row.findViewById(R.id.studentIdInput);
                EditText resultInput = row.findViewById(R.id.resultInput);

                String studentId = studentIdInput.getText().toString().trim();
                String result = resultInput.getText().toString().trim();

                if (!studentId.isEmpty() && !result.isEmpty()) {
                    dbHelper.addStudentResult(studentId, course, date, time, result);
                    rowsSaved++;
                }
            }

            if (rowsSaved > 0) {
                Toast.makeText(requireContext(), "Saved " + rowsSaved + " result(s)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No valid results to save", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addResultRow(LinearLayout container, LayoutInflater inflater) {
        View row = inflater.inflate(R.layout.result_row_layout, container, false);
        container.addView(row);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    datePicker.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timePicker.setText(formattedTime);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }
}
