package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AttendanceFragment extends Fragment {

    // UI Components
    private TextInputEditText startDateEditText, endDateEditText, examDateEditText;
    private AutoCompleteTextView courseAutoComplete, examCourseAutoComplete;
    private Button generateAttendanceButton, generateResultReportButton;

    // Date related variables
    private Calendar startDateCalendar, endDateCalendar, examDateCalendar;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Make sure this matches your XML filename
        View view = inflater.inflate(R.layout.fragment_admin_generate, container, false);

        initializeViews(view);
        setupDatePickers();
        setupCourseDropdowns();
        setupButtons();

        return view;
    }

    private void initializeViews(View view) {
        // Initialize all view components
        startDateEditText = view.findViewById(R.id.startDate);
        endDateEditText = view.findViewById(R.id.endDate);
        examDateEditText = view.findViewById(R.id.examDate);
        courseAutoComplete = view.findViewById(R.id.course);
        examCourseAutoComplete = view.findViewById(R.id.examCourse);
        generateAttendanceButton = view.findViewById(R.id.buttonGenerateAttendance);
        generateResultReportButton = view.findViewById(R.id.buttonGenerateResultReport);

        // Initialize calendars
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        examDateCalendar = Calendar.getInstance();
    }

    private void setupDatePickers() {
        // Set up date pickers for all date fields
        startDateEditText.setOnClickListener(v -> showDatePicker(startDateEditText, startDateCalendar));
        endDateEditText.setOnClickListener(v -> showDatePicker(endDateEditText, endDateCalendar));
        examDateEditText.setOnClickListener(v -> showDatePicker(examDateEditText, examDateCalendar));
    }

    private void showDatePicker(TextInputEditText editText, Calendar calendar) {
        new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editText.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void setupCourseDropdowns() {
        // No XML resources needed - define courses directly
        String[] courses = {
                "Biology", "Combined Maths", "Chemistry",
                "Physics", "ICT", "Business Studies",
                "Economics", "Accounting", "English", "French", "English Literature"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                courses
        );

        courseAutoComplete.setAdapter(adapter);
        examCourseAutoComplete.setAdapter(adapter);
    }

    private void setupButtons() {
        generateAttendanceButton.setOnClickListener(v -> {
            if (validateAttendanceInputs()) {
                generateAttendanceReport();
            }
        });

        generateResultReportButton.setOnClickListener(v -> {
            if (validateExamInputs()) {
                generateExamReport();
            }
        });
    }

    private boolean validateAttendanceInputs() {
        boolean isValid = true;

        if (startDateEditText.getText().toString().isEmpty()) {
            startDateEditText.setError("Start date is required");
            isValid = false;
        }

        if (endDateEditText.getText().toString().isEmpty()) {
            endDateEditText.setError("End date is required");
            isValid = false;
        }

        if (courseAutoComplete.getText().toString().isEmpty()) {
            courseAutoComplete.setError("Course selection is required");
            isValid = false;
        }

        // Additional validation for date range
        if (isValid && startDateCalendar.after(endDateCalendar)) {
            Toast.makeText(getContext(), "End date must be after start date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private boolean validateExamInputs() {
        boolean isValid = true;

        if (examDateEditText.getText().toString().isEmpty()) {
            examDateEditText.setError("Exam date is required");
            isValid = false;
        }

        if (examCourseAutoComplete.getText().toString().isEmpty()) {
            examCourseAutoComplete.setError("Course selection is required");
            isValid = false;
        }

        return isValid;
    }

    private void generateAttendanceReport() {
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        String course = courseAutoComplete.getText().toString();

        // TODO: Implement actual report generation
        // This could involve:
        // 1. Querying your database
        // 2. Processing the data
        // 3. Generating a PDF/CSV
        // 4. Sharing or saving the report

        Toast.makeText(getContext(),
                "Attendance Report Generated for " + course +
                        " from " + startDate + " to " + endDate,
                Toast.LENGTH_SHORT).show();
    }

    private void generateExamReport() {
        String examDate = examDateEditText.getText().toString();
        String course = examCourseAutoComplete.getText().toString();

        // TODO: Implement actual exam report generation
        Toast.makeText(getContext(),
                "Exam Report Generated for " + course +
                        " on " + examDate,
                Toast.LENGTH_SHORT).show();
    }
}