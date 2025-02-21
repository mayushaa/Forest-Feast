package com.example.forestfeast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forestfeast.R;

public class Ending extends AppCompatActivity {

    private ImageView ivLetter;
    private TextView tv1, tv2, tvThank;
    private Handler handler;

    private void init() {
        ivLetter = findViewById(R.id.ivLetter);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tvThank = findViewById(R.id.tvThank);
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ending);

        init();
        hideSystemUI();

        startMusicService();

        scheduleAnimations();
    }

    private void startMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.menu_music);
        intent.putExtra("LOOPING", false);
        startService(intent);
    }

    private void scheduleAnimations() {
        handler.postDelayed(() -> fadeInImage(ivLetter, R.drawable.letter1), 5000);
        handler.postDelayed(() -> fadeInImage(ivLetter, R.drawable.letter2), 7000);
        handler.postDelayed(() -> fadeInImage(ivLetter, R.drawable.letter3), 9000);

        handler.postDelayed(() -> showAndScrollText(tv1), 10000);

        handler.postDelayed(() -> showAndScrollText(tv2), 22000);

        handler.postDelayed(() -> fadeInView(tvThank), 37000);
    }

    private void fadeInImage(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
        imageView.setAlpha(0f);
        imageView.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
    }

    private static void fadeInView(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
    }

    public static void showAndScrollText(TextView textView)
    {
        int screenHeight = textView.getResources().getDisplayMetrics().heightPixels;
        Animation animation = new TranslateAnimation(0, 0,0, -screenHeight);
        animation.setDuration(15000);
        animation.setFillAfter(true);
        textView.startAnimation(animation);
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
}
