package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Arrays;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.LinearLayout;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.firebase.firestore.DocumentReference;
import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.sql.Types.NULL;

public class WeekActivity extends AppCompatActivity {

    TextView statsTitle, weekDisplayView;
    Button backButton;
    private String date, mDay, mMonth, mYear;

    // nutrient details
    int carbs=0;
    int fat=0;
    int protein=0;
    int sodium=0;
    int sugar=0;
    int fiber=0;
    String [] nutrName;
    public String nutrientName;
    int intake;
    int counter2=0;


    // get calendar dates
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String firstDate = formatter.format(new Date());
    private String lastDate = formatter.format(new Date());
    private SimpleDateFormat displayStartFormat;
    private SimpleDateFormat displayEndFormat;

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

        // get data from "users" Firestore collection
        //getFirestoreData
        userDB.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                if(!queryDocumentSnapshot.isEmpty()){
                    List<DocumentSnapshot> userDBDataList = queryDocumentSnapshot.getDocuments();
                    int indexCounter=-1;
                    for(DocumentSnapshot d: userDBDataList) {
                        indexCounter++;
                        userDBData = d.toObject(User.class);
                        userData.add(userDBData);
                        if (userDBData.getEmail().equalsIgnoreCase(currUser.getEmail())) {

                            currUserID = userDBData.getId();
                            currUserIndex = indexCounter;

                            // get date for beginning of week- MONDAY
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // provides tdl form
                            String startDate = formatter.format(cal.getTime()); // format to yyyyMMdd for data use
                            displayStartFormat = new SimpleDateFormat("MMM d");
                            String displayStartDate = displayStartFormat.format(cal.getTime()); // format to MMMd for display on UI
                            System.out.println("start date: "+ startDate);

                            // get date for end of week - SUNDAY
                            cal.add(Calendar.DAY_OF_WEEK, 6);
                            String endDate = formatter.format(cal.getTime()); // variable for data use
                            displayEndFormat = new SimpleDateFormat("MMM d");
                            String displayEndDate = displayEndFormat.format(cal.getTime()); // variable to display time
                            System.out.println("end date: " + endDate);

                            weekDisplayView.setText(displayStartDate + " - " + displayEndDate); // display beginning and end dates of current week

                            // convert string dates to int dates to use for data comparison
                            int startNum = Integer.parseInt(startDate);
                            int endNum = Integer.parseInt(endDate);
                            int tempNum = 0;
                            String tempString;
                            int counter =0;

                            // traverse through each day of week
                            tempString=startDate; // initialize as start date aka int i. Increase as needed
                            for(int i=startNum; i<=endNum; i++){
                                // count how many dates are valid
                                tempString=Integer.toString(i);
                                    if((userData.get(currUserIndex).getStats().get(tempString) != null)){
                                        if((userData.get(currUserIndex).getStats().get(tempString).getNutrients() != null))
                                        {
                                            counter++;
                                        }
                                    }
                            }

                            // create arrays to store valid dates
                            String[] datesArray = new String[counter];
                            int[] validDates = new int[counter];
                            // store valid dates in array
                            tempString=startDate; // initialize as start date aka int i. Increase as needed
                            for(int i=startNum; i<=endNum; i++){
                                // count how many dates are valid
                                tempString=Integer.toString(i);
                                if((userData.get(currUserIndex).getStats().get(tempString) != null)){
                                    if((userData.get(currUserIndex).getStats().get(tempString).getNutrients() != null))
                                    {
                                       for(int j=0; j<counter;j++) {
                                           datesArray[j]=tempString;
                                           validDates[j]=i;
                                       }

                                    }
                                }

                            }
                            System.out.println("string array: " + datesArray[0]); // delete later
                            System.out.println("num array: " + Arrays.toString(validDates)); // delete later

                            // go through each valid date and check for nutrients tracked
                            for(int i=0; i<counter; i++) {
                                // get names of ingredients tracked
                                Set<String> milliName = userData.get(currUserIndex).getStats().get(datesArray[i]).getNutrients().keySet(); // set keys to Set
                                nutrName = convert(milliName); //convert to string array
                                System.out.println("Array of string: " + Arrays.toString(nutrName)); // delete later

                                // count number of ingredients tracked
                                for (int l = 0; l < nutrName.length; l++) {
                                    counter2++;
                                }
                                System.out.println("Counter is: " + counter); // delete later

                                // run through each ingredient tracked to get intake amount
                                for (int k = 0; k < counter2; k++) {
                                    nutrientName = nutrName[k];
                                    if (userData.get(currUserIndex).getStats().get(datesArray[i]).getNutrients().get(nutrName[k]) != NULL) {
                                        intake = userData.get(currUserIndex).getStats().get(datesArray[i]).getNutrients().get(nutrName[k]);

                                        System.out.println(nutrientName + ": " + intake + " mg");
                                        // if nutrient intake is detected, add to previous intake to get total intake for week
                                        if (nutrientName == "sodium") {
                                            sodium = sodium + intake;
                                        }
                                        if (nutrientName == "fiber") {
                                            fiber = fiber + intake;
                                        }
                                        if (nutrientName == "protein") {
                                            protein = protein + intake;
                                        }
                                        if (nutrientName == "fat") {
                                            fat = fat + intake;
                                        }
                                        if (nutrientName == "sugar") {
                                            sugar = sugar + intake;
                                        }
                                        if (nutrientName == "carbohydrate") {
                                            carbs = carbs + intake;
                                        }
                                    }
                                    else {
                                        intake = 0;
                                    }

                                }

                                System.out.println("total carbs: " + carbs);
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