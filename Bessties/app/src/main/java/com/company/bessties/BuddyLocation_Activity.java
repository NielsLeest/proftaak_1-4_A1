package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Public class BuddyLocation_Activity
 * Creates functionalities for the GameComplete screen
 */

public class BuddyLocation_Activity extends AppCompatActivity {

    /**
     * Method onCreate
     * Creates GameComplete screen lay-out
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_location);
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
     * Method openToGameScreen
     * Starts the next screen activity when button is pressed
     * @param view
     */

    public void openToGameScreen(View view) {
        Intent intent = new Intent(this, ToGame_Activity.class);
        startActivity(intent);
    }
}