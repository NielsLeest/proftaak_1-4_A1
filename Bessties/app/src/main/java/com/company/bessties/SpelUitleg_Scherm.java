package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpelUitleg_Scherm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spel_uitleg_scherm);
    }

    public void openSpelVoltooid(View view) {
        Intent intent = new Intent(this, SpelVoltooid_Scherm.class);
        startActivity(intent);
    }
}