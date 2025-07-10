package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.widget.EditText;
import androidx.appcompat.widget.AppCompatButton;

public class UploadAssignmentActivity extends AppCompatActivity {

    private EditText editTextSession, editTextDateTime, editTextAssignmentTitle, editTextOpenDate, editTextDueDate;
    private AppCompatButton buttonUpload, buttonDelete;
    private RecyclerView recyclerViewAssignments;

    private List<Assignment> assignmentList = new ArrayList<>();
    private AssignmentAdapter adapter;

    private Calendar selectedDateTime = Calendar.getInstance();
    private Calendar openDate = Calendar.getInstance();
    private Calendar dueDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);

        // Initialize views
        editTextSession = findViewById(R.id.editTextSession);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        editTextAssignmentTitle = findViewById(R.id.editTextAssignmentTitle);
        editTextOpenDate = findViewById(R.id.editTextOpenDate);
        editTextDueDate = findViewById(R.id.editTextDueDate);

        buttonUpload = findViewById(R.id.buttonUpload);
        buttonDelete = findViewById(R.id.buttonDelete);

        recyclerViewAssignments = findViewById(R.id.recyclerViewAssignments);
        recyclerViewAssignments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssignmentAdapter(assignmentList);
        recyclerViewAssignments.setAdapter(adapter);

        // Date & Time pickers
        editTextDateTime.setOnClickListener(v -> showDateTimePicker(editTextDateTime, selectedDateTime));
        editTextOpenDate.setOnClickListener(v -> showDatePicker(editTextOpenDate, openDate));
        editTextDueDate.setOnClickListener(v -> showDatePicker(editTextDueDate, dueDate));

        // Upload button click
        buttonUpload.setOnClickListener(v -> uploadAssignment());

        // Delete button click - clear all fields and list
        buttonDelete.setOnClickListener(v -> {
            clearFields();
            assignmentList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "All assignments deleted", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDateTimePicker(EditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, m);
                    calendar.set(Calendar.DAY_OF_MONTH, d);

                    // After picking date, open time picker
                    showTimePicker(editText, calendar);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText, Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, h, m) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);

                    // Format and display datetime
                    String formatted = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", calendar).toString();
                    editText.setText(formatted);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePicker(EditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, m);
                    calendar.set(Calendar.DAY_OF_MONTH, d);

                    String formatted = android.text.format.DateFormat.format("yyyy-MM-dd", calendar).toString();
                    editText.setText(formatted);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void uploadAssignment() {
        String session = editTextSession.getText().toString().trim();
        String dateTime = editTextDateTime.getText().toString().trim();
        String title = editTextAssignmentTitle.getText().toString().trim();
        String openDateStr = editTextOpenDate.getText().toString().trim();
        String dueDateStr = editTextDueDate.getText().toString().trim();

        if (TextUtils.isEmpty(session) || TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(title) ||
                TextUtils.isEmpty(openDateStr) || TextUtils.isEmpty(dueDateStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Assignment newAssignment = new Assignment(session, dateTime, title, openDateStr, dueDateStr);
        assignmentList.add(newAssignment);
        adapter.notifyItemInserted(assignmentList.size() - 1);
        Toast.makeText(this, "Assignment uploaded", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        editTextSession.setText("");
        editTextDateTime.setText("");
        editTextAssignmentTitle.setText("");
        editTextOpenDate.setText("");
        editTextDueDate.setText("");
    }
}

