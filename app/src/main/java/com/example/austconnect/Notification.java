package com.example.austconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Notification extends AppCompatActivity {

    ImageButton menu,event,notification,friends,jobsite,home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        event = findViewById(R.id.event);
        friends = findViewById(R.id.friend);
        notification = findViewById(R.id.notification);
        jobsite = findViewById(R.id.jobSite);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Event.class);
                startActivity(intent);
                finish();
            }
        });
        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Notification.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}