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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactViewReportActivity extends AppCompatActivity {
    private Button back;

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference morning = db.collection("Client").document("pnX0EcGFTQG24EcNR4P2").collection("morning");
    private CollectionReference afternoon = db.collection("Client").document("pnX0EcGFTQG24EcNR4P2").collection("afternoon");
    private CollectionReference evening = db.collection("Client").document("pnX0EcGFTQG24EcNR4P2").collection("evening");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view_report);

        morning.whereEqualTo("caregiverComplete", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout layout = findViewById(R.id.completedTasksLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        morning.whereEqualTo("caregiverComplete", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "incomplete tasks");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout layout = findViewById(R.id.incompleteLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        afternoon.whereEqualTo("caregiverComplete", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "complete tasks");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout layout = findViewById(R.id.completedTasksLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        afternoon.whereEqualTo("caregiverComplete", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "incomplete tasks");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout layout = findViewById(R.id.incompleteLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        evening.whereEqualTo("caregiverComplete", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "complete tasks");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout layout = findViewById(R.id.completedTasksLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        evening.whereEqualTo("caregiverComplete", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "incomplete tasks");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout layout = findViewById(R.id.incompleteLayout);
                                TextView text = new TextView(ContactViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactViewReportActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}