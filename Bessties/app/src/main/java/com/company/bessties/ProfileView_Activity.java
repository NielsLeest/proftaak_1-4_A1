package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

/**
 * Public class ProfileView_Activity
 * Creates functionalities for the ProfileView screen
 */

public class ProfileView_Activity extends AppCompatActivity {
    private boolean allowBack = true;

    /**
     * Method onCreate
     * Creates profileview screen lay-out
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_screen);

        /*
        Checks if user has logged in before. If so continue where he left of.
         */

        try {
            if (getIntent().getExtras().getBoolean("firstTime")) {
                Objects.requireNonNull(getSupportActionBar()).hide();
                this.allowBack = false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        ImageView image = (ImageView) findViewById(R.id.profileViewPicture);
        image.setImageResource(pictureHandler.getImageID());
    }

    /**
     * Method onBackPressed
     * Allows to go back to previous activity
     */

    @Override
    public void onBackPressed() {
        if (allowBack) {
            super.onBackPressed();
        }
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

        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method openQueue
     * Starts the next screen activity when button is pressed
     * @param view
     */

    public void openQueue(View view) {
        Intent intent = new Intent(this, Queue_Activity.class);
        startActivity(intent);
    }
}