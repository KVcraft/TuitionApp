package com.example.tuitionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tuitiondb";
    private static final int DB_VERSION = 2; // Incremented version

    // Students table
    public static final String TABLE_STUDENTS = "Students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_QR_CODE = "qr_code";

    // Admin table
    public static final String TABLE_ADMINS = "Admins";
    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_ADMIN_FIRSTNAME = "admin_firstname";
    public static final String COLUMN_ADMIN_LASTNAME = "admin_lastname";
    public static final String COLUMN_ADMIN_EMAIL = "admin_email";
    public static final String COLUMN_ADMIN_PASSWORD = "admin_password";
    public static final String COLUMN_ADMIN_CONTACT = "admin_contact";
    public static final String COLUMN_ADMIN_ADDRESS = "admin_address";

    // Teacher table
    public static final String TABLE_TEACHERS = "Teachers";
    public static final String COLUMN_TEACHER_ID = "teacher_id";
    public static final String COLUMN_TEACHER_FIRSTNAME = "teacher_firstname";
    public static final String COLUMN_TEACHER_LASTNAME = "teacher_lastname";
    public static final String COLUMN_TEACHER_NIC = "teacher_nic";
    public static final String COLUMN_TEACHER_EMAIL = "teacher_email";
    public static final String COLUMN_TEACHER_PASSWORD = "teacher_password";
    public static final String COLUMN_TEACHER_CONTACT = "teacher_contact";
    public static final String COLUMN_TEACHER_ADDRESS = "teacher_address";
    public static final String COLUMN_TEACHER_PHOTO = "teacher_photo";

    // Courses table (now with course_name as primary key)
    public static final String TABLE_COURSES = "Courses";
    public static final String COLUMN_COURSE_NAME = "course_name";

    // Student-Course relationship table
    public static final String TABLE_STUDENT_COURSES = "Student_Courses";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_COURSE_ID_FK = "course_id";

    // Teacher-Course relationship table
    public static final String TABLE_TEACHER_COURSES = "Teacher_Courses";
    public static final String COLUMN_TEACHER_ID_FK = "teacher_id";
    public static final String COLUMN_COURSE_ID_FK_TEACHER = "course_id";

    // Create tables SQL
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FIRSTNAME + " TEXT NOT NULL,"
            + COLUMN_LASTNAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_CONTACT_NUMBER + " TEXT,"
            + COLUMN_ADDRESS + " TEXT,"
            + COLUMN_PHOTO + " BLOB,"
            + COLUMN_QR_CODE + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ADMINS = "CREATE TABLE " + TABLE_ADMINS + "("
            + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ADMIN_FIRSTNAME + " TEXT NOT NULL,"
            + COLUMN_ADMIN_LASTNAME + " TEXT NOT NULL,"
            + COLUMN_ADMIN_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_ADMIN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_ADMIN_CONTACT + " TEXT,"
            + COLUMN_ADMIN_ADDRESS + " TEXT"
            + ")";

    private static final String CREATE_TABLE_TEACHERS = "CREATE TABLE " + TABLE_TEACHERS + "("
            + COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TEACHER_FIRSTNAME + " TEXT NOT NULL,"
            + COLUMN_TEACHER_LASTNAME + " TEXT NOT NULL,"
            + COLUMN_TEACHER_NIC + " TEXT UNIQUE NOT NULL,"
            + COLUMN_TEACHER_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_TEACHER_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_TEACHER_CONTACT + " TEXT,"
            + COLUMN_TEACHER_ADDRESS + " TEXT,"
            + COLUMN_TEACHER_PHOTO + " BLOB"
            + ")";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
            + COLUMN_COURSE_NAME + " TEXT PRIMARY KEY NOT NULL"
            + ")";

    private static final String CREATE_TABLE_STUDENT_COURSES = "CREATE TABLE " + TABLE_STUDENT_COURSES + "("
            + COLUMN_STUDENT_ID + " INTEGER,"
            + COLUMN_COURSE_ID_FK + " TEXT,"
            + "PRIMARY KEY (" + COLUMN_STUDENT_ID + ", " + COLUMN_COURSE_ID_FK + "),"
            + "FOREIGN KEY (" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY (" + COLUMN_COURSE_ID_FK + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_NAME + ")"
            + ")";

    private static final String CREATE_TABLE_TEACHER_COURSES = "CREATE TABLE " + TABLE_TEACHER_COURSES + "("
            + COLUMN_TEACHER_ID_FK + " INTEGER,"
            + COLUMN_COURSE_ID_FK_TEACHER + " TEXT,"
            + "PRIMARY KEY (" + COLUMN_TEACHER_ID_FK + ", " + COLUMN_COURSE_ID_FK_TEACHER + "),"
            + "FOREIGN KEY (" + COLUMN_TEACHER_ID_FK + ") REFERENCES " + TABLE_TEACHERS + "(" + COLUMN_TEACHER_ID + "),"
            + "FOREIGN KEY (" + COLUMN_COURSE_ID_FK_TEACHER + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_NAME + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Results Table
    public static final String TABLE_RESULTS = "Student_Results";
    public static final String COLUMN_RESULT_ID = "id";
    public static final String COLUMN_RESULT_STUDENT_ID = "student_id";
    public static final String COLUMN_RESULT_COURSE = "course";
    public static final String COLUMN_RESULT_DATE = "date";
    public static final String COLUMN_RESULT_TIME = "time";
    public static final String COLUMN_RESULT_SCORE = "result";  // could be marks, grade, etc.

    // SQL to create table
    private static final String CREATE_TABLE_RESULTS = "CREATE TABLE " + TABLE_RESULTS + " ("
            + COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RESULT_STUDENT_ID + " INTEGER, "
            + COLUMN_RESULT_COURSE + " TEXT, "
            + COLUMN_RESULT_DATE + " TEXT, "
            + COLUMN_RESULT_TIME + " TEXT, "
            + COLUMN_RESULT_SCORE + " TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_ADMINS);
        db.execSQL(CREATE_TABLE_TEACHERS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_STUDENT_COURSES);
        db.execSQL(CREATE_TABLE_TEACHER_COURSES);
        insertDefaultCourses(db);
        insertDefaultAdmin(db);
        db.execSQL(CREATE_TABLE_RESULTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    private void insertDefaultCourses(SQLiteDatabase db) {
        String[] courses = {
                "Biology", "Combined Maths", "Physics", "Chemistry", "ICT",
                "Business Studies", "Economics", "Accounting", "English",
                "French", "English Literature"
        };

        for (String course : courses) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COURSE_NAME, course);
            try {
                db.insertOrThrow(TABLE_COURSES, null, values);
            } catch (SQLiteConstraintException e) {
                // Course already exists
            }
        }
    }

    private void insertDefaultAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_FIRSTNAME, "Admin");
        values.put(COLUMN_ADMIN_LASTNAME, "User");
        values.put(COLUMN_ADMIN_EMAIL, "admin@tuitionapp.com");
        values.put(COLUMN_ADMIN_PASSWORD, "admin123");
        values.put(COLUMN_ADMIN_CONTACT, "1234567890");
        values.put(COLUMN_ADMIN_ADDRESS, "123 Admin Street");

        db.insert(TABLE_ADMINS, null, values);
    }

    // Add methods for teacher operations (updated for course_name as primary key)
    public long addTeacher(String firstName, String lastName, String nic, String email,
                           String password, String contact, String address,
                           byte[] photo, List<String> courseNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        long teacherId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEACHER_FIRSTNAME, firstName);
            values.put(COLUMN_TEACHER_LASTNAME, lastName);
            values.put(COLUMN_TEACHER_NIC, nic);
            values.put(COLUMN_TEACHER_EMAIL, email);
            values.put(COLUMN_TEACHER_PASSWORD, password);
            values.put(COLUMN_TEACHER_CONTACT, contact);
            values.put(COLUMN_TEACHER_ADDRESS, address);
            values.put(COLUMN_TEACHER_PHOTO, photo);

            teacherId = db.insert(TABLE_TEACHERS, null, values);

            // Insert teacher-course relationships
            if (teacherId != -1 && courseNames != null && !courseNames.isEmpty()) {
                for (String courseName : courseNames) {
                    ContentValues tcValues = new ContentValues();
                    tcValues.put(COLUMN_TEACHER_ID_FK, teacherId);
                    tcValues.put(COLUMN_COURSE_ID_FK_TEACHER, courseName);
                    db.insert(TABLE_TEACHER_COURSES, null, tcValues);
                }
            }
        } finally {
            db.close();
        }
        return teacherId;
    }

    public boolean checkTeacher(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_TEACHER_ID};
        String selection = COLUMN_TEACHER_EMAIL + " = ? AND " + COLUMN_TEACHER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_TEACHERS,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public List<String> getCoursesForTeacher(int teacherId) {
        List<String> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_COURSE_ID_FK_TEACHER +
                " FROM " + TABLE_TEACHER_COURSES +
                " WHERE " + COLUMN_TEACHER_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(teacherId)});

        if (cursor.moveToFirst()) {
            do {
                courses.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courses;
    }

    public boolean courseExists(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES,
                new String[]{COLUMN_COURSE_NAME},
                COLUMN_COURSE_NAME + " = ?",
                new String[]{courseName},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Utility methods
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Course class (updated to work with course_name as primary key)
    public static class Course {
        private String name;

        public Course() {}

        public Course(String name) {
            this.name = name;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Add these methods to your DatabaseHelper class:

    public boolean checkAdminExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ADMIN_ID};
        String selection = COLUMN_ADMIN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_ADMINS,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public long addAdmin(String firstName, String lastName, String email,
                         String password, String contact, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_FIRSTNAME, firstName);
        values.put(COLUMN_ADMIN_LASTNAME, lastName);
        values.put(COLUMN_ADMIN_EMAIL, email);
        values.put(COLUMN_ADMIN_PASSWORD, password);
        values.put(COLUMN_ADMIN_CONTACT, contact);
        values.put(COLUMN_ADMIN_ADDRESS, address);

        long id = db.insert(TABLE_ADMINS, null, values);
        db.close();
        return id;
    }

    public long addStudent(String firstName, String lastName, String email,
                           String password, String contact, String address,
                           byte[] photo, List<String> courseNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        long studentId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRSTNAME, firstName);
            values.put(COLUMN_LASTNAME, lastName);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_CONTACT_NUMBER, contact);
            values.put(COLUMN_ADDRESS, address);
            values.put(COLUMN_PHOTO, photo);

            studentId = db.insert(TABLE_STUDENTS, null, values);

            // Insert student-course relationships
            if (studentId != -1 && courseNames != null && !courseNames.isEmpty()) {
                for (String courseName : courseNames) {
                    ContentValues scValues = new ContentValues();
                    scValues.put(COLUMN_STUDENT_ID, studentId);
                    scValues.put(COLUMN_COURSE_ID_FK, courseName);
                    db.insert(TABLE_STUDENT_COURSES, null, scValues);
                }
            }
        } finally {
            db.close();
        }
        return studentId;
    }

    public List<String> getAllCourseNames() {
        List<String> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_COURSES,
                    new String[]{COLUMN_COURSE_NAME},
                    null, null, null, null, COLUMN_COURSE_NAME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    courses.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return courses;
    }

    public boolean checkAdminCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ADMIN_ID};
        String selection = COLUMN_ADMIN_EMAIL + " = ? AND " + COLUMN_ADMIN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_ADMINS,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // New table required:
