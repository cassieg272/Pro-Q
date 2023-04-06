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
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {
    private TextView category, timeOfDay;
    private TextView title;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private EditText description;
    private LinearLayout deleteConfirm;
    private Button create, back;
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference clientInfoRef = db.collection("Client");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_task_detail);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Receiving data from previous activity
        String taskId = sharedPref.getString("taskId", "");
        String clientId = sharedPref.getString("clientId", "");

        // Reference to specific task in database
        DocumentReference clientDoc = clientInfoRef.document(clientId);

        category = findViewById(R.id.categoryValue);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.descriptionEdit);
        timeOfDay = findViewById(R.id.timeValue);

        // The Buttons
        back = findViewById(R.id.goBackButton);


//        String[] timeArr = getResources().getStringArray(R.array.time);
//        ArrayAdapter timeArrayAdapter = new ArrayAdapter(ContactTaskDetailActivity.this, R.layout.dropdown_item, timeArr);
//        timeOfDay.setAdapter(timeArrayAdapter);

//        //Attach adapters to AutoCompleteTextView create drop down list
//        String[] splitedCatList = catList.split("\\s*,\\s*");
//        Log.d("TAG", "onCreate: "+splitedCatList.length+" "+splitedCatList.toString());
//        ArrayAdapter catArrayAdapter = new ArrayAdapter(ContactTaskDetailActivity.this, R.layout.dropdown_item, splitedCatList);
//        category.setAdapter(catArrayAdapter);


        // Create Task Button - create task upon click
        create.setOnClickListener(view -> {
            String updateDescription = description.getText().toString();
            String newTime = timeOfDay.getText().toString();
//            task.update(KEY_DESCRIPTION, updateDescription);
        });

        // Delete Button - sets cardview to Visible

        // Back Button - returns user to ContactMainActivity
        back.setOnClickListener(view -> {
            Intent intent = new Intent(AddTaskActivity.this, ContactMainActivity.class);
            startActivity(intent);
        });


    }
}