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

import com.example.austconnect.adapters.AdapterBlood;
import com.example.austconnect.models.ModelBlood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BloodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterBlood adapterBlood;
    List<ModelBlood>bloodList;
    FirebaseAuth auth;
    FirebaseUser user;

    ImageButton home,event,jobsite,friends,notification,addBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        recyclerView=findViewById(R.id.bloodRecycler);
        addBlood=findViewById(R.id.addBlood);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        bloodList=new ArrayList<>();

        loadPost();
        if(user==null){

            Intent intent= new Intent(BloodActivity.this, login.class);
            startActivity(intent);
            finish();
        }

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(BloodActivity.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });





        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(BloodActivity.this, Event.class);
                startActivity(intent);
                finish();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(BloodActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(BloodActivity.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });
        addBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(BloodActivity.this, AddBloodPostActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }


    private void loadPost() {

        //String  myUid=user.getUid();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Bloods");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bloodList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                  ModelBlood modelBlood=ds.getValue(ModelBlood.class);
                    bloodList.add(modelBlood);

                    adapterBlood=new AdapterBlood(BloodActivity.this,bloodList);
                    recyclerView.setAdapter(adapterBlood);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(BloodActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(BloodActivity.this, Menu.class);
        startActivity(intent);
        finish();
    }
}