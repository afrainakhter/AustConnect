package com.example.austconnect;






import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
StorageReference storageReference;
String storagePath="Users_Profile_cover_Imgs/";
    ImageView profilePic, CoverPic;
    ImageButton addProPic, addCoverPic;
    TextView emailText, nameText, phoneText;
       FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    Uri image_uri;

    String ProfileOrCover;




    //Permission Constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String cameraPermission[];
    String storagePermission[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference();



        emailText = findViewById(R.id.emailTv);
        nameText = findViewById(R.id.ProfileNameTV);
        phoneText = findViewById(R.id.phoneTv);
        profilePic = findViewById(R.id.profilePicture);
        CoverPic = findViewById(R.id.coverPicture);
        floatingActionButton = findViewById(R.id.floating);
        progressDialog = new ProgressDialog(this);


        // addCoverPic=findViewById(R.id.addCoverPicture);
        //addProPic=findViewById(R.id.addProfilePicture);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();
                    nameText.setText(name);
                    emailText.setText(email);
                    phoneText.setText(phone);


                    try {
                        Picasso.get().load(image).into(profilePic);


                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.ic_add_photo).into(profilePic);

                    }
                    try {
                        Picasso.get().load(cover).into(CoverPic);


                    } catch (Exception e) {

                        // Picasso.get().load(R.drawable.ic_add_photo).into(CoverPic);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditProfileDialog();

            }


        });
    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;

    }

    private void requestStoragePermission() {
       requestPermissions( storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;


    }

    private void requestCameraPermission() {
       requestPermissions( cameraPermission, CAMERA_REQUEST_CODE);
    }


    private void showEditProfileDialog() {

        String options[] = {"Edit Profile Picture", "Edit Cover Picture", "Edit Name", "Edit Phone"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {

                    progressDialog.setMessage("Updating Profile Picture");
                    ProfileOrCover="image";
                    showImgPicDialog();


                } else if (i == 1) {
                    progressDialog.setMessage("Updating Cover Picture");
                    ProfileOrCover="cover";
                    showImgPicDialog();
                } else if (i == 2) {
                    progressDialog.setMessage("Updating Name");
                    showNamePhoneUpdateDialog("name");


                } else if (i == 3) {
                    progressDialog.setMessage("Updating Phone");
                    showNamePhoneUpdateDialog("phone");
                }


            }
        });

        builder.create().show();

    }

    private void showNamePhoneUpdateDialog(String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Update"+key);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        EditText editText=new EditText(this);
        editText.setHint("Enter"+key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            String value=editText.getText().toString().trim();
            if(!TextUtils.isEmpty(value)){
                progressDialog.show();
                HashMap<String,Object>result=new HashMap<>();
                result.put(key,value);
                databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }else{
                Toast.makeText(ProfileActivity.this, "please enter"+key, Toast.LENGTH_SHORT).show();
            }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
        });
builder.create().show();

    }


    private void showImgPicDialog() {

        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {

                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                    //camera clicked

                } else if (i == 1)
                { if(!checkCameraPermission()){
                    requestCameraPermission();
                }else{
                    pickFromGallery();
                }

                    //gallery clicked
                }


            }
        });

        builder.create().show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "please enable camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "please enable media permission", Toast.LENGTH_SHORT).show();

                }}}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                image_uri=data.getData();
                uploadProfileCoverPhoto(image_uri);

            }if(requestCode==IMAGE_PICK_CAMERA_CODE){

                uploadProfileCoverPhoto(image_uri);

            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        progressDialog.show();
String filePathAndName=storagePath+""+ProfileOrCover+"_"+user.getUid();

StorageReference storageReference1=storageReference.child(filePathAndName);
storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
        while(!uriTask.isSuccessful());
        Uri downloadUri=uriTask.getResult();

        if(uriTask.isSuccessful()){
            HashMap<String , Object>results=new HashMap<>();
            results.put(ProfileOrCover,downloadUri.toString());
            databaseReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                 progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Image Updated...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Error ...", Toast.LENGTH_SHORT).show();

                }
            });

        }else{
            progressDialog.dismiss();
            Toast.makeText(ProfileActivity.this, "Error ...", Toast.LENGTH_SHORT).show();

        }


    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
      progressDialog.dismiss();
        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});

    }

    private void pickFromCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri=this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    private void pickFromGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);

        


    }




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



