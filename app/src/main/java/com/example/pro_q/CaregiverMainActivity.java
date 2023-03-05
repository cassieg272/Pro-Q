package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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
    public static final String KEY_STREET = "street";
    public static final String KEY_CITY = "city";
    public static final String KEY_POSTALCODE = "postalCode";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Path to what you want to reference
    private DocumentReference clientNameRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");
//            .collection("clientName")
//            .document("name");

    private DocumentReference clientIdRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");

    private DocumentReference clientPhoneRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");
//            .collection("clientPhone")
//            .document("phoneNumber");

    private DocumentReference clientAddressRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");
//            .collection("clientAddress")
//            .document("address");

    @SuppressLint("WrongViewCast")
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
        clientIdRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String clId = documentSnapshot.getId();
                            clientId.setText("Client Id: " + clId);
                        }
                        else {
                            Toast.makeText(CaregiverMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        clientNameRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String fname = documentSnapshot.getString(KEY_FIRSTNAME);
                            String lname = documentSnapshot.getString(KEY_LASTNAME);
                            String name = fname + " " + lname;

                            clientName.setText(name);
                        }
                        else {
                            Toast.makeText(CaregiverMainActivity.this,
                                            "No data exists",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        clientPhoneRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String phone = documentSnapshot.getString(KEY_PHONE);
                            clientPhone.setText(phone);
                        }
                        else {
                            Toast.makeText(CaregiverMainActivity.this,
                                            "No data exists",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

//        clientAddressRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    // TODO - figure out how to do the map
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Map<String, Object> addressMap = dataSnapshot.getValue();
//
//                            String st = (String) addressMap.get("street");
//                            String c = (String) addressMap.get("city");
//                            String pcode = (String) addressMap.get("postalCode");
//                            String address = st + ", " + c + " " + pcode;
//
////                            String st = documentSnapshot.getString(KEY_STREET);
////                            String c = documentSnapshot.getString(KEY_CITY);
////                            String pcode = documentSnapshot.getString(KEY_POSTALCODE);
////                            String address = st + ", " + c + " " + pcode;
//
//                            clientAddress.setText(address);
//                        }
//                        else {
//                            Toast.makeText(CaregiverMainActivity.this,
//                                            "No data exists",
//                                            Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

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
    }
}