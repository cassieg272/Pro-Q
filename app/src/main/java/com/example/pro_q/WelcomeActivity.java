package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    // The Buttons
    private Button caregiver;
    private Button contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Navigate to Caregiver Login Page
        caregiver = findViewById(R.id.caregiverButton);

        // On click, change view from Welcome Activity to Caregiver Login Activity (and start the activity)
        caregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Contact Login Page
        contact = findViewById(R.id.contactButton);

        // On click, change view from Welcome Activity to Contact Login Activity (and start the activity)
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, ContactMainActivity.class);
                startActivity(intent);
            }
        });
    }
}