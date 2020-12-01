package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nutrobud.ui.home.Stats;
import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Settings_Main extends AppCompatActivity {

    FirebaseFirestore userDB = FirebaseFirestore.getInstance();//Firestore ref to pull user data
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    int currUserID;
    int currUserIndex;
    User userDBData;
    List<User> userData = new ArrayList<>();
    DocumentReference dr;
   // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__main);

        makeUserData();



        /*End of Creating User*/

        Button btn2EditProfile = (Button)findViewById(R.id.editProfileBtn);                                     //Button to go to EditProfile page
        btn2EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("HELP","UserData FIRST NAME "+userData.get(currUserIndex).getFirstName());
                String userID = userData.get(currUserIndex).getId()+"";
                String fName = userData.get(currUserIndex).getFirstName();
                String lName = userData.get(currUserIndex).getSecondName();
                int age = userData.get(currUserIndex).getAge();
                int weight = userData.get(currUserIndex).getWeight();
                Intent i = new Intent(getApplicationContext(), Settings_EditProfile.class);
                i.putExtra("userID",userID);
                i.putExtra("fname",fName);
                i.putExtra("lname",lName);
                i.putExtra("age",age);
                i.putExtra("weight",weight);
                startActivity(i);
            }
        });


        Button btn2EditAllergen = (Button)findViewById(R.id.editAllergenBtn);                                   //Button to go to EditAllergen page
        btn2EditAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String allergensString = "";
                List<String> allergens = userData.get(currUserIndex).getIngredientsNo();
                for (String q: allergens){
                    allergensString= allergensString+q+" ";
                }
                String userID = userData.get(currUserIndex).getId()+"";
                Intent i = new Intent(getApplicationContext(), Settings_EditAllergen.class);
                i.putExtra("userID",userID);
                i.putExtra("allergens", allergensString);
                startActivity(i);
            }
        });

        Button btn2Main = (Button)findViewById(R.id.cancel2MainBtn);                                            //Button to go back to MainDashboard
        btn2Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });

        final Button deleteBtn = (Button)findViewById(R.id.delete_acc);                                            //Button to go to delete account
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_Delete.class));
            }
        });

        Button btn2Login = (Button)findViewById(R.id.signOutBtn);                                            //Button to go back to LOGIN
        btn2Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Settings_Main.this, Login.class));
            }
        });

//        Log.d("HELP","dr "+dr);

        Button btn2EditNutrient = findViewById(R.id.editNutrientsBtn);                                             //Button to go to Edit Nutrients
        btn2EditNutrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nutrientsString = "";
                String nutrientsGoalsString = "";
                int calGoals = userData.get(currUserIndex).getCalorieGoalsQty();
                List<String> nutrients = userData.get(currUserIndex).getIngredientsYes();
                List<String> nutrientsGoals = userData.get(currUserIndex).getIngredientsYesGoalsQty();
                for (String q: nutrients){
                    nutrientsString= nutrientsString + " " + q;
                    if(nutrientsString.charAt(nutrientsString.length()-1) == ',')
                    {
                        nutrientsString.substring(nutrientsString.length()-1);
                    }
                }
                for (String q: nutrientsGoals){
                    nutrientsGoalsString= nutrientsGoalsString+q+" ";
                }
                String userID = userData.get(currUserIndex).getId()+"";
                Intent i = new Intent(getApplicationContext(), Settings_EditNutrient.class);
                i.putExtra("userID",userID);
                i.putExtra("calGoals",calGoals);
                i.putExtra("nutrients", nutrientsString);
                i.putExtra("nutrientsGoals", nutrientsGoalsString);
                startActivity(i);
            }
        });

    }
    void makeUserData(){
        userDB.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                if(!queryDocumentSnapshot.isEmpty()){
                    List<DocumentSnapshot> userDBDataList = queryDocumentSnapshot.getDocuments();
                    int indexCounter=-1;
                    for(DocumentSnapshot d: userDBDataList){
                        indexCounter++;
                        userDBData = d.toObject(User.class);
                        userData.add(userDBData);
                        if(userDBData.getEmail().equalsIgnoreCase(currUser.getEmail())){
                            currUserID = userDBData.getId();
                            currUserIndex = indexCounter;
//                            Log.d("HELP","UserID index inside "+currUserIndex);
                        }
                    }
                }
            }
        });
    }
};
