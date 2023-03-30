package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ContactPersonClientList extends AppCompatActivity {
    private SearchView searchBar;
    private TextView clientListTitle;
    private DocumentReference clientDoc;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String contactPersonId = getIntent().getStringExtra("contactPersonID");

        searchBar = findViewById(R.id.searchView);
        clientListTitle=findViewById(R.id.clientListTitle);
        clientDoc = FirebaseFirestore.getInstance().collection("/ContactPerson").document(contactPersonId);

        searchBar.setVisibility(View.INVISIBLE);
        clientListTitle.setVisibility(View.VISIBLE);

        clientDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> clientList = new ArrayList<>();
                DocumentSnapshot document = task.getResult();
                List<String> group = (List<String>) document.get("clientList");
                Log.d("TAG", "onComplete: "+group);
            }
        });
//        Search clientList = new Search();
//        clientList.getClientList(query);

    }
}