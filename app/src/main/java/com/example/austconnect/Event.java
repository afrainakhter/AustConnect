package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.austconnect.adapters.AdapterEvent;
import com.example.austconnect.adapters.AdapterPost;
import com.example.austconnect.models.ModelEvent;
import com.example.austconnect.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Event extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton menu,event,notification,friends,jobsite,home,addEvent;

    RecyclerView recyclerView;
    List<ModelEvent>eventList;
    AdapterEvent adapterEvent;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        recyclerView=findViewById(R.id.eventRecycler);
        addEvent=findViewById(R.id.addEvent);
         user = FirebaseAuth.getInstance().getCurrentUser();


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        eventList=new ArrayList<>();
        loadevent();


        if(user==null){

            Intent intent= new Intent(Event.this, login.class);
            startActivity(intent);
            finish();
        }


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, Event.class);
                startActivity(intent);
                finish();
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, addEventActivity.class);
                startActivity(intent);
                finish();
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, Notification.class);
                startActivity(intent);
                finish();
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });


        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Event.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });




    }


    private void loadevent() {

      //String  myUid=user.getUid();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Events");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelEvent modelEvent=ds.getValue(ModelEvent.class);
                    eventList.add(modelEvent);

                    adapterEvent=new AdapterEvent(Event.this,eventList);
                    recyclerView.setAdapter(adapterEvent);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Event.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}