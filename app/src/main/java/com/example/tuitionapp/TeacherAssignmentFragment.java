package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TeacherAssignmentFragment extends Fragment {

    private AutoCompleteTextView sessionSpinner;
    private EditText sessionDatePicker, sessionTimePicker, openDateTimePicker, dueDateTimePicker;
    private LinearLayout uploadArea;
    private Button addAssignmentButton, deleteAssignmentButton, viewSubmissionsButton;
    private Calendar sessionDateCalendar, sessionTimeCalendar, openDateTimeCalendar, dueDateTimeCalendar;
    private final int FILE_PICKER_REQUEST_CODE = 1001;
    private Uri selectedFileUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_assignment, container, false);

        // Initialize all views
        initializeViews(view);

        // Setup session spinner
        setupSessionSpinner();

        // Setup date and time pickers
        setupDateTimePickers();

        // Setup file upload area
        setupFileUpload();

        // Setup buttons
        setupButtons();

        return view;
    }

    private void initializeViews(View view) {
        sessionSpinner = view.findViewById(R.id.sessionSpinner);
        sessionDatePicker = view.findViewById(R.id.sessionDatePicker);
        sessionTimePicker = view.findViewById(R.id.sessionTimePicker);
        openDateTimePicker = view.findViewById(R.id.openDateTimePicker);
        dueDateTimePicker = view.findViewById(R.id.dueDateTimePicker);
        uploadArea = view.findViewById(R.id.uploadArea);
        addAssignmentButton = view.findViewById(R.id.addAssignmentButton);
        deleteAssignmentButton = view.findViewById(R.id.deleteAssignmentButton);
        viewSubmissionsButton = view.findViewById(R.id.viewSubmissionsButton);

        // Initialize calendars
        sessionDateCalendar = Calendar.getInstance();
        sessionTimeCalendar = Calendar.getInstance();
        openDateTimeCalendar = Calendar.getInstance();
        dueDateTimeCalendar = Calendar.getInstance();
    }

    private void setupSessionSpinner() {
        // Sample session data - replace with your actual data source
        List<String> sessions = new ArrayList<>();
        sessions.add("Session 1: Introduction");
        sessions.add("Session 2: Basic Concepts");
        sessions.add("Session 3: Advanced Topics");
        sessions.add("Session 4: Final Review");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                sessions
        );

        sessionSpinner.setAdapter(adapter);
    }

    private void setupDateTimePickers() {
        // Session Date Picker
        sessionDatePicker.setOnClickListener(v -> showDatePickerDialog(sessionDatePicker, sessionDateCalendar));

        // Session Time Picker
        sessionTimePicker.setOnClickListener(v -> showTimePickerDialog(sessionTimePicker, sessionTimeCalendar));

        // Open Date & Time Picker
        openDateTimePicker.setOnClickListener(v -> showDateTimePickerDialog(openDateTimePicker, openDateTimeCalendar));

        // Due Date & Time Picker
        dueDateTimePicker.setOnClickListener(v -> showDateTimePickerDialog(dueDateTimePicker, dueDateTimeCalendar));
    }

    private void showDatePickerDialog(EditText editText, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editText.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog(EditText editText, Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    editText.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void showDateTimePickerDialog(EditText editText, Calendar calendar) {
        // First show date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    // Then show time picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                                editText.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupFileUpload() {
        uploadArea.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // All file types
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                Toast.makeText(requireContext(), "File selected: " + selectedFileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupButtons() {
        addAssignmentButton.setOnClickListener(v -> {
            if (validateInputs()) {
                // Here you would typically save the assignment to your database
                Toast.makeText(requireContext(), "Assignment added successfully!", Toast.LENGTH_SHORT).show();
                // Clear form or navigate as needed
            }
        });

        deleteAssignmentButton.setOnClickListener(v -> {
            // Implement delete functionality
            Toast.makeText(requireContext(), "Assignment deleted", Toast.LENGTH_SHORT).show();
        });

        viewSubmissionsButton.setOnClickListener(v -> {
            // Navigate to submissions view
            Toast.makeText(requireContext(), "Viewing submissions", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs() {
        if (sessionSpinner.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select a session", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sessionDatePicker.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select session date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sessionTimePicker.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select session time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (openDateTimePicker.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select open date/time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dueDateTimePicker.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select due date/time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedFileUri == null) {
            Toast.makeText(requireContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Additional validation can be added here (e.g., due date after open date)

        return true;
    }
}