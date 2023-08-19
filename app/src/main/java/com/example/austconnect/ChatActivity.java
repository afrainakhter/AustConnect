package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.austconnect.adapters.AdapterChat;
import com.example.austconnect.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageButton send;
    TextView nameTv,status;
    EditText messageEt;
    CircleImageView profile;
    FirebaseAuth firebaseAuth;

    FirebaseDatabase  database;
    DatabaseReference ref;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
   AdapterChat adapterChat;




    String hisUid,MyUid;
    String hisImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        send=findViewById(R.id.msgSent);
        nameTv=findViewById(R.id.profileName);

        chatList=new ArrayList<>();
        status=findViewById(R.id.status);
        profile=findViewById(R.id.profilePicture);
        messageEt=findViewById(R.id.messageEdit);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Users");

        recyclerView=findViewById(R.id.chat_recycle);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatActivity.this);

       linearLayoutManager.setStackFromEnd(true);
       linearLayoutManager.setReverseLayout(false);
        //recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(linearLayoutManager);
       // LinearLayoutManager layoutManager=  new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        //layoutManager.setStackFromEnd(true);
        //recyclerView.setLayoutManager(layoutManager);

        readMessage();



         Intent intent=getIntent();
         hisUid=intent.getStringExtra("hisUid");

        Query userQuery=ref.orderByChild("uid").equalTo(hisUid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();

                     hisImage = "" + ds.child("image").getValue();

                     String onlineStatus=""+ds.child("onlineStatus") .getValue();

                    String typingStatus=""+ds.child("typingTo") .getValue();

                    nameTv.setText(name);



                     if(typingStatus.equals(MyUid)){
                         status.setText("Typing...");
                     }else{


                         if(onlineStatus.equals("online")){
                             status.setText(onlineStatus);
                         }

                         else{
                             Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
                             calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                             SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
                             String currentDateandTime = sdf.format(new Date());
                             status.setText("Last Seen at: "+currentDateandTime);


                         }

                     }




                    try {
                        Picasso.get().load(hisImage).into(profile);


                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.profile).into(profile);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message= messageEt.getText().toString().trim();

                if(TextUtils.isEmpty(message)){

                    Toast.makeText(ChatActivity.this, "cannot sent empty message", Toast.LENGTH_SHORT).show();
                }else{

                    sendMessage(message);
                }


            }
        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {


                if(charSequence.toString().trim().length()==0){
                    CheckTypingStatus("noOne");
                }else{

                    CheckTypingStatus(hisUid);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

       // readMessage();
      seenMessage();


    }



    private void seenMessage() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    ModelChat chat = ds.getValue(ModelChat.class);

                    if(chat.getReceiver().equals(MyUid)&&chat.getSender().equals(hisUid)){
                        HashMap<String ,Object>hasSeenHashMap=new HashMap<>();
                        hasSeenHashMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasSeenHashMap);


                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readMessage() {

        chatList=new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelChat chat=ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(MyUid) && chat.getSender().equals(hisUid)||
                            chat.getReceiver().equals(hisUid)&& chat.getSender().equals(MyUid)){

                        chatList.add(chat);
                    }
                    adapterChat=new AdapterChat(ChatActivity.this,chatList,hisImage);
                   // adapterChat.notifyDataSetChanged();
                  // int position=(chatList.size()-1);
                   // recyclerView.scrollToPosition(position);
                    recyclerView.setAdapter(adapterChat);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
       String timestamp=String.valueOf(System.currentTimeMillis());

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",MyUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);

           messageEt.setText("");


    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {

         MyUid=user.getUid();
        } else {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();

        }
    }

    private void CheckOnlineStatus(String status){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(MyUid);
        HashMap<String ,Object>hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);
        dbRef.updateChildren(hashMap);





    }

    private void CheckTypingStatus(String typing){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(MyUid);
        HashMap<String ,Object>hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);
        dbRef.updateChildren(hashMap);





    }

    @Override
    protected void onStart() {
        checkUserStatus();
        CheckOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String timestamp=String.valueOf(System.currentTimeMillis());
        CheckOnlineStatus(timestamp);
        //CheckOnlineStatus("offline");
        //status.setText("Offline");
        CheckTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckOnlineStatus("online");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(ChatActivity.this, Friend.class);
        startActivity(intent);
        finish();
    }
}