package com.example.forestfeast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;

import android.util.Log;


public class Outside extends AppCompatActivity {

    public ImageView ivPicture, ivCriticTalk, ivLevel;
    public static int currentLevel;
    public FrameLayout flCustomer;
    private WalkingView view;

    public void init() {
        ivPicture = findViewById(R.id.ivPicture);
        ivCriticTalk = findViewById(R.id.ivCriticTalk);
        ivLevel = findViewById(R.id.ivLevel);
        currentLevel = getIntent().getIntExtra("level", 0);
        flCustomer = findViewById(R.id.flCustomer);
        Log.d("maya debugging", "Outside"+currentLevel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_outside);

        init();

        hideSystemUI();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);

        Bitmap[] frames = getFramesForLevel(currentLevel);

        view = new WalkingView(this, this.flCustomer.getWidth(), this.flCustomer.getHeight(), frames);
        this.flCustomer.addView(view);

        ivLevel.setVisibility(View.VISIBLE);
        ivLevel.setAlpha(0f);
        ivLevel.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();

        if(currentLevel == 0)
        {
            ivCriticTalk.setVisibility(View.VISIBLE);
            ivCriticTalk.setAlpha(0f);
            ivCriticTalk.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .start();
            playTalkingSound();

        }
        else
        {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.theme);
            intent.putExtra("LOOPING", false);
            startService(intent);

            ivCriticTalk.setVisibility(View.INVISIBLE);

            if(currentLevel == 1)
                ivLevel.setImageResource(R.drawable.level2);
            if(currentLevel == 2)
                ivLevel.setImageResource(R.drawable.level3);
            if(currentLevel == 3)
                ivLevel.setImageResource(R.drawable.level4);
            if(currentLevel == 4)
                ivLevel.setImageResource(R.drawable.level5);
            if(currentLevel == 5)
                ivLevel.setImageResource(R.drawable.level6);
            if(currentLevel == 6)
                ivLevel.setImageResource(R.drawable.level7);
        }

        Log.d("maya debugging", "music on(outside)"+currentLevel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("maya debugging", "before handler(outside)"+currentLevel);

        new Handler().postDelayed(() -> {
            Log.d("maya debugging", "navigate to restaurant(outside)"+currentLevel);
            navigateToRestaurant();
        }, 5000);
    }

    private Bitmap[] getFramesForLevel(int level) {
        switch (level) {
            case 0:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_critic_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_critic_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_critic_3)
                };
            case 1:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_3)
                };
            case 2:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_fox_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_fox_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_fox_3)
                };
            case 3:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_deer_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_deer_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_deer_3)
                };
            case 4:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_3)
                };
            case 5:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_3)
                };
            case 6:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_3)
                };
            default:
                return new Bitmap[]{
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.out_squirrel_3)
                };
        }
    }

    private void playTalkingSound()
    {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.talking);
        intent.putExtra("LOOPING", false);
        startService(intent);
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

    private void navigateToRestaurant() {
        Intent intent = new Intent(Outside.this, Restaurant.class);
        intent.putExtra("level", currentLevel);
        startActivity(intent);
        finish();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("WALKING","Outside -onWindowFocusChanged");
        Log.d("maya debugging", "focus changed(outside)"+currentLevel);

        if (hasFocus) {
            Log.d("WALKING", "onWindowFocusChanged: width - " + this.flCustomer.getWidth());
            Log.d("WALKING", "onWindowFocusChanged: height - " + this.flCustomer.getHeight());
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