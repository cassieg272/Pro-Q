package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CaregiverMainActivity";
    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;

    private Button addNote, addTask, viewReport;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    //    String phone, gender, name, address, id;
    public static final String KEY_TASKTITLE = "title";

    // get references for client document and collections
    private DocumentReference clientDoc;
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);

        String name = getIntent().getStringExtra("Name");
        String address = getIntent().getStringExtra("Address");
        String id = getIntent().getStringExtra("ID");

        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);

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
                    clientPhone.setText(phone);
                    String gender = documentSnapshot.getString(KEY_GENDER);
                    clientGender.setText(gender);
                } else {
                    Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Navigate to Add Note Page
        findViewById(R.id.noteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Add Task Page
        findViewById(R.id.taskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to View Report Page
        findViewById(R.id.reportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, ViewReportActivity.class);
                intent.putExtra("clientID", id);
                startActivity(intent);
            }
        });

        //generate task list
        getTaskList(clientEveningTaskRef, findViewById(R.id.eveningLayout));
        getTaskList(clientMorningTaskRef, findViewById(R.id.morningLayout));
        getTaskList(clientAfternoonTaskRef, findViewById(R.id.afternoonLayout));
    }

    private void getTaskList (CollectionReference clientTaskRef, LinearLayout layout){
        clientTaskRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Button button = new Button(CaregiverMainActivity.this);
                        button.setText(document.getId());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String passTaskId = String.valueOf(button.getText());
                                String passRef = String.valueOf(clientTaskRef);
                                Intent intent = new Intent(CaregiverMainActivity.this, TaskDetailActivity.class);
                                intent.putExtra("taskId", passTaskId);
                                intent.putExtra("ref", clientTaskRef.getId());
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
    }
}



