package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewManager;
import android.widget.Button;
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
import java.util.Map;
import java.util.Set;

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


public class GoalsActivity extends AppCompatActivity {
    Button b2Dashbtn;
    TextView titleView, ratio, noDataText;

    //nutrient progress bars
    //private ProgressBar;

    int[] nutrGoal;
    public String nutrientName;
    String [] nutrName;
    int counter=0;
    int intake;
    int goal;
    ArrayList<TextView> textArr = new ArrayList<TextView>(); // Create an ArrayList object
    ArrayList<ProgressBar> progArr = new ArrayList<ProgressBar>(); // Create an ArrayList object
    ArrayList<String> name = new ArrayList<String>(); // Create an ArrayList object
    ArrayList<Integer> number = new ArrayList<Integer>(); // Create an ArrayList object
    double progPercent = 0.0;

    //goal calorie progress
    int currentCals;
    int calGoals;
    private Handler hdlr = new Handler ();

    //get today's date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

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

    // convert set from hashmap to string array
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
        setContentView(R.layout.activity_goals);
        final Resources res = getResources();

        b2Dashbtn=(Button)findViewById(R.id.backToDashButton);
        b2Dashbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashActivity.class);
                startActivity(i);
            }
        });

       // hardcode progress bars to dynamically add
        for(int i =0; i< 15; i++){      //adding 15 prog bars and text views
            TextView texttt = new TextView(this);
            ProgressBar proggg = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            proggg.setSecondaryProgress(100);
            proggg.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_limit));
            proggg.setMinimumWidth(100);

            textArr.add(texttt);
            progArr.add(proggg);
        }

        final int[] i = new int[1];

        titleView = (TextView) findViewById(R.id.titleView); // "goals" title
        titleView = (TextView) findViewById(R.id.calorieText); // "CALORIES" title

        // display current date
        dateTimeDisplay = (TextView) findViewById(R.id.date_textView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE- MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        // get data from "users" Firestore collection
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
                        if(userDBData.getEmail().equalsIgnoreCase(currUser.getEmail())){

                            currUserID = userDBData.getId();
                            currUserIndex = indexCounter;
                            calGoals = userData.get(currUserIndex).getCalorieGoalsQty();
                            // check if data exists for current date and calories tracked
                            if ((userData.get(currUserIndex).getStats().get(todayDate) != null)) {
                                if (userData.get(currUserIndex).getStats().get(todayDate).getCaloriesTrackedQty() != NULL) {
                                    currentCals = userData.get(currUserIndex).getStats().get(todayDate).getCaloriesTrackedQty();
                                }
                                else {
                                    currentCals = 0;
                                }
                            }
                            else {
                                currentCals = 0;
                            }

                            // horizontal progress bar for calories, but in circular shape
                            Drawable drawable = res.getDrawable(R.drawable.circular);
                            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.calProgressBar);
                            mProgress.setProgress(0); // main progress starting at 0. Will correspond to database once pulled successfully
                            mProgress.setSecondaryProgress(calGoals); // secondary progress aka max progress aka calorie goals
                            mProgress.setMax(calGoals); // maximum progress aka calorie goals
                            mProgress.setProgressDrawable(drawable);

                            ratio = (TextView) findViewById(R.id.ratioView);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (currentCals<=calGoals) {
                                        //update progress bar and display current value in text view
                                        hdlr.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgress.setProgress(currentCals);
                                                ratio.setText(currentCals + "/" + calGoals);
                                            }
                                        });
                                        try {
                                            //sleep for 100 ms to show progress slowly
                                            Thread.sleep(16);
                                        } catch(InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).start();

                            // check if data exists for current date ****************
                            if ((userData.get(currUserIndex).getStats().get(todayDate) != null)) {
                                // check if user has inputted ingredients to track *********************
                                if (userData.get(currUserIndex).getIngredientsYes() != null) {
                                    // create new arrays for ingredientsYes and respective goal intake
                                    nutrGoal = new int[userData.get(currUserIndex).getIngredientsYes().size()];
                                    nutrName = new String[userData.get(currUserIndex).getIngredientsYes().size()];
                                    if (userData.get(currUserIndex).getIngredientsYes().size() != 0) {
                                        for (int i = 0; i < userData.get(currUserIndex).getIngredientsYes().size(); i++) {
                                            nutrGoal[i] = Integer.parseInt(userData.get(currUserIndex).getIngredientsYesGoalsQty().get(i));
                                            counter++;
                                        }
                                    }

                                    // check if ingredients have been tracked for date *******************
                                    if (userData.get(currUserIndex).getStats().get(todayDate).getIngredientsYesTrackedQty() != null) {
                                        Map<String, Integer> map = userData.get(currUserIndex).getStats().get(todayDate).getIngredientsYesTrackedQty();
                                        for (String key : map.keySet()) {
                                            name.add(key);
                                            number.add((Integer) map.get(key));
                                        }

                                        // get names of ingredients tracked
                                        Set<String> milliName = userData.get(currUserIndex).getStats().get(todayDate).getIngredientsYesTrackedQty().keySet(); // set keys to Set
                                        nutrName = convert(milliName); //convert to string array
                                        System.out.println("Array of string: " + Arrays.toString(nutrName)); // delete later
                                        System.out.println("Counter is: " + counter); // delete later
                                        System.out.println("Size is: " + userData.get(currUserIndex).getIngredientsYes().size()); // delete later

                                        // run through each ingredient for tracking and dynamically add
                                        int x = 0;
                                        LinearLayout linlayout = (LinearLayout) findViewById(R.id.linlayoutGoals);
                                        for (x = 0; x < counter; x++) {
                                            goal = nutrGoal[x];
                                            nutrientName = nutrName[x];
                                            intake = userData.get(currUserIndex).getStats().get(todayDate).getIngredientsYesTrackedQty().get(nutrName[x]);
                                            System.out.println(nutrientName + ": " + intake + "/" + goal);
                                            textArr.get(x).setText(nutrientName + ": " + intake + "/" + goal);
                                            Log.d("123", nutrientName + ": " + intake + "/" + goal);
                                            Log.d("123", "Intake is " + intake);
                                            Log.d("123", "Goal is " + goal);
                                            progPercent = (double) intake / (double) goal * 100;
                                            Log.d("123", "ProgPercent " + progPercent);
                                            progArr.get(x).setProgress((int) progPercent);
                                            linlayout.addView(textArr.get(x));
                                            linlayout.addView(progArr.get(x));
                                        }
                                    }
                                    // if user has ingredients to track but no scans for the date
                                    else {
                                        // run through each ingredient for tracking
                                        int x = 0;
                                        LinearLayout linlayout = (LinearLayout) findViewById(R.id.linlayoutGoals);
                                        for (x = 0; x < counter; x++) {
                                            goal = nutrGoal[x];
                                            nutrientName = nutrName[x];
                                            intake = 0;
                                            System.out.println(nutrientName + ": " + intake + "/" + goal);
                                            textArr.get(x).setText(nutrientName + ": " + intake + "/" + goal);
                                            Log.d("123", nutrientName + ": " + intake + "/" + goal);
                                            Log.d("123", "Intake is " + intake);
                                            Log.d("123", "Goal is " + goal);
                                            progPercent = (double) intake / (double) goal * 100;
                                            Log.d("123", "ProgPercent " + progPercent);
                                            progArr.get(x).setProgress((int) progPercent);
                                            linlayout.addView(textArr.get(x));
                                            linlayout.addView(progArr.get(x));
                                        }
                                    }
                                }
                                // if user has not entered any ingredients to track
                                else {
                                    noDataText = (TextView) findViewById(R.id.noDataDisplay);
                                    noDataText.setText("Ingredients to track have not been specified.");
                                }
                            }
                            // if data does not exist for date, but user specifies ingredients to track
                            else{
                                noDataText = (TextView) findViewById(R.id.noDataDisplay);
                                noDataText.setText("No ingredients have been tracked. Please scan nutrition label to view intake goal.");
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