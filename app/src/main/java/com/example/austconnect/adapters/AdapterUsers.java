package com.example.austconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.austconnect.ChatActivity;
import com.example.austconnect.models.ModelUser;
import com.example.austconnect.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{

    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        String hisUID=userList.get(i).getUid();
        String userImage =userList.get(i).getImage();
        String userName =userList.get(i).getName();
        String userEmail =userList.get(i).getEmail();
        holder.aName.setText(userName);
        holder.aEmail.setText(userEmail);

    try{
        Picasso.get().load(userImage).placeholder(R.drawable.ic_add_photo).into(holder.aProfile);

    }catch (Exception e){


    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(context, ""+userEmail, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("hisUid",hisUID);
            context.startActivity(intent);
        }
    });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView aProfile;
        TextView aName,aEmail;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            aProfile=itemView.findViewById(R.id.profilePicture );
            aName=itemView.findViewById(R.id.ProfileNameTV);
            aEmail=itemView.findViewById(R.id.emailTv);


        }
    }
}
