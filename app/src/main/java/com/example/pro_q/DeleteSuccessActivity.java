package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteSuccessActivity extends AppCompatActivity {

    private Button returnButton;

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    TODO - how do you get the current document ID to delete?
    private DocumentReference noteRef = db.collection("ContactPerson")
            .document("TMBNDIqG23KA6woWXF2L")
            .collection("Notes")
            .document();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_success);

        // Navigate to Dashboard
        returnButton = findViewById(R.id.returnButton);

//        noteRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        String noteDoc = documentSnapshot.getId();
//
//                        if (documentSnapshot.exists()) {
//                            db.collection("ContactPerson")
//                                    .document("TMBNDIqG23KA6woWXF2L")
//                                    .collection("Notes")
//                                    .document(noteDoc)
//                                    .delete();
//                        }
//                    }
//                });

        // On click, change view to Dashboard Activity
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeleteSuccessActivity.this, CaregiverMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
