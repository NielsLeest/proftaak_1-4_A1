package com.company.bessties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.company.bessties.socket.Client;
import com.company.bessties.socket.SingleSocket;

public class ProfileView_Activity extends AppCompatActivity {
    private Client client;
    private TextView name ;
   private TextView age ;
    private String Name;
    private String Age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_screen);
        name  = (TextView)findViewById(R.id.NameField);
        age = (TextView) findViewById(R.id.ageField);





        this.client = SingleSocket.getInstance().client;

        Thread t1 = new Thread(()->{
           Name = client.getFirstName()+ " "+ client.getLastName();
           Age = ""+client.getAge();
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name.setText(Name);
        age.setText(Age);


    }

    public void openQueue(View view) {
        Intent intent = new Intent(this, Queue_Activity.class);

        startActivity(intent);
    }
}