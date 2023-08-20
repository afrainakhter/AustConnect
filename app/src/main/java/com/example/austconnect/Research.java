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

import com.example.austconnect.adapters.AdapterJob;
import com.example.austconnect.adapters.AdapterResearch;
import com.example.austconnect.models.ModelJob;
import com.example.austconnect.models.ModelResearch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Research extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    RecyclerView recyclerView;

    ImageButton menu,event,notification,friends,jobsite,home,addResearch;
    List<ModelResearch> researchesList;
    AdapterResearch adapterResearch;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        recyclerView = findViewById(R.id.researchRecycler);
        addResearch=findViewById(R.id.addResearch);

        user = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        researchesList=new ArrayList<>();
        loadReasearches();


        if(user==null){

            Intent intent= new Intent(Research.this, login.class);
            startActivity(intent);
            finish();}









        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Research.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });

        addResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Research.this, AddReasearchActivity.class);
                startActivity(intent);
                finish();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Research.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Research.this, Event.class);
                startActivity(intent);
                finish();
            }
        });
        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Research.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void loadReasearches() {

        //String  myUid=user.getUid();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Researches");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                researchesList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelResearch modelResearch=ds.getValue(ModelResearch.class);
                    researchesList.add(modelResearch);

                    adapterResearch=new AdapterResearch(Research.this,researchesList);
                    recyclerView.setAdapter(adapterResearch);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Research.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}