package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nutrobud.ui.home.Stats;
import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Settings_Main extends AppCompatActivity {

    private DocumentReference userDB = FirebaseFirestore.getInstance().collection("users").document("10006");//Firestore ref to pull user data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__main);

        /*From Here to Line 56 is me trying to hardcode a user so I can manipulate the data!*/
        List<String> ingredientsNo = new ArrayList<String>();
        ingredientsNo.add("nuts");
        ingredientsNo.add("almonds");
        ingredientsNo.add("chicken");
        List<String> ingredientsYes = new ArrayList<String>();
        ingredientsYes.add("chocolate");
        ingredientsYes.add("icecream");
        ingredientsYes.add("vanilla");
        List<String> ingredientsYesGoalsQty = new ArrayList<String>();
        ingredientsYesGoalsQty.add("1");
        ingredientsYesGoalsQty.add("2");
        ingredientsYesGoalsQty.add("3");
        List<String> ingredientsYesTrackedQty = new ArrayList<String>();
        ingredientsYesTrackedQty.add("1");
        ingredientsYesTrackedQty.add("2");
        ingredientsYesTrackedQty.add("3");

        final User[] user = {new User()};
        user[0].setId(10002);
        user[0].setEmail("anh.nguyen2@mav.uta.edu");
        user[0].setPassword("myPassWo@rd");
        user[0].setFirstName("Anh");
        user[0].setSecondName("Nguyen");
        user[0].setGender("m");
        user[0].setAge(69);
        user[0].setWeight(169);
        user[0].setIngredientsNo(ingredientsNo);
        user[0].setIngredientsYes(ingredientsYes);
        user[0].setIngredientsYesGoalsQty(ingredientsYesGoalsQty);
        user[0].setIngredientsYesTrackedQty(ingredientsYesTrackedQty);
        user[0].setCalorieGoalsQty(2000);
        user[0].setcalorieTrackedQty(2000);

        userDB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.d("User Data",doc.getData().toString());
                    }
                }
            }
        });

        /*End of Creating User*/

        Button btn2EditProfile = (Button)findViewById(R.id.editProfileBtn);                                     //Button to go to EditProfile page
        btn2EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditProfile.class));
            }
        });

        Button btn2EditAllergen = (Button)findViewById(R.id.editAllergenBtn);                                   //Button to go to EditAllergen page
        btn2EditAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditAllergen.class));

            }
        });

        Button btn2Main = (Button)findViewById(R.id.cancel2MainBtn);                                            //Button to go back to MainDashboard
        btn2Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });

        Button deleteBtn = (Button)findViewById(R.id.delete_acc);                                            //Button to go to delete account
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });

    }
}