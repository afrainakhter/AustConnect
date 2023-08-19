package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {
    EditText email, password;
    Button login;
    FirebaseAuth mAuth;
    SignInButton google;

    TextView regText,recoverPass;
    ProgressDialog pd;
    GoogleSignInClient mGoogleSignClient;
    private static final int RC_SIGN_IN=100;

    @Override
    //on start method if already sign in load the user account
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent= new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //configure google sign in
       GoogleSignInOptions gso=new   GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN).
               requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();


       mGoogleSignClient=GoogleSignIn.getClient(this,gso);


        //initialize  views
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.loginButton);

        regText=findViewById(R.id.registerText);
       recoverPass=findViewById(R.id.ForgetPass);
       google=findViewById(R.id.googleSignIn);

        //registration page
        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        recoverPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }

        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signInIntent=mGoogleSignClient.getSignInIntent();
                startActivityForResult(signInIntent ,RC_SIGN_IN);

            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Logging In....");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail, pass;
                mail=String.valueOf(email.getText());
                pass=String.valueOf(password.getText());
                // check mail and password
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){

                    email.setError("Invalid Email");
                    email.setFocusable(true);

                }
                else if(pass.length()<6){
                    password.setError("Password length should 6");
                    password.setFocusable(true);


                }


                if(TextUtils.isEmpty(mail)){
                    Toast.makeText( login.this, "Enter E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText( login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd.setMessage("Logging In....");
                pd.show();
                mAuth.signInWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Toast.makeText( login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    // Sign in success, update UI with the signed-in user's information

                                } else {
                                    // If sign in fails, display a message to the user.
                                    pd.dismiss();
                                    Toast.makeText(login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }


    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover PassWord");
        LinearLayout linearLayout=new LinearLayout(this);
       final EditText emailEt=new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        emailEt.setMinEms(10);

        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String email=emailEt.getText().toString().trim();
                beginRecovery(email);
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


    @Override
    public  void onActivityResult(int requestCode, int resultCode,Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){

            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
     Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=mAuth.getCurrentUser();


                    if(task.getResult().getAdditionalUserInfo().isNewUser()){

                        String email=user.getEmail();
                        String uid=user.getUid();

                        //when user is register store user data into firebase realtime database to using hashmap

                        HashMap<Object, String> hashMap=new HashMap<>();
                        hashMap.put("email",email);
                        hashMap.put("uid",uid);
                        hashMap.put("name","");
                        hashMap.put("onlineStatus","online");
                        hashMap.put("typingTo","noOne");
                        hashMap.put("phone","");
                        hashMap.put("image","");
                        hashMap.put("cover","");
                        FirebaseDatabase database=FirebaseDatabase.getInstance();

                        DatabaseReference reference=database.getReference("Users");
                        reference.child(uid).setValue(hashMap);

                    }



                    Intent intent= new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();



                }else{
                    Toast.makeText(login.this,"Login Failed...",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void beginRecovery(String email) {
        pd.setMessage("Sending mail....");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    pd.dismiss();
                    Toast.makeText(login.this,"Email sent",Toast.LENGTH_SHORT).show();
                }else{
                   pd.dismiss();
                    Toast.makeText(login.this,"Failed....",Toast.LENGTH_SHORT).show();


                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}