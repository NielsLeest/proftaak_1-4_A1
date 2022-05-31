package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Queue_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_screen);
    }

    public void openBuddyLocation(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        startActivity(intent);
    }

    public void retryQueue(View view) {
        Intent intent = new Intent(this, Queue_Activity.class);
        startActivity(intent);
    }
}