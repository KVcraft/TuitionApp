package com.example.tuitionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentLogin extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextStudentEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbHelper = new DatabaseHelper(this);

        // Login button click handler
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = dbHelper.checkStudentCredentials(email, password);
                if (isValid) {
                    int studentId = dbHelper.getStudentIdByEmail(email, password);

                    // Store session in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("student_email", email);
                    editor.apply();


                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to Dashboard
                    startActivity(new Intent(StudentLogin.this, StudentDashboard.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
