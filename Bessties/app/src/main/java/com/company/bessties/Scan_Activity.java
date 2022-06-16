
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

/**
 * Public class Scan_Activity
 * Creates functionalities for the Scan screen
 */

public class Scan_Activity extends AppCompatActivity {
private String message = "";
private Button button;

    /**
     * Method onCreate
     * Creates Scan screen lay out
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_screen);
        /*
         * Creates new button functionality to scan barcode
         */
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeScan();
            }
        });
    }

    //TODO delete method
    /**
     * Method openLogIn
     * Starts the next screen activity when button is pressed
     * @param view
     */

    public void openLogIn(View view) {
        Intent intent = new Intent(this, LogIn_Activity.class);
        startActivity(intent);
    }

    /**
     * Method barcodeScan
     * Makes scanning of barcode possible
     */

    public void barcodeScan() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(Scan_Activity.this);

        intentIntegrator.setPrompt("for flash use volume up key");

        intentIntegrator.setBeepEnabled(true);

        intentIntegrator.setOrientationLocked(true);

        intentIntegrator.setCaptureActivity(Capture.class);

        intentIntegrator.initiateScan();

    }

    /**
     * Method onActivityResult
     * If correct barcode is scanned it goes to start activity
     * @param requestCode code that needs to be scanned
     * @param resultCode code that is scanned
     */

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