package com.example.nutrobud.ui.scanResult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutrobud.DashActivity;
import com.example.nutrobud.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.nutrobud.ui.home.Stats;
import com.example.nutrobud.ui.home.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScanResult extends AppCompatActivity {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String todayDate = formatter.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        ScanHelper user= (ScanHelper) getIntent().getParcelableExtra("userMap");
        DocumentReference dr = FirebaseFirestore.getInstance().document("users/"+user.getId());

        TextView calDisplay = (TextView)findViewById(R.id.calDisplay);
        calDisplay.setText(String.valueOf(user.getCaloriesTrackedQty()) + " cal.");

        TextView carbDisplay = (TextView)findViewById(R.id.carbDisplay);
        carbDisplay.setText(String.valueOf(user.getCarbohydrate())+"g");

        TextView proteinDisplay = (TextView)findViewById(R.id.proteinDisplay);
        proteinDisplay.setText(String.valueOf(user.getProtein())+"g");

        TextView fatDisplay = (TextView)findViewById(R.id.fatDisplay);
        fatDisplay.setText(String.valueOf(user.getFat())+"g");

        TextView sodiumDisplay = (TextView)findViewById(R.id.sodiumDisplay);
        sodiumDisplay.setText(String.valueOf(user.getSodium())+"g");

        Button proceedBtn = findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashActivity.class));
            }
        });
    }
}