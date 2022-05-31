package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameInfo_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info_screen);
    }

    public void openSpelVoltooid(View view) {
        Intent intent = new Intent(this, GameComplete_Activity.class);
        startActivity(intent);
    }
}