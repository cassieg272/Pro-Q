package com.example.pro_q;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//This class get all the tasks for the provided day when DayModel object was declared and initialized
public class DayModel extends AppCompatActivity {
    @Override
    public void setContentView(View view) {
        super.setContentView(R.layout.viewpager_item_day_dashboard);
    }

    private CollectionReference dayTaskCollection;
    String clientId, day;
    Context context;

    String[] timeOfDay = {"Morning", "Afternoon", "Evening"};
    public ArrayList<TaskModel> afternoonTaskArr = new ArrayList<>();
   public ArrayList<TaskModel> morningTaskArr = new ArrayList<>();
    public ArrayList<TaskModel> eveningTaskArr = new ArrayList<>();


    public String getDay() {
        return day;
    }

    public DayModel(String id, Context context, String day) {
        this.clientId = id;
        this.day = day;
        this.context = context;
        dayTaskCollection = FirebaseFirestore.getInstance().collection("Client").document(id).collection(day);
        getDayTask(dayTaskCollection);
    }
    public ArrayList<TaskModel> getEveningTaskArr() {
        return eveningTaskArr;
    }

    //retrieve the task information on the day stored in field <day>
    private void getDayTask(CollectionReference dayTaskCollection) {
        dayTaskCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //go through each document (Morning, Afternoon, Evening) in that weekday collection
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String time = document.getId();
                        switch (time.toLowerCase()) {
                            //if the document name is morning then retrieve the tasks and add to morningTaskArr
                            case "morning":
                                morningTaskArr = getTimeTask(time, morningTaskArr);
                                break;
                            //if the document name is afternoon then retrieve the tasks and add to afternoonTaskArr
                            case "afternoon":
                                afternoonTaskArr = getTimeTask(time, afternoonTaskArr);
                                break;
                            //if the document name is evening then retrieve the tasks and add to eveningTaskArr
                            case "evening":
                                eveningTaskArr = getTimeTask(time, eveningTaskArr);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    Toast.makeText(context, "Fail to retrieve task list for " + day, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //retrieve task list for the given time of day and create TaskModel objects and store them in the corresponding morning/afternoon/evening arraylist
    private ArrayList<TaskModel> getTimeTask(String time, ArrayList<TaskModel> taskArr) {
        dayTaskCollection.document(time).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        //get "taskList" array in firestore
                        ArrayList<Map<String, String>> taskList = (ArrayList<Map<String, String>>) document.getData().get("taskList");
                        if (taskList != null) {
                            //go through each index of the array to get data and create TaskModel object
                            for (int i = 0; i < taskList.size(); i++) {
                                Map<String, String> currentMap = taskList.get(i);

                                //create TaskModel object
                                TaskModel taskModel = new TaskModel(currentMap.get("name"), currentMap.get("description"), currentMap.get("category"));
                                if (taskModel != null) {
                                    taskArr.add(taskModel); //add TaskModel object to the provided arraylist
                                    Log.d("TAG", "onComplete: " + taskModel);
                                } else {
                                    Log.d("TAG", "getTimeTask: null");
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Please create task for " + day + " " + time, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return taskArr;
    }
}
