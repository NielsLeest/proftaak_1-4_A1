package com.company.bessties;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.bessties.socket.Client;

public class LogIn_Activity extends AppCompatActivity {
    String firstname;
    String lastName;
    int age;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText ageInput;
    Button toProfileButton;
    Client client;
    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

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

        try {
            age = Integer.valueOf(ageInput.getText().toString());
            if (!ageIsValid(age)){
                ageInput.setText("");
                showToast(getString(R.string.age_invalid));
                return;
            }
        }catch (Exception e){
            return;
        };

        if (!nameIsValid(firstname)){
            firstNameInput.setText("");
            showToast(getString(R.string.firstname_invalid));
            return;
        }
        if (!nameIsValid(lastName)){
            lastNameInput.setText("");
            showToast(getString(R.string.lastname_invalid));
            return;
        }

        
        Thread thread = new Thread(() -> {
            isValid = this.client.sendLogin(firstname, lastName);

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, ProfileView_Activity.class);
        startActivity(intent);
    }

    public boolean nameIsValid(String name){
        if (!name.matches("^[a-zA-Záéíóúàèìòùâêîôûãõñç]{2,32}$")){
            return false;
        }
        return true;
    }

    public boolean ageIsValid(int age){
        if (age < 18){
            return false;
        }
        return true;
    }

    public void volgend(View view) {
        Intent intent = new Intent(this, ProfileView_Activity.class);
        startActivity(intent);
    }
}