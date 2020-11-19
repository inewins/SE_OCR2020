package com.example.nutrobud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   DISCLAIMER, CODE WRITTEN ON THIS FILE WAS PARTIALLY TAKEN FROM LYDIA SARVER
*/
//class allergenData{
//    int index;
//    String data="";
//}
public class Settings_EditAllergen extends AppCompatActivity {
    List<String> ingredient_no = new ArrayList<>();    //initializing variables
    private DocumentReference dr;
    private Map<String, Object> user = new HashMap<String, Object>();
    TextView  editAllergen;
    Button addnoBtn, submit;
    Button[] btnArr = new Button[15];
    private int i;
    String allergens;
    String strArr[];
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__edit_allergen);

        final LinearLayout linLayout = findViewById(R.id.linLayout);    //add buttons to this bad boy
        editAllergen = findViewById(R.id.editAllergenText);
        submit= findViewById(R.id.subBtn);

        Intent ii = getIntent();                                        //grab data from previous activity
        Bundle b = ii.getExtras();
        if(b!=null)
        {
//            Log.d("HELP","HHHHH"+(String)b.get("fname"));
            userID = (String) b.get("userID");
            allergens = (String) b.get("allergens");
            allergens.trim();
            strArr = allergens.split(" ", 0);
//            Log.d("HELP","THIS IS ALLERGENS LIST "+strArr[0]);
        }
        dr = FirebaseFirestore.getInstance().document("users/"+userID); //to get reference to users data
        for (String s: strArr){
            ingredient_no.add(s);
        }

        for (i = 0; i < 15; i++){                                   //get context for buttons
            btnArr[i] = new Button(this);
        }

        i=0;
        if (ingredient_no.size() > 0) {                             //make buttons for all ingredient no
            for (String s:ingredient_no){
//                Log.d("123","Created button index: " +i);
                btnArr[i].setText(s);
                btnArr[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                btnArr[i].setBackgroundColor(0xFF4FDD35);
                btnArr[i].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_delete,0);
                linLayout.addView(btnArr[i]);
                deleteButtonFunc(i, s);                             //dynamically create delete button for each button
                i++;
            }

        }
        addnoBtn = (Button) findViewById(R.id.addBtn);   //addnoBtn is the add button
        addnoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = editAllergen.getText().toString().trim();
                int duplicate = 0;
                //Check if Item is already in list of unwanted ingredients
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                        duplicate = 1;
                }

                if (duplicate == 0) {           //if not duplicate, create button for that item
                    ingredient_no.add(Item);

                    btnArr[i] = new Button(btnArr[i].getContext());
                    btnArr[i].setText(Item);
                    btnArr[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    btnArr[i].setBackgroundColor(0xFF4FDD35);
                    btnArr[i].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_delete,0);
                    deleteButtonFunc(i,Item);
                    linLayout.addView(btnArr[i]);
                    i++;
                    Log.d("BUTTON MADE", String.valueOf(i));

                } else if (duplicate == 1) {
                    Toast.makeText(Settings_EditAllergen.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                }
                editAllergen.setText("");
            }
        }); //end of add button function

        submit.setOnClickListener(new View.OnClickListener() {      //if clicked submit button
            @Override
            public void onClick(View v) {
                Log.d("123", String.valueOf(ingredient_no));
                user.put("ingredientsNo", ingredient_no);
                dr.set(user, SetOptions.merge());
                Toast.makeText(Settings_EditAllergen.this, "Update Successful", Toast.LENGTH_SHORT).show();  //display notification
                startActivity(new Intent(Settings_EditAllergen.this, Settings_Main.class));
            }
        });

    } //end of onCreate
    void deleteButtonFunc(int index, String word){
        final int intlocal = index;
        final String stringlocal = word;

        btnArr[i].setOnClickListener(new View.OnClickListener() {    //if clicked, delete the button
            @Override
            public void onClick(View v) {
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (stringlocal.equals(ingredient_no.get(j)))
                        ingredient_no.remove(j);
                }
                btnArr[intlocal].setVisibility(View.GONE);
                i--;
            }
        });

    }
}