// CREATE TABLE Student_Assignments (id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT, class TEXT, title TEXT, file_name TEXT);

    public long saveAssignment(String studentId, String studentClass, String title, String fileName, String submissionDate, String string) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("class", studentClass);
        values.put("title", title);
        values.put("file_name", fileName);

        long id = db.insert("Student_Assignments", null, values);
        db.close();
        return id;
    }

    public void addStudentResult(String studentId, String course, String date, String time, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT_STUDENT_ID, studentId);
        values.put(COLUMN_RESULT_COURSE, course);
        values.put(COLUMN_RESULT_DATE, date);
        values.put(COLUMN_RESULT_TIME, time);
        values.put(COLUMN_RESULT_SCORE, result);

        long id = db.insert(TABLE_RESULTS, null, values);
        db.close();
    }


    public boolean checkStudentCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getStudentIdByEmail(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int studentId = -1;

        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }

        cursor.close();
        db.close();
        return studentId;
    }

    public Cursor getStudentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM students WHERE email = ?", new String[]{email});
    }

    public Teacher getTeacherByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM teachers WHERE email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            Teacher teacher = new Teacher(
                    cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nic")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("contact")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getString(cursor.getColumnIndexOrThrow("course"))
            );
            cursor.close();
            return teacher;
        }

        return null;
    }


}