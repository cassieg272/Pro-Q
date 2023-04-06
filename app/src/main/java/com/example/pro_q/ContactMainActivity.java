package com.example.pro_q;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ContactMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;
    private Button addTask, viewReport, logout, searchReturn;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
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
        FirebaseApp.initializeApp(this);

        String id = getIntent().getStringExtra("ID");

        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);

        clientId.setText(id);

        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");

        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);

        // CLIENT INFO - Retrieve data from collection
        clientDoc.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fname = documentSnapshot.getString(KEY_FIRSTNAME);
                        String lname = documentSnapshot.getString(KEY_LASTNAME);
                        String phone = documentSnapshot.getString(KEY_PHONE);
                        String gender = documentSnapshot.getString(KEY_GENDER);

                        clientName.setText(fname + " " + lname);
                        clientPhone.setText(phone);
                        clientGender.setText(gender);
                    }
                    else {
                        Toast.makeText(ContactMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        clientDoc.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get Client Address
                            Map<String, String> addressMap = (Map<String, String>) document.get("address");
                            String st = addressMap.get("street");
                            String c = addressMap.get("city");
                            String pcode = addressMap.get("postalCode");
                            String address = st + ", " + c + " " + pcode;
                            clientAddress.setText(address);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Navigate to Add Task Page
        addTask = findViewById(R.id.timerButton);
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent(ContactMainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Navigate to View Report Page
        viewReport = findViewById(R.id.reportButton);
        viewReport.setOnClickListener(view -> {
            Intent intent = new Intent(ContactMainActivity.this, ContactViewReportActivity.class);
            intent.putExtra("clientID", id);
            startActivity(intent);
        });

        clientMorningTaskRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            LinearLayout layout = findViewById(R.id.morningLayout);
                            Button button = new Button(ContactMainActivity.this);
                            button.setText(document.getId());

                            button.setOnClickListener(v -> {
                                String passTaskId = document.getId();
                                String time = "morning";
                                Intent intent = new Intent(ContactMainActivity.this, ContactTaskDetailActivity.class);
                                intent.putExtra("clientID", id);
                                intent.putExtra("time", time);
                                intent.putExtra("taskId", passTaskId);
                                startActivity(intent);
                            });
                            layout.addView(button);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        clientAfternoonTaskRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            LinearLayout layout = findViewById(R.id.afternoonLayout);
                            Button button = new Button(ContactMainActivity.this);
                            button.setText(document.getId());
                            button.setOnClickListener(v -> {
                                String passTaskId = document.getId();
                                String time = "afternoon";
                                Intent intent = new Intent(ContactMainActivity.this, ContactTaskDetailActivity.class);
                                intent.putExtra("clientID", id);
                                intent.putExtra("time", time);
                                intent.putExtra("taskId", passTaskId);
                                startActivity(intent);
                            });
                            layout.addView(button);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        clientEveningTaskRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.eveningLayout);
                            Button button = new Button(ContactMainActivity.this);
                            button.setText(document.getId());
                            button.setOnClickListener(v -> {
                                String passTaskId = document.getId();
                                String time = "evening";
                                Intent intent = new Intent(ContactMainActivity.this, ContactTaskDetailActivity.class);
                                intent.putExtra("clientID", id);
                                intent.putExtra("time", time);
                                intent.putExtra("taskId", passTaskId);
                                startActivity(intent);
                            });
                            layout.addView(button);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        searchReturn = findViewById(R.id.searchBackButton);
        searchReturn.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(ContactMainActivity.this, Search.class);
            startActivity(intent);
        });

        // Logout Button - signs user out of firestore and brings them back to login page
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.leaveButton);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(ContactMainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });
    };
}

