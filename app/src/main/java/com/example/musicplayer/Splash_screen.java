package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.SystemClock.sleep;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread sleeping = new Thread(new Runnable() {
            @Override
            public void run() {
                sleep(750);
                Intent i = new Intent(Splash_screen.this , MainActivity.class);
                startActivity(i);

            }
        });

        sleeping.start();

    }




}