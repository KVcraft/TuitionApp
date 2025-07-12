package com.example.tuitionapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TeacherRegister extends AppCompatActivity {
    private EditText firstName, lastName, nic, email, password, confirmPassword, contact, address;
    private Spinner courseSpinner;
    private ImageView imageView;
    private TextView photoText;
    private FrameLayout photoFrame;
    private DatabaseHelper databaseHelper;
    private Bitmap teacherPhoto;
    private String selectedCourse;

    private static final int REQUEST_CAMERA = 100;
    private static final int PERMISSION_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Bind views
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nic = findViewById(R.id.nic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        courseSpinner = findViewById(R.id.course);
        imageView = findViewById(R.id.imageView);
        photoText = findViewById(R.id.photo_text);
        photoFrame = findViewById(R.id.photoFrame);

        // Set up course spinner
        setupCourseSpinner();

        photoFrame.setOnClickListener(view -> checkPermissionAndOpenCamera());
    }

    private void setupCourseSpinner() {
        // Get courses from database
        List<String> courses = databaseHelper.getAllCourseNames();

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                courses
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        // Set spinner item selection listener
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCourse = null;
            }
        });
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            teacherPhoto = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(teacherPhoto);
            photoText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRegisterClick(View view) {
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String nicNo = nic.getText().toString().trim();
        String emailAddr = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String phone = contact.getText().toString().trim();
        String addr = address.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(fname)) {
            firstName.setError("First name is required");
            return;
        }

        if (TextUtils.isEmpty(lname)) {
            lastName.setError("Last name is required");
            return;
        }

        if (TextUtils.isEmpty(nicNo) || nicNo.length() != 12) {
            nic.setError("Valid NIC (12 characters) is required");
            return;
        }

        if (TextUtils.isEmpty(emailAddr) || !Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches()) {
            email.setError("Valid email is required");
            return;
        }

        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return;
        }

        if (!pass.equals(confirmPass)) {
            confirmPassword.setError("Passwords don't match");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            contact.setError("Contact number is required");
            return;
        }

        if (TextUtils.isEmpty(addr)) {
            address.setError("Address is required");
            return;
        }

        if (selectedCourse == null || selectedCourse.isEmpty()) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert photo to byte array
        byte[] photoBytes = teacherPhoto != null ?
                DatabaseHelper.getBytesFromBitmap(teacherPhoto) : null;

        // Create list of selected courses (can be expanded for multiple selections)
        List<String> courseNames = new ArrayList<>();
        courseNames.add(selectedCourse);

        // Register teacher
        long teacherId = databaseHelper.addTeacher(fname, lname, nicNo, emailAddr, pass,
                phone, addr, photoBytes, courseNames);

        if (teacherId != -1) {
            Toast.makeText(this, "Teacher registered successfully", Toast.LENGTH_SHORT).show();
            clearForm();
            // After successfully adding teacher to database
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email or NIC may already exist.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        firstName.setText("");
        lastName.setText("");
        nic.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
        contact.setText("");
        address.setText("");
        courseSpinner.setSelection(0);

        teacherPhoto = null;
        imageView.setImageBitmap(null);
        photoText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}