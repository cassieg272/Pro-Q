package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;


    private FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener authStateListener;
    private EditText email_editText;
    private EditText password_editText;
    private ProgressBar progressBar;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.email_login_button);

        email_editText = findViewById(R.id.email);
        password_editText = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.email_login_button:
                        userLogin();
                        break;
                }
            }
        });
    }

    private void userLogin() {
        String email = email_editText.getText().toString().trim();
        String password = password_editText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //direct user to the dashboard page
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, SearchActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });





        }


    }




