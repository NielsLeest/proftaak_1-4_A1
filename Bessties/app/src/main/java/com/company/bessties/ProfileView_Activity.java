package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

public class ProfileView_Activity extends AppCompatActivity {
    private boolean allowBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_screen);

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

    @Override
    public void onBackPressed() {
        if (allowBack) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openQueue(View view) {
        Intent intent = new Intent(this, Queue_Activity.class);
        startActivity(intent);
    }
}