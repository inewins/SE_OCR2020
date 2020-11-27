package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.view.View;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import android.widget.ScrollView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.LinearLayout;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.example.nutrobud.ui.home.User;
import com.example.nutrobud.ui.home.Stats;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.sql.Types.NULL;

public class StatisticsDayActivity extends AppCompatActivity {
    Button backButton;
    ListView statsView;
    private List<User> userD = new ArrayList<>();
    TextView noData;
    ImageView pic;

    // variables to get nurtient and calorie data
    int[] nutrGoal;
    String[] nutritionGoal;
    public String nutrientName;
    public String temp = "";
    String[] text;
    int[] bar;
    //Set<milliName>;
    String [] nutrName;
    int [] nutrAmount;
    int counter=0;
    int milligrams;
    int intake;
    int goal;
    int currentCals;

    // get today's date
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String todayDate = formatter.format(new Date());


    // get data from Firestore
    private FirebaseFirestore userDB = FirebaseFirestore.getInstance();
    private List<User> userData= new ArrayList<>();
    private User userDBData;
    private DocumentReference dr;
    private int currUserID;
    private int currUserIndex;
    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();


    // function to convert map set into strings to use hashmap elements
    public static String[] convert(Set<String> setofString){
        // create String[] of size of setOfString
        String[] arrayOfString = new String[setofString.size()];

        //copy elements from set to string array
        int index = 0;
        for (String str : setofString) {
            arrayOfString[index++] = str;
        }

        //return formed String[]
        return arrayOfString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_day);


        statsView = findViewById(R.id.statsView);

        // button to go back to initial statistics page to change view
        backButton=(Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(i);
            }
        });

        //getFirestoreData
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
                        if(userDBData.getEmail().equalsIgnoreCase(currUser.getEmail())) {
                            currUserID = userDBData.getId();
                            currUserIndex = indexCounter;

                            // check if data exists for current date and calories tracked
                            if ((userData.get(currUserIndex).getStats().get(todayDate) != null)) {
                                if (userData.get(currUserIndex).getStats().get(todayDate).getCaloriesTrackedQty() != NULL) {
                                    currentCals = userData.get(currUserIndex).getStats().get(todayDate).getCaloriesTrackedQty();
                                } else {
                                    currentCals = 0;
                                }

                                // get names of ingredients tracked
                                Set<String> milliName = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().keySet(); // set keys to Set
                                nutrName = convert(milliName); //convert to string array
                                System.out.println("Array of string: " + Arrays.toString(nutrName)); // delete later

                                for (int i = 0; i < nutrName.length; i++) {
                                    counter++;
                                }
                                System.out.println("Counter is: " + counter); // delete later

                                // run through each ingredient to get intake amount
                                for (int i = 0; i < counter; i++) {
                                    nutrientName = nutrName[i];
                                    if (userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get(nutrName[i]) != NULL) {
                                        intake = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get(nutrName[i]);
                                    } else {
                                        intake = 0;
                                    }
                                    System.out.println(nutrientName + ": " + intake + " mg");

                                }

                                // make list to display statistics
                                final ArrayList<String> output = new ArrayList<>();
                                output.add("calories: " + currentCals + " cals");
                                for (int i = 0; i < counter; i++) {
                                    output.add(nutrName[i] + ": " + userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get(nutrName[i]) + " mg");
                                }
                                final ArrayAdapter outputAdapted = new ArrayAdapter(StatisticsDayActivity.this, android.R.layout.simple_list_item_1, output);
                                statsView.setAdapter(outputAdapted);
                            }
                            // if data does not exist for current date
                            else {
                                noData=(TextView) findViewById(R.id.noDataDisplay);
                                noData.setText("No data is available. Please scan nutrition label to view nutritional statistics!");
                                pic=(ImageView) findViewById(R.id.picture);
                            }
                        }
                    }
                    dr = FirebaseFirestore.getInstance().document("users/"+currUserID);//Document ref to post data
                }
            }
        });
        //END: getFirestoreData
    }
}
