package com.company.bessties;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

/**
 * Public class LogIn_Activity
 * Creates functionalities for the LogIn screen
 */

public class LogIn_Activity extends AppCompatActivity {
    String firstname;
    String lastName;
    int age;
    String barcode;
    int image;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText ageInput;
    EditText barcodeInput;
    Client client;

    /*
    Adds images to int array for profile pictures
     */

    private int currentImage = 0;
    int[] images = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5,
            R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8};
    ImageView currentImageView;

    /**
     * Method onCreate
     * Creates LogIn screen lay-out
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        this.client = SingleSocket.getInstance().client;

        currentImageView = (ImageView) findViewById(R.id.profilePicture);

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        ageInput = (EditText) findViewById(R.id.ageInput);
    }

    /**
     * Method showToast
     * Shows error message when input is invalid
     * @param text
     */

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method openProfile
     * Gets input and calls isValid
     * @param view
     */

    public void openProfile(View view) {
        firstname = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        try{
            age = Integer.valueOf(ageInput.getText().toString());
        }
        catch (Exception e){
            return;
        }
        image = images[currentImage];

//        barcode = barcodeInput.getText().toString();

        this.client.setFirstName(firstname);
        this.client.setLastName(lastName);
        this.client.setAge(age);
        this.client.setBarcode(barcode);
        this.client.setImage(image);

        String isValid = Validation.validateLogin(this.client, this);

        if(isValid.equals("valid")){
            new Thread(()->{
                this.client.send("login/"+firstname+"/"+lastName+"/"+age+"/"+image);
            }).start();
            pictureHandler.saveImageID(images[currentImage]);
            Intent intent = new Intent(this, ProfileView_Activity.class);
            startActivity(intent);
        }
        else {
            showToast("De gegeven " + isValid + " is geen geldige waarde.");
        }
    }

    /**
     * Method toProfileView
     * Starts the next screen activity when button is pressed and saves chosen profile picture
     * @param view
     */

    public void toProfileView(View view) {
        pictureHandler.saveImageID(images[currentImage]); //Saves ID of selected image
        Intent intent = new Intent(this, ProfileView_Activity.class);
        intent.putExtra("firstTime", true);
        startActivity(intent);
    }

    /**
     * Method leftArrowButton
     * Cycles through profile images to the left
     * @param view
     */

    public void leftArrowButton(View view) {
        currentImage--;
        if (currentImage < 0){
            currentImage = images.length - 1;
        }
        currentImageView.setImageResource(images[currentImage]);
    }

    /**
     * Method leftArrowButton
     * Cycles through profile images to the right
     * @param view
     */

    public void rightArrowButton(View view) {
        currentImage++;
        if (currentImage >= images.length){
            currentImage = 0;
        }
        currentImageView.setImageResource(images[currentImage]);
    }

}