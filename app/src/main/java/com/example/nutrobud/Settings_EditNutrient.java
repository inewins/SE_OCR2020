package com.example.nutrobud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings_EditNutrient extends AppCompatActivity {
    List<String> ingredient_yes = new ArrayList<>();    //initializing variables
    List<String> ingredient_yesGoal = new ArrayList<>();    //initializing variables
    private DocumentReference dr;
    private Map<String, Object> user = new HashMap<String, Object>();
    TextView editnutrient, editCal;
    Button addnoBtn, submit;
    Button[] btnArr = new Button[15];
    private int i;
    String nutrients;
    String[] nutriArr;
    String[] nutriGoalArr;
    String userID;
    int calGoals;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView nutriName;
    EditText editNutri;
    Button save, delete, cancel, back2Settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__edit_nutrient);

        back2Settings = findViewById(R.id.back2SettingBtn3);
        back2Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_EditNutrient.this, Settings_Main.class));
            }
        });

        final LinearLayout linLayout = findViewById(R.id.NutlinLayout);    //add buttons to this bad boy
        editnutrient = findViewById(R.id.NutEditText);
        submit= findViewById(R.id.NutsubBtn);

        Intent ii = getIntent();                                        //grab data from previous activity
        Bundle b = ii.getExtras();
        if(b!=null)
        {
//            Log.d("HELP","HHHHH"+(String)b.get("fname"));
            userID = (String) b.get("userID");
            calGoals = (int) b.get("calGoals");
            nutrients = (String) b.get("nutrients");
            nutrients.trim();
            nutriArr = nutrients.split(" ", 0);             //store nutrients in array nutriArr
            nutrients = (String) b.get("nutrientsGoals");
            nutrients.trim();
            nutriGoalArr = nutrients.split(" ", 0);         //store nutrient goals in array nutriGoalArr
//            Log.d("123","THIS IS CAL GOALS "+calGoals);
//            Log.d("123","THIS IS  NUTRIARR "+ Arrays.toString(nutriArr));
//            Log.d("123","THIS IS  NUTRIGOALARR "+ Arrays.toString(nutriGoalArr));
        }
        dr = FirebaseFirestore.getInstance().document("users/"+userID); //to get reference to users data
        editCal = findViewById(R.id.NutDailyCal);
        editCal.setText(calGoals+"");

        for (String s: nutriArr){                                               //convert arrays into lists
            ingredient_yes.add(s);
        }
        for (String s: nutriGoalArr){
            ingredient_yesGoal.add(s);
        }
        if(ingredient_yes.size()<ingredient_yesGoal.size())                     //if there are uneven num of values to key
        {
            while(ingredient_yes.size()<ingredient_yesGoal.size())              //remove values until match num of keys
            {
                int index = ingredient_yesGoal.size() -1;
                ingredient_yesGoal.remove(index);
            }
        }

        for (i = 0; i < 15; i++){                                   //get context for buttons
            btnArr[i] = new Button(this);
        }

        i=0;
        if (ingredient_yes.size() > 0) {                             //make buttons for all ingredient yes
            for (String s:ingredient_yes){
//                Log.d("123","Created button index: " +i);
                int check = Integer.parseInt(ingredient_yesGoal.get(i));
                String second;
                if(check == -1)
                {
                    second= "";
                }
                else
                {
                    second = ingredient_yesGoal.get(i)+ " mg";
                }
                String toDisplay = s + "  "+second;

                btnArr[i].setText(toDisplay);
                btnArr[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                btnArr[i].setBackgroundColor(0xFF4FDD35);
                btnArr[i].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_edit,0);
                linLayout.addView(btnArr[i]);
                popupFunc(i, s,ingredient_yesGoal.get(i));                             //dynamically create delete button for each button
                i++;
            }

        }
        addnoBtn = (Button) findViewById(R.id.NutaddBtn);   //addnoBtn is the add button
        addnoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = editnutrient.getText().toString().trim();
                int duplicate = 0;
                //Check if Item is already in list of unwanted ingredients
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    if (Item.equals(ingredient_yes.get(j)))
                        duplicate = 1;
                }

                if (duplicate == 0) {           //if not duplicate, create button for that item
                    ingredient_yes.add(Item);
                    ingredient_yesGoal.add("-1");               //default no QTY tracked

                    btnArr[i] = new Button(btnArr[i].getContext());
                    btnArr[i].setText(Item);
                    btnArr[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    btnArr[i].setBackgroundColor(0xFF4FDD35);
                    btnArr[i].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_edit,0);
                    popupFunc(i,Item, ingredient_yesGoal.get(i));
                    linLayout.addView(btnArr[i]);
                    i++;
//                    Log.d("BUTTON MADE", String.valueOf(i));

                } else if (duplicate == 1) {
                    Toast.makeText(Settings_EditNutrient.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                }
                editnutrient.setText("");
            }
        }); //end of add button function

        submit.setOnClickListener(new View.OnClickListener() {      //if clicked submit button
            @Override
            public void onClick(View v) {
//                Log.d("123", String.valueOf(ingredient_yes));
                user.put("ingredientsYes", ingredient_yes);
                user.put("ingredientsYesGoalsQty", ingredient_yesGoal);
                dr.set(user, SetOptions.merge());
                Toast.makeText(Settings_EditNutrient.this, "Update Successful", Toast.LENGTH_SHORT).show();  //display notification
                startActivity(new Intent(Settings_EditNutrient.this, Settings_Main.class));
            }
        });

    } //end of onCreate
    void popupFunc(int index, String word, String num){
        final int intlocal = index;
        final String stringlocal = word;
        final String number = num;

        btnArr[i].setOnClickListener(new View.OnClickListener() {    //if clicked, delete the button
            @Override
            public void onClick(View v) {
                Log.d("123","String is "+stringlocal);
                createPopup(intlocal, stringlocal,number);

            }
        });
    }

