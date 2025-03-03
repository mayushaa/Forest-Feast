package com.example.forestfeast;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Register extends AppCompatActivity {

    public EditText etName, etEmail, etUsername, etPassword;
    public ExtendedFloatingActionButton fabReady;
    public HelperDB helperDB;
    public SQLiteDatabase db;

    public void init() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        fabReady = findViewById(R.id.fabReady);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        init();

        hideSystemUI();

        helperDB = new HelperDB(Register.this);
        db = helperDB.getWritableDatabase();

        fabReady.setOnClickListener(v -> {
            if (validateInput()) {
                String fullName = etName.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (isUsernameTaken(username)) {
                    Toast.makeText(Register.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (insertUser(fullName, username, email, password)) {
                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    intent.putExtra("MUSIC_RES_ID", R.raw.click);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent pauseMusicIntent = new Intent(this, MusicService.class);
        pauseMusicIntent.setAction("PAUSE_MUSIC");
        startService(pauseMusicIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusicIntent = new Intent(this, MusicService.class);
        resumeMusicIntent.setAction("RESUME_MUSIC");
        startService(resumeMusicIntent);
    }

    private boolean validateInput() {
        if (etName.getText().toString().trim().isEmpty() ||
                etUsername.getText().toString().trim().isEmpty() ||
                etEmail.getText().toString().trim().isEmpty() ||
                etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean insertUser(String fullName, String username, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(HelperDB.FULL_NAME, fullName);
        values.put(HelperDB.USERNAME, username);
        values.put(HelperDB.EMAIL, email);
        values.put(HelperDB.PASSWORD, password);
        values.put(HelperDB.CORRECT_COUNTER, 0);

        long result = db.insert(HelperDB.USERS_TABLE, null, values);
        return result != -1;
    }

    private boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM " + HelperDB.USERS_TABLE + " WHERE " + HelperDB.USERNAME + "=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{username});
            boolean exists = cursor.getCount() > 0;
            return exists;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}
