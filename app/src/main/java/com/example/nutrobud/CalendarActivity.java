package com.example.nutrobud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                String sendit = i+""+ (i1+1)+i2;
                Log.d("123", "onSelectedDayChange: mm/dd/yyyy: " + date);
                Log.d("123", "DATE IN CALENDAR " + sendit);

                Intent ii = new Intent(getApplicationContext(), StatisticsDayActivityCalendar.class);
                ii.putExtra("date",sendit);
                startActivity(ii);
            }
        });


    }
}
