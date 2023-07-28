package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ImageView profilePic, CoverPic;
    ImageButton addProPic, addCoverPic;
    TextView emailText, nameText, phoneText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailTv);
        nameText = findViewById(R.id.ProfileNameTV);
        phoneText = findViewById(R.id.phoneTv);
        profilePic = findViewById(R.id.profilePicture);
        //CoverPic=findViewById(R.id.coverPicture);
        // addCoverPic=findViewById(R.id.addCoverPicture);
        //addProPic=findViewById(R.id.addProfilePicture);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name  =  "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    nameText.setText(name);
                    emailText.setText(email);
                    phoneText.setText(phone);


                    try {
                        Picasso.get().load(image).into(profilePic);


                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.ic_add_photo).into(profilePic);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//        private void checkUserStatus(){
//        FirebaseUser user= firebaseAuth.getCurrentUser();
//
//
//         if(user!=null){
//             emailText.setText(user.getEmail());
//
//         }else{
//             startActivity(new Intent(ProfileActivity.this,MainActivity.class));
//             finish();
//
//         }
//    }

//    protected void onStart(){
//            checkUserStatus();
//            super.onStart();
//        }

}
