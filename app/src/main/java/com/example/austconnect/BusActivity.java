package com.example.austconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class BusActivity extends AppCompatActivity implements SingleChoiceDialog.SingleChoiceListener {
    private Button location, route, about, schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        this.setTitle("Click which one you want to know!");

        location = findViewById(R.id.location);
        route = findViewById(R.id.route);
        about = findViewById(R.id.about);
        schedule = findViewById(R.id.schedule);


        schedule.setOnClickListener((v) -> {
            Intent intent = new Intent(BusActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        route.setOnClickListener((v) -> {
            Intent intent = new Intent(BusActivity.this, RouteActivity.class);
            startActivity(intent);
        });

        about.setOnClickListener((v) -> {
            Intent intent = new Intent(BusActivity.this, AboutUs.class);
            startActivity(intent);
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment singleChoiceDialog = new SingleChoiceDialog();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(), "Single Choice Dialog");

            }
        });
    }


    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        String selectedOption = list[position];

        if ("I AM DRIVER".equals(selectedOption)) {
            // Start the activity for the driver
            Intent intent = new Intent(BusActivity.this, DriverActivity.class);
            startActivity(intent);
        } else if ("I AM STUDENT".equals(selectedOption)) {
            // Start the activity for the student
            Intent intent = new Intent(BusActivity.this, StudentActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onNegativeButtonClicked() {

    }
}