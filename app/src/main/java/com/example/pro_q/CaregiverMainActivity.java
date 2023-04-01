package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CaregiverMainActivity";
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";

    // Path to Document and Collections
    private DocumentReference clientDoc;
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);

        // Receive data from previous activity
        String id = getIntent().getStringExtra("ID");
//        String name = getIntent().getStringExtra("Name");
//        String address = getIntent().getStringExtra("Address");


        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);

        // Set the fields to the data passed in and display it
        clientId.setText(id);
//        clientName.setText(name);
//        clientAddress.setText(address);

        // Create Document and Collection References
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");

        // CLIENT DOC - Retrieve data from collection
        clientDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // If the document exists... get the values of phone and gender from the document reference and set display them in app
                            String fname = documentSnapshot.getString(KEY_FIRSTNAME);
                            String lname = documentSnapshot.getString(KEY_LASTNAME);
                            String phone = documentSnapshot.getString(KEY_PHONE);
                            String gender = documentSnapshot.getString(KEY_GENDER);

                            clientName.setText(fname + " " + lname);
                            clientPhone.setText(phone);
                            clientGender.setText(gender);
                        } else {
                            Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Timer Functions
        findViewById(R.id.timerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Navigate to View Report Page
        findViewById(R.id.reportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On click - go to ViewReportActivity and pass the data held in id (store it under the name "clientID")
                Intent intent = new Intent(CaregiverMainActivity.this, ViewReportActivity.class);
                intent.putExtra("clientID", id);
                startActivity(intent);
            }
        });

        // Get info from client's morning tasks
        clientMorningTaskRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Retrieve information from the document snapshot
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Find the morning layout in the application, create a button and set its text to the document id
                                LinearLayout layout = findViewById(R.id.morningLayout);
                                Button button = new Button(CaregiverMainActivity.this);
                                button.setText(document.getId());

                                // Set the OnClickListener for the new button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // On click - go to TaskDetailActivity and pass id, time, and passTaskId
                                        String passTaskId = document.getId();
                                        String time = "morning";
                                        Intent intent = new Intent(CaregiverMainActivity.this, TaskDetailActivity.class);
                                        intent.putExtra("clientID", id);
                                        intent.putExtra("time", time);
                                        intent.putExtra("taskId", passTaskId);
                                        startActivity(intent);
                                    }
                                });
                                // Add the button to the layout
                                layout.addView(button);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Get info from client's afternoon tasks
        clientAfternoonTaskRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout layout = findViewById(R.id.afternoonLayout);
                                Button button = new Button(CaregiverMainActivity.this);
                                button.setText(document.getId());
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String passTaskId = document.getId();
                                        String time = "afternoon";
                                        Intent intent = new Intent(CaregiverMainActivity.this, TaskDetailActivity.class);
                                        intent.putExtra("clientID", id);
                                        intent.putExtra("time", time);
                                        intent.putExtra("taskId", passTaskId);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(button);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Get info from client's evening tasks
        clientEveningTaskRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout layout = findViewById(R.id.eveningLayout);
                                Button button = new Button(CaregiverMainActivity.this);
                                button.setText(document.getId());
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String passTaskId = document.getId();
                                        String time = "evening";
                                        Intent intent = new Intent(CaregiverMainActivity.this, TaskDetailActivity.class);
                                        intent.putExtra("clientID", id);
                                        intent.putExtra("time", time);
                                        intent.putExtra("taskId", passTaskId);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(button);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    };
}



