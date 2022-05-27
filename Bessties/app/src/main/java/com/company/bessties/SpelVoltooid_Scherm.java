package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpelVoltooid_Scherm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spel_voltooid_scherm);
    }

    public void openProfiel(View view) {
        Intent intent = new Intent(this, ProfielOverzicht_Scherm.class);
        startActivity(intent);
    }
}