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

public class ContactTaskDetailActivity extends AppCompatActivity {
    private TextView category, timeOfDay;
    private TextView title;
    private String chosenCat, chosenTime, chosenTitle, chosenDesc;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private EditText description;
    private LinearLayout deleteConfirm;
    private Button delete, update, back, confirmDelete, cancel;
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
        chosenTime = sharedPref.getString("taskTime", "");
        String clientId = sharedPref.getString("clientId", "");

        // Reference to specific task in database
        DocumentReference clientDoc = clientInfoRef.document(clientId);
        DocumentReference task = clientInfoRef.document(clientId).collection(chosenTime).document(taskId);

        category = findViewById(R.id.categoryValue);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.descriptionEdit);
        timeOfDay = findViewById(R.id.timeValue);
        deleteConfirm = findViewById(R.id.deleteConfirm);
        deleteConfirm.setVisibility(View.GONE);

        // The Buttons
        back = findViewById(R.id.goBackButton);
        update = findViewById(R.id.updateButton);
        delete = findViewById(R.id.deleteButton);
        cancel = findViewById(R.id.cancelButton);
        confirmDelete = findViewById(R.id.confirmDeleteButton);

        // Retrieve task information from the database and display it
        task.get().addOnSuccessListener(documentSnapshot -> {
            //Pre-fill fields with current values for selected task
            description.setText(documentSnapshot.getString(KEY_DESCRIPTION));
            category.setText(documentSnapshot.getString(KEY_CATEGORY));
        });
        chosenTitle = taskId;
        title.setText(chosenTitle);

        timeOfDay.setText(chosenTime.substring(0, 1).toUpperCase() + chosenTime.substring(1));
//        String[] timeArr = getResources().getStringArray(R.array.time);
//        ArrayAdapter timeArrayAdapter = new ArrayAdapter(ContactTaskDetailActivity.this, R.layout.dropdown_item, timeArr);
//        timeOfDay.setAdapter(timeArrayAdapter);

//        //Attach adapters to AutoCompleteTextView create drop down list
//        String[] splitedCatList = catList.split("\\s*,\\s*");
//        Log.d("TAG", "onCreate: "+splitedCatList.length+" "+splitedCatList.toString());
//        ArrayAdapter catArrayAdapter = new ArrayAdapter(ContactTaskDetailActivity.this, R.layout.dropdown_item, splitedCatList);
//        category.setAdapter(catArrayAdapter);


        // Update Button - updates the task description
        // TODO - allow to update anything else?
        update.setOnClickListener(view -> {
            String updateDescription = description.getText().toString();
            String newTime = timeOfDay.getText().toString();
            task.update(KEY_DESCRIPTION, updateDescription);
        });

        // Delete Button - sets cardview to Visible
        delete.setOnClickListener(view -> deleteConfirm.setVisibility(View.VISIBLE));

        // Confirm Delete Button - deletes the task from the database
        confirmDelete.setOnClickListener(view -> {
            task.delete();
            Toast.makeText(ContactTaskDetailActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
        });

        // Cancel Button - sets cardview back to Invisible
        cancel.setOnClickListener(view -> deleteConfirm.setVisibility(View.INVISIBLE));

        // Back Button - returns user to ContactMainActivity
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ContactTaskDetailActivity.this, ContactMainActivity.class);
            startActivity(intent);
        });


    }
}