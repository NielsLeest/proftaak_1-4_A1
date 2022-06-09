package com.company.bessties;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        this.client = SingleSocket.getInstance().client;


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
//        barcode = barcodeInput.getText().toString();

        this.client.setFirstName(firstname);
        this.client.setLastName(lastName);
        this.client.setAge(age);
        this.client.setBarcode(barcode);

        String isValid = Validation.validateLogin(this.client, this);

        if(isValid.equals("valid")){
            new Thread(()->{
                this.client.send("login/"+firstname+"/"+lastName+"/"+age);
            }).start();
            Intent intent = new Intent(this, ProfileView_Activity.class);
            startActivity(intent);
        }
        else {
            showToast("De gegeven " + isValid + " is geen geldige waarde.");
        }
    }

    public void volgend(View view) {
        Intent intent = new Intent(this, ProfileView_Activity.class);
        startActivity(intent);
    }
}