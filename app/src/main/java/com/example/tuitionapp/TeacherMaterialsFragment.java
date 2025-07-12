package com.example.tuitionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TeacherMaterialsFragment extends Fragment {

    private static final int FILE_PICKER_REQUEST_CODE = 1001;

    private AutoCompleteTextView sessionSpinner;
    private TextInputEditText datePicker, timePicker;
    private TextView fileNameText;
    private Button uploadButton, updateButton, deleteButton;
    private LinearLayout uploadBox;

    private Uri selectedFileUri;
    private String selectedFileName;
    private Calendar dateCalendar, timeCalendar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher_materials, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupSessionSpinner();
        setupDateTimePickers();
        setupFileUpload();
        setupButtons();
    }

    private void initializeViews(View view) {
        sessionSpinner = view.findViewById(R.id.sessionSpinner);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        fileNameText = view.findViewById(R.id.fileNameText);
        uploadButton = view.findViewById(R.id.uploadButton);
        updateButton = view.findViewById(R.id.updateButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        uploadBox = view.findViewById(R.id.uploadBox);

        dateCalendar = Calendar.getInstance();
        timeCalendar = Calendar.getInstance();
    }

    private void setupSessionSpinner() {
        // Sample session data - replace with your actual data source
        String[] sessions = new String[]{
                "Session 1: Introduction",
                "Session 2: Basic Concepts",
                "Session 3: Advanced Topics",
                "Session 4: Final Review"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                sessions
        );

        sessionSpinner.setAdapter(adapter);
    }

    private void setupDateTimePickers() {
        // Date Picker
        datePicker.setOnClickListener(v -> showDatePicker());

        // Time Picker
        timePicker.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    dateCalendar.set(year, month, dayOfMonth);
                    updateDateText();
                },
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> {
                    timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    timeCalendar.set(Calendar.MINUTE, minute);
                    updateTimeText();
                },
                timeCalendar.get(Calendar.HOUR_OF_DAY),
                timeCalendar.get(Calendar.MINUTE),
                false
        ).show();
    }

    private void updateDateText() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        datePicker.setText(sdf.format(dateCalendar.getTime()));
    }

    private void updateTimeText() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        timePicker.setText(sdf.format(timeCalendar.getTime()));
    }

    private void setupFileUpload() {
        uploadBox.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // All file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                selectedFileName = getFileNameFromUri(selectedFileUri);
                fileNameText.setText(selectedFileName);
                fileNameText.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void setupButtons() {
        uploadButton.setOnClickListener(v -> {
            if (validateInputs()) {
                uploadMaterial();
            }
        });

        updateButton.setOnClickListener(v -> {
            // Implement update functionality
            Toast.makeText(getContext(), "Update functionality will be implemented", Toast.LENGTH_SHORT).show();
        });

        deleteButton.setOnClickListener(v -> {
            // Implement delete functionality
            Toast.makeText(getContext(), "Delete functionality will be implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs() {
        if (sessionSpinner.getText().toString().isEmpty()) {
            sessionSpinner.setError("Please select a session");
            return false;
        }

        if (datePicker.getText().toString().isEmpty()) {
            datePicker.setError("Please select a date");
            return false;
        }

        if (timePicker.getText().toString().isEmpty()) {
            timePicker.setError("Please select a time");
            return false;
        }

        if (selectedFileUri == null) {
            Toast.makeText(getContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadMaterial() {
        String session = sessionSpinner.getText().toString();
        String date = datePicker.getText().toString();
        String time = timePicker.getText().toString();

        // 1. Upload the file to your server/storage
        // 2. Save the metadata to database
        // 3. Show success message

        Toast.makeText(getContext(),
                "Uploading material for " + session +
                        " on " + date + " at " + time +
                        "\nFile: " + selectedFileName,
                Toast.LENGTH_LONG).show();

        // Reset form after upload
        resetForm();
    }

    private void resetForm() {
        sessionSpinner.setText("");
        datePicker.setText("");
        timePicker.setText("");
        fileNameText.setText("No file selected");
        fileNameText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        selectedFileUri = null;
        selectedFileName = null;
    }
}