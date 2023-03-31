package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class TaskDetailActivity extends AppCompatActivity {
    private TextView category, description, title, timeOfDay;
    private CardView cardView;
    private EditText reason;
    private Button incomplete, markIncomplete, markComplete;

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REASON = "reason";
    public static final boolean KEY_CAREGIVER_COMPLETE = Boolean.parseBoolean("caregiverComplete");

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference clientInfoRef = db.collection("Client");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String taskId = getIntent().getStringExtra("taskId");

        String time = getIntent().getStringExtra("time");
        String clientId = getIntent().getStringExtra("clientID");
        Log.d(TAG, "client id is:" + clientId);

        DocumentReference task = clientInfoRef.document(clientId).collection(time).document(taskId);

        category = findViewById(R.id.categoryFill);
        title = findViewById(R.id.titleFill);
        description = findViewById(R.id.descriptionFill);
        timeOfDay = findViewById(R.id.timeFill);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);

        task.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cat = documentSnapshot.getString(KEY_CATEGORY);
                        String t = taskId;
                        String d = documentSnapshot.getString(KEY_DESCRIPTION);

                        category.setText(cat);
                        title.setText(t);
                        description.setText(d);
                        timeOfDay.setText(time);
                    }
                });
        incomplete = findViewById(R.id.deleteButton);
        incomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(view.VISIBLE);
            }
        });

        markIncomplete = findViewById(R.id.markIncompleteButton);
        markIncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = findViewById(R.id.reasonFill);
                String reasonEntered = String.valueOf(reason.getText());
                if (reasonEntered.equals("")) {
                    Toast.makeText(TaskDetailActivity.this, "Please enter a reason.", Toast.LENGTH_LONG).show();
                }
                else {
                    String reasonSend = String.valueOf(reason.getText());
                    task.update(KEY_REASON, reasonSend);
                    Toast.makeText(TaskDetailActivity.this, "Task marked incomplete.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        markComplete = findViewById(R.id.updateButton);
        markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO figure out how to change this
                task.update(String.valueOf(KEY_CAREGIVER_COMPLETE), true);
                Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}