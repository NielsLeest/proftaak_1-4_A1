package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ToGame_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_game_screen);
    }

    public void openBuddyLocation(View view) {
        Intent intent = new Intent(this, BuddyLocation_Activity.class);
        startActivity(intent);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        startActivity(intent);
    }


    public void showHint(View view) {
        Toast.makeText(this, "hint", Toast.LENGTH_SHORT).show();
    }
}