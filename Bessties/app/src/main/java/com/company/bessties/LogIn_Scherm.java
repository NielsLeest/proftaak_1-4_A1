package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn_Scherm extends AppCompatActivity {
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
        setContentView(R.layout.activity_log_in_scherm);

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        ageInput = (EditText) findViewById(R.id.ageInput);
        toProfileButton = (Button) findViewById(R.id.toProfileButton);
        toProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 firstname = firstNameInput.getText().toString();
                 lastName = lastNameInput.getText().toString();
                 age = Integer.valueOf(ageInput.getText().toString());
                 //TODO Add code to go to next page (= profile page)

                 showToast(firstname);
                 showToast(lastName);
                 showToast(String.valueOf(age));
            }
        });
    }

    //TODO Remove this code when name + age can be sended to server
    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}