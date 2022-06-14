package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

public class waitingscreen_activity extends AppCompatActivity {
    private Client client;
    private Boolean que = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingscreen);

        this.client = SingleSocket.getInstance().client;

        client.send("join");
        new Thread(()->client.handleConnection()).start();


        Intent next = new Intent(this, Queue_Activity.class);

        new Thread(()->{
            waitingforplayer();
        }).start();

        if(que){
            startActivity(next);
        }


    }

    private void waitingforplayer() {
        while (!client.que){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        que = true;


    }
}