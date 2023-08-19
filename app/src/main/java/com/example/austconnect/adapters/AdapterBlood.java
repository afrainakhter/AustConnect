package com.example.austconnect.adapters;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.austconnect.R;
import com.example.austconnect.models.ModelBlood;
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

public class AdapterBlood extends RecyclerView.Adapter<AdapterBlood.MyHolder>{
    Context context;
    List<ModelBlood> bloodList;
    String myUid;

    public AdapterBlood(Context context, List<ModelBlood> bloodList) {
        this.context = context;
        this.bloodList = bloodList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.blood_row,viewGroup,false);

        return new AdapterBlood.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        String uid=bloodList.get(i).getUid();
        String email=bloodList.get(i).getEmail();
        String name=bloodList.get(i).getName();
        String uDP=bloodList.get(i).getuDP();
        String pId=bloodList.get(i).getpId();
        String pImage=bloodList.get(i).getpImage();
        String pTimeStamp=bloodList.get(i).getpTime();

        String contactName=bloodList.get(i).getContactName();
        String description=bloodList.get(i).getDescription();
        String BGroup=bloodList.get(i).getbGroup();

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        holder.contact.setText(contactName);
        holder.patientDescription.setText(description);
        holder.userName.setText(name);
        holder.bloodGroup.setText(BGroup);
        holder.time.setText(currentDateandTime);
        try{
            Picasso.get().load(uDP).placeholder(R.drawable.ic_add_photo).into(holder.userImg);

        }catch (Exception e){


        }

        if(pImage.equals("noImage")){

            holder.bloodImage.setVisibility(View.GONE);

        }else{

            holder.bloodImage.setVisibility(View.VISIBLE);


            try{
                Picasso.get().load(pImage ).placeholder(R.drawable.ic_add_photo).into(holder.bloodImage);

            }catch (Exception e) {

            }
        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOption(holder.moreBtn,uid,myUid,pId,pImage);
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
       return  bloodList.size();
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

                Query query= FirebaseDatabase.getInstance().getReference("Bloods").orderByChild("pId").equalTo(pId);
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


        Query query=FirebaseDatabase.getInstance().getReference("Bloods").orderByChild("pId").equalTo(pId);
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



    class MyHolder extends RecyclerView.ViewHolder {


        ImageView userImg ,bloodImage;
        TextView userName,time,bloodGroup,contact, patientDescription;
        ImageButton moreBtn;
        Button likeBtn,commentBtn,shareBtn;





        public MyHolder(@NonNull View itemView) {

            super(itemView);


            userImg=itemView.findViewById(R.id.postDP);
            bloodImage=itemView.findViewById(R.id.bloodImg);
            userName=itemView.findViewById(R.id.username);
             time=itemView.findViewById(R.id.postTime);
           contact=itemView.findViewById(R.id.contactName);
            patientDescription=itemView.findViewById(R.id.patientDescriptionTv);
            bloodGroup=itemView.findViewById(R.id.bloodtypeTv);





            moreBtn=itemView.findViewById(R.id.moreBloodBtn);

            commentBtn=itemView.findViewById(R.id.CommentBtn);
            shareBtn=itemView.findViewById(R.id.ShareBtn);








        }
    }

}
