package com.example.forestfeast;

import static java.sql.Types.INTEGER;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {

    public static final String DB_FILE = "forest_feast.db";
    public static final String USERS_TABLE = "Users";
    public static final String FULL_NAME = "full_name";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CORRECT_COUNTER = "correct_counter";
    public static final String CURRENT_LEVEL = "current_level";

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
                + CORRECT_COUNTER + " INTEGER, "
                + CURRENT_LEVEL + " INTEGER);";
        db.execSQL(st);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public boolean isUsernameTaken(SQLiteDatabase db, String username) {
//        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERNAME + "=?";
//        Cursor cursor = db.rawQuery(query, new String[]{username});
//        boolean exists = cursor.getCount() > 0;
//        cursor.close();
//        return exists;
//    }

    public int getCurrentLevel(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int currentLevel = -1;

        String query = "SELECT " + CURRENT_LEVEL + " FROM " + USERS_TABLE + " WHERE " + USERNAME + " = ?";

        Log.d("maya debugging", "helperDB getCurrentLevel"+query);
        Log.d("maya debugging", "helperDB getCurrentLevel username"+username);

        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(CURRENT_LEVEL);

            if (columnIndex != -1) {
                currentLevel = cursor.getInt(columnIndex);
            }
        }

        cursor.close();
        db.close();

        return currentLevel;
    }
}
