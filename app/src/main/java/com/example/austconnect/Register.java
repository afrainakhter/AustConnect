package com.example.austconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {
    EditText email, password;
    Button register;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginText;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent= new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        progressBar=findViewById(R.id.progress_circular);
        loginText=findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String mail, pass;
                mail=String.valueOf(email.getText());
                pass=String.valueOf(password.getText());

                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){

                    email.setError("Invalid Email");
                    email.setFocusable(true);

                }
                else if(pass.length()<6){
                    password.setError("Password length should 6");
                    password.setFocusable(true);


                }



                if(TextUtils.isEmpty(mail)){
                    Toast.makeText( Register.this, "Enter E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText( Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);


                                    FirebaseUser user=mAuth.getCurrentUser();
                                    String email=user.getEmail();
                                    String uid=user.getUid();

                                    //when user is register store user data into firebase realtime database to using hashmap

                                    HashMap<Object, String>hashMap=new HashMap<>();
                                    hashMap.put("email",email);
                                    hashMap.put("uid",uid);
                                    hashMap.put("name","");
                                    hashMap.put("phone","");
                                    hashMap.put("image","");
                                    hashMap.put("cover","");
                                    FirebaseDatabase database=FirebaseDatabase.getInstance();

                                    DatabaseReference reference=database.getReference("Users");
                                    reference.child(uid).setValue(hashMap);



                                    Toast.makeText( Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(Register.this, login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }


        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(Register.this, login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}