package com.example.tuitionapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TeacherRegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, nic, email, password, confirmPassword, contact, address, course;
    Button registerBtn;

    // Photo UI references
    FrameLayout photoFrame;
    ImageView photoView;
    TextView photoText;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        // Link UI
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nic = findViewById(R.id.nic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        course = findViewById(R.id.course);
        photoView = findViewById(R.id.photo_view);
        photoText = findViewById(R.id.photo_text);
    }

    // Called when photo frame is clicked
    public void onPhotoClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    // Called when register button is clicked
    public void onRegisterClick(View view) {
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String nicNo = nic.getText().toString().trim();
        String emailAddr = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String phone = contact.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String courseName = course.getText().toString().trim();

        if (fname.isEmpty() || lname.isEmpty() || nicNo.isEmpty() ||
                emailAddr.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() ||
                phone.isEmpty() || addr.isEmpty() || courseName.isEmpty()) {

            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();

        } else if (nicNo.length() != 12) {
            Toast.makeText(this, "Please enter a valid NIC", Toast.LENGTH_SHORT).show();

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();

        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            firstName.setText("");
            lastName.setText("");
            nic.setText("");
            email.setText("");
            password.setText("");
            confirmPassword.setText("");
            contact.setText("");
            address.setText("");
            course.setText("");

            // Reset photo
            photoView.setImageBitmap(null);
            photoView.setVisibility(View.GONE);
            photoText.setVisibility(View.VISIBLE);
        }
    }

    // Open camera
    private void openCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle camera result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            photoView.setImageBitmap(imageBitmap);
            photoView.setVisibility(View.VISIBLE);
            photoText.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
