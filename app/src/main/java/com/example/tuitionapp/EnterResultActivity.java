package com.example.tuitionapp;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EnterResultActivity extends AppCompatActivity {

    EditText editTextSession, editTextDateTime;
    AppCompatButton buttonLoadStudents, buttonSaveResults;
    RecyclerView recyclerView;
    StudentAdapter adapter;

    List<Student> studentList = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_result);

        editTextSession = findViewById(R.id.editTextSession);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        buttonLoadStudents = findViewById(R.id.buttonLoadStudents);
        buttonSaveResults = findViewById(R.id.buttonSaveResults);
        recyclerView = findViewById(R.id.recyclerViewStudents);

        editTextDateTime.setOnClickListener(v -> showDateTimePicker());

        buttonLoadStudents.setOnClickListener(v -> {
            // Simulated student loading
            studentList.clear();
            studentList.add(new Student("Ali Khan", "ali123"));
            studentList.add(new Student("Zara Ahmed", "zara456"));
            studentList.add(new Student("Hassan Raza", "hassan789"));

            adapter = new StudentAdapter(studentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            Toast.makeText(this, "Students loaded for session " + editTextSession.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        buttonSaveResults.setOnClickListener(v -> {
            if (adapter == null) {
                Toast.makeText(this, "Load students first", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Student> entered = adapter.getStudents();
            for (Student s : entered) {
                Log.d("ResultEntry", s.getName() + " (" + s.getUsername() + "): " + s.getMarks());
            }
            Toast.makeText(this, "Results saved (see Logcat)", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                editTextDateTime.setText(sdf.format(calendar.getTime()));
            },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}

