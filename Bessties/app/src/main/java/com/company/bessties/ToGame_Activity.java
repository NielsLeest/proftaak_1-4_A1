package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ToGame_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_game_screen);
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
        } else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        startActivity(intent);
    }

    public void showHint(View view) {
        Toast.makeText(this, getText(R.string.hint_text), Toast.LENGTH_LONG).show();
    }
}