package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewReportActivity extends AppCompatActivity {
    private Button back, saveReport, datePicker;
    public static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";
    public static final String KEY_REASON = "reason";

    // Declare collection & document references
    private DocumentReference clientDoc, careReportDay;
    private CollectionReference morning, afternoon, evening;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    Map<String, String> incompleteTasks = new HashMap<>();
    private ArrayList<String> completedTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        saveReport = findViewById(R.id.saveReportButton);
        back = findViewById(R.id.backButton);
        datePicker = findViewById(R.id.datePicker);

        // Get client ID & currentDayDoc from previous activity
        String id = sharedPref.getString("clientId", "");
        String finalDay = sharedPref.getString("currentDayDoc", "");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");
//        careReportDay = clientDoc.collection("CareReport").document(finalDay);

        // If task is marked complete - find the layout in the app and create a textview with text set to completed task title
        getCompletedTask(morning);
        getCompletedTask(afternoon);
        getCompletedTask(evening);

        // If task is marked incomplete - find the layout in the app and create a textview with text set to incomplete task title
        getIncompleteTask(morning);
        getIncompleteTask(afternoon);
        getIncompleteTask(evening);

        Boolean fromCaregiverMainActivity = sharedPref.getBoolean("fromCaregiverMainActivity", true);
        if (fromCaregiverMainActivity) {
            saveReport.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.GONE);
        }else{
            DatePickerDialog datePickerDialog;
            saveReport.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
            initDatePicker();
        }
         saveReport.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Map<String, Object> taskReport = new HashMap<>();
                 taskReport.put("Completed", completedTasks);
                 taskReport.put("Incomplete", incompleteTasks);
                 careReportDay.set(taskReport).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Toast.makeText(ViewReportActivity.this, "Report Saved", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
                         startActivity(intent);
                     }
                 });
             }
         });
        // Back Button - returns user to CaregiverMainActivity
        back.setOnClickListener(view -> {
            Intent intent;
            if (fromCaregiverMainActivity) {
                intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
            } else {
                intent = new Intent(ViewReportActivity.this, ContactMainActivity.class);
            }
            startActivity(intent);
        });
    }

    private void initDatePicker() {

    }

    private void getCompletedTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    LinearLayout layout = findViewById(R.id.completedTasksLayout);
                    TextView text = new TextView(ViewReportActivity.this);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    completedTasks.add(document.getId());
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
                    incompleteTasks.put(taskId, reason);
                    layout.addView(text);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

}