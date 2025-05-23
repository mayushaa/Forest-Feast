package com.example.forestfeast;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Result extends AppCompatActivity {

    public TextView tvScore;
    public ExtendedFloatingActionButton fabProceed;
    public int currentLevel;
    public ImageView ivOatmeal;
        public int[] imageResources = {
                R.drawable.basic_oats, R.drawable.decorated_meal, R.drawable.banana_cream_pie,
                R.drawable.apple_pie_treat, R.drawable.cherry_pond, R.drawable.beary_delighted,
                R.drawable.strawberry_dream, R.drawable.witches_stew
        };
    public HelperDB helperDB;
    public SQLiteDatabase db;

    public void init()
    {
        tvScore = findViewById(R.id.tvScore);
        fabProceed = findViewById(R.id.fabProceed);
        currentLevel = getIntent().getIntExtra("level", 0);
        ivOatmeal = findViewById(R.id.ivOatmeal);
        Log.d("maya debugging", "onCreate result"+currentLevel);
        helperDB = new HelperDB(Result.this);
        db = helperDB.getWritableDatabase();
    }

    private void navigateToOutside() {
        fabProceed.setOnClickListener(v -> {
            stopService(new Intent(this, MusicService.class));

            currentLevel++;
            upgradeLevel();

            Log.d("maya debugging", "go to outside (from result)"+currentLevel);

            Intent intent = new Intent(Result.this, Outside.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.click);
            intent.putExtra("level", currentLevel);
            startActivity(intent);
        });
    }

    private void navigateToStory() {
        fabProceed.setOnClickListener(v -> {
            stopService(new Intent(this, MusicService.class));

            currentLevel++;
            upgradeLevel();

            Intent intent = new Intent(Result.this, Story.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.click);
            intent.putExtra("level", currentLevel);
            startActivity(intent);
        });
    }

    private void navigateToEnding() {
        fabProceed.setOnClickListener(v -> {
            stopService(new Intent(this, MusicService.class));

            currentLevel = 0;
            upgradeLevel();

            Intent intent = new Intent(Result.this, Ending.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.click);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        init();

        Log.d("maya debugging", "after init result"+currentLevel);

        hideSystemUI();

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.recipe_result_music);
        intent.putExtra("LOOPING", false); // Don't loop the music
        startService(intent);

        Log.d("maya debugging", "after music result"+currentLevel);

        setCurrentLevelOatmeal();

        Log.d("maya debugging", "after oatmeal result"+currentLevel);

        int correctCounter = getIntent().getIntExtra("correctCounter", 0);
        tvScore.setText(String.valueOf(correctCounter * 15));

        if (fabProceed != null) {
            if(currentLevel <= 5)
                navigateToOutside();

            else if(currentLevel == 6)
                navigateToStory();

            else
                navigateToEnding();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void upgradeLevel() {
        SharedPreferences sharedPref = getSharedPreferences("MAYA", MODE_PRIVATE);
        String username = sharedPref.getString("user", null);

        SQLiteDatabase db = helperDB.getWritableDatabase();
        String [] oldData = {username};
        String infield = helperDB.USERNAME+"=?";
        ContentValues cv = new ContentValues();
        cv.put(helperDB.CURRENT_LEVEL,currentLevel);

        Log.d("maya debugging", "current level result" + currentLevel);
        Log.d("maya debugging", "username result" + username);

        int rowsChanged = db.update(helperDB.USERS_TABLE, cv, infield,

                oldData);
        if (rowsChanged>0)
        {

        }
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

    private void setCurrentLevelOatmeal()
    {
        switch (currentLevel) {
            case 0:
                ivOatmeal.setImageResource(imageResources[0]);
                break;
            case 1:
                ivOatmeal.setImageResource(imageResources[1]);
                break;
            case 2:
                ivOatmeal.setImageResource(imageResources[2]);
                break;
            case 3:
                ivOatmeal.setImageResource(imageResources[3]);
                break;
            case 4:
                ivOatmeal.setImageResource(imageResources[4]);
                break;
            case 5:
                ivOatmeal.setImageResource(imageResources[5]);
                break;
            case 6:
                ivOatmeal.setImageResource(imageResources[6]);
                break;
            case 7:
                ivOatmeal.setImageResource(imageResources[7]);
                break;
            default:
                ivOatmeal.setImageResource(imageResources[imageResources.length - 1]);
                break;
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

    @Override
    public void onBackPressed() {}
}