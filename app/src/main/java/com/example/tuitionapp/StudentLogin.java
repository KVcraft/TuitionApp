package com.example.tuitionapp;

import android.content.Intent;
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

        editTextEmail = findViewById(R.id.editTextStudentEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        dbHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean isValid = dbHelper.checkStudentCredentials(email, password);
                if (isValid) {
                    int studentId = dbHelper.getStudentIdByEmail(email, password);
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(StudentLogin.this, StudentDashboard.class);
                    intent.putExtra("student_id", studentId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
