package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.austconnect.adapters.AdapterEvent;
import com.example.austconnect.adapters.AdapterJob;
import com.example.austconnect.models.ModelEvent;
import com.example.austconnect.models.ModelJob;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobSite extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton menu,event,notification,friends,jobsite,home,addJob;

    RecyclerView recyclerView;

    List<ModelJob> jobList;
    AdapterJob adapterjob;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_site);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        recyclerView=findViewById(R.id.JobRecycler);
        addJob=findViewById(R.id.addJobs);
        user = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
       linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        jobList=new ArrayList<>();
        loadJobs();



        if(user==null){

            Intent intent= new Intent(JobSite.this, login.class);
            startActivity(intent);
            finish();}
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, Event.class);
                startActivity(intent);
                finish();
            }
        });
        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, AddJobActivity.class);
                startActivity(intent);
                finish();
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, Notification.class);
                startActivity(intent);
                finish();
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(JobSite.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });







    }

    private void loadJobs() {

        //String  myUid=user.getUid();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Jobs");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelJob modelJob=ds.getValue(ModelJob.class);
                    jobList.add(modelJob);

                    adapterjob=new AdapterJob(JobSite.this,jobList);
                    recyclerView.setAdapter(adapterjob);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(JobSite.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}