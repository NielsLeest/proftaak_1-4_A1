package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BuddyLocation_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_location);
    }

    public void openToGameScreen(View view) {
        Intent intent = new Intent(this, ToGame_Activity.class);
        startActivity(intent);
    }
}