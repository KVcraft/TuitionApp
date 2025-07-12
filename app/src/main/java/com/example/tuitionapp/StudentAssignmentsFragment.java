package com.example.tuitionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentAssignmentsFragment extends Fragment {

    private static final int PICK_FILE_CODE = 100;
    private static final String[] ACCEPTED_MIME_TYPES = {
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };

    private EditText editTextStudentId, editTextClass, editTextAssignmentTitle;
    private TextView textSelectedFile;
    private Button buttonAttach, buttonUpload;
    private Uri selectedFileUri;
    private String selectedFileName;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_assignments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        dbHelper = new DatabaseHelper(requireContext());

        setupButtonListeners();
    }

    private void initializeViews(View view) {
        editTextStudentId = view.findViewById(R.id.editTextStudentId);
        editTextClass = view.findViewById(R.id.editTextClass);
        editTextAssignmentTitle = view.findViewById(R.id.editTextAssignmentTitle);
        textSelectedFile = view.findViewById(R.id.textSelectedFile);
        buttonAttach = view.findViewById(R.id.buttonAttachAssignment);
        buttonUpload = view.findViewById(R.id.buttonUploadAssignment);
    }

    private void setupButtonListeners() {
        buttonAttach.setOnClickListener(v -> openFileChooser());

        buttonUpload.setOnClickListener(v -> {
            if (validateInputs()) {
                uploadAssignment();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPTED_MIME_TYPES);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Assignment File"), PICK_FILE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                handleSelectedFile(data.getData());
            }
        }
    }

    private void handleSelectedFile(Uri uri) {
        selectedFileUri = uri;
        selectedFileName = getFileNameFromUri(uri);
        textSelectedFile.setText(selectedFileName);
        textSelectedFile.setTextColor(getResources().getColor(android.R.color.black));
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(editTextStudentId.getText())) {
            showError(editTextStudentId, "Student ID is required");
            return false;
        }

        if (TextUtils.isEmpty(editTextClass.getText())) {
            showError(editTextClass, "Class is required");
            return false;
        }

        if (TextUtils.isEmpty(editTextAssignmentTitle.getText())) {
            showError(editTextAssignmentTitle, "Assignment title is required");
            return false;
        }

        if (selectedFileUri == null) {
            Toast.makeText(getContext(), "Please select a file first", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    private void uploadAssignment() {
        String studentId = editTextStudentId.getText().toString().trim();
        String studentClass = editTextClass.getText().toString().trim();
        String title = editTextAssignmentTitle.getText().toString().trim();
        String submissionDate = getCurrentDateTime();

        // Save to local database
        long result = dbHelper.saveAssignment(
                studentId,
                studentClass,
                title,
                selectedFileName,
                submissionDate,
                selectedFileUri.toString()
        );

        if (result != -1) {
            clearForm();
            Toast.makeText(getContext(), "Assignment submitted successfully!", Toast.LENGTH_SHORT).show();

            // Here you would typically also upload to a server
            // uploadToServer(studentId, studentClass, title, selectedFileUri);
        } else {
            Toast.makeText(getContext(), "Failed to save assignment", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void clearForm() {
        editTextStudentId.setText("");
        editTextClass.setText("");
        editTextAssignmentTitle.setText("");
        textSelectedFile.setText("No file selected");
        textSelectedFile.setTextColor(getResources().getColor(android.R.color.darker_gray));
        selectedFileUri = null;
        selectedFileName = null;
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

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
