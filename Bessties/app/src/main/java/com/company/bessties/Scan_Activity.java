
package com.company.bessties;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scan_Activity extends AppCompatActivity {
private String message = "";
private Button button;
private Client client;

private boolean barcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.client = SingleSocket.getInstance().client;
        new Thread(this.client::startConnection).start();
//        Intent intent = new Intent(Scan_Activity.this, BackgroundSoundService.class);
//        startService(intent);

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

            Thread thread = new Thread(() -> {
                this.barcheck = client.sendBarcode(message);

            });
           thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(this.barcheck) {
               startActivity(next);
           }

        } else {
            Toast.makeText(getApplicationContext(), "OOPS... you did not scan", Toast.LENGTH_SHORT).show();


        }

    }

}