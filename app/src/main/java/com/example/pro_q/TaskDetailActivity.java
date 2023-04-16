package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskDetailActivity extends AppCompatActivity {
    private TextView category, description, title, timeOfDay;
    private CardView cardView;
    private EditText reason;
    private Button incomplete, markIncomplete, markComplete;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REASON = "reason";
    private SharedPreferences sharedPref ;
    private SharedPreferences.Editor editor ;
    public static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";

    // Connection to Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Collection Reference
    private final CollectionReference clientInfoRef = db.collection("Client");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Get the data from the sharedPreferences file
        String taskId = sharedPref.getString("taskId", "");
        String taskTime = sharedPref.getString("taskTime", "");
        String clientId = sharedPref.getString("clientId", "");

        // Create a new document reference using the data passed in
        DocumentReference task = clientInfoRef.document(clientId).collection(taskTime).document(taskId);

        category = findViewById(R.id.categoryFill);
        title = findViewById(R.id.titleFill);
        description = findViewById(R.id.descriptionFill);
        timeOfDay = findViewById(R.id.timeFill);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);

        // Get data from the newly created document reference
        task.get().addOnSuccessListener(documentSnapshot -> {
            String cat = documentSnapshot.getString(KEY_CATEGORY);
            String t = taskId;
            String d = documentSnapshot.getString(KEY_DESCRIPTION);

            // Set and display task information in the app
            category.setText(cat);
            title.setText(t);
            description.setText(d);
            timeOfDay.setText(taskTime.substring(0, 1).toUpperCase() + taskTime.substring(1)); //capitalize first letter of taskTime value
        });
        // Find the Incomplete Button and set On Click to make the cardview visible
        incomplete = findViewById(R.id.deleteButton);
        incomplete.setOnClickListener(view -> cardView.setVisibility(View.VISIBLE));

        // Find the Mark Incomplete Button
        markIncomplete = findViewById(R.id.markIncompleteButton);
        markIncomplete.setOnClickListener(view -> {
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
                task.update(KEY_CAREGIVER_COMPLETE, "no");
                Toast.makeText(TaskDetailActivity.this, "Task marked incomplete.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });

        // Find the Mark Complete Button
        markComplete = findViewById(R.id.updateButton);
        markComplete.setOnClickListener(view -> {
            // When clicked - set the value of caregiverComplete in Firestore to true, then return to CaregiverMainActivity
            task.update(KEY_CAREGIVER_COMPLETE, "yes");
            task.update(KEY_REASON, "");
            Toast.makeText(TaskDetailActivity.this, "Task marked complete.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
            startActivity(intent);
        });
    }
}