package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ContactMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "CaregiverMainActivity";
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;
    private Button logout, clientListButton, addTask, viewReport;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    static String id, name, address;

    // Path to Document and Collections
    private DocumentReference clientDoc;
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Get values from sharedPreferences file
        name = sharedPref.getString("clientName", "<Name>");
        address = sharedPref.getString("clientAddress", "<Address>");
        id = sharedPref.getString("clientId", "<Id>");

        addTask = findViewById(R.id.addTask);
        viewReport = findViewById(R.id.reportButton);

        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);

        clientId.setText(id);
        clientName.setText(name);
        clientAddress.setText(address);

        // Declare & initialize database references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");

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
                String gender = documentSnapshot.getString(KEY_GENDER);
                clientGender.setText(gender);
            } else {
                Toast.makeText(ContactMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Navigate to Add Task Page
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent(ContactMainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Navigate to View Report Page
        viewReport.setOnClickListener(view -> {
            Intent intent = new Intent(ContactMainActivity.this, ViewReportActivity.class);
            editor.putBoolean("fromCaregiverMainActivity", false);
            editor.commit();
            startActivity(intent);
        });

        // Populate the Layouts with Tasks in Database
        getTaskList(clientMorningTaskRef, findViewById(R.id.morningLayout), "morning");
        getTaskList(clientAfternoonTaskRef, findViewById(R.id.afternoonLayout), "afternoon");
        getTaskList(clientEveningTaskRef, findViewById(R.id.eveningLayout), "evening");

        // BOTTOM BUTTONS

        // Client List Button - return user to client list (Search Activity)
        clientListButton = findViewById(R.id.clientListButton);
        clientListButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactMainActivity.this, ContactPersonClientList.class);
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
    }

    // Method to create buttons dynamically based on the number of tasks for each time of day
    private void getTaskList(CollectionReference taskCollection, LinearLayout layout, String time) {
        taskCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Button button = new Button(ContactMainActivity.this);
                    button.setText(document.getId());
                    button.setOnClickListener(v -> {
                        String passTaskId = document.getId();
                        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("taskId", passTaskId);
                        editor.putString("taskTime", time);
                        editor.commit();
                        Intent intent = new Intent(ContactMainActivity.this, ContactTaskDetailActivity.class);
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

