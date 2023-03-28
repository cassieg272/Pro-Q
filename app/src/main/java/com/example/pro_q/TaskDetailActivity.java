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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class TaskDetailActivity extends AppCompatActivity {
    private TextView showIt;
    private TextView textView11;
    private TextView category;
    private TextView description;
    private TextView title;
    private CardView cardView;
    private EditText reason;
    private Button incomplete;
    private Button markIncomplete;
    private Button markComplete;

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REASON = "reason";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference clientInfoRef = db.collection("Client");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String reference = getIntent().getStringExtra("ref");
        String taskId = getIntent().getStringExtra("taskId");

        // TODO How do I pass a document reference
        DocumentReference task = db.collection(reference).document(taskId);

        category = findViewById(R.id.categoryFill);
        title = findViewById(R.id.titleFill);
        description = findViewById(R.id.categoryFill);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);

        task.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cat = documentSnapshot.getString(KEY_CATEGORY);
                        String t = documentSnapshot.getString(KEY_TITLE);
                        String d = documentSnapshot.getString(KEY_DESCRIPTION);

                        category.setText(cat);
                        title.setText(t);
                        description.setText(d);
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
                    // TODO get the task and set reason to what is entered in the field
                    String reasonSend = String.valueOf(reason.getText());

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
                // TODO set caregiverComplete to true
                // on main page if button.caregiverComplete = true set background colour to green
                Intent intent = new Intent(TaskDetailActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}