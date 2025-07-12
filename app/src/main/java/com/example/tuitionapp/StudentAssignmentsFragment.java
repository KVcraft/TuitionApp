package com.example.tuitionapp;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StudentAssignmentsFragment extends Fragment {

    private static final int PICK_PDF_CODE = 100;

    private EditText editTextStudentId, editTextClass, editTextAssignmentTitle;
    private TextView textSelectedFile;
    private Button buttonAttach, buttonUpload;

    private Uri selectedFileUri;
    private String selectedFileName;

    private DatabaseHelper dbHelper;

    public StudentAssignmentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_assignments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextStudentId = view.findViewById(R.id.editTextStudentId);
        editTextClass = view.findViewById(R.id.editTextClass);
        editTextAssignmentTitle = view.findViewById(R.id.editTextAssignmentTitle);
        textSelectedFile = view.findViewById(R.id.textSelectedFile);
        buttonAttach = view.findViewById(R.id.buttonAttachAssignment);
        buttonUpload = view.findViewById(R.id.buttonUploadAssignment);

        dbHelper = new DatabaseHelper(requireContext());

        buttonAttach.setOnClickListener(v -> openFileChooser());

        buttonUpload.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(getContext(), "Please select a file first", Toast.LENGTH_SHORT).show();
                return;
            }
            saveAssignmentToSQLite();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            selectedFileName = getFileNameFromUri(selectedFileUri);
            textSelectedFile.setText("Selected: " + selectedFileName);
        }
    }

    private void saveAssignmentToSQLite() {
        String studentId = editTextStudentId.getText().toString().trim();
        String studentClass = editTextClass.getText().toString().trim();
        String title = editTextAssignmentTitle.getText().toString().trim();

        if (studentId.isEmpty() || studentClass.isEmpty() || title.isEmpty() || selectedFileName == null) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the assignment metadata (file name only)
        long result = dbHelper.saveAssignment(studentId, studentClass, title, selectedFileName);

        if (result != -1) {
            Toast.makeText(getContext(), "Assignment saved locally", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save assignment", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }
}
