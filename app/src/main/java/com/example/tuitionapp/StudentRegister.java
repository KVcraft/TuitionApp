package com.example.tuitionapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class StudentRegister extends AppCompatActivity {

    ImageView imageView;
    TextView photoText;
    FrameLayout photoFrame;

    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etContact, etAddress;
    private CheckBox chkBio, chkMath, chkPhys, chkChem, chkICT, chkBS, chkEcon, chkAcc, chkEng, chkFrench, chkLit;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etContact = findViewById(R.id.etContact);
        etAddress = findViewById(R.id.etAddress);

        // Initialize checkboxes
        chkBio = findViewById(R.id.chkBio);
        chkMath = findViewById(R.id.chkMath);
        chkPhys = findViewById(R.id.chkPhys);
        chkChem = findViewById(R.id.chkChem);
        chkICT = findViewById(R.id.chkICT);
        chkBS = findViewById(R.id.chkBS);
        chkEcon = findViewById(R.id.chkEcon);
        chkAcc = findViewById(R.id.chkAcc);
        chkEng = findViewById(R.id.chkEng);
        chkFrench = findViewById(R.id.chkFrench);
        chkLit = findViewById(R.id.chkLit);

        btnRegister = findViewById(R.id.btnRegister);

        imageView = findViewById(R.id.studImageView);
        photoText = findViewById(R.id.photo_text);
        photoFrame = findViewById(R.id.studPhotoFrame);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStudent();
            }
        });
    }

    private void registerStudent() {
        // Get input values
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected courses
        List<Integer> selectedCourseIds = getSelectedCourseIds();

        if (selectedCourseIds.isEmpty()) {
            Toast.makeText(this, "Please select at least one course", Toast.LENGTH_SHORT).show();
            return;
        }

        // For photo, you would typically get it from an ImageView
        // Here we're just using null - you should implement proper photo handling
        byte[] photo = null; // Implement photo capture from your UI

        // Add student to database
        long studentId = dbHelper.addStudent(firstName, lastName, email, password,
                contact, address, photo, selectedCourseIds);

        if (studentId != -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            clearForm();

        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Integer> getSelectedCourseIds() {
        List<Integer> courseIds = new ArrayList<>();
        List<DatabaseHelper.Course> allCourses = dbHelper.getAllCourses();

        // This assumes the checkboxes are in the same order as the courses in the database
        // You might need a more robust matching system
        if (chkBio.isChecked()) courseIds.add(allCourses.get(0).getId());
        if (chkMath.isChecked()) courseIds.add(allCourses.get(1).getId());
        if (chkPhys.isChecked()) courseIds.add(allCourses.get(2).getId());
        if (chkChem.isChecked()) courseIds.add(allCourses.get(3).getId());
        if (chkICT.isChecked()) courseIds.add(allCourses.get(4).getId());
        if (chkBS.isChecked()) courseIds.add(allCourses.get(5).getId());
        if (chkEcon.isChecked()) courseIds.add(allCourses.get(6).getId());
        if (chkAcc.isChecked()) courseIds.add(allCourses.get(7).getId());
        if (chkEng.isChecked()) courseIds.add(allCourses.get(8).getId());
        if (chkFrench.isChecked()) courseIds.add(allCourses.get(9).getId());
        if (chkLit.isChecked()) courseIds.add(allCourses.get(10).getId());

        return courseIds;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    // Add this new method to clear all fields
    private void clearForm() {
        // Clear all EditText fields
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        etContact.setText("");
        etAddress.setText("");

        // Uncheck all checkboxes
        chkBio.setChecked(false);
        chkMath.setChecked(false);
        chkPhys.setChecked(false);
        chkChem.setChecked(false);
        chkICT.setChecked(false);
        chkBS.setChecked(false);
        chkEcon.setChecked(false);
        chkAcc.setChecked(false);
        chkEng.setChecked(false);
        chkFrench.setChecked(false);
        chkLit.setChecked(false);

        // Reset photo
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.GONE);
        photoText.setVisibility(View.VISIBLE);
    }
}