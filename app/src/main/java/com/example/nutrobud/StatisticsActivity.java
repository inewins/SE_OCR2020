package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {

    TextView statsTitle;
    Button b2Dashbtn, weekButton, dayButton;

    //today's date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);



        //display (button) titles
        statsTitle=(TextView) findViewById(R.id.statsTitle);
        b2Dashbtn=(Button)findViewById(R.id.backToDashButton);
        weekButton= (Button)findViewById(R.id.weekButton);
        dayButton= (Button) findViewById(R.id.dayButton);

        b2Dashbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashActivity.class);
                startActivity(i);
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WeekActivity.class);
                startActivity(i);
            }
        });

        dayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StatisticsDayActivity.class);
                startActivity(i);
            }
        });

    }
}