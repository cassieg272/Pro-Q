package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CaregiverMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "CaregiverMainActivity";
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;
    private Button logout, searchReturn, timerButton;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    static String id, name, address;

    // Path to Document and Collections
    private DocumentReference clientDoc;
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //get values from sharedPreferences file
        name = sharedPref.getString("clientName", "<Name>");
        address = sharedPref.getString("clientAddress", "<Address>");
        id = sharedPref.getString("clientId", "<Id>");


        clientId = findViewById(R.id.clientId);
        timerButton = findViewById(R.id.timerButton);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);
        searchReturn = findViewById(R.id.searchReturnButton);
        logout = findViewById(R.id.logoutButton);

        clientId.setText(id);
        clientName.setText(name);
        clientAddress.setText(address);

        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");

        // CLIENT INFO - Retrieve data from collection
        clientDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get Client Phone
                    String phone = documentSnapshot.getString(KEY_PHONE);
                    if (phone == null) {
                        clientPhone.setText("No Phone");
                    } else {
                        clientPhone.setText(phone);
                    }
                    String gender = documentSnapshot.getString(KEY_GENDER);
                    clientGender.setText(gender);
                } else {
                    Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Timer Functions
        findViewById(R.id.timerButton).setOnClickListener(view -> {
            // On click - go to CaregiverTimerMainActivity
            Date startTime = Calendar.getInstance().getTime(); //record the time user click Start time button
            timerButton.setText("Tracking...");
            timerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerButton.setText("Finished");
                    Date finishTime = Calendar.getInstance().getTime(); //record the time user click Finish button
                    timerButton.setClickable(false); //make button no longer clickable
                    timerButton.setBackgroundColor(timerButton.getContext().getResources().getColor(R.color.pink_disable));
                    openDialog(startTime, finishTime);
                }
            });
        });

        // Navigate to View Report Page
        findViewById(R.id.reportButton).setOnClickListener(view -> {
            // On click - go to ViewReportActivity
            Intent intent = new Intent(CaregiverMainActivity.this, ViewReportActivity.class);
            editor.putBoolean("fromCaregiverMainActivity", true);
//            editor.putBoolean("fromContactMainActivity", false);
            editor.commit();
            startActivity(intent);
        });

        // Get info from client's morning, afternoon, evening tasks
        getTaskList(clientMorningTaskRef, findViewById(R.id.morningLayout), "morning");
        getTaskList(clientAfternoonTaskRef, findViewById(R.id.afternoonLayout), "afternoon");
        getTaskList(clientEveningTaskRef, findViewById(R.id.eveningLayout), "evening");

        mAuth = FirebaseAuth.getInstance();

        // Search Return Button - returns user to Client Search page
        searchReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        // Logout Button - signs user out of firestore and brings them back to login page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(CaregiverMainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openDialog(Date startTime, Date finishTime) {
        Log.d(TAG, "openDialog: "+startTime+" "+finishTime);
        TimerDialog timerDialog = new TimerDialog(startTime, finishTime);
        timerDialog.show(getSupportFragmentManager(), "Timer Dialog");
    }

    //method to create buttons dynamically based on the number of tasks for each time of day
    private void getTaskList(CollectionReference taskCollection, LinearLayout layout, String time) {
        taskCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Button button = new Button(CaregiverMainActivity.this);
                    button.setText(document.getId());
                    button.setOnClickListener(v -> {
                        String passTaskId = document.getId();
                        Log.d(TAG, "getTaskList: " + passTaskId + " " + id + " " + time);
                        Intent intent = new Intent(CaregiverMainActivity.this, TaskDetailActivity.class);
                        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("taskId", passTaskId);
                        editor.putString("taskTime", time);
                        editor.commit();
                        startActivity(intent);
                    });
                    layout.addView(button);
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}



