package com.example.forestfeast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;

public class Recipe extends AppCompatActivity {

    public TextView tvTitle, tvRecipe;
    public Button fabSkip;
    public static int currentLevel;
    public Handler handler;

    public void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvRecipe = findViewById(R.id.tvRecipe);
        fabSkip = findViewById(R.id.fabSkip);
        currentLevel = getIntent().getIntExtra("level", 0);
        Log.d("maya debugging", "init_Recipe"+currentLevel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);

        init();

        hideSystemUI();

        setTitleAndRecipe(currentLevel);

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.recipe_result_music);
        intent.putExtra("LOOPING", false); // Don't loop the music
        startService(intent);

        fabSkip.setOnClickListener(v -> moveToGameplayActivity());

        handler = new Handler();
        handler.postDelayed(this::moveToGameplayActivity, 60000);

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

    private void moveToGameplayActivity() {
        handler.removeCallbacksAndMessages(null); //comment skibidi wow

        stopService(new Intent(this, MusicService.class));

        Log.d("maya debugging", "Recipe"+currentLevel);

        Intent intent = new Intent(Recipe.this, Gameplay.class);
        intent.putExtra("level", currentLevel);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setTitleAndRecipe(int level) {
        switch (level) {
            case 0:
                tvTitle.setText("Recipe for Basic Oats");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        1 tbsp honey
                        1 tspn vanilla extract
                        1 tspn cinnamon""");
                break;
            case 1:
                tvTitle.setText("Recipe for Decorated Meal");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        1 tbsp honey
                        1 tspn vanilla extract
                        1 tspn cinnamon
                        Toppings:
                        Hearty banana slices
                        Biscoff cookie""");
                break;
            case 2:
                tvTitle.setText("Recipe for Banana Cream Pie");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        1tbsp honey
                        1 tspn vanilla extract
                        1 tspn cinnamon
                        Toppings:
                        Vanilla yoghurt
                        Banana slices
                        Crushed caramelized pecans""");
                break;
            case 3:
                tvTitle.setText("Recipe for Apple Spice Treat");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        Chopped 1/4 apple
                        1 tbsp mapel
                        1 tspn vanilla
                        1 tspn cinnamon
                        Toppings:
                        Greek yoghurt
                        Chopped apple
                        Mixed nuts""");
                break;
            case 4:
                tvTitle.setText("Recipe for Cherry Pond");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        Handful of pitted cherries
                        1 tbsp mapel
                        1 tspn vanilla extract
                        1 tspn cinnamon
                        Toppings: Greek yoghurt
                        Pitted cherries
                        Mixed nuts""");
                break;
            case 5:
                tvTitle.setText("Recipe for Beary Delighted");
                tvRecipe.setText("""
                        1 cup milk
                        1/2 cup oats
                        1 tbsp honey
                        1 tbsp cocoa powder
                        Topping:
                        Face details""");
                break;
            case 6:
                tvTitle.setText("Recipe for Strawberry Dream");
                tvRecipe.setText("""
                        1 cup strawberry milk
                        1/2 cup oats
                        1 tbsp honey
                        Toppings:
                        Strawberry yoghurt
                        Strawberry slices
                        Mini marshmallows
                        Hearty sprinkles""");
                break;
            case 7:
                tvTitle.setText("Recipe for Witch's Stew");
                tvRecipe.setText("""
                        1 cup moonlit stardust
                        1/2 cup oats
                        1 tbsp dragon's nectar
                        1 tspn fairy's breath essence
                        1 tspn pheonix feather ash
                        Toppings:
                        Mystic fog pearls
                        Crushed crystal shards""");
                break;
            default:
                tvTitle.setText("Unknown Recipe");
                tvRecipe.setText("No recipe available.");
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
}
