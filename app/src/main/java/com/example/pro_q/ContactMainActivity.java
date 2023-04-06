package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactMainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private TextView clientId, clientName, clientPhone, clientAddress, clientGender;

    private Button addNote, addTask, viewReport;

    // Keys - Match the keys to the field value in the database
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    String phone, gender, name, address, id;
    public static final String KEY_TASKTITLE = "title";

    // get references for client document and collections
    private DocumentReference clientDoc;
    private ViewPager2 viewPagerDayTask;
    private DayModel[] allDayList = new DayModel[7];
    private String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private CollectionReference clientMorningTaskRef, clientAfternoonTaskRef, clientEveningTaskRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);

        name = getIntent().getStringExtra("Name");
        address = getIntent().getStringExtra("Address");
        id = getIntent().getStringExtra("ID");

        clientId = findViewById(R.id.clientId);
        viewPagerDayTask = findViewById(R.id.viewPagerDayTask);
        clientName = findViewById(R.id.clientName);
        clientPhone = findViewById(R.id.clientPhone);
        clientAddress = findViewById(R.id.clientAddress);
        clientGender = findViewById(R.id.clientGender);

        clientId.setText(id);
        clientName.setText(name);
        clientAddress.setText(address);

        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        clientMorningTaskRef = clientDoc.collection("morning");
        clientAfternoonTaskRef = clientDoc.collection("afternoon");
        clientEveningTaskRef = clientDoc.collection("evening");
//
        // CLIENT INFO - Retrieve data from collection
        clientDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Get Client Phone
                            phone = documentSnapshot.getString(KEY_PHONE);
                            if (phone == "" || phone == null) {
                                clientPhone.setText("No phone number");
                            } else {
                                clientPhone.setText(phone);
                            }
                            gender = documentSnapshot.getString(KEY_GENDER);
                            clientGender.setText(gender);
                        } else {
                            Toast.makeText(ContactMainActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < weekDays.length; i++) {
                            String day = weekDays[i];
                            allDayList[i] = (new DayModel(id, ContactMainActivity.this, day));
                            Log.d("TAG", "onSuccess: "+allDayList[i].getAllDayTask().length);
                            Log.d("TAG", "onSuccess: "+allDayList[i].getAllDayTask()[0]);
                            Log.d("TAG", "onSuccess: "+allDayList[i].getAllDayTask()[1]);
                            Log.d("TAG", "onSuccess: "+allDayList[i].getAllDayTask()[2]);
//                            for(ArrayList<TaskModel> time: allDayList[i].getAllDayTask()) {
//                                for(TaskModel task:time){
//                                    Log.d("TAG", "onSuccess: " + task);
//                                }
//                            }
                        }
                        Log.d("TAG", "onCreate: " + allDayList.length);
                        Log.d("TAG", "onCreate: end");
//                        DayAdapter dayAdapter = new DayAdapter(ContactMainActivity.this, allDayList, ContactMainActivity.this);
//                        viewPagerDayTask.setAdapter(dayAdapter);
//                        viewPagerDayTask.setClipToPadding(false);
//                        viewPagerDayTask.setClipChildren(false);
//                        viewPagerDayTask.setOffscreenPageLimit(2);
//                        viewPagerDayTask.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
                    }
                })
                .addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.toString()));

        // BUTTON BAR

        // Navigate to Add Note Page
        findViewById(R.id.noteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactMainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Add Task Page
        findViewById(R.id.taskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactMainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to View Report Page
        findViewById(R.id.reportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactMainActivity.this, ViewReportActivity.class);
                intent.putExtra("clientID", id);
                startActivity(intent);
            }
        });


        Log.d("TAG", "onCreate: start");

    }

    @Override
    public void onItemClick(int position) {

    }
}



