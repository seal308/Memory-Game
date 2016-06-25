package com.pathtoandroidgamedev.memorygamejune;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener {

    Drawable defaultButtonBackgroundDrawable;

    //animation
    Animation wobble;

    // for high scores
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;
    int hiScore;

    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    TextView textScore;
    TextView textDifficulty;
    TextView textWatchGo;

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button buttonReplay;

    // Variables for our thread
    int difficultyLevel = 3;
    int difficultyText = 0;
    // An array to hold the randomly generated sequence
    int[] sequenceToCopy = new int[100];

    private Handler myHandler;
    //are we playing a sequence at the moment?
    boolean playSequence = false;
    //And which element of the sequence are we on
    int elementToPlay = 0;

    //For checking the players answer
    int playerResponses;
    int playerScore;
    boolean isResponding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //animation
        wobble = AnimationUtils.loadAnimation(this, R.anim.wobble);

        //phase 4 initialize SharedPreferences
        prefs = getSharedPreferences(dataName, MODE_PRIVATE);
        editor = prefs.edit();
        hiScore = prefs.getInt(intName, defaultInt);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor,0);
        }
        catch (IOException e){

        }

        textScore = (TextView)findViewById(R.id.textScore);
        textScore.setText("Score: " + playerScore);
        textDifficulty = (TextView)findViewById(R.id.textDifficulty);
        textDifficulty.setText("Level: " + difficultyText);
        textWatchGo = (TextView)findViewById(R.id.textWatchGo);

        button1 = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        defaultButtonBackgroundDrawable = button1.getBackground();
        buttonReplay = (Button)findViewById(R.id.buttonReplay);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        buttonReplay.setOnClickListener(this);

        // This is the code which will define our thread
        myHandler = new Handler() {
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if(playSequence){
                    // All the thread action will go here
                    // make sure all the buttons are made visible
                    /*
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.VISIBLE);*/

                    button1.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                    button2.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                    button3.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                    button4.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));

                    switch(sequenceToCopy[elementToPlay]){
                        case 1:
                            //hide a button
                            //button1.setVisibility(View.INVISIBLE);
                            button1.startAnimation(wobble);
                            button1.setBackgroundColor(Color.RED);
                            // play a sound
                            soundPool.play(sample1, 1, 1, 0, 0, 1);
                            break;

                        case 2:
                            //hide a button
                            //button2.setVisibility(View.INVISIBLE);
                            button2.startAnimation(wobble);
                            button2.setBackgroundColor(Color.BLUE);
                            // play a sound
                            soundPool.play(sample2, 1, 1, 0, 0, 1);
                            break;

                        case 3:
                            //hide a button
                            //button3.setVisibility(View.INVISIBLE);
                            button3.startAnimation(wobble);
                            button3.setBackgroundColor(Color.YELLOW);
                            // play a sound
                            soundPool.play(sample3, 1, 1, 0, 0, 1);
                            break;

                        case 4:
                            //hide a button
                            //button4.setVisibility(View.INVISIBLE);
                            button4.startAnimation(wobble);
                            button4.setBackgroundColor(Color.GREEN);
                            // play a sound
                            soundPool.play(sample4, 1, 1, 0, 0, 1);
                            break;
                        case 5:
                            button1.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                            button2.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                            button3.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                            button4.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
                            break;

                    }
                    /*

                    elementToPlay++;

                    if(elementToPlay == difficultyLevel){
                        sequenceFinished();;
                    }*/

                    if (sequenceToCopy[elementToPlay] == 5)
                    {
                        sequenceFinished();
                    }
                    else if (elementToPlay==difficultyLevel)
                    {
                        sequenceToCopy[elementToPlay] = 5;
                    }
                    else
                    {
                        elementToPlay++;
                    }

                    /*

                    if(elementToPlay == difficultyLevel){
                        elementToPlay--;
                        sequenceToCopy[elementToPlay] = 5;

                    }
                    else (sequenceToCopy[])
                    {
                        sequenceFinished();
                    }*/

                }
                //myHandler.sendEmptyMessageDelayed(0,900);
                myHandler.sendEmptyMessageDelayed(0,900);
            }
        }; //end of thread

        myHandler.sendEmptyMessage(0);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick(View view)
    {
        if (!playSequence){//only accept input if sequence not playing
            switch (view.getId())
            {
                //case stements here...
                case R.id.button:
                    soundPool.play(sample1, 1, 1, 0, 0, 1);
                    checkElement(1);
                    break;
                case R.id.button2:
                    soundPool.play(sample2, 1, 1, 0, 0, 1);
                    checkElement(2);
                    break;
                case R.id.button3:
                    soundPool.play(sample3, 1, 1, 0, 0, 1);
                    checkElement(3);
                    break;
                case R.id.button4:
                    soundPool.play(sample4, 1, 1, 0, 0, 1);
                    checkElement(4);
                    break;
                case R.id.buttonReplay:
                    difficultyLevel = 3;
                    difficultyText = 0;
                    playerScore = 0;
                    textScore.setText("Score: "+ playerScore);
                    textDifficulty.setText("Level: "+ difficultyText);
                    playASequence();
                    break;

            }
        }
    }

    public void checkElement(int thisElement)
    {
        if (isResponding)
        {
            playerResponses++;
            if (sequenceToCopy[playerResponses-1]== thisElement)
            {
                // correct
                playerScore = playerScore + ((thisElement + 1) * 2);
                textScore.setText("Score = " + playerScore);
                if (playerResponses == difficultyLevel)
                {
                    // Got the whole sequence
                    // don't checkElement anymore
                    isResponding = false;
                    difficultyLevel ++;
                    difficultyText++;
                    textDifficulty.setText("Level: " + difficultyText);
                    playASequence();
                }
            }
            else
            {
                // wrong answer
                textWatchGo.setText("FAILED!");
                //don't checkElement anymore
                isResponding = false;

                // for highscores, phase 4
                if (playerScore > hiScore)
                {
                    hiScore = playerScore;
                    editor.putInt(intName, hiScore);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "New Hi-score", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void createSequence()
    {
        //for choosing a random button
        Random randInt = new Random();
        int ourRandom;
        for (int i = 0; i < difficultyLevel; i++){
            ourRandom = randInt.nextInt(4);
            ourRandom++; //make sure # is not zero
            //save that number to our array
            sequenceToCopy[i] = ourRandom;
        }
    }

    public void playASequence()
    {
        createSequence();
        isResponding = false;
        elementToPlay = 0;
        playerResponses = 0;
        textWatchGo.setText("Watch!");
        playSequence = true;
    }

    public void sequenceFinished()
    {
        // problem is after the last part of pattern, all auto visibile so don't see invisible one. (Too fast)
        playSequence = false;
        //make sure all the buttons are made visible

        /*
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);*/
        /*
        button1.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
        button2.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
        button3.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
        button4.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));*/
        textWatchGo.setText("Go!");
        isResponding = true;
    }
}
