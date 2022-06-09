
package com.company.bessties;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scan_Activity extends AppCompatActivity {
private String message = "";
private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_screen);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }

    public void openLogIn(View view) {
        Intent intent = new Intent(this, LogIn_Activity.class);
        startActivity(intent);
    }

    public void scanCode() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(Scan_Activity.this);

        intentIntegrator.setPrompt("for flash use volume up key");

        intentIntegrator.setBeepEnabled(false);

        intentIntegrator.setOrientationLocked(true);


        intentIntegrator.setCaptureActivity(Capture.class);

        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (intentResult.getContents() != null) {

            message = intentResult.getContents();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent next = new Intent(this,LogIn_Activity.class);
            startActivity(next);

        } else {
            Toast.makeText(getApplicationContext(), "OOPS... you did not scan", Toast.LENGTH_SHORT).show();


        }

    }

}