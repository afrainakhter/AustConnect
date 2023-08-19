package com.example.austconnect;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.austconnect.adapters.AdapterUsers;
import com.example.austconnect.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> userList;
    FirebaseAuth firebaseAuth;
    SearchView searchView;
    ImageButton menu,event,notification,friends,jobsite,home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = findViewById(R.id.friend_recyclerView);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();
        searchView = findViewById(R.id.search_user);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Friend.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Friend.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Friend.this, Event.class);
                startActivity(intent);
                finish();
            }
        });

        getAllUser();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()

        {
            @Override
            public boolean onQueryTextSubmit (String s){
                if (!TextUtils.isEmpty(s.trim())) {
                    searchUsers(s);
                } else {
                    getAllUser();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange (String s){
                if (!TextUtils.isEmpty(s.trim())) {
                    searchUsers(s);
                } else {
                    getAllUser();

                }

                return false;
            }
        });



    }




    private void getAllUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (!modelUser.getUid().equals(fUser.getUid())) {
                        userList.add(modelUser);

                    }

                    adapterUsers = new AdapterUsers(Friend.this, userList);
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {


        } else {
            startActivity(new Intent(Friend.this, MainActivity.class));
            finish();

        }
    }






    private void searchUsers(String s) {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (!modelUser.getUid().equals(fUser.getUid())) {

                        if(modelUser.getName().toLowerCase().contains(s.toLowerCase())||modelUser.getEmail().toLowerCase().contains(s.toLowerCase())){
                            userList.add(modelUser);
                        }




                    }


                    adapterUsers = new AdapterUsers(peekAvailableContext(), userList);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(Friend.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}