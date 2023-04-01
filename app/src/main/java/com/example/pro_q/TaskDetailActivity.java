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

    // Keys - Match the keys to the field value in the database
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REASON = "reason";
    // TODO - make this work
    public static final boolean KEY_CAREGIVER_COMPLETE = Boolean.parseBoolean("caregiverComplete");

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Collection Reference
    private CollectionReference clientInfoRef = db.collection("Client");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Get the data from the previous activity
        String taskId = getIntent().getStringExtra("taskId");
        String time = getIntent().getStringExtra("time");
        String clientId = getIntent().getStringExtra("clientID");

        // Create a new document reference using the data passed in
        DocumentReference task = clientInfoRef.document(clientId).collection(time).document(taskId);

        category = findViewById(R.id.categoryFill);
        title = findViewById(R.id.titleFill);
        description = findViewById(R.id.descriptionFill);
        timeOfDay = findViewById(R.id.timeFill);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);

        // Get data from the newly created document reference
        task.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cat = documentSnapshot.getString(KEY_CATEGORY);
                        String t = taskId;
                        String d = documentSnapshot.getString(KEY_DESCRIPTION);

                        // Set and display task information in the app
                        category.setText(cat);
                        title.setText(t);
                        description.setText(d);
                        timeOfDay.setText(time);
                    }
                });
        // Find the Incomplete Button and set On Click to make the cardview visible
        incomplete = findViewById(R.id.deleteButton);
        incomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(view.VISIBLE);
            }
        });

        // Find the Mark Incomplete Button
        markIncomplete = findViewById(R.id.markIncompleteButton);
        markIncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the value of Reason
                reason = findViewById(R.id.reasonFill);
                String reasonEntered = String.valueOf(reason.getText());

                // If nothing is entered, display a toast requesting user to enter a reason
                if (reasonEntered.equals("")) {
                    Toast.makeText(TaskDetailActivity.this, "Please enter a reason.", Toast.LENGTH_LONG).show();
                }
                // Else, update the task document in Firestore by setting Reason to what user entered. Make a toast. Then return to CaregiverMainActivity
                else {
                    String reasonSend = String.valueOf(reason.getText());
                    task.update(KEY_REASON, reasonSend);
                    Toast.makeText(TaskDetailActivity.this, "Task marked incomplete.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Find the Mark Complete Button
        markComplete = findViewById(R.id.updateButton);
        markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO figure out how to change this
                // When clicked - set the value of caregiverComplete in Firestore to true, then return to CaregiverMainActivity
                task.update(String.valueOf(KEY_CAREGIVER_COMPLETE), true);
                Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}