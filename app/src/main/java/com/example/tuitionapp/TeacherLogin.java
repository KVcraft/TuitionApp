package com.example.tuitionapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherLogin extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login); // Make sure this matches your layout file name

        // Initialize views
        editTextEmail = findViewById(R.id.editTextTeacherEmail);
        editTextPassword = findViewById(R.id.editTextTeacherPassword);
        buttonLogin = findViewById(R.id.buttonTeacherLogin);
        dbHelper = new DatabaseHelper(this);



        // Handle login button click
        buttonLogin.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValid = dbHelper.checkTeacher(email, password);
            if (isValid) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                // You can pass teacher info if needed (e.g., email or ID)
                Intent intent = new Intent(TeacherLogin.this, TeacherDashboard.class);
                intent.putExtra("teacher_email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
