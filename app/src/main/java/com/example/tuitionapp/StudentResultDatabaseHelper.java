package com.example.tuitionapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class StudentResultDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_results.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "results";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_GRADE = "grade";
    public static final String COLUMN_DATE = "date";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE + " TEXT, " +
                    COLUMN_GRADE + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    public StudentResultDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new result
    public void insertResult(String course, String grade, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_GRADE, grade);
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Fetch all results
    public Cursor getAllResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_COURSE + " ASC");
    }
}
