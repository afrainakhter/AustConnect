package com.example.austconnect.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.austconnect.AddPostActivity;
import com.example.austconnect.R;
import com.example.austconnect.models.ModelEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.MyHolder> {

    Context context;
    List<ModelEvent>eventList;
    String myUid;

    public AdapterEvent(Context context, List<ModelEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public AdapterEvent.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_event,viewGroup,false);

        return new AdapterEvent.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEvent.MyHolder holder, int i) {


        String uid=eventList.get(i).getUid();
        String email=eventList.get(i).getEmail();
        String name=eventList.get(i).getName();
        String uDP=eventList.get(i).getuDP();
        String pId=eventList.get(i).getpId();
        String pTitle=eventList.get(i).getpTitle();
        String pDescription=eventList.get(i).getDescription();
        String pImage=eventList.get(i).getpImage();
        String pTimeStamp=eventList.get(i).getpTime();
        String eName=eventList.get(i).getEventName();
        String deptartment=eventList.get(i).geteDepeartment();



        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        holder.eventName.setText(eName);
        holder.eventDepartment.setText(deptartment);
        holder.userName.setText(name);
        holder.eventTitle.setText(pTitle);
        holder.eventDescription.setText(pDescription);
        holder.eventTime.setText(currentDateandTime);

        try{
            Picasso.get().load(uDP).placeholder(R.drawable.ic_add_photo).into(holder.userImg);

        }catch (Exception e){


        }

        if(pImage.equals("noImage")){

            holder.eventImage.setVisibility(View.GONE);

        }else{

            holder.eventImage.setVisibility(View.VISIBLE);


            try{
                Picasso.get().load(pImage ).placeholder(R.drawable.ic_add_photo).into(holder.eventImage);

            }catch (Exception e) {

            }
        }
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOption(holder.moreBtn,uid,myUid,pId,pImage);
            }
        });


        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();


            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();



            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            }
        });



    }




    @Override
    public int getItemCount() {
        return  eventList.size();
    }

      private void showMoreOption(ImageButton moreBtn, String uid, String myUid, String pId, String pImage) {
        PopupMenu popupMenu=new PopupMenu(context,moreBtn, Gravity.END);

        if(uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
            //popupMenu.getMenu().add(Menu.NONE,1,0,"Edit");

        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==0) {

                    beingDelete(pId, pImage);
//                }else if(id==1){
//
//                    Intent intent=new Intent(context, AddPostActivity.class);
//                    intent.putExtra("key","editPost");
//                    intent.putExtra("editPostId",pId);
//                    context.startActivity(intent);
//                }

                }
                return false;
            }
        });

        popupMenu.show();


    }

    private void beingDelete(String pId, String pImage) {





        if(pImage.equals("noImage")){


            deleteWithoutImage(pId);

        }else{

            deleteWithImage(pId,pImage);
        }

    }

    private void deleteWithImage(String pId, String pImage) {
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Deleting....");

        StorageReference picRef= FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Query query= FirebaseDatabase.getInstance().getReference("Events").orderByChild("pId").equalTo(pId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds:snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }

                        Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void deleteWithoutImage(String pId) {

        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Deleting....");


        Query query=FirebaseDatabase.getInstance().getReference("Events").orderByChild("pId").equalTo(pId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){
                    ds.getRef().removeValue();
                }

                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    class MyHolder extends RecyclerView.ViewHolder{

        ImageView userImg,eventImage;
        TextView userName,eventTime,eventTitle,eventDescription,eventLike,eventName,eventDepartment;
        ImageButton moreBtn;
        Button likeBtn,commentBtn,shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            userImg=itemView.findViewById(R.id.postDP);
            eventImage=itemView.findViewById(R.id.EventImageTv);
            userName=itemView.findViewById(R.id.username);
            eventTime=itemView.findViewById(R.id.eventTime);
            eventTitle=itemView.findViewById(R.id.EventTitleTV);
            eventDescription=itemView.findViewById(R.id.EventDescriptionTv);
            eventName=itemView.findViewById(R.id.EventNameTV);
            eventDepartment=itemView.findViewById(R.id.EventDepartmentTv);

            eventLike=itemView.findViewById(R.id.eventLikeTv);
            moreBtn=itemView.findViewById(R.id.moreEventBtn);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            commentBtn=itemView.findViewById(R.id.CommentBtn);
            shareBtn=itemView.findViewById(R.id.ShareBtn);



        }
    }
}
