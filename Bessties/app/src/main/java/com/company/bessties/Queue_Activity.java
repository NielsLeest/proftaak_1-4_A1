package com.company.bessties;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

public class Queue_Activity extends AppCompatActivity {
    private Client client;
    private String opponentName;
    private String age;
    TextView name;
    TextView ageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_screen);
        name =  (TextView)findViewById(R.id.textView5);
        ageBox = (TextView)findViewById(R.id.textView7);
        this.client = SingleSocket.getInstance().client;

        client.send("pending");

        Thread t1 = new Thread(()-> { setCard();
        });
        t1.start();

//        ActionBar actionBar = getSupportActionBar();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuButton) {
            Intent intent = new Intent(this, ProfileView_Activity.class);
            startActivity(intent);
        } else if (id == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openBuddyLocation(View view) {
        client.send("accept");
        Intent intent = new Intent(this, BuddyLocation_Activity.class);
        startActivity(intent);
    }

    public void retryQueue(View view) {
        client.send("decline");
        Intent intent = new Intent(this, Queue_Activity.class);
        startActivity(intent);
    }

    public void setCard(){

        opponentName= client.read();
        age = client.read();


        name.setText(this.opponentName);
        ageBox.setText(this.age);
    }
}