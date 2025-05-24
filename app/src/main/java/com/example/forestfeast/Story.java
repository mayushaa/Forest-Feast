package com.example.forestfeast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Story extends AppCompatActivity {

    public ExtendedFloatingActionButton fabSkip;
    public ImageView ivPicture;
    public int[] imageResources = {
            R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4,
            R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
            R.drawable.s9, R.drawable.s10
    };
    public int currentPicture = 0;
    public int currentLevel;
    //private boolean finished;
    private boolean canPlayerNext;
    private Handler handler;

    public void init() {
        fabSkip = findViewById(R.id.fabSkip);
        ivPicture = findViewById(R.id.ivPicture);
        currentLevel = getIntent().getIntExtra("level", 0);
        handler = new Handler(Looper.getMainLooper());;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story);

        //finished = false;
        canPlayerNext = true;

        init();

        hideSystemUI();

//        ivPicture.setImageResource(imageResources[currentPicture]);
//        ivPicture.setVisibility(View.VISIBLE);
//        ivPicture.setAlpha(0f);
//        ivPicture.animate()
//                .alpha(1f)
//                .setDuration(1000)
//                .start();

        ivPicture.setOnClickListener(v -> {
            if (!canPlayerNext) return;
            handler.removeCallbacksAndMessages(null);
            nextImageLoop();
        });

//        playSoundForCurrentPicture();
        nextImageLoop();

        fabSkip.setOnClickListener(v -> navigateToRestaurant());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("PAUSE_MUSIC");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("RESUME_MUSIC");
        startService(intent);
    }

    //if (currentPicture >= imageResources.length - 1) { אם צריך את finished משום מה להחליף לזה

    private void nextImageLoop() {
        if (currentPicture >= imageResources.length) {
            navigateToRestaurant();
            return;
        }

        canPlayerNext = false;
        handler.postDelayed(() -> canPlayerNext = true, 500);

        playSoundForCurrentPicture();
        ivPicture.setImageResource(imageResources[currentPicture]);
        ivPicture.setVisibility(View.VISIBLE);
        ivPicture.setAlpha(0f);
        ivPicture.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
        currentPicture++;

        handler.postDelayed(this::nextImageLoop, 5000);
    }

//    private void nextImage() {
//        if (currentPicture < imageResources.length - 1) {
//            currentPicture++;
//            ivPicture.setImageResource(imageResources[currentPicture]);
//            ivPicture.setVisibility(View.VISIBLE);
//            ivPicture.setAlpha(0f);
//            ivPicture.animate()
//                    .alpha(1f)
//                    .setDuration(1000)
//                    .start();
//            playSoundForCurrentPicture();
//            handler.postDelayed(this::nextImage, 5000);
//        } else {
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    navigateToRestaurantActivity();
//                }
//            }, 5000);
//        }
//    }

//    private void playSoundForCurrentPicture() {
//        Intent intent = new Intent(this, MusicService.class);
//        switch (currentPicture) {
//            case 0:
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//                intent.putExtra("MUSIC_RES_ID", R.raw.talking);
//                intent.putExtra("LOOPING", true);
//                break;
//            case 8:
//                intent.putExtra("MUSIC_RES_ID", 0);
//                // No sound for s9
//                break;
//            case 9:
//                Log.d("maya debugging", "dundundun");
//                intent.putExtra("MUSIC_RES_ID", R.raw.dundundun);
//                intent.putExtra("LOOPING", false);
//                break;
//            default:
//        }
//        startService(intent);
//    }

    private void playSoundForCurrentPicture() {
        Intent intent = new Intent(Story.this, MusicService.class);

        switch (currentPicture) {
            case 0:
                intent.putExtra("MUSIC_RES_ID", R.raw.s1_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 1:
                intent.putExtra("MUSIC_RES_ID", R.raw.s2_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 2:
                intent.putExtra("MUSIC_RES_ID", R.raw.s3_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 3:
                intent.putExtra("MUSIC_RES_ID", R.raw.s4_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 4:
                intent.putExtra("MUSIC_RES_ID", R.raw.s5_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 5:
                intent.putExtra("MUSIC_RES_ID", R.raw.s6_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 6:
                intent.putExtra("MUSIC_RES_ID", R.raw.s7_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 7:
                intent.putExtra("MUSIC_RES_ID", R.raw.s8_voice);
                intent.putExtra("LOOPING", false);
                break;
            case 8:
                // No sound for s9
                intent.putExtra("MUSIC_RES_ID", 0);
                break;
            case 9:
                Log.d("maya debugging", "dundundun");
                intent.putExtra("MUSIC_RES_ID", R.raw.dundundun);
                intent.putExtra("LOOPING", false);
                break;
            default:
                return;
        }

        Intent stopMusicIntent = new Intent(Story.this, MusicService.class);
        stopMusicIntent.setAction("STOP_MUSIC");
        startService(stopMusicIntent);

        Log.d("maya debugging", "playing sound for picture " + currentPicture);
        startService(intent);
    }

    private void navigateToRestaurant() {
        handler.removeCallbacksAndMessages(null);
        stopService(new Intent(this, MusicService.class));

        Intent intent = new Intent(Story.this, Restaurant.class);
        intent.putExtra("level", currentLevel);
        startActivity(intent);
        finish();
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