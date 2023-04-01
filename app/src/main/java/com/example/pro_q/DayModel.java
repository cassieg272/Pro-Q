package com.example.pro_q;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DayModel extends AppCompatActivity {
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;
    private DocumentReference clientDoc;
    String clientId;
    Context context;


    public DayModel(String id, Context context) {
        this.clientId = id;
        this.context = context;
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");
    }
    private void getTaskList (CollectionReference clientTaskRef, LinearLayout layout){
        clientTaskRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Button button = new Button(context);
                        button.setText(document.getId());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String passTaskId = String.valueOf(button.getText());
                                String passRef = String.valueOf(clientTaskRef);
                                Intent intent = new Intent(context, TaskDetailActivity.class);
                                intent.putExtra("taskId", passTaskId);
                                intent.putExtra("ref", clientTaskRef.getId());
                                startActivity(intent);
                            }
                        });
                        layout.addView(button);
                    }
                } else {
                    Toast.makeText(context, "Error getting documents "+ task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
