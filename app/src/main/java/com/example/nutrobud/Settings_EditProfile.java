package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.ui.home.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings_EditProfile extends AppCompatActivity {
    private DocumentReference dr;
    private Map<String, Object> user = new HashMap<String, Object>();
    EditText first, last, age, weight;
    String userID, lname, fname;
    int sage, sweight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__edit_profile);

        first = findViewById(R.id.editFirstName);                                                   //Get id of all text view
        last = findViewById(R.id.editLastName);
        age = findViewById(R.id.editAge);
        weight = findViewById(R.id.editWeight);


        Button btn2GoBack = (Button)findViewById(R.id.back2SettingBtn);                                     //Button to go to back to Settings_Main
        btn2GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_EditProfile.this, Settings_Main.class));
            }
        });
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null)
        {
//            Log.d("HELP","HHHHH"+(String)b.get("fname"));
            userID = (String) b.get("userID");
            lname = (String) b.get("lname");
            fname = (String) b.get("fname");
            sage = (int) b.get("age");
            sweight = (int) b.get("weight");
            first.setText(fname);
            last.setText(lname);
            age.setText(sage+"");
            weight.setText(sweight+"");
        }
        dr = FirebaseFirestore.getInstance().document("users/"+userID); //to get reference to users data
        Button submit = (Button)findViewById(R.id.submitBtn);                                               //Button to upload to database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!first.getText().toString().isEmpty())                                                  //if field is NOT empty, add to hashmap
                    user.put("firstName", first.getText().toString());                                      //add all data into hashmap
                if (!last.getText().toString().isEmpty())
                    user.put("lastName", last.getText().toString());
                if (!age.getText().toString().isEmpty())
                    user.put("age", Integer.parseInt(age.getText().toString()));
                if (!weight.getText().toString().isEmpty())
                    user.put("weight", Integer.parseInt(weight.getText().toString()));

                dr.set(user, SetOptions.merge());                                                           //update DB
                Toast.makeText(Settings_EditProfile.this, "Update Successful", Toast.LENGTH_SHORT).show();  //display notification
            }
        });





    }
}