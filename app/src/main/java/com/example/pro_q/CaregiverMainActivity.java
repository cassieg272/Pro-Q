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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

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

    // Path to database reference
    private DocumentReference clientInfoRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");

    private DocumentReference clientAddressRef = db.collection("Client")
            .document("pnX0EcGFTQG24EcNR4P2");

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
    }
}