//    PopupWindow goalPopUp;
    public void createPopup(final int index, final String word, String num){

        dialogBuilder = new AlertDialog.Builder(this);
        final View editPopView = getLayoutInflater().inflate(R.layout.editpopup, null);
        nutriName = (TextView) editPopView.findViewById(R.id.NVNUT1);
        editNutri = editPopView.findViewById(R.id.NUTgoalText);
        save = editPopView.findViewById(R.id.NUTsaveBtn);
        delete = editPopView.findViewById(R.id.NUTdeleteBtn);
        cancel = editPopView.findViewById(R.id.NUTcanceleBtn);

        Log.d("123","String is111 "+word);
        dialogBuilder.setView(editPopView);
        dialog = dialogBuilder.create();
        dialog.show();

        nutriName.setText(word);
        editNutri.setText(num);

        cancel.setOnClickListener(new View.OnClickListener() {                                  //button to cancel
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {                                    //button to save
            @Override
            public void onClick(View v) {
                final LinearLayout linLayout = findViewById(R.id.NutlinLayout);
                String fromUser = String.valueOf(editNutri.getText());
                Log.d("123","Valuse of editNutri "+String.valueOf(editNutri.getText()));
                final int val = ingredient_yes.size();
                for (int j = 0; j < val; j++) {
                    if (word.equals(ingredient_yes.get(j))) {
                        ingredient_yes.remove(j);
                        ingredient_yesGoal.remove(j);
                        i--;
                        btnArr[index].setVisibility(View.GONE);
                        ingredient_yes.add(0,word);
                        ingredient_yesGoal.add(0,fromUser);               //default no QTY tracked

                        String second = fromUser + " mg";
                        String toDisplay = word + "  "+second;

                        btnArr[i] = new Button(btnArr[i].getContext());
                        btnArr[i].setText(toDisplay);
                        btnArr[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        btnArr[i].setBackgroundColor(0xFF4FDD35);
                        btnArr[i].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_edit,0);
                        popupFunc(i,word, ingredient_yesGoal.get(i));
                        linLayout.addView(btnArr[i]);
                        i++;

                    }
                }
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {                                  //button to delete
            @Override
            public void onClick(View v) {
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    if (word.equals(ingredient_yes.get(j))) {
                        ingredient_yes.remove(j);
                        ingredient_yesGoal.remove(j);
                    }
                }
                btnArr[index].setVisibility(View.GONE);
                i--;
                dialog.dismiss();

            }
        });

    }
}