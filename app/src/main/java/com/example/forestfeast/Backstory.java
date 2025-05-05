package com.example.forestfeast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Backstory extends AppCompatActivity {

    public ExtendedFloatingActionButton fabSkip;
    public ImageView ivPicture;
    public int[] imageResources = {
            R.drawable.bs1, R.drawable.bs2, R.drawable.bs3, R.drawable.bs4,
            R.drawable.bs5, R.drawable.bs6, R.drawable.bs7, R.drawable.bs8,
            R.drawable.bs9, R.drawable.bs10
    };
    public int currentPicture = 0;
    private boolean finished;
    private boolean isThemePlaying = false;
    private Handler nextImageHandler;
    private boolean canPlayerNext;

    public void init() {
        fabSkip = findViewById(R.id.fabSkip);
        ivPicture = findViewById(R.id.ivPicture);
        finished = false;
        canPlayerNext = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_backstory);

        init();

        hideSystemUI();

        ivPicture.setImageResource(imageResources[currentPicture]);
        ivPicture.setVisibility(View.VISIBLE);
        ivPicture.setAlpha(0f);
        ivPicture.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();

        ivPicture.setOnClickListener(v -> {
            if (!canPlayerNext) return;

            nextImageHandler.removeCallbacksAndMessages(null);
            nextImageHandler = new Handler();
            nextImageLoop();

            nextImage();
        });

        playSoundForCurrentPicture();
        nextImageLoop();

        fabSkip.setOnClickListener(v -> {
            if (!isThemePlaying) {
                Intent themeIntent = new Intent(this, MusicService.class);
                themeIntent.putExtra("MUSIC_RES_ID", R.raw.theme);
                themeIntent.putExtra("LOOPING", true);
                startService(themeIntent);
                isThemePlaying = true;
            }
            navigateToOutsideActivity();
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

        Intent stopMusicIntent = new Intent(this, MusicService.class);
        stopMusicIntent.setAction("STOP_MUSIC");
        startService(stopMusicIntent);

        ivPicture.clearAnimation();

        nextImageHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusicIntent = new Intent(this, MusicService.class);
        resumeMusicIntent.setAction("RESUME_MUSIC");
        startService(resumeMusicIntent);
    }

    private void nextImageLoop() {
        if (finished) return;

        nextImageHandler = new Handler();
        nextImageHandler.postDelayed(() -> {
            canPlayerNext = false;
            new Handler().postDelayed(() -> canPlayerNext = true, 500);

            nextImage();
            nextImageLoop();
        }, 7000);
    }

    private void nextImage() {
        if (currentPicture < imageResources.length - 1) {
            currentPicture++;
            ivPicture.setImageResource(imageResources[currentPicture]);
            ivPicture.setVisibility(View.VISIBLE);
            ivPicture.setAlpha(0f);
            ivPicture.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .start();
            playSoundForCurrentPicture();
        } else {
            navigateToOutsideActivity();
        }
    }

    private void playSoundForCurrentPicture() {
        if (currentPicture < 5 || currentPicture == 10) {
            Intent stopMusicIntent = new Intent(this, MusicService.class);
            stopMusicIntent.setAction("STOP_MUSIC");
            startService(stopMusicIntent);
        }

        int soundResource = 0;
        switch (currentPicture) {
            case 0:
                soundResource = R.raw.bs1_sound;
                break;
            case 1:
                soundResource = R.raw.bs2_sound;
                break;
            case 2:
                soundResource = R.raw.bs3_sound;
                break;
            case 3:
                soundResource = R.raw.bs4_sound;
                break;
            case 4:
                soundResource = R.raw.theme;
                break;
            case 5:
            case 9:
                if (!isThemePlaying) {
                    Intent themeIntent = new Intent(this, MusicService.class);
                    themeIntent.putExtra("MUSIC_RES_ID", R.raw.theme);
                    themeIntent.putExtra("LOOPING", true);
                    startService(themeIntent);
                    isThemePlaying = true;
                }
                break;
            case 10:
                if (isThemePlaying) {
                    Intent stopThemeIntent = new Intent(this, MusicService.class);
                    stopThemeIntent.setAction("STOP_MUSIC");
                    startService(stopThemeIntent);
                    isThemePlaying = false;
                }
                soundResource = R.raw.transform;
                break;
        }

        if (soundResource != 0) {
            Intent playMusicIntent = new Intent(this, MusicService.class);
            playMusicIntent.putExtra("MUSIC_RES_ID", soundResource);
            playMusicIntent.putExtra("LOOPING", currentPicture == 4 || currentPicture == 5 || currentPicture == 9);
            startService(playMusicIntent);
        }
    }

    private void navigateToOutsideActivity() {
        finished = true;

        Intent stopMusicIntent = new Intent(this, MusicService.class);
        stopMusicIntent.setAction("STOP_MUSIC");
        startService(stopMusicIntent);

        Intent intent = new Intent(Backstory.this, Outside.class);
        startActivity(intent);
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
