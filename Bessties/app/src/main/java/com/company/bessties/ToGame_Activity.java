package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Public class ToGame_Activity
 * Creates functionalities for the ToGame screen
 */

public class ToGame_Activity extends AppCompatActivity {

    /**
     * Method onCreate
     * Created lay-out of screen
     * @param savedInstanceState references to activity bundle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_game_screen);
    }

    /**
     * Method onCreateOptionsMenu
     * Creates menubar.xml in ToGame screen
     * @param menu references to menubar.xml
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method onOptionsItemSelected
     * Selects item from options
     * @param item to be selected
     * @return true if item is selected
     */

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

    /**
     * Method openGameScreen
     * Starts the next screen activity when button is pressed
     */

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        startActivity(intent);
    }

    /**
     * Method showHint
     * Shows toast message of hint
     */

    public void showHint(View view) {
        Toast.makeText(this, getText(R.string.hint_text), Toast.LENGTH_LONG).show();
    }
}