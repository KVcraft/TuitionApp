/*package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AttendanceFilterActivity extends AppCompatActivity {

        private Spinner sessionSpinner;
        private EditText dateEditText, timeEditText;
        private Button submitButton;
        private CardView attendanceGridCard;
        private RecyclerView attendanceRecyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_attendance_filter);

            sessionSpinner = findViewById(R.id.sessionSpinner);
            dateEditText = findViewById(R.id.dateEditText);
            timeEditText = findViewById(R.id.timeEditText);
            submitButton = findViewById(R.id.submitButton);
            attendanceGridCard = findViewById(R.id.attendanceGridCard);
            attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);

            // Setup Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    Arrays.asList("Morning", "Afternoon", "Evening"));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sessionSpinner.setAdapter(adapter);

            // Date Picker
            dateEditText.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) ->
                                dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            });

            // Time Picker
            timeEditText.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(
                        this,
                        (view, hourOfDay, minute) ->
                                timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute)),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);
                dialog.show();
            });

            // Submit Button
            submitButton.setOnClickListener(v -> {
                List<String> dummyAttendance = Arrays.asList("John Doe", "Jane Smith", "Alice", "Bob");

                attendanceGridCard.setVisibility(View.VISIBLE);
                attendanceRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                AttendanceAdapter adapter1 = new AttendanceAdapter(this, dummyAttendance);
                attendanceRecyclerView.setAdapter(adapter1);
            });
        }
    }*/

package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class AttendanceFilterActivity extends AppCompatActivity {

    private Spinner sessionSpinner;
    private EditText dateEditText, timeEditText;
    private Button submitButton;
    private CardView attendanceGridCard;
    private RecyclerView attendanceRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_filter);

        /*sessionSpinner = findViewById(R.id.sessionSpinner);*/
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);
        attendanceGridCard = findViewById(R.id.attendanceGridCard);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);

        // Spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Morning", "Afternoon", "Evening"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);

        // Date picker
        dateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                dateEditText.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker
        timeEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                timeEditText.setText(selectedTime);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        // Submit button click
        submitButton.setOnClickListener(v -> {
            // Dummy data
            List<AttendanceModel> attendanceList = new ArrayList<>();
            attendanceList.add(new AttendanceModel("John Doe", true));
            attendanceList.add(new AttendanceModel("Jane Smith", false));
            attendanceList.add(new AttendanceModel("Alice Johnson", true));
            attendanceList.add(new AttendanceModel("Bob Brown", false));

            attendanceGridCard.setVisibility(View.VISIBLE);
            attendanceRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this, attendanceList);
            attendanceRecyclerView.setAdapter(attendanceAdapter);
        });
    }
}
