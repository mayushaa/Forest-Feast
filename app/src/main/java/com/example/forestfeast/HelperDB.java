package com.example.forestfeast;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {

    public static final String DB_FILE = "com.example.trivia.HelperDB.db";
    public static final String USERS_TABLE = "Users";
    public static final String FULL_NAME = "full_name";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CORRECT_COUNTER = "correct_counter";

    public HelperDB(@Nullable Context context) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String st = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " ( "
                + FULL_NAME + " TEXT, "
                + USERNAME + " TEXT, "
                + EMAIL + " TEXT, "
                + PASSWORD + " TEXT, "
                + CORRECT_COUNTER + " INTEGER);";
        db.execSQL(st);
    }

    public boolean isUsernameTaken(SQLiteDatabase db, String username) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }
}
