package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

public class ToGame_Activity extends AppCompatActivity {
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = SingleSocket.getInstance().client;
        setContentView(R.layout.activity_to_game_screen);
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openBuddyLocation(View view) {
        Intent intent = new Intent(this, BuddyLocation_Activity.class);
        startActivity(intent);
    }

    public void openGameScreen(View view) {
        Intent intent = new Intent(this, GameInfo_Activity.class);
        client.send("startgame");
        startActivity(intent);
    }


    public void showHint(View view) {
        Toast.makeText(this, "hint", Toast.LENGTH_SHORT).show();
    }
}