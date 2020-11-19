package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nutrobud.DashActivity;
import com.example.nutrobud.R;
import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    EditText EmailText, PasswordText;
    Button loginbtn, signupbtn, forgotbtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthStateLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //This user variable will be passed through the sign up activities to store all data if needed
        final User user = new User();

        EmailText = findViewById(R.id.email);
        PasswordText = findViewById(R.id.password);
        loginbtn = findViewById(R.id.LoginBtn);
        signupbtn = findViewById(R.id.SignUpBtn);
        forgotbtn = findViewById(R.id.ForgotBtn);
        progressBar = findViewById(R.id.ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();


        fAuthStateLister = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser fFirebaseUser = fAuth.getCurrentUser();
                if( fFirebaseUser != null){
                    Toast.makeText(Login.this, "Log in Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DashActivity.class));
                }
                else{
                    Toast.makeText(Login.this, "Your email or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //If there is already a user active through authenticator, they will be automatically logged in
       /* if(fAuth.getCurrentUser() != null)
            startActivity(new Intent(getApplicationContext(), DashActivity.class));*/
       

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = EmailText.getText().toString().trim();
                final String Password = PasswordText.getText().toString().trim();

                //Show progressbar to show loading
                progressBar.setVisibility(View.VISIBLE);

                //Check if email is empty
                if (TextUtils.isEmpty(Email)) {
                    EmailText.setError("Email is required");
                    return;
                }

                //Check if password is empty
                if (TextUtils.isEmpty(Password)) {
                    PasswordText.setError("Password is required");
                    return;
                }

                fAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Hide progress bar because task is complete
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getApplicationContext(), Settings_Main.class));
                        }
                    }
                });


            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Pass intent so user goes as well
                Intent i = new Intent(getApplicationContext(), SignUpLoginInfo.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailText.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    EmailText.setError("Enter email first");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}
