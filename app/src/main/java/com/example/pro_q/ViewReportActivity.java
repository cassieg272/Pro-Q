package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewReportActivity extends AppCompatActivity {
    private Button back, reset;
    public static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";
    public static final String KEY_REASON = "reason";

    // Declare collection & document references
    private DocumentReference clientDoc;
    private CollectionReference morning, afternoon, evening;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        reset = findViewById(R.id.resetButton);
        back = findViewById(R.id.backButton);

        // Get client ID from previous activity
        String id = sharedPref.getString("clientId", "");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");

        // If task is marked complete - find the layout in the app and create a textview with text set to completed task title
        getCompletedTask(morning);
        getCompletedTask(afternoon);
        getCompletedTask(evening);

        // If task is marked incomplete - find the layout in the app and create a textview with text set to incomplete task title
        getIncompleteTask(morning);
        getIncompleteTask(afternoon);
        getIncompleteTask(evening);

        Boolean fromCaregiverMainActivity = sharedPref.getBoolean("fromCaregiverMainActivity", true);


        // Back Button - returns user to CaregiverMainActivity
        back.setOnClickListener(view -> {
            Intent intent;
            if(fromCaregiverMainActivity) {
                intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
            }else{
                intent = new Intent(ViewReportActivity.this, ContactMainActivity.class);
            }
            startActivity(intent);
        });

        if (!fromCaregiverMainActivity) {
            reset.setVisibility(View.VISIBLE);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetTask(morning);
                    resetTask(afternoon);
                    resetTask(evening);
                    Intent intent = new Intent(ViewReportActivity.this, ContactMainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            reset.setVisibility(View.GONE);
        }

        Alarm alarm = new Alarm(this);
        alarm.activateAlarm();
        Log.d(TAG, "alarm set");
    }

    private void getCompletedTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    LinearLayout layout = findViewById(R.id.completedTasksLayout);
                    TextView text = new TextView(ViewReportActivity.this);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text.setText(document.getId());
                    layout.addView(text);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void getIncompleteTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "no").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    LinearLayout layout = findViewById(R.id.incompleteLayout);
                    TextView text = new TextView(ViewReportActivity.this);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    String taskId = document.getId();
                    String reason = document.getString(KEY_REASON);
                    text.setText(taskId + " : " + reason);
                    layout.addView(text);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    private void resetTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskRef = document.getId();
                            // Set tasks marked completed in database back to no (not complete)
                            taskCollection.document(taskRef).update(KEY_CAREGIVER_COMPLETE, "no");
                        }
                    }
                });
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "no").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskRef = document.getId();
                            // Remove any "reasons" in database - reset to ""
                            taskCollection.document(taskRef).update(KEY_REASON, "");
                        }
                    }
                });
    }
}