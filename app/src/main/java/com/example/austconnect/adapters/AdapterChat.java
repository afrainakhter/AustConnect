package com.example.austconnect.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.austconnect.R;
import com.example.austconnect.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_LEFT=1;
    private static final int MSG_RIGHT=2;
    Context context;
    List<ModelChat>chatList;
    String imageUrl;
    FirebaseUser fUser;



    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        if (i==MSG_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
            return new MyHolder(view) ;
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view) ;
        }



    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message=chatList.get(position).getMessage();
        String timeStamp=chatList.get(position).getTimestamp();

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());


        holder.messageTv.setText(message);
        holder.timeTv.setText(currentDateandTime);
        try{
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_add_photo).into(holder.profile);

        }catch (Exception e){


        }



        if(position==chatList.size()-1){
            if(chatList.get(position).isSeen()){
                holder.isSeenTv.setText("Seen");
            }else{

                holder.isSeenTv.setText("Delivered");

            }

        }else {
            holder.isSeenTv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){

            return MSG_RIGHT;
        }else {

            return MSG_LEFT;
        }




    }

    class MyHolder extends RecyclerView.ViewHolder{


        CircleImageView profile;
        TextView messageTv,timeTv,isSeenTv;




        public MyHolder(@NonNull View itemView) {
            super(itemView);

          profile=itemView.findViewById(R.id.profilePicture);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            isSeenTv=itemView.findViewById(R.id.StatusTv);




        }
    }
}
