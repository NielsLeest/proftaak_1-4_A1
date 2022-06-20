package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

/**
 * Public class Queue_Activity
 * Creates functionalities for the Queue screen
 */

public class BuddyInfo_Activity extends AppCompatActivity {
    private Client client;
    private String buddyName;
    private String age;
    private int image;
    TextView name;
    TextView ageBox;
    ImageView imageView;

    /**
     * Method onCreate
     * Creates queue screen lay-out
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_info_screen);
        name =  (TextView)findViewById(R.id.foundBuddy_firstName);
        ageBox = (TextView)findViewById(R.id.foundBuddy_age);
        imageView = (ImageView)findViewById(R.id.image_found_buddy);
        this.client = SingleSocket.getInstance().client;

        client.send("pending");
    /**
     * Method onCreateOptionsMenu
     * Creates menubar.xml in ToGame screen
     * @param menu references to menubar.xml
     * @return
     */

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

    /**
     * Method onOptionsItemSelected
     * Selects item from options
     * @param item to be selected
     * @return true if item is selected
     */

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

    /**
     * Method openBuddyLocation
     * Starts the next screen activity when button is pressed
     * @param view
     */

    public void openBuddyLocation(View view) {
        client.send("accept");
        Intent intent = new Intent(this, BuddyLocation_Activity.class);
        startActivity(intent);
    }

    /**
     * Method retryQueue
     * Restarts the queue activity when button is pressed
     * @param view
     */

    public void retryQueue(View view) {
        client.send("decline");
        Intent intent = new Intent(this, BuddyInfo_Activity.class);
        startActivity(intent);
    }

    public void setCard(){

        buddyName = client.read();
        age = client.read();
        image = Integer.parseInt(client.read());


        name.setText(this.buddyName);
        ageBox.setText(this.age);
        Log.i("image id", String.valueOf(image));
        imageView.setImageResource(image);
    }
}