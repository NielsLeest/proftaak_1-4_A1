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



        Intent next = new Intent(this, Queue_Activity.class);

      Thread t1 =  new Thread(()->{
            client.handleConnection();
//            waitingforplayer();
        });
      t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(client.que){
            startActivity(next);
        }


    }

//    private void waitingforplayer() {
//        while (!client.que){
//            try {
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        que = true;
//
//
//    }
}