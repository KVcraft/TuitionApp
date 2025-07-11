package com.example.tuitionapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CourseMaterialActivity extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST_CODE = 101;

    EditText editTextSession, editTextDateTime;
    AppCompatButton btnSelectFile, btnSave, btnDelete;
    TextView textSelectedFile;

    Uri selectedFileUri = null;
    String selectedFileName = "No file selected";

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_course_material);

        editTextSession = findViewById(R.id.editTextSession);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        btnSelectFile = findViewById(R.id.btnSelectFile);
        btnSave = findViewById(R.id.buttonSave);
        btnDelete = findViewById(R.id.buttonDelete);
        textSelectedFile = findViewById(R.id.textSelectedFile);

        editTextDateTime.setOnClickListener(v -> showDateTimePicker());

        btnSelectFile.setOnClickListener(v -> openFilePicker());

        btnSave.setOnClickListener(v -> {
            String session = editTextSession.getText().toString().trim();
            String dateTime = editTextDateTime.getText().toString().trim();

            if (session.isEmpty() || dateTime.isEmpty() || selectedFileUri == null) {
                Toast.makeText(this, "Please fill all fields and select a file", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Save logic here
                Toast.makeText(this, "Course material saved!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            editTextSession.setText("");
            editTextDateTime.setText("");
            textSelectedFile.setText("No file selected");
            selectedFileUri = null;
            selectedFileName = "No file selected";
            Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (TimePicker timeView, int hourOfDay, int minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                editTextDateTime.setText(sdf.format(calendar.getTime()));
                            },
                            currentDate.get(Calendar.HOUR_OF_DAY),
                            currentDate.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "text/xml", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            selectedFileName = getFileNameFromUri(selectedFileUri);
            textSelectedFile.setText(selectedFileName);
        }
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = "Unknown";
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        } else {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}
