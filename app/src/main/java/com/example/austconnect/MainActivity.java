package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.austconnect.adapters.AdapterPost;
import com.example.austconnect.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton menu,event,notification,friends,jobsite,home;
    CircleImageView profileImg;
    DatabaseReference databaseReference;
    EditText Post;

    RecyclerView recyclerView;
    List<ModelPost>postList;
    AdapterPost adapterPost;
    SearchView searchPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        Post=findViewById(R.id.createPost);
        profileImg=findViewById(R.id.profilePicture);
        searchPost=findViewById(R.id.search_post);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView=findViewById(R.id.post_recycle);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

       recyclerView.setLayoutManager(linearLayoutManager);
        postList=new ArrayList<>();
        loadPost();


        if(user==null){

            Intent intent= new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        }




        Query query=databaseReference.orderByChild("uid").equalTo(user.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                   // String name = "" + ds.child("name").getValue();
                    String image = "" + ds.child("image").getValue();

                   // profileName.setText(name);


                    try {
                        Picasso.get().load(image).into(profileImg);


                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.ic_add_photo).into(profileImg);

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Event.class);
                startActivity(intent);
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
                finish();
            }
        });
        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });


        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(intent);
                finish();
            }
        });



        searchPost.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s.trim())) {
                    searchPost(s);
                } else {
                    loadPost();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s.trim())) {
                    searchPost(s);
                } else {
                    loadPost();

                }
                return false;
            }
        });

    }






    private void searchPost(String s) {

        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelPost modelPost=ds.getValue(ModelPost.class);
                    if(modelPost.getpTitle().toLowerCase().contains(s.toLowerCase())||
                            modelPost.getDescription().toLowerCase().contains(s.toLowerCase())) {
                        postList.add(modelPost);
                    }
                    adapterPost=new AdapterPost(MainActivity.this,postList);
                    recyclerView.setAdapter(adapterPost);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPost() {

        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 postList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelPost modelPost=ds.getValue(ModelPost.class);
                   postList.add(modelPost);

                    adapterPost=new AdapterPost(MainActivity.this,postList);
                    recyclerView.setAdapter(adapterPost);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
}

}