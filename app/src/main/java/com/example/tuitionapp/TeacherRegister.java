package com.example.tuitionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ComponentCaller;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TeacherRegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, nic, email, password, confirmPassword, contact, address, course;
    Button registerBtn;

    // Photo UI references
    ImageView imageView;
    TextView photoText;
    FrameLayout photoFrame;

    private static final int REQUEST_CAMERA = 100;
    private static final int PERMISSION_REQUEST_CODE = 2000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        // Bind views
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nic = findViewById(R.id.nic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        course = findViewById(R.id.course);
        imageView = findViewById(R.id.imageView);
        photoText = findViewById(R.id.photo_text);
        photoFrame = findViewById(R.id.photoFrame);

        photoFrame.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              checkPermisionAndOpenCamera();
                                          }
                                      }
        );

    }

    private void checkPermisionAndOpenCamera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        switch (requestCode){
            case REQUEST_CAMERA:
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(capturedImage);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkPermisionAndOpenCamera();
                }
                break;
        }
    }

    private void openCamera() {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (openCamera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(openCamera, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
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

        if (fname.isEmpty() || lname.isEmpty() || nicNo.isEmpty() || emailAddr.isEmpty() ||
                pass.isEmpty() || confirmPass.isEmpty() || phone.isEmpty() || addr.isEmpty() || courseName.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
        } else if (nicNo.length() != 12) {
            Toast.makeText(this, "NIC should be 12 characters", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            firstName.setText(""); lastName.setText(""); nic.setText("");
            email.setText(""); password.setText(""); confirmPassword.setText("");
            contact.setText(""); address.setText(""); course.setText("");

            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
            photoText.setVisibility(View.VISIBLE);
        }
    }

}
