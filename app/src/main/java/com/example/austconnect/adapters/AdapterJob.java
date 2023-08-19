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
import com.example.austconnect.models.ModelEvent;
import com.example.austconnect.models.ModelJob;
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

public class AdapterJob extends RecyclerView.Adapter<AdapterJob.MyHolder> {


    Context context;
    List<ModelJob> jobList;
    String myUid;


    public AdapterJob(Context context, List<ModelJob> jobList) {
        this.context = context;
        this.jobList = jobList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_job,viewGroup,false);

        return new AdapterJob.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        String uid=jobList.get(i).getEmail();
        String name=jobList.get(i).getName();
        String uDP=jobList.get(i).getuDP();
        String pId=jobList.get(i).getpId();
        String pImage=jobList.get(i).getpImage();
        String pTimeStamp=jobList.get(i).getpTime();
        String jCategory=jobList.get(i).getjCategory();

        String jDescription=jobList.get(i).getjDescription();

        String jSkill=jobList.get(i).getjSkill();

        String jType=jobList.get(i).getjType();

        String jPreference=jobList.get(i).getjPreference();

        String jSalary=jobList.get(i).getjSalary();

        String jExp=jobList.get(i).getjExp();

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        holder.jobDescription.setText("Description:"+jDescription);
        holder.jobCategory.setText("Category: "+jCategory);

        holder.skill.setText("Skills : "+jSkill);
        holder.pref.setText("Preference : " +jPreference);
        holder.exp.setText("Experience:" +jExp);
        holder.type.setText("Type: "+jType);
        holder.userName.setText(name);
        holder.salary.setText("Salary :"+jSalary);
        holder.eventTime.setText(currentDateandTime);




        try{
            Picasso.get().load(uDP).placeholder(R.drawable.ic_add_photo).into(holder.userImg);

        }catch (Exception e){


        }
        if(pImage.equals("noImage")){

            holder.JobImage.setVisibility(View.GONE);

        }else{

            holder.JobImage.setVisibility(View.VISIBLE);


            try{
                Picasso.get().load(pImage ).placeholder(R.drawable.ic_add_photo).into(holder.JobImage);

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
        return  jobList.size();
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

                Query query= FirebaseDatabase.getInstance().getReference("Jobs").orderByChild("pId").equalTo(pId);
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


        Query query=FirebaseDatabase.getInstance().getReference("Jobs").orderByChild("pId").equalTo(pId);
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


        ImageView userImg,JobImage;
        TextView userName,eventTime,jobCategory,jobDescription,jobLike,skill,pref,salary,exp,type;
        ImageButton moreBtn;
        Button likeBtn,commentBtn,shareBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            userImg=itemView.findViewById(R.id.postDP);
            JobImage=itemView.findViewById(R.id.jobImageTv);
            userName=itemView.findViewById(R.id.username);
            eventTime=itemView.findViewById(R.id.eventTime);


            jobCategory=itemView.findViewById(R.id.jobCategoryTv);
           salary=itemView.findViewById(R.id.JobSalaryTv);
            exp=itemView.findViewById(R.id.jobExp);
            pref=itemView.findViewById(R.id.JobPreferenceTv);

            type=itemView.findViewById(R.id.JobTypeTv);
            jobDescription=itemView.findViewById(R.id.jobDescription);
            skill=itemView.findViewById(R.id.jobSkill);




            jobLike=itemView.findViewById(R.id.jobLikeTv);
            moreBtn=itemView.findViewById(R.id.moreJobBtn);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            commentBtn=itemView.findViewById(R.id.CommentBtn);
            shareBtn=itemView.findViewById(R.id.ShareBtn);




        }
        }

}
