package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactTaskDetailActivity extends AppCompatActivity {
    private TextView category, title, timeOfDay;
    private EditText description;
    private CardView cardView;
    private Button delete, update, back, confirmDelete, cancel;
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference clientInfoRef = db.collection("Client");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_task_detail);

        // Receiving data from previous activity
        String taskId = getIntent().getStringExtra("taskId");
        String time = getIntent().getStringExtra("time");
        String clientId = getIntent().getStringExtra("clientID");

        // Reference to specific task in database
        DocumentReference task = clientInfoRef.document(clientId).collection(time).document(taskId);

        category = findViewById(R.id.categoryEdit);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.descriptionEdit);
        timeOfDay = findViewById(R.id.timeEdit);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);

        // Retrieve task information from the database and display it
        task.get()
                .addOnSuccessListener(documentSnapshot -> {
                    String cat = documentSnapshot.getString(KEY_CATEGORY);
                    String t = taskId;
                    String d = documentSnapshot.getString(KEY_DESCRIPTION);

                    category.setText(cat);
                    title.setText(t);
                    description.setText(d);
                    timeOfDay.setText(time);
                });

        // The Buttons
        back = findViewById(R.id.goBackButton);
        update = findViewById(R.id.updateButton);
        delete = findViewById(R.id.deleteButton);
        cancel = findViewById(R.id.cancelButton);
        confirmDelete = findViewById(R.id.confirmDeleteButton);

        // Update Button - updates the task description
        // TODO - allow to update anything else?
        update.setOnClickListener(view -> {
            String updateDescription = description.getText().toString();
            task.update(KEY_DESCRIPTION, updateDescription);
        });

        // Delete Button - sets cardview to Visible
        delete.setOnClickListener(view -> cardView.setVisibility(View.VISIBLE));

        // Confirm Delete Button - deletes the task from the database
        confirmDelete.setOnClickListener(view -> {
            task.delete();
            Toast.makeText(ContactTaskDetailActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
        });

        // Cancel Button - sets cardview back to Invisible
        cancel.setOnClickListener(view -> cardView.setVisibility(View.INVISIBLE));

        // Back Button - returns user to ContactMainActivity
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ContactTaskDetailActivity.this, ContactMainActivity.class);
            startActivity(intent);
        });
    }
}