package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewReportActivity extends AppCompatActivity {
    private Button back;
    public static final String KEY_REASON = "reason";

    // Declare collection & document references
    private DocumentReference clientDoc;
    private CollectionReference morning, afternoon, evening;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        // Get client ID from previous activity
        String id = getIntent().getStringExtra("clientID");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");

        // If task is marked complete - find the layout in the app and create a textview with text set to completed task title
        morning.whereEqualTo("caregiverComplete", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout layout = findViewById(R.id.completedTasksLayout);
                                TextView text = new TextView(ViewReportActivity.this);
                                // TODO change text size
                                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
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
                                TextView text = new TextView(ViewReportActivity.this);
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
                                TextView text = new TextView(ViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // If task is marked incomplete - find the layout in the app and create a textview with text set to incomplete task title
        morning.whereEqualTo("caregiverComplete", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout layout = findViewById(R.id.incompleteLayout);
                                TextView text = new TextView(ViewReportActivity.this);
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
                                TextView text = new TextView(ViewReportActivity.this);
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
                                TextView text = new TextView(ViewReportActivity.this);
                                text.setText(document.getId());
                                layout.addView(text);
                            }
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Back Button - returns user to CaregiverMainActivity
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}