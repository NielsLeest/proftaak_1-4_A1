package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn_Activity extends AppCompatActivity {
    String firstname;
    String lastName;
    int age;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText ageInput;
    Button toProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

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
        age = Integer.valueOf(ageInput.getText().toString());

        if (!ageIsValid(age)){
            ageInput.setText("");
            return;
        }

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

        Intent intent = new Intent(this, ProfileView_Activity.class);
        startActivity(intent);
    }

    public boolean nameIsValid(String name){
        if (!name.matches("^\\D{2,32}$")){
            return false;
        }
        return true;
    }

    public boolean ageIsValid(int age){
        if (age < 18){
            showToast(getString(R.string.age_invalid));
            return false;
        }
        return true;
    }
}