package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {
    private AutoCompleteTextView category, timeOfDay;
    private TextView title;
    String newDesc, newTitle, newTime, newCategory ="";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private EditText description;
    private Button create, back;
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference clientDoc;
    private CollectionReference clientInfoRef = db.collection("Client");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Receiving data from previous activity
        String clientId = sharedPref.getString("clientId", "");

        // Reference to specific task in database
        clientDoc = clientInfoRef.document(clientId);

        category = findViewById(R.id.categoryValue);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.descriptionEdit);
        timeOfDay = findViewById(R.id.timeValue);

        back = findViewById(R.id.goBackButton);
        create = findViewById(R.id.createBtn);

        //set drop down list to select time
        String[] timeArr = getResources().getStringArray(R.array.time);
        ArrayAdapter timeArrayAdapter = new ArrayAdapter(AddTaskActivity.this, R.layout.dropdown_item, timeArr);
        timeOfDay.setAdapter(timeArrayAdapter);

//        //Attach adapters to AutoCompleteTextView create drop down list
//        String[] splitedCatList = catList.split("\\s*,\\s*");
//        Log.d("TAG", "onCreate: "+splitedCatList.length+" "+splitedCatList.toString());
//        ArrayAdapter catArrayAdapter = new ArrayAdapter(ContactTaskDetailActivity.this, R.layout.dropdown_item, splitedCatList);
//        category.setAdapter(catArrayAdapter);


        // Create Task Button - create task upon click
        create.setOnClickListener(view -> {
             newDesc = description.getText().toString();
             newTitle = title.getText().toString();
             newTime = timeOfDay.getText().toString();
             newCategory = "category";//category.getText().toString();

            Log.d("TAG", "onCreate: "+newDesc+" "+newTime+" "+newTitle+" "+newCategory);
            //check if user left any field blank, if yes, display error message. If no, create task from input
            if (!newDesc.isEmpty() && !newDesc.isEmpty() && !newTime.isEmpty() && !newTitle.isEmpty()) {

                //create a set of key:value pair to enter into task document
                Map<String, Object> data = new HashMap<>();
                data.put("reason", "");
                data.put("caregiverComplete", "no");
                data.put("category", newCategory);
                data.put("description", newDesc);

                //created task document and put data in it
                clientDoc.collection(newTime.toLowerCase()).document(newTitle)
                        .set(data)
                        //display message if finished creating task
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddTaskActivity.this, "Task Created Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //display message if fail to create task
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTaskActivity.this, "Fail to Create Task!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {
                Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_LONG).show();

            }
        });

        // Back Button - returns user to ContactMainActivity
        back.setOnClickListener(view -> {
            Intent intent = new Intent(AddTaskActivity.this, ContactMainActivity.class);
            startActivity(intent);
        });


    }
}