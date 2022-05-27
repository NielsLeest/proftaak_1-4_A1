package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProfielOverzicht_Scherm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiel_overzicht_scherm);
    }

    public void openWachtrij(View view) {
        Intent intent = new Intent(this, SpelUitleg_Scherm.class);
        startActivity(intent);
    }
}