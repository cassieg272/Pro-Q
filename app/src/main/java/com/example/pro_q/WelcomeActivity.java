package com.example.pro_q;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private Button caregiverBtn;
    private Button contactPersonBtn;

    //when user click on Back button, clear the back stack of all previous activities
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        caregiverBtn = findViewById(R.id.caregiverBtn);
        contactPersonBtn = findViewById(R.id.contactPersonBtn);

        // Caregiver Button - when clicked, go to CaregiverLogin
        caregiverBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, CaregiverLogin.class);
            startActivity(intent);
        });

        // Contact Person Button - when clicked, go to ContactPersonLogin
        contactPersonBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ContactPersonLogin.class);
            startActivity(intent);
        });
    }
}