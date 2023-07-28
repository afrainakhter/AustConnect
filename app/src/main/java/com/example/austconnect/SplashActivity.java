package com.example.austconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private  int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar =findViewById(R.id.pbar1);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                doWork();
                startAPP();
            }
        });

        thread.start();


    }

    public void doWork() {


        for (count = 20; count <= 100; count = count + 20) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(count);

            } catch (InterruptedException e) {
                e.printStackTrace();


            }
        }


    }
    public void startAPP(){
        Intent intent= new Intent(SplashActivity.this,login.class);
        startActivity(intent);
        finish();
    }
}