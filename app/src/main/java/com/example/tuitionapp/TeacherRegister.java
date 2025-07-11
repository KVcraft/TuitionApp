package com.example.tuitionapp;

import android.Manifest;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TeacherRegister extends AppCompatActivity {

    FrameLayout photoFrame;
    ImageView photoView;
    TextView photoText;

    EditText firstName, lastName, nic, email, password, confirmPassword, contact, address, course;
    Button registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.teacher_reg), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        photoView = findViewById(R.id.photo_view);
        photoText = findViewById(R.id.photo_text);
        photoFrame = findViewById(R.id.photo_frame);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nic = findViewById(R.id.nic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        course = findViewById(R.id.course);
        registerBtn = findViewById(R.id.btnRegister);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, 100);
        }

    }

    public void capturePhoto(View v){
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamera, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 100){
            Bitmap Image = (Bitmap) intent.getExtras().get("data");
            photoView.setImageBitmap(Image);
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



}