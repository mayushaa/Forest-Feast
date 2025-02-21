package com.example.forestfeast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Login extends AppCompatActivity {

    public EditText etUsername, etPassword;
    public ExtendedFloatingActionButton fabNewGame, fabLoadGame, fabRegister;
    public HelperDB helperDB;
    public SQLiteDatabase db;
    public AlertDialog alertDialog;

    public void init()
    {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        fabNewGame = findViewById(R.id.fabNewGame);
        fabLoadGame = findViewById(R.id.fabLoadGame);
        fabRegister = findViewById(R.id.fabRegister);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        init();

        loadSharedPreferences();

        hideSystemUI();

        helperDB = new HelperDB(Login.this);
        db = helperDB.getWritableDatabase();

        if (fabNewGame != null) {
            fabNewGame.setOnClickListener(v -> {
                if(validateLogin())
                {
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Are you certain you'd like to proceed?")
                            .setMessage("This action will discard your current saved progress if exists.")
                            .setPositiveButton("Continue", (dialog, which) -> {
                                Log.d("MainActivity", "fabNewGame clicked");
                                stopService(new Intent(Login.this, MusicService.class)); // Stop the music service
                                saveSharedPreferences(etUsername.getText().toString(), etPassword.getText().toString());
                                Intent intent = new Intent(Login.this, Backstory.class);
                                startActivity(intent);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            });
        }

        if (fabLoadGame != null) {
            fabLoadGame.setOnClickListener(v -> {
                if(validateLogin())
                {
                    stopService(new Intent(Login.this, MusicService.class)); // Stop the music service
                    Log.d("MainActivity", "fabLoadGame clicked");
                    saveSharedPreferences(etUsername.getText().toString(), etPassword.getText().toString());
                    Intent intent = new Intent(Login.this, Outside.class);
                    startActivity(intent);
                }
            });
        }

        fabRegister.setOnClickListener(v -> {
            saveSharedPreferences(etUsername.getText().toString(), etPassword.getText().toString());
            Intent intent = new Intent(Login.this, Register.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loadSharedPreferences()
    {
        SharedPreferences sp = getSharedPreferences("MAYA",MODE_PRIVATE);

        String name= sp.getString("user", "");
        String password= sp.getString("pass", "");
        etUsername.setText(name);
        etPassword.setText(password);
    }

    public void saveSharedPreferences(String name, String pass)
    {

        SharedPreferences sp=getSharedPreferences("MAYA",MODE_PRIVATE);

        SharedPreferences.Editor editor= sp.edit();

        editor.putString("user",name);
        editor.putString("pass",pass);

        editor.commit();
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

    private boolean validateLogin() {
        String enteredUsername = etUsername.getText().toString().trim();
        String enteredPassword = etPassword.getText().toString().trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(Login.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + HelperDB.USERS_TABLE + " WHERE " +
                    HelperDB.USERNAME + "=? AND " + HelperDB.PASSWORD + "=?";
            cursor = db.rawQuery(query, new String[]{enteredUsername, enteredPassword});

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                return false;
            }
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