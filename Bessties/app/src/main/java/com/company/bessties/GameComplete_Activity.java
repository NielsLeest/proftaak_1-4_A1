package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GameComplete_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_complete_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuButton) {
            Intent intent = new Intent(this, ProfileView_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, ToGame_Activity.class);
        startActivity(intent);
    }
}