package com.example.austconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView logout,profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        logout=findViewById(R.id.logout);
        profile=findViewById(R.id.profileName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(Menu.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                Intent intent= new Intent(Menu.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}