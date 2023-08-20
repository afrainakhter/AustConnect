package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.austconnect.adapters.AdapterUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    AdapterUsers adapterUsers;
    TextView logout,profileName,Edit;

    ImageButton home,event,jobsite,friends,notification;
    CardView bus,help,blood,comment,research,job,friend,events;
    ImageView profileImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        logout=findViewById(R.id.logout);
        profileName=findViewById(R.id.profileName);
        home=findViewById(R.id.home);
        event=findViewById(R.id.event);
        friends=findViewById(R.id.friend);
        notification=findViewById(R.id.notification);
        jobsite=findViewById(R.id.jobSite);
        bus=findViewById(R.id.busService);
        help=findViewById(R.id.HelpCard);
        comment=findViewById(R.id.CommentsCard);
        research=findViewById(R.id.research);
        blood=findViewById(R.id.blood);
        job=findViewById(R.id.jobOffers);
        friend=findViewById(R.id.friendCard);
        events=findViewById(R.id.eventCard);
        Edit=findViewById(R.id.EditProfile);
        profileImg=findViewById(R.id.profilePicture);



        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Query query=databaseReference.orderByChild("uid").equalTo(user.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String image = "" + ds.child("image").getValue();

                    profileName.setText(name);


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





        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, Friend.class);
                startActivity(intent);
                finish();
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, Event.class);
                startActivity(intent);
                finish();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, Event.class);
                startActivity(intent);
                finish();
            }
        });
        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, Research.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(Menu.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, BusActivity.class);
                startActivity(intent);
                finish();
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });
        jobsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, JobSite.class);
                startActivity(intent);
                finish();
            }
        });





        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(Menu.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Menu.this, BloodActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(Menu.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}