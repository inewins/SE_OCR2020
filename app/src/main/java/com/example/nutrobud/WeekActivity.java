package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeekActivity extends AppCompatActivity {

    TextView statsTitle, weekDisplayView;
    Button backButton;
    private String date, mDay, mMonth, mYear;

    Calendar c1=Calendar.getInstance();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String firstDate = formatter.format(new Date());
    private String lastDate = formatter.format(new Date());
    private SimpleDateFormat displayStartFormat;
    private SimpleDateFormat displayEndFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        // display (button) titles
        statsTitle=(TextView) findViewById(R.id.statsTitle);
        backButton=(Button)findViewById(R.id.backButton);
        weekDisplayView = (TextView) findViewById(R.id.weekView);

        // back button activity
        backButton=(Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(i);
            }
        });

        // get date for beginning of week- MONDAY
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = formatter.format(cal.getTime());
        displayStartFormat = new SimpleDateFormat("MMM d");
        String displayStartDate = displayStartFormat.format(cal.getTime());
        System.out.println("start date: "+ startDate);
        // get date for end of week - SUNDAY
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = formatter.format(cal.getTime());
        displayEndFormat = new SimpleDateFormat("MMM d");
        String displayEndDate = displayEndFormat.format(cal.getTime());
        System.out.println("end date: " + endDate);

        weekDisplayView.setText(displayStartDate + " - " + displayEndDate);


    }



}