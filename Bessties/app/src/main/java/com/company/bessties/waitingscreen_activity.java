package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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




    }

    private void waitingforplayer() {


        Intent next = new Intent(this, BuddyInfo_Activity.class);
        client.send("join");
        client.handleConnection();

            if (client.que) {
                startActivity(next);
            }
        }



}