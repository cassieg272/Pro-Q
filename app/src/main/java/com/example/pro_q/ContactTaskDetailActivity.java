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

    private TextView category;
    private TextView description;
    private TextView title;
    private CardView cardView;
    private EditText reason;
    private Button delete;
    private Button update;
    private Button back;

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
        setContentView(R.layout.activity_contact_task_detail);
        String reference = getIntent().getStringExtra("ref");
        String taskId = getIntent().getStringExtra("taskId");

        // TODO How do I pass a document reference
        DocumentReference task = db.collection(reference).document(taskId);

        category = findViewById(R.id.categoryFill);
        title = findViewById(R.id.titleFill);
        description = findViewById(R.id.categoryFill);
        cardView = findViewById(R.id.cardview);
        cardView.setVisibility(View.INVISIBLE);
        delete = findViewById(R.id.deleteButton);
        update = findViewById(R.id.updateButton);

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