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

public class ContactTaskDetailActivity extends AppCompatActivity {
    private EditText category, description, title, timeOfDay;
    private CardView cardView;
    // TODO add delete and update functions
    private Button delete, update, back;

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REASON = "reason";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference clientInfoRef = db.collection("Client");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_task_detail);
        String taskId = getIntent().getStringExtra("taskId");
        String time = getIntent().getStringExtra("time");
        String clientId = getIntent().getStringExtra("clientID");

        DocumentReference task = clientInfoRef.document(clientId).collection(time).document(taskId);

        category = findViewById(R.id.categoryEdit);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.categoryEdit);
        timeOfDay = findViewById(R.id.timeEdit);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);
        delete = findViewById(R.id.deleteButton);
        update = findViewById(R.id.updateButton);

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
        back = findViewById(R.id.goBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactTaskDetailActivity.this, ContactMainActivity.class);
                startActivity(intent);
            }
        });
    }
}