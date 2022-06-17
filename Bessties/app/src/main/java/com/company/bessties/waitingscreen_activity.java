package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

public class waitingscreen_activity extends AppCompatActivity {
    private Client client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingscreen);
        this.client = SingleSocket.getInstance().client;
        new Thread(()->waitingforplayer()).start();

        String loadingInfo = getIntent().getStringExtra("loadingInfo");
        Log.i("msg", loadingInfo);
        TextView loadInfoText = findViewById(R.id.textView);
        loadInfoText.setText(loadingInfo);

    }

    private void waitingforplayer() {


        Intent next = new Intent(this, Queue_Activity.class);
        client.send("join");
        client.handleConnection();

            if (client.que) {
                startActivity(next);
            }
        }



}