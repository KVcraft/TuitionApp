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

    private ImageView imageView;
    private TextView photoText;
    private FrameLayout photoFrame;
    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etContact, etAddress;
    private CheckBox chkBio, chkMath, chkPhys, chkChem, chkICT, chkBS, chkEcon, chkAcc, chkEng, chkFrench, chkLit;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    private Bitmap studentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupPhotoCapture();
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etContact = findViewById(R.id.etContact);
        etAddress = findViewById(R.id.etAddress);

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

        btnRegister.setOnClickListener(v -> registerStudent());
    }

    private void setupPhotoCapture() {
        photoFrame.setOnClickListener(v -> {
            // Implement your photo capture logic here
            // For example:
            // Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        });
    }

    private void registerStudent() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!validateInputs(firstName, lastName, email, password, confirmPassword, contact, address)) {
            return;
        }

        List<String> selectedCourses = getSelectedCourseNames();
        if (selectedCourses.isEmpty()) {
            Toast.makeText(this, "Please select at least one course", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] photoBytes = studentPhoto != null ? DatabaseHelper.getBytesFromBitmap(studentPhoto) : null;

        long studentId = dbHelper.addStudent(firstName, lastName, email, password,
                contact, address, photoBytes, selectedCourses);

        if (studentId != -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            clearForm();
        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String firstName, String lastName, String email,
                                   String password, String confirmPassword,
                                   String contact, String address) {
        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            return false;
        }
        if (lastName.isEmpty()) {
            etLastName.setError("Last name is required");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords don't match");
            return false;
        }
        if (contact.isEmpty()) {
            etContact.setError("Contact number is required");
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            return false;
        }
        return true;
    }

    private List<String> getSelectedCourseNames() {
        List<String> courseNames = new ArrayList<>();

        if (chkBio.isChecked()) courseNames.add("Biology");
        if (chkMath.isChecked()) courseNames.add("Combined Maths");
        if (chkPhys.isChecked()) courseNames.add("Physics");
        if (chkChem.isChecked()) courseNames.add("Chemistry");
        if (chkICT.isChecked()) courseNames.add("ICT");
        if (chkBS.isChecked()) courseNames.add("Business Studies");
        if (chkEcon.isChecked()) courseNames.add("Economics");
        if (chkAcc.isChecked()) courseNames.add("Accounting");
        if (chkEng.isChecked()) courseNames.add("English");
        if (chkFrench.isChecked()) courseNames.add("French");
        if (chkLit.isChecked()) courseNames.add("English Literature");

        return courseNames;
    }

    private void clearForm() {
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        etContact.setText("");
        etAddress.setText("");

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

        studentPhoto = null;
        imageView.setImageBitmap(null);
        photoText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}