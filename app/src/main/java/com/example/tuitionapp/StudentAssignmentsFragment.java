package com.example.tuitionapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentAssignmentsFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;

    private EditText editTextStudentId, editTextClass, editTextAssignmentTitle;
    private Button buttonAttachAssignment, buttonUploadAssignment;
    private TextView textSelectedFile;

    private Uri selectedFileUri;
    private String selectedFileName = "";

    private DatabaseHelper dbHelper;

    public StudentAssignmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_assignments, container, false);

        // Initialize Views
        editTextStudentId = view.findViewById(R.id.editTextStudentId);
        editTextClass = view.findViewById(R.id.editTextClass);
        editTextAssignmentTitle = view.findViewById(R.id.editTextAssignmentTitle);
        buttonAttachAssignment = view.findViewById(R.id.buttonAttachAssignment);
        buttonUploadAssignment = view.findViewById(R.id.buttonUploadAssignment);
        textSelectedFile = view.findViewById(R.id.textSelectedFile);

        dbHelper = new DatabaseHelper(requireContext());

        // Attach button
        buttonAttachAssignment.setOnClickListener(v -> openFileChooser());

        // Upload button
        buttonUploadAssignment.setOnClickListener(v -> uploadAssignment());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // You can restrict to "application/pdf" if needed
        startActivityForResult(Intent.createChooser(intent, "Select Assignment File"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            selectedFileName = getFileName(selectedFileUri);
            textSelectedFile.setText(selectedFileName);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1 && result != null) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    private void uploadAssignment() {
        String studentId = editTextStudentId.getText().toString().trim();
        String studentClass = editTextClass.getText().toString().trim();
        String title = editTextAssignmentTitle.getText().toString().trim();

        if (TextUtils.isEmpty(studentId) || TextUtils.isEmpty(studentClass) || TextUtils.isEmpty(title) || TextUtils.isEmpty(selectedFileName)) {
            Toast.makeText(getContext(), "Please fill all fields and attach a file", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.insertStudentAssignment(studentId, studentClass, title, selectedFileName);
        Toast.makeText(getContext(), "Assignment submitted successfully", Toast.LENGTH_SHORT).show();

        clearFields();
    }

    private void clearFields() {
        editTextStudentId.setText("");
        editTextClass.setText("");
        editTextAssignmentTitle.setText("");
        textSelectedFile.setText("No file selected");
        selectedFileUri = null;
        selectedFileName = "";
    }
}
