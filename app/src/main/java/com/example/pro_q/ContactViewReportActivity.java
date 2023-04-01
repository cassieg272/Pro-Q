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

public class ContactViewReportActivity extends AppCompatActivity {
    private Button back;
    public static final String KEY_REASON = "reason";

    //Declare collection & document references
    private DocumentReference clientDoc;
    private CollectionReference morning, afternoon, evening;

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view_report);

        // Get client ID from previous activity
        String id = getIntent().getStringExtra("clientID");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");

        // If task is marked complete - find the layout in the app and create a textview with text set to completed task title
        morning.whereEqualTo("caregiverComplete", "yes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.completedTasksLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        afternoon.whereEqualTo("caregiverComplete", "yes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.completedTasksLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        evening.whereEqualTo("caregiverComplete", "yes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.completedTasksLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        // If task is marked incomplete - find the layout in the app and create a textview with text set to incomplete task title and reason for not completing
        morning.whereEqualTo("caregiverComplete", "no").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.incompleteLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            String taskId = document.getId();
                            String reason = document.getString(KEY_REASON);
                            text.setText(taskId + ": " + reason);
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        afternoon.whereEqualTo("caregiverComplete", "no").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.incompleteLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            String taskId = document.getId();
                            String reason = document.getString(KEY_REASON);
                            text.setText(taskId + ": " + reason);
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        evening.whereEqualTo("caregiverComplete", "no").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout layout = findViewById(R.id.incompleteLayout);
                            TextView text = new TextView(ContactViewReportActivity.this);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setText(document.getId());
                            String taskId = document.getId();
                            String reason = document.getString(KEY_REASON);
                            text.setText(taskId + ": " + reason);
                            layout.addView(text);
                        }
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        // Back Button - returns user to ContactMainActivity
        back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ContactViewReportActivity.this, ContactMainActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });
    }
}