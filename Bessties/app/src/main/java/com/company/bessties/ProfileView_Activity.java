package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileView_Activity extends AppCompatActivity {

    private int currentImage = 0;
    int[] images = {R.drawable.avatar_icon, R.drawable.appicon, R.drawable.qr_scanner};
    ImageView currentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_screen);

        currentImageView = (ImageView) findViewById(R.id.profilePicture);

    }

    public void openQueue(View view) {
        Intent intent = new Intent(this, Queue_Activity.class);
        startActivity(intent);
    }

    public void leftArrowButton(View view) {
        currentImage--;
        if (currentImage < 0){
            currentImage = images.length - 1;
        }
        currentImageView.setImageResource(images[currentImage]);
    }

    public void rightArrowButton(View view) {
        currentImage++;
        if (currentImage >= images.length){
            currentImage = 0;
        }
        currentImageView.setImageResource(images[currentImage]);
    }
}