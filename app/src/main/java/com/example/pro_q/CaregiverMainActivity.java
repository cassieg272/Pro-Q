package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CaregiverMainActivity";
    private Button showButton;
    private TextView recName;

    // Keys
    public static final String KEY_CLIENTNAME = "firstName";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference clientNameRef = db.collection("client")
            .document("ID_000002")
            .collection("clientName")
            .document("name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);
        FirebaseApp.initializeApp(this);

        recName = findViewById(R.id.recName);
        showButton = findViewById(R.id.button);

        showButton.setOnClickListener(v -> {
            // Retrieve data from collection
            clientNameRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String clientName = documentSnapshot.getString(KEY_CLIENTNAME);

                                recName.setText(clientName);
                            }
                            else {
                                Toast.makeText(CaregiverMainActivity.this,
                                        "No data exists",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
        });

        // Create a hashmap
//        Map<String, Object> data = new HashMap<>();
//        data.put(KEY_CLIENTNAME, recName);


    }

}