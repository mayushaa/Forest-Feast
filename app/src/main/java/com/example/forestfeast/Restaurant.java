package com.example.forestfeast;

import android.content.Intent;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;

import android.util.Log;

public class Restaurant extends AppCompatActivity {

    public ImageView ivPicture;
    public ImageButton ibOrder;
    public int[] imageResources = {
            R.drawable.in_critic, R.drawable.in_squirrel, R.drawable.in_fox,
            R.drawable.in_deer, R.drawable.in_raccoon, R.drawable.in_bear,
            R.drawable.in_bunny, R.drawable.in_baba_angry
    };
    public int[] buttonResources = {
            R.drawable.ib_basic_oats, R.drawable.ib_decorated_meal, R.drawable.ib_banana_cream_pie,
            R.drawable.ib_apple_pie_treat, R.drawable.ib_cherry_pond, R.drawable.ib_beary_delighted,
            R.drawable.ib_strawberry_dream, R.drawable.ib_witches_stew
    };
    public static int currentLevel;

    public void init() {
        ivPicture = findViewById(R.id.ivPicture);
        ibOrder = findViewById(R.id.ibOrder);
        currentLevel = getIntent().getIntExtra("level", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant);

        init();

        hideSystemUI();

        Log.d("maya debugging", "Restaurant: Current Level: " + currentLevel);

        if(currentLevel == 0)
        {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("MUSIC_RES_ID", R.raw.theme);
            intent.putExtra("LOOPING", false); // Don't loop the music
            startService(intent);
        }

        setCurrentLevelImages();

        ibOrder.setOnClickListener(v -> {
            navigateToRecipe();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusicIntent = new Intent(this, MusicService.class);
        resumeMusicIntent.setAction("RESUME_MUSIC");
        startService(resumeMusicIntent);
    }

    private void setCurrentLevelImages()
    {
        switch (currentLevel) {
            case 0:
                ivPicture.setImageResource(imageResources[0]);
                ibOrder.setImageResource(buttonResources[0]);
                break;
            case 1:
                ivPicture.setImageResource(imageResources[1]);
                ibOrder.setImageResource(buttonResources[1]);
                break;
            case 2:
                ivPicture.setImageResource(imageResources[2]);
                ibOrder.setImageResource(buttonResources[2]);
                break;
            case 3:
                ivPicture.setImageResource(imageResources[3]);
                ibOrder.setImageResource(buttonResources[3]);
                break;
            case 4:
                ivPicture.setImageResource(imageResources[4]);
                ibOrder.setImageResource(buttonResources[4]);
                break;
            case 5:
                ivPicture.setImageResource(imageResources[5]);
                ibOrder.setImageResource(buttonResources[5]);
                break;
            case 6:
                ivPicture.setImageResource(imageResources[6]);
                ibOrder.setImageResource(buttonResources[6]);
                break;
            case 7:
                ivPicture.setImageResource(imageResources[7]);
                ibOrder.setImageResource(buttonResources[7]);
                break;
            default:
                // Handle default case or error
                ivPicture.setImageResource(imageResources[imageResources.length - 1]);
                ibOrder.setImageResource(buttonResources[buttonResources.length - 1]);
                break;
        }
    }

    public void navigateToRecipe()
    {
        stopService(new Intent(this, MusicService.class));

        Log.d("maya debugging", "Restaurant"+currentLevel);

        Intent intent = new Intent(Restaurant.this, Recipe.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.click);
        intent.putExtra("level", currentLevel);
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
