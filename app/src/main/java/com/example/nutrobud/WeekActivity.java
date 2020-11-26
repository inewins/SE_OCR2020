package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import java.util.Calendar;

public class WeekActivity extends AppCompatActivity {

    TextView statsTitle, weekDisplay;
    Button backButton;
    private String date;

    Calendar c1=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //display (button) titles
        statsTitle=(TextView) findViewById(R.id.statsTitle);
        backButton=(Button)findViewById(R.id.backButton);


        backButton=(Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(i);
            }
        });

        //first day of week
        weekDisplay = (TextView) findViewById(R.id.date_textView);
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH)+1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);
//
//        weekDisplay.setText("Week of: " +year1 + month1 + day1  + "-" + year7 + month7 + day1);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.set(Calendar.DAY_OF_WEEK, cal.MONDAY);
        String firstWkDay = String.valueOf(cal.getTime());
//cal.set(Calendar.DAY_OF_WEEK, cal.SUNDAY);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String lastWkDay =  String.valueOf(cal.getTime());

        System.out.println("first day to last day: " + firstWkDay + "-" + lastWkDay);


//        dayButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
//                startActivity(i);
//            }
//        });
    }



}