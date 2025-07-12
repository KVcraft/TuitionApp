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

public class TeachersFragment extends Fragment {

    private LinearLayout cardsContainer;
    private EditText etSearch;
    private DatabaseHelper dbHelper;
    private List<Teacher> teacherList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers, container, false);

        // Initialize views
        AppCompatButton btnAddTeacher = view.findViewById(R.id.btnAddTeacher);
        cardsContainer = view.findViewById(R.id.cardsContainer);
        etSearch = view.findViewById(R.id.etSearch);
        dbHelper = new DatabaseHelper(getContext());

        // Set up button click listener
        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherRegister.class);
                startActivity(intent);
            }
        });

        // Load teachers from database
        loadTeachersFromDatabase();

        // Setup search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterTeachers(s.toString());
            }
        });

        return view;
    }

    private void loadTeachersFromDatabase() {
        teacherList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get teachers with their courses
        String query = "SELECT t.*, GROUP_CONCAT(tc.course_id, ', ') AS courses " +
                "FROM " + DatabaseHelper.TABLE_TEACHERS + " t " +
                "LEFT JOIN " + DatabaseHelper.TABLE_TEACHER_COURSES + " tc ON t.teacher_id = tc.teacher_id " +
                "GROUP BY t.teacher_id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_LASTNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_EMAIL));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_CONTACT));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ADDRESS));
                String courses = cursor.getString(cursor.getColumnIndexOrThrow("courses"));

                Teacher teacher = new Teacher(id, firstName, lastName, email, contact, address, courses);
                teacherList.add(teacher);

                // Create and add card for this teacher
                addTeacherCard(teacher);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private void addTeacherCard(Teacher teacher) {
        View cardView = LayoutInflater.from(getContext())
                .inflate(R.layout.teacher_card, cardsContainer, false);

        // Set teacher details
        TextView tvName = cardView.findViewById(R.id.tvTeacherName);
        TextView tvId = cardView.findViewById(R.id.tvTeacherId);
        TextView tvCourse = cardView.findViewById(R.id.tvTeacherCourse);

        tvName.setText(teacher.getFirstName() + " " + teacher.getLastName());
        tvId.setText("ID: " + teacher.getId());
        tvCourse.setText("Courses: " + (teacher.getCourses() != null ? teacher.getCourses() : "None"));

        // Set edit/delete click listeners
        ImageView ivEdit = cardView.findViewById(R.id.ivEdit);
        ImageView ivDelete = cardView.findViewById(R.id.ivDelete);

        ivEdit.setOnClickListener(v -> showEditDialog(teacher, cardView));
        ivDelete.setOnClickListener(v -> deleteTeacher(teacher, cardView));

        // Add the card to the container
        cardsContainer.addView(cardView);
    }

    private void showEditDialog(Teacher teacher, View cardView) {
        // Implement your edit dialog here
        Toast.makeText(getContext(), "Edit teacher: " + teacher.getFirstName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteTeacher(Teacher teacher, View cardView) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // First delete from teacher_courses table
            db.delete(DatabaseHelper.TABLE_TEACHER_COURSES,
                    DatabaseHelper.COLUMN_TEACHER_ID_FK + " = ?",
                    new String[]{String.valueOf(teacher.getId())});

            // Then delete from teachers table
            int deleted = db.delete(DatabaseHelper.TABLE_TEACHERS,
                    DatabaseHelper.COLUMN_TEACHER_ID + " = ?",
                    new String[]{String.valueOf(teacher.getId())});

            if (deleted > 0) {
                teacherList.remove(teacher);
                cardsContainer.removeView(cardView);
                Toast.makeText(getContext(), "Teacher deleted", Toast.LENGTH_SHORT).show();
            }
        } finally {
            db.close();
        }
    }

    private void filterTeachers(String query) {
        String queryLower = query.toLowerCase(Locale.getDefault());

        for (int i = 0; i < cardsContainer.getChildCount(); i++) {
            View cardView = cardsContainer.getChildAt(i);
            TextView tvName = cardView.findViewById(R.id.tvTeacherName);
            TextView tvId = cardView.findViewById(R.id.tvTeacherId);

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

    // Teacher model class (simplified without NIC)
    private static class Teacher {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String contact;
        private String address;
        private String courses;

        public Teacher(int id, String firstName, String lastName, String email,
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