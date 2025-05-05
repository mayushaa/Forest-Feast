package com.example.forestfeast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton fabBegin;
    private FrameLayout flChef;
    private MixingView view;
    public Button btnDev;

    public void init()
    {
        this.fabBegin = findViewById(R.id.fabBegin);
        this.flChef = findViewById(R.id.flChef);
        this.btnDev = findViewById(R.id.btnDev);
        view = new MixingView(MainActivity.this, flChef.getWidth(), flChef.getHeight());
        flChef.addView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.d("MIXING", "OnCreate before init");

        init();

        hideSystemUI();

        Log.d("MIXING", "OnCreate after init");

        if (fabBegin != null) {
            navigateToLogin();
        }

        Log.d("MIXING", "OnCreate after button");

        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.putExtra("MUSIC_RES_ID", R.raw.menu_music);
        musicIntent.putExtra("LOOPING", true);
        musicIntent.putExtra("VOLUME", 0.5);
        startService(musicIntent);

        Log.d("MIXING", "OnCreate after music");

        if (btnDev != null) {
            btnDev.setOnClickListener(v -> {
                Log.d("MainActivity", "btnDev clicked");
                Intent intent = new Intent(MainActivity.this, Shake.class);
                startActivity(intent);
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void navigateToLogin()
    {
        fabBegin.setOnClickListener(v -> {
            Log.d("MainActivity", "fabBegin clicked");
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.click);
            startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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