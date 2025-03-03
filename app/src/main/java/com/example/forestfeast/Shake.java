package com.example.forestfeast;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Shake extends AppCompatActivity {

    private ImageView ivFruit, ivTopping1, ivYoghurt, ivTbsp, ivTspn1, ivTspn2, ivMilk, ivTopping2;
    private int currentLevel;
    private Handler handler;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate;
    private float last_x, last_y, last_z;

    public void init()
    {
        Log.d("maya debugging", "shake init");

        ivFruit = findViewById(R.id.ivFruit);
        ivTopping1 = findViewById(R.id.ivTopping1);
        ivYoghurt = findViewById(R.id.ivYoghurt);
        ivTbsp = findViewById(R.id.ivTbsp);
        ivTspn1 = findViewById(R.id.ivTspn1);
        ivTspn2 = findViewById(R.id.ivTspn2);
        ivMilk = findViewById(R.id.ivMilk);
        ivTopping2 = findViewById(R.id.ivTopping2);
        handler = new Handler();
        currentLevel = getIntent().getIntExtra("level", 0);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        Log.d("maya debugging", "onCreate shake");
        init();
        Log.d("maya debugging", "after init");
        hideSystemUI();
        setIngredients();

        animateImageView(ivFruit);
        animateImageView(ivTopping1);
        animateImageView(ivYoghurt);
        animateImageView(ivTbsp);
        animateImageView(ivTspn1);
        animateImageView(ivTspn2);
        animateImageView(ivMilk);
        animateImageView(ivTopping2);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                long curTime = System.currentTimeMillis();
                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        Log.d("maya debugging", "Device shaken!");
                        increaseImageMovement();
                        handler.postDelayed(() -> navigateToResult(), 10000);
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void navigateToResult()
    {
        Intent intent = new Intent(Shake.this, Result.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.click);
        intent.putExtra("level", currentLevel);
        stopService(new Intent(this, MusicService.class));
        startActivity(intent);
    }

    private void setIngredients() {

        switch(currentLevel)
        {
            case 0:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.honey);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setVisibility(View.INVISIBLE);
                ivFruit.setVisibility(View.INVISIBLE);
                ivTopping1.setVisibility(View.INVISIBLE);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 1:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.honey);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setVisibility(View.INVISIBLE);
                ivFruit.setImageResource(R.drawable.banana_hearts);
                ivTopping1.setImageResource(R.drawable.biscoff);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 2:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.honey);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setImageResource(R.drawable.vanilla_yoghurt);
                ivFruit.setImageResource(R.drawable.bananas);
                ivTopping1.setImageResource(R.drawable.pecans);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 3:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.mapel);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setImageResource(R.drawable.greek_yoghurt);
                ivFruit.setImageResource(R.drawable.apples);
                ivTopping1.setImageResource(R.drawable.nuts);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 4:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.mapel);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setImageResource(R.drawable.greek_yoghurt);
                ivFruit.setImageResource(R.drawable.cherries);
                ivTopping1.setImageResource(R.drawable.nuts);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 5:
                ivMilk.setImageResource(R.drawable.plain_milk);
                ivTbsp.setImageResource(R.drawable.honey);
                ivTspn1.setImageResource(R.drawable.cacao);
                ivTspn2.setVisibility(View.INVISIBLE);
                ivYoghurt.setVisibility(View.INVISIBLE);
                ivFruit.setImageResource(R.drawable.face);
                ivTopping1.setVisibility(View.INVISIBLE);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            case 6:
                ivMilk.setImageResource(R.drawable.strawberry_milk);
                ivTbsp.setImageResource(R.drawable.honey);
                ivTspn1.setImageResource(R.drawable.vanilla);
                ivTspn2.setImageResource(R.drawable.cinnamon);
                ivYoghurt.setImageResource(R.drawable.strawberry_yoghurt);
                ivFruit.setImageResource(R.drawable.strawberries);
                ivTopping1.setImageResource(R.drawable.marshmallow);
                ivTopping2.setImageResource(R.drawable.sprinkles);
                break;

            case 7:
                ivMilk.setImageResource(R.drawable.moonlit_milk);
                ivTbsp.setImageResource(R.drawable.dragon);
                ivTspn1.setImageResource(R.drawable.fairy);
                ivTspn2.setImageResource(R.drawable.pheonix);
                ivYoghurt.setImageResource(R.drawable.pearl_yoghurt);
                ivFruit.setVisibility(View.INVISIBLE);
                ivTopping1.setImageResource(R.drawable.crystals);
                ivTopping2.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    private void animateImageView(ImageView imageView) {
        Random random = new Random();

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", -5f, 5f);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(imageView, "translationX", -10f, 10f);
        translateXAnimator.setDuration(3000);
        translateXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translateXAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(imageView, "translationY", -10f, 10f);
        translateYAnimator.setDuration(3000);
        translateYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translateYAnimator.setRepeatCount(ValueAnimator.INFINITE);

        rotateAnimator.start();
        translateXAnimator.start();
        translateYAnimator.start();
    }

    private void increaseImageMovement() {
        ImageView[] allImageViews = {ivTspn2, ivTbsp, ivTspn1, ivFruit, ivTopping1, ivMilk, ivYoghurt, ivTopping2};
        for (ImageView imageView : allImageViews) {
            ObjectAnimator shakeX = ObjectAnimator.ofFloat(imageView, "translationX", -50f, 50f);
            ObjectAnimator shakeY = ObjectAnimator.ofFloat(imageView, "translationY", -50f, 50f);
            ObjectAnimator shakeRotate = ObjectAnimator.ofFloat(imageView, "rotation", -20f, 20f);

            shakeX.setDuration(500);
            shakeY.setDuration(500);
            shakeRotate.setDuration(500);

            shakeX.setRepeatMode(ValueAnimator.REVERSE);
            shakeY.setRepeatMode(ValueAnimator.REVERSE);
            shakeRotate.setRepeatMode(ValueAnimator.REVERSE);

            shakeX.setRepeatCount(5);
            shakeY.setRepeatCount(5);
            shakeRotate.setRepeatCount(5);

            shakeX.start();
            shakeY.start();
            shakeRotate.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent pauseMusicIntent = new Intent(this, MusicService.class);
        pauseMusicIntent.setAction("PAUSE_MUSIC");
        sensorManager.unregisterListener(sensorEventListener);
        startService(pauseMusicIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusicIntent = new Intent(this, MusicService.class);
        resumeMusicIntent.setAction("RESUME_MUSIC");
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        startService(resumeMusicIntent);
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