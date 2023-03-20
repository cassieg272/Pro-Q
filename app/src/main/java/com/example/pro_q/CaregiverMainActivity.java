package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CaregiverMainActivity";
    private TextView clientId;
    private TextView clientName;
    private TextView clientPhone;
    private TextView clientAddress;
    private Button addNote;
    private Button addTask;
    private Button viewReport;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TASKTITLE = "title";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Path to database reference
    private DocumentReference clientInfoRef = db.collection("Client")
            .document("fKvP44KanzN3izfiFfL5");

    private DocumentReference clientAddressRef = db.collection("Client")
            .document("fKvP44KanzN3izfiFfL5");

    private CollectionReference clientMorningTaskRef = db.collection("Client")
            .document("fKvP44KanzN3izfiFfL5")
            .collection("morning");

    private DocumentReference clientAfternoonTaskRef = db.collection("Client")
            .document("fKvP44KanzN3izfiFfL5")
            .collection("Monday")
            .document("afternoon");

    private DocumentReference clientEveningTaskRef = db.collection("Client")
            .document("fKvP44KanzN3izfiFfL5")
            .collection("Monday")
            .document("evening");

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);
        FirebaseApp.initializeApp(this);

        clientId = findViewById(R.id.clientId);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);

        // CLIENT INFO - Retrieve data from collection
        clientInfoRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            // Get Client Id
                            String clId = documentSnapshot.getId();
                            clientId.setText("Client Id: " + clId);

                            // Get Client Name
                            String fname = documentSnapshot.getString(KEY_FIRSTNAME);
                            String lname = documentSnapshot.getString(KEY_LASTNAME);
                            String name = fname + " " + lname;
                            clientName.setText(name);

                            // Get Client Phone
                            String phone = documentSnapshot.getString(KEY_PHONE);
                            clientPhone.setText(phone);
                        }
                        else {
                            Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));


        clientAddressRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        // BUTTON BAR

        // Navigate to Add Note Page
        addNote = findViewById(R.id.noteButton);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Add Task Page
        addTask = findViewById(R.id.taskButton);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to View Report Page
        viewReport = findViewById(R.id.reportButton);
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMainActivity.this, EditNoteActivity.class);
                startActivity(intent);
            }
        });
//TODO - Style all buttons the same... change to buttons? add afternoon and evening
        clientMorningTaskRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<String> doclist = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                doclist.add(document.getId());
                            }

                            for (int i=0; i<doclist.size(); i++) {
                                Log.d(TAG, "testing");
                                Log.d(TAG, String.valueOf(i));
                                Log.d(TAG, String.valueOf(doclist));
                                String v = doclist.get(i);
                                Log.d(TAG, v);
                                if (doclist.get(i).equals("Bathing"))
                                {
                                    Log.d(TAG, "bathing");
                                    DocumentReference bath = clientMorningTaskRef.document("Bathing");
                                    bath.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Compression Stockings"))
                                {
                                    DocumentReference stockings = clientMorningTaskRef.document("Compression Stockings");
                                    stockings.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Dress Assist"))
                                {
                                    DocumentReference dress = clientMorningTaskRef.document("Dress Assist");
                                    dress.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Housekeeping"))
                                {
                                    DocumentReference house = clientMorningTaskRef.document("Housekeeping");
                                    house.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Meal Prep"))
                                {
                                    DocumentReference meal = clientMorningTaskRef.document("Meal Prep");
                                    meal.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Medication"))
                                {
                                    DocumentReference meds = clientMorningTaskRef.document("Medication");
                                    meds.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                                else if (doclist.get(i).equals("Personal Care"))
                                {
                                    DocumentReference pCare = clientMorningTaskRef.document("Personal Care");
                                    pCare.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String tTitle = documentSnapshot.getString(KEY_TASKTITLE);
                                                Chip chip = new Chip(CaregiverMainActivity.this, null, R.style.morningChipStyle);
                                                chip.setText(tTitle);
                                                ChipGroup chipGroup = findViewById(R.id.morningChips);
                                                chipGroup.addView(chip);
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        ChipGroup chipGroup = findViewById(R.id.morningChips);
        chipGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDetailsDialog();
            }

            public void taskDetailsDialog()
            {
                TaskDetailsDialog taskDetailsDialog = new TaskDetailsDialog();
                taskDetailsDialog.show(getSupportFragmentManager(), "it worked!");
            }
        });
    }
}
