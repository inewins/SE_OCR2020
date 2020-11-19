package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings_Delete extends AppCompatActivity {
    FirebaseFirestore userDB = FirebaseFirestore.getInstance();//Firestore ref to pull user data
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    int currUserID;
    int currUserIndex;
    User userDBData;
    List<User> userData = new ArrayList<>();
    DocumentReference dr;
    EditText deleteEdit;
    private Map<String, Object> user = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__delete);
        makeUserData();
        final TextView deleteText = findViewById(R.id.delView);
        final EditText deleteEdit = findViewById(R.id.delPass);
        final Button deleteBtn = findViewById(R.id.finalDeleteBtn);
        final CheckBox deleteBox = findViewById(R.id.confirmBox);

        deleteBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(deleteBox.isChecked()){
                    deleteText.setVisibility(View.VISIBLE);
                    deleteEdit.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.VISIBLE);
                }
                else{
                    deleteText.setVisibility(View.GONE);
                    deleteEdit.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                }

            }
        }); // end of check box

        deleteBtn.setOnClickListener(new View.OnClickListener() {           //if Delete button is clicked
            @Override
            public void onClick(View v) {
                String userID = String.valueOf(userData.get(currUserIndex).getId());
                dr = FirebaseFirestore.getInstance().document("users/"+userID); //to get reference to users data

                String pass = userData.get(currUserIndex).getPassword();                //password from DB
                String enteredPass = String.valueOf(deleteEdit.getText()).trim();       //password entered

                if(pass.equals(enteredPass)) {                                      //if password matches
                    user.put("firstName", FieldValue.delete());
                    user.put("secondName", FieldValue.delete());
                    user.put("age", FieldValue.delete());
                    user.put("calorieGoalsQty", FieldValue.delete());
                    user.put("id", FieldValue.delete());
                    user.put("ingredientsNo", FieldValue.delete());
                    user.put("ingredientsYes", FieldValue.delete());
                    user.put("password", FieldValue.delete());
                    user.put("stats", FieldValue.delete());
                    user.put("weight", FieldValue.delete());
                    user.put("email", FieldValue.delete());

                    dr.set(user, SetOptions.merge());
                    final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    user1.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("123", "User account deleted.");
                                    }
                                }
                            });

                    startActivity(new Intent(Settings_Delete.this, Login.class));
                }
                else{                                                               //if password doesn't match
                    Toast.makeText(Settings_Delete.this, "Incorrect Password", Toast.LENGTH_SHORT).show();  //display notification
                }

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
}