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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class StudentAssignmentsFragment extends Fragment {

    private static final int PICK_PDF_CODE = 100;

    private EditText editTextStudentId, editTextClass, editTextAssignmentTitle;
    private TextView textSelectedFile;
    private Button buttonAttach, buttonUpload;

    private Uri selectedFileUri;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

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

        dbRef = FirebaseDatabase.getInstance().getReference("student_assignments");
        storageRef = FirebaseStorage.getInstance().getReference("student_assignments");

        buttonAttach.setOnClickListener(v -> openFileChooser());

        buttonUpload.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(getContext(), "Please select a file first", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadAssignment();
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
            String fileName = getFileNameFromUri(selectedFileUri);
            textSelectedFile.setText("Selected: " + fileName);
        }
    }

    private void uploadAssignment() {
        String studentId = editTextStudentId.getText().toString().trim();
        String studentClass = editTextClass.getText().toString().trim();
        String title = editTextAssignmentTitle.getText().toString().trim();

        if (studentId.isEmpty() || studentClass.isEmpty() || title.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = getFileNameFromUri(selectedFileUri);
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "_" + fileName);

        fileRef.putFile(selectedFileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, String> assignmentData = new HashMap<>();
                    assignmentData.put("studentId", studentId);
                    assignmentData.put("class", studentClass);
                    assignmentData.put("title", title);
                    assignmentData.put("fileUrl", uri.toString());

                    dbRef.push().setValue(assignmentData)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Assignment uploaded", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save to database", Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show());
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
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
