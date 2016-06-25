package com.pathtoandroidgamedev.memorygamejune;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

    SharedPreferences prefs;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;
    public static int hiScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize our 2 sharedPreferences objects
        prefs = getSharedPreferences(dataName, MODE_PRIVATE);

        //Either load our High score or if not avail our default of 0
        hiScore = prefs.getInt(intName, defaultInt);

        //Make a reference to the Hiscore textview in our layout

        TextView textHiScore = (TextView)findViewById(R.id.textHiScore);
        // Display the hi score
        textHiScore.setText("Hi: " + hiScore);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    public void onClick (View view)
    {
        Intent i;
        i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

}
