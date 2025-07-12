package com.example.tuitionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tuitiondb";
    private static final int DB_VERSION = 1;

    // Students table
    public static final String TABLE_STUDENTS = "Students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CONTACT = "contact_number";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_QR_CODE = "qr_code";

    // Courses table
    public static final String TABLE_COURSES = "Courses";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";

    // Student-Course relationship table
    public static final String TABLE_STUDENT_COURSES = "Student_Courses";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_COURSE_ID_FK = "course_id";

    // Create tables SQL
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FIRSTNAME + " TEXT NOT NULL,"
            + COLUMN_LASTNAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_CONTACT + " TEXT,"
            + COLUMN_ADDRESS + " TEXT,"
            + COLUMN_PHOTO + " BLOB,"
            + COLUMN_QR_CODE + " TEXT"
            + ")";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
            + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_COURSE_NAME + " TEXT UNIQUE NOT NULL"
            + ")";

    private static final String CREATE_TABLE_STUDENT_COURSES = "CREATE TABLE " + TABLE_STUDENT_COURSES + "("
            + COLUMN_STUDENT_ID + " INTEGER,"
            + COLUMN_COURSE_ID_FK + " INTEGER,"
            + "PRIMARY KEY (" + COLUMN_STUDENT_ID + ", " + COLUMN_COURSE_ID_FK + "),"
            + "FOREIGN KEY (" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY (" + COLUMN_COURSE_ID_FK + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_STUDENT_COURSES);
        insertDefaultCourses(db); // Insert default courses when database is created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    // Insert default courses
    private void insertDefaultCourses(SQLiteDatabase db) {
        String[] courses = {
                "Biology", "Combined Maths", "Physics", "Chemistry", "ICT",
                "Business Studies", "Economics", "Accounting", "English",
                "French", "English Literature"
        };

        for (String course : courses) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COURSE_NAME, course);
            db.insert(TABLE_COURSES, null, values);
        }
    }

    // Method to add a new student
    public long addStudent(String firstName, String lastName, String email, String password,
                           String contact, String address, byte[] photo, List<Integer> courseIds) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert student data
        ContentValues studentValues = new ContentValues();
        studentValues.put(COLUMN_FIRSTNAME, firstName);
        studentValues.put(COLUMN_LASTNAME, lastName);
        studentValues.put(COLUMN_EMAIL, email);
        studentValues.put(COLUMN_PASSWORD, password);
        studentValues.put(COLUMN_CONTACT, contact);
        studentValues.put(COLUMN_ADDRESS, address);
        studentValues.put(COLUMN_PHOTO, photo);
        // QR code can be generated later and updated

        long studentId = db.insert(TABLE_STUDENTS, null, studentValues);

        // Insert student-course relationships if student was added successfully
        if (studentId != -1 && courseIds != null && !courseIds.isEmpty()) {
            for (int courseId : courseIds) {
                ContentValues scValues = new ContentValues();
                scValues.put(COLUMN_STUDENT_ID, studentId);
                scValues.put(COLUMN_COURSE_ID_FK, courseId);
                db.insert(TABLE_STUDENT_COURSES, null, scValues);
            }
        }

        db.close();
        return studentId;
    }

    // Helper method to convert Bitmap to byte array
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Helper method to convert byte array to Bitmap
    public static Bitmap getBitmapFromBytes(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES,
                new String[]{COLUMN_COURSE_ID, COLUMN_COURSE_NAME},
                null, null, null, null, COLUMN_COURSE_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_COURSE_ID)));
                course.setName(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME)));
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courses;
    }

    // Course model class
    public static class Course {
        private int id;
        private String name;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}