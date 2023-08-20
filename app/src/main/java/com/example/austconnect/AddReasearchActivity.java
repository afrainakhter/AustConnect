package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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

import java.util.HashMap;

public class AddReasearchActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    EditText description,category,skill ,experience;
    ImageView researchImage;
    MaterialButton uploadBtn;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    String cameraPermission[];
    String storagePermission[];

    Uri image_uri=null;

    String name,email,uid,dp;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reasearch);

        description=findViewById(R.id.rDescriptionET);
        category=findViewById(R.id.rCategory);
        researchImage=findViewById(R.id.rImg);
        uploadBtn=findViewById(R.id.rUpload);
        skill=findViewById(R.id.rSkillEt);
        experience=findViewById(R.id.rExpET);
        pd=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        checkUserStatus();
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        reference=FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("email").equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    dp = "" + ds.child("image").getValue();



                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        researchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowImgDialogPicker();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String jobCategory=category.getText().toString().trim();
                String JobDescription=description.getText().toString().trim();
                String jobSkill=skill.getText().toString().trim();
                String JExp=experience.getText().toString().trim();

                if(TextUtils.isEmpty(jobCategory)) {

                    Toast.makeText(AddReasearchActivity.this, "jobCategory", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(JobDescription)){

                    Toast.makeText(AddReasearchActivity.this, "Enter a description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(jobSkill)){

                    Toast.makeText(AddReasearchActivity.this, "Enter JobSkill", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(JExp)){

                    Toast.makeText(AddReasearchActivity.this, "Enter Your Department", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(image_uri==null){
                    uploadData(jobCategory,JobDescription,jobSkill,JExp,"noImage");

                }else{

                }
                uploadData(jobCategory,JobDescription,jobSkill,JExp,String.valueOf(image_uri));


            }
        });



    }


    private void uploadData(String jobCategory, String jobDescription, String jobSkill, String jExp, String uri) {

        pd.setMessage("publishing job Research Post.....");
        pd.show();

        String timeStamp=String.valueOf(System.currentTimeMillis());

        String filePathAndName="Researches/"+"research_"+timeStamp;

        if(!uri.equals("noImage")){

            StorageReference ref= FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    String downloadUri=uriTask.getResult().toString();

                    if(uriTask.isSuccessful()){

                        HashMap<Object,String> hashMap=new HashMap<>();
                        hashMap.put("uid",uid);

                        hashMap.put("email",email);
                        hashMap.put("name",name);

                        hashMap.put("uDP",dp);
                        hashMap.put("pId",timeStamp);
                        hashMap.put("rCategory",jobCategory);
                        hashMap.put("rDescription",jobDescription);
                        hashMap.put("rSkill",jobSkill);
                        hashMap.put("rExp",jExp);
                        hashMap.put("pImage",downloadUri);
                        hashMap.put("pTime",timeStamp);

                        FirebaseDatabase database=FirebaseDatabase.getInstance();

                        DatabaseReference reference=database.getReference("Researches");
                        reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(AddReasearchActivity.this, "Job Published", Toast.LENGTH_SHORT).show();

                                description.setText("");

                                category.setText("");
                                skill.setText("");
                                experience.setText("");

                               researchImage.setImageURI(null);

                                image_uri=null;

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(AddReasearchActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }






                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                    Toast.makeText(AddReasearchActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });

        }else{


            HashMap<Object,String>hashMap=new HashMap<>();
            hashMap.put("uid",uid);

            hashMap.put("email",email);
            hashMap.put("name",name);

            hashMap.put("uDP",dp);
            hashMap.put("pId",timeStamp);

            hashMap.put("rCategory",jobCategory);
            hashMap.put("rDescription",jobDescription);
            hashMap.put("rSkill",jobSkill);
            hashMap.put("rExp",jExp);

            hashMap.put("pImage","noImage");
            hashMap.put("pTime",timeStamp);

            FirebaseDatabase database=FirebaseDatabase.getInstance();

            DatabaseReference reference=database.getReference("Researches");
            reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.dismiss();
                    Toast.makeText(AddReasearchActivity.this, "Job Published", Toast.LENGTH_SHORT).show();


                    description.setText("");

                    category.setText("");
                    skill.setText("");
                    experience.setText("");
                   researchImage.setImageURI(null);
                    image_uri=null;


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddReasearchActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });



        }
    }




    private void ShowImgDialogPicker() {

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


    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;

    }


    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;


    }

    private void requestCameraPermission() {
        requestPermissions( cameraPermission, CAMERA_REQUEST_CODE);
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

               researchImage.setImageURI(image_uri);

            }if(requestCode==IMAGE_PICK_CAMERA_CODE){

                researchImage.setImageURI(image_uri);

            }
        }



        super.onActivityResult(requestCode, resultCode, data);
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



    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }
    private  void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

            email=user.getEmail();
            uid=user.getUid();

        }
        else{

            startActivity(new Intent(AddReasearchActivity.this,MainActivity.class));

        }    }
}