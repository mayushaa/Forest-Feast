package com.example.forestfeast;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forestfeast.R;

public class Gameplay extends AppCompatActivity {
    public ImageView ivBg;
    public ImageButton ibAns1, ibAns2, ibAns3, ibAns4;
    public TextView tvPoints;
    public TextView tvAns1, tvAns2, tvAns3, tvAns4;
    public Question[] questions;
    private static int currentQuestion = 0;
    private static int currentLevel;
    public static int correctCounter = 0;
    private CountDownTimer timer;

    public void init ()
    {
        ivBg = findViewById(R.id.ivBg);
        ibAns1 = findViewById(R.id.ibAns1);
        ibAns2 = findViewById(R.id.ibAns2);
        ibAns3 = findViewById(R.id.ibAns3);
        ibAns4 = findViewById(R.id.ibAns4);
        tvAns1 = findViewById(R.id.tvAns1);
        tvAns2 = findViewById(R.id.tvAns2);
        tvAns3 = findViewById(R.id.tvAns3);
        tvAns4 = findViewById(R.id.tvAns4);
        tvPoints = findViewById(R.id.tvPoints);
        currentLevel = getIntent().getIntExtra("level", 0);
        Log.d("maya debugging", "init gameplay "+currentLevel);
    }

    private void startLevelTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish()
            {
                Log.d("maya debugging","time's up!"+currentLevel);
                navigateToShake();
            }
        };
        timer.start();
    }

    private void displayQuestion() {
        Question current = questions[currentQuestion];

        Log.d("maya debugging", "question "+currentQuestion);
        Log.d("maya debugging", "before display "+current.toString());

        ibAns1.setImageResource(current.getAnswerImageIds()[0]);
        ibAns2.setImageResource(current.getAnswerImageIds()[1]);
        ibAns3.setImageResource(current.getAnswerImageIds()[2]);
        ibAns4.setImageResource(current.getAnswerImageIds()[3]);
        tvAns1.setText(current.getAnswerTexts()[0]);
        tvAns2.setText(current.getAnswerTexts()[1]);
        tvAns3.setText(current.getAnswerTexts()[2]);
        tvAns4.setText(current.getAnswerTexts()[3]);

        Log.d("maya debugging", "after display "+current.toString());

    }

    private void setupAnswerButtons() {
        Log.d("maya debugging", "setUpAnswerButtons");
        setupAnswerButton(ibAns1, 0);
        setupAnswerButton(ibAns2, 1);
        setupAnswerButton(ibAns3, 2);
        setupAnswerButton(ibAns4, 3);
    }

    private void setupAnswerButton(ImageButton button, int answerId){
        button.setOnClickListener(v -> {
            Log.d("maya debugging", "before setUpAnswerButton"+answerId+","+button);

            Question question = questions[currentQuestion];
            Log.d("maya debugging", "after setUpAnswerButton"+question.toString());
            if (question.isCorrectAnswer(answerId)) {
                correctCounter++;
                tvPoints.setText(String.valueOf(correctCounter * 15));
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                nextQuestion();
            } else
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        });
    }

    private void nextQuestion() {
        currentQuestion++;

        Log.d("maya debugging", "current question" + currentQuestion);

        if (currentQuestion < questions.length) {
            displayQuestion();
        } else {
            Log.d("maya debugging", "next question");
            navigateToShake();
        }
    }

    private void navigateToShake() {

        Log.d("maya debugging", "navigateToShake "+currentLevel);
        if (timer != null) timer.cancel();

        Log.d("maya", "navigate from gameplay 1 "+currentLevel);

        Intent intent = new Intent(Gameplay.this, Shake.class);
        intent.putExtra("level", currentLevel);
        Log.d("maya debugging", "navigate from gameplay 2 "+currentLevel);
        Log.d("maya debugging", "correct counter "+correctCounter);
        intent.putExtra("correctCounter", correctCounter);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        if (timer != null) timer.cancel();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gameplay);

        init();

        Log.d("maya debugging" , "OnCreate" + currentLevel);

        hideSystemUI();

        if(currentLevel == 5)
            ivBg.setImageResource(R.drawable.game_chocolate);
        else if (currentLevel == 6)
            ivBg.setImageResource(R.drawable.game_strawberry);
        else if(currentLevel == 7)
            ivBg.setImageResource(R.drawable.game_witch);

        tvPoints.setText(String.valueOf(0));
        correctCounter = 0;
        currentQuestion = 0;

        Log.d("maya debugging", "game_music"+currentLevel);

        Log.d("OnCreate" , "before service" + currentLevel);

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("MUSIC_RES_ID", R.raw.game_music);
        intent.putExtra("LOOPING", false);
        startService(intent);

        Log.d("maya debugging" , "loadQuestionsForCurrentLevel" + currentLevel);

        loadQuestionsForCurrentLevel();
        Log.d("maya debugging" , "displayQuestion" + currentLevel);

        displayQuestion();

        Log.d("maya debugging" , "setupAnswerButtons" + currentLevel);
        setupAnswerButtons();

        Log.d("maya debugging" , "startLevelTimer" + currentLevel);
        startLevelTimer();
        Log.d("maya debugging" , "after service" + currentLevel);

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
        // Resume music when activity is resumed (brought to the foreground)
        Intent resumeMusicIntent = new Intent(this, MusicService.class);
        resumeMusicIntent.setAction("RESUME_MUSIC");
        startService(resumeMusicIntent);
    }

    private void loadQuestionsForCurrentLevel() {

        Log.d("maya debugging", "level"+currentLevel);

        switch (currentLevel) {
            case 0: //Basic Oats
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry \nmilk", "Moonlit \nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"},1)
                };
                break;

            case 1: // Decorated Meal
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 1),
                        new Question(new int[]{R.drawable.banana_hearts, R.drawable.strawberries, R.drawable.cherries, R.drawable.apples}, new String[]{"Hearty\nbananas", "Strawberries", "Cherries", "Apples"}, 0),
                        new Question(new int[]{R.drawable.marshmallow, R.drawable.sprinkles, R.drawable.biscoff, R.drawable.face}, new String[]{"Mini\nmarshmallows", "Sprinkles", "Biscoff\ncookie", "Face\ndetails"}, 2)
                };
                break;

            case 2: // Banana Cream Pie
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 1),
                        new Question(new int[]{R.drawable.greek_yoghurt, R.drawable.vanilla_yoghurt, R.drawable.strawberry_yoghurt, R.drawable.pearl_yoghurt}, new String[]{"Greek\nyoghurt", "Vanilla\nyoghurt", "Strawberry\nyoghurt", "Mystic fog\npearls"}, 1),
                        new Question(new int[]{R.drawable.bananas, R.drawable.strawberries, R.drawable.cherries, R.drawable.apples}, new String[]{"Hearty\nbananas", "Strawberries", "Cherries", "Apples"}, 0),
                        new Question(new int[]{R.drawable.nuts, R.drawable.pecans, R.drawable.crystals, R.drawable.sprinkles}, new String[]{"Nuts", "Pecans", "Crushed\ncrystals", "sprinkles"}, 1)
                };
                break;

            case 3: // Apple Spice Treat
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 1),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 1),
                        new Question(new int[]{R.drawable.greek_yoghurt, R.drawable.vanilla_yoghurt, R.drawable.strawberry_yoghurt, R.drawable.pearl_yoghurt}, new String[]{"Greek\nyoghurt", "Vanilla\nyoghurt", "Strawberry\nyoghurt", "Mystic fog\npearls"}, 0),
                        new Question(new int[]{R.drawable.bananas, R.drawable.strawberries, R.drawable.cherries, R.drawable.apples}, new String[]{"Hearty\nbananas", "Strawberries", "Cherries", "Apples"}, 3),
                    new Question(new int[]{R.drawable.nuts, R.drawable.pecans, R.drawable.crystals, R.drawable.sprinkles}, new String[]{"Nuts", "Pecans", "Crushed\ncrystals", "Sprinkles"}, 0)
                };
                break;

            case 4: // Cherry Pond
                Log.d("maya debugging", "cherry pond questions");
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats},
                                new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"},
                                0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats},
                                new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"},
                                3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon},
                                new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"},
                                1),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix},
                                new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"},
                                0),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix},
                                new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"},
                                1),
                        new Question(new int[]{R.drawable.greek_yoghurt, R.drawable.vanilla_yoghurt, R.drawable.strawberry_yoghurt, R.drawable.pearl_yoghurt},
                                new String[]{"Greek\nyoghurt", "Vanilla\nyoghurt", "Strawberry\nyoghurt", "Mystic fog\npearls"},
                                0),
                        new Question(new int[]{R.drawable.bananas, R.drawable.strawberries, R.drawable.cherries, R.drawable.apples},
                                new String[]{"Hearty\nbananas", "Strawberries", "Cherries", "Apples"},
                                2),
                        new Question(new int[]{R.drawable.nuts, R.drawable.pecans, R.drawable.crystals, R.drawable.sprinkles},
                                new String[]{"Nuts", "Pecans", "Crushed\ncrystals", "Sprinkles"},
                                0)
                };
                for (int i=0;i<questions.length;i++)
                {
                    Question q=questions[i];
                    Log.d("maya debugging", "current q"+q.getCorrectAnswerIndex()+","+q.getAnswerTexts());


                }
                Log.d("maya debugging", "cherry pond questions after");

                break;

            case 5: // Beary Delighted
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 0),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 0),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 2),
                        new Question(new int[]{R.drawable.marshmallow, R.drawable.sprinkles, R.drawable.biscoff, R.drawable.face}, new String[]{"Mini\nmarshmallows", "Hearty\nsprinkles", "Biscoff\ncookie", "Face\ndetails"}, 3)
                };
                break;

            case 6: // Strawberry Dream
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 1),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 0),
                        new Question(new int[]{R.drawable.greek_yoghurt, R.drawable.vanilla_yoghurt, R.drawable.strawberry_yoghurt, R.drawable.pearl_yoghurt}, new String[]{"Greek\nyoghurt", "Vanilla\nyoghurt", "Strawberry\nyoghurt", "Mystic fog\npearls"}, 2),
                        new Question(new int[]{R.drawable.bananas, R.drawable.strawberries, R.drawable.cherries, R.drawable.apples}, new String[]{"Hearty\nbananas", "Strawberries", "Cherries", "Apples"}, 1),
                        new Question(new int[]{R.drawable.marshmallow, R.drawable.sprinkles, R.drawable.biscoff, R.drawable.face}, new String[]{"Mini\nmarshmallows", "Sprinkles", "Biscoff\ncookie", "Face\ndetails"}, 0),
                        new Question(new int[]{R.drawable.nuts, R.drawable.pecans, R.drawable.crystals, R.drawable.sprinkles}, new String[]{"Nuts", "Pecans", "Crushed\ncrystals", "Sprinkles"}, 3)
                };
                break;

            case 7: // Witch's Stew
                questions = new Question[]{
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 2),
                        new Question(new int[]{R.drawable.plain_milk, R.drawable.strawberry_milk, R.drawable.moonlit_milk, R.drawable.oats}, new String[]{"Plain\nmilk", "Strawberry\nmilk", "Moonlit\nmilk", "Oats"}, 3),
                        new Question(new int[]{R.drawable.honey, R.drawable.mapel, R.drawable.cacao, R.drawable.dragon}, new String[]{"Honey", "Mapel", "Cacao", "Dragon's\nnectar"}, 3),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 2),
                        new Question(new int[]{R.drawable.vanilla, R.drawable.cinnamon, R.drawable.fairy, R.drawable.pheonix}, new String[]{"Vanilla", "Cinnamon", "Fairy's\nbreath", "Pheonix\nfeather"}, 3),
                        new Question(new int[]{R.drawable.greek_yoghurt, R.drawable.vanilla_yoghurt, R.drawable.strawberry_yoghurt, R.drawable.pearl_yoghurt}, new String[]{"Greek\nyoghurt", "Vanilla\nyoghurt", "Strawberry\nyoghurt", "Mystic fog\npearls"}, 3),
                        new Question(new int[]{R.drawable.nuts, R.drawable.pecans, R.drawable.crystals, R.drawable.sprinkles}, new String[]{"Nuts", "Pecans", "Crushed\ncrystals", "Face\ndetails"}, 2)
                };
                break;

            default:
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