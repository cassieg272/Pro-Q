package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CaregiverMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "CaregiverMainActivity";
    public static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";
    public static final String KEY_REASON = "reason";
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;
    private Button logout, searchReturn, timerButton;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    static String id, name, address;

    // Path to Document and Collections
    private DocumentReference clientDoc, careReportDay;
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Get values from sharedPreferences file
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

        // Set the text to the values from the shared prefs file
        clientId.setText(id);
        clientName.setText(name);
        clientAddress.setText(address);

        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");

        //Determine the current date and format it for writing/reading to/from database
        Date currentDate = Calendar.getInstance().getTime();
        String finalDay = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        finalDay += " (" + format2.format(currentDate) + ")";

        careReportDay = clientDoc.collection("CareReport").document(finalDay);

        //put formatted current date to sharePreferences for later references
        editor.putString("currentDayDoc", finalDay);
        editor.commit();

        //check if document with formatted current date is already in database. If not, create one
        careReportDay.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    careReportDay.set(new HashMap<>()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //reset tasks if create new document with current date
                                    resetTask(clientAfternoonTaskRef);
                                    resetTask(clientMorningTaskRef);
                                    resetTask(clientEveningTaskRef);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CaregiverMainActivity.this, "Fail to reset tasks", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // CLIENT INFO - Retrieve data from collection
        clientDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get Client Phone
                String phone = documentSnapshot.getString(KEY_PHONE);
                if (phone == null) {
                    clientPhone.setText("No Phone");
                } else {
                    clientPhone.setText(phone);
                }
                // Get Client Gender
                String gender = documentSnapshot.getString(KEY_GENDER);
                clientGender.setText(gender);
            } else {
                Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));

        // BUTTON BAR

        // Timer Functions
        findViewById(R.id.timerButton).setOnClickListener(view -> {
            // On click - go to CaregiverTimerMainActivity
            Date startTime = Calendar.getInstance().getTime();
            timerButton.setText("Tracking...");
            timerButton.setOnClickListener(v -> {
                timerButton.setText("Finished");
                Date finishTime = Calendar.getInstance().getTime();
                timerButton.setClickable(false);
                timerButton.setBackgroundColor(timerButton.getContext().getResources().getColor(R.color.pink_disable));
                openDialog(startTime, finishTime);
            });
        });

        // Navigate to View Report Page
        findViewById(R.id.reportButton).setOnClickListener(view -> {
            // On click - go to ViewReportActivity
            Intent intent = new Intent(CaregiverMainActivity.this, ViewReportActivity.class);
            editor.putBoolean("fromCaregiverMainActivity", true);
            editor.commit();
            startActivity(intent);
        });

        // Get info from client's morning, afternoon, evening tasks
        getTaskList(clientMorningTaskRef, findViewById(R.id.morningLayout), "morning");
        getTaskList(clientAfternoonTaskRef, findViewById(R.id.afternoonLayout), "afternoon");
        getTaskList(clientEveningTaskRef, findViewById(R.id.eveningLayout), "evening");

        // Search Return Button - returns user to Client Search page
        searchReturn.setOnClickListener(view -> {
            Intent intent = new Intent(CaregiverMainActivity.this, Search.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();

        // Logout Button - signs user out of firestore and brings them back to login page
        logout.setOnClickListener((view -> {
            mAuth.signOut();
            Intent intent = new Intent(CaregiverMainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }));


    }

    //reset tasks
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

    private void openDialog(Date startTime, Date finishTime) {
        TimerDialog timerDialog = new TimerDialog(startTime, finishTime);
        timerDialog.show(getSupportFragmentManager(), "Timer Dialog");
    }

    // Method to create buttons dynamically based on the number of tasks for each time of day
    private void getTaskList(CollectionReference taskCollection, LinearLayout layout, String time) {
        taskCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Button button = new Button(CaregiverMainActivity.this);
                    button.setText(document.getId());
                    button.setOnClickListener(v -> {
                        String passTaskId = document.getId();
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



