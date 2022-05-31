package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameComplete_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_complete_screen);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        startActivity(intent);
    }
}