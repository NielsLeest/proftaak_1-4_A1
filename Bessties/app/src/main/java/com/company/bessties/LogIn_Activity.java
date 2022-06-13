package com.company.bessties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.bessties.socket.Client;

import java.io.ByteArrayOutputStream;

public class LogIn_Activity extends AppCompatActivity {
    String firstname;
    String lastName;
    int age;
    String barcode;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText ageInput;
    EditText barcodeInput;
    Button toProfileButton;
    Client client;
    boolean isValid = false;

    private int currentImage = 0;
    int[] images = {R.drawable.avatar_icon, R.drawable.appicon, R.drawable.qr_scanner};
    ImageView currentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        currentImageView = (ImageView) findViewById(R.id.profilePicture);

        this.client = new Client();
        new Thread(this.client::startConnection).start();

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        ageInput = (EditText) findViewById(R.id.ageInput);
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void openProfile(View view) {
        firstname = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        try{
            age = Integer.valueOf(ageInput.getText().toString());
        }
        catch (Exception e){
            return;
        }
        barcode = barcodeInput.getText().toString();

        this.client.setFirstName(firstname);
        this.client.setLastName(lastName);
        this.client.setAge(age);
        this.client.setBarcode(barcode);

        String isValid = Validation.validateLogin(this.client, this);

        if(isValid.equals("valid")){
            Intent intent = new Intent(this, ProfileView_Activity.class);
            startActivity(intent);
        }
        else {
            showToast("De gegeven " + isValid + " is geen geldige waarde.");
        }
    }

    public void volgend(View view) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), images[currentImage]);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(this, ProfileView_Activity.class);
        intent.putExtra("picture", byteArray);
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