package com.example.pro_q;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CaregiverTimerMainActivity extends AppCompatActivity{
    private TextView mDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView,mTotalTextView;
    private Button doneButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_timer_main);

        mDateTextView = findViewById(R.id.date_text_view);
        mStartTimeTextView = findViewById(R.id.start_time_text_view);
        mEndTimeTextView = findViewById(R.id.end_time_text_view);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String startTime = intent.getStringExtra("start_time");
        String endTime = intent.getStringExtra("end_time");
        String totalTime = intent.getStringExtra("total-time");

        mDateTextView.setText(date);
        mStartTimeTextView.setText(startTime);
        mEndTimeTextView.setText(endTime);



    }
}