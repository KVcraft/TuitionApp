package com.example.tuitionapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentsFragment extends Fragment {

    private LinearLayout cardsContainer;
    private EditText etSearch;
    private DatabaseHelper dbHelper;
    private List<Student> studentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        // Initialize views
        AppCompatButton btnAddStudent = view.findViewById(R.id.btnAddStudent);
        cardsContainer = view.findViewById(R.id.cardsContainer);
        etSearch = view.findViewById(R.id.etSearch);
        dbHelper = new DatabaseHelper(getContext());

        // Set up button click listener
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StudentRegister.class);
                startActivity(intent);
            }
        });

        // Load students from database
        loadStudentsFromDatabase();

        // Setup search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterStudents(s.toString());
            }
        });

        return view;
    }

    private void loadStudentsFromDatabase() {
        studentList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get students with their courses
        String query = "SELECT s.*, GROUP_CONCAT(sc.course_id, ', ') AS courses " +
                "FROM " + DatabaseHelper.TABLE_STUDENTS + " s " +
                "LEFT JOIN " + DatabaseHelper.TABLE_STUDENT_COURSES + " sc ON s.id = sc.student_id " +
                "GROUP BY s.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LASTNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS));
                String courses = cursor.getString(cursor.getColumnIndexOrThrow("courses"));

                Student student = new Student(id, firstName, lastName, email, contact, address, courses);
                studentList.add(student);

                // Create and add card for this student
                addStudentCard(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private void addStudentCard(Student student) {
        View cardView = LayoutInflater.from(getContext())
                .inflate(R.layout.student_card, cardsContainer, false);

        // Set student details
        TextView tvName = cardView.findViewById(R.id.tvStudentName);
        TextView tvId = cardView.findViewById(R.id.tvStudentId);
        TextView tvCourse = cardView.findViewById(R.id.tvStudentCourse);

        tvName.setText(student.getFirstName() + " " + student.getLastName());
        tvId.setText("ID: " + student.getId());
        tvCourse.setText("Courses: " + (student.getCourses() != null ? student.getCourses() : "None"));

        // Set edit/delete click listeners
        ImageView ivEdit = cardView.findViewById(R.id.ivEdit);
        ImageView ivDelete = cardView.findViewById(R.id.ivDelete);

        ivEdit.setOnClickListener(v -> showEditDialog(student, cardView));
        ivDelete.setOnClickListener(v -> deleteStudent(student, cardView));

        // Add the card to the container
        cardsContainer.addView(cardView);
    }

    private void showEditDialog(Student student, View cardView) {
        // Implement your edit dialog here
        Toast.makeText(getContext(), "Edit student: " + student.getFirstName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteStudent(Student student, View cardView) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // First delete from student_courses table
            db.delete(DatabaseHelper.TABLE_STUDENT_COURSES,
                    DatabaseHelper.COLUMN_STUDENT_ID + " = ?",
                    new String[]{String.valueOf(student.getId())});

            // Then delete from students table
            int deleted = db.delete(DatabaseHelper.TABLE_STUDENTS,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(student.getId())});

            if (deleted > 0) {
                studentList.remove(student);
                cardsContainer.removeView(cardView);
                Toast.makeText(getContext(), "Student deleted", Toast.LENGTH_SHORT).show();
            }
        } finally {
            db.close();
        }
    }

    private void filterStudents(String query) {
        String queryLower = query.toLowerCase(Locale.getDefault());

        for (int i = 0; i < cardsContainer.getChildCount(); i++) {
            View cardView = cardsContainer.getChildAt(i);
            TextView tvName = cardView.findViewById(R.id.tvStudentName);
            TextView tvId = cardView.findViewById(R.id.tvStudentId);

            if (tvName != null && tvId != null) {
                String name = tvName.getText().toString().toLowerCase(Locale.getDefault());
                String id = tvId.getText().toString().toLowerCase(Locale.getDefault());

                if (name.contains(queryLower) || id.contains(queryLower)) {
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // Student model class
    private static class Student {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String contact;
        private String address;
        private String courses;

        public Student(int id, String firstName, String lastName, String email,
                       String contact, String address, String courses) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.contact = contact;
            this.address = address;
            this.courses = courses;
        }

        // Getters
        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getContact() { return contact; }
        public String getAddress() { return address; }
        public String getCourses() { return courses; }
    }
}