package com.example.tuitionapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ResultEntryActivity extends AppCompatActivity {

    private Spinner sessionSpinner;
    private EditText dateEditText, timeEditText;
    private Button submitButton;
    private RecyclerView resultRecyclerView;
    private CardView resultCard;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_entry); // make sure layout name matches

        sessionSpinner = findViewById(R.id.sessionSpinner);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        resultCard = findViewById(R.id.resultGridCard);

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
                dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker
        timeEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        submitButton.setOnClickListener(v -> {
            List<ResultModel> studentList = new ArrayList<>();
            studentList.add(new ResultModel("John Doe", ""));
            studentList.add(new ResultModel("Jane Smith", ""));
            studentList.add(new ResultModel("Alice", ""));
            studentList.add(new ResultModel("Bob", ""));

            resultCard.setVisibility(View.VISIBLE);
            resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            resultAdapter = new ResultAdapter(this, studentList);
            resultRecyclerView.setAdapter(resultAdapter);
        });
    }
}

