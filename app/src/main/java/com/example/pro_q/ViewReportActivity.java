package com.example.pro_q;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ViewReportActivity extends AppCompatActivity {
    private Button back, saveReport, datePickerBtn;
    private static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";
    private static final String KEY_REASON = "reason";
    private DatePickerDialog datePickerDialog;
    private LinearLayout completedLayout, incompleteLayout;
    // Declare collection & document references
    private DocumentReference clientDoc, careReportDay;
    private CollectionReference morning, afternoon, evening;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Map<String, String> incompleteTasks = new HashMap<>();
    private ArrayList<String> completedTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        completedLayout = findViewById(R.id.completedTasksLayout);
        incompleteLayout = findViewById(R.id.incompleteLayout);
        saveReport = findViewById(R.id.saveReportButton);
        back = findViewById(R.id.backButton);
        datePickerBtn = findViewById(R.id.datePicker);

        // Get client ID & currentDayDoc from previous activity
        String id = sharedPref.getString("clientId", "");
        String finalDay = sharedPref.getString("currentDayDoc", "");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");

        Boolean fromCaregiverMainActivity = sharedPref.getBoolean("fromCaregiverMainActivity", true);

        //initial look of report upon creating View Report activity
        datePickerBtn.setText(getTodayDate());

        getCompletedTask(morning);
        getCompletedTask(afternoon);
        getCompletedTask(evening);

        getIncompleteTask(morning);
        getIncompleteTask(afternoon);
        getIncompleteTask(evening);

        //do certain thing depending on if the account is caregiver or contact person
        if (fromCaregiverMainActivity) {
            careReportDay = clientDoc.collection("CareReport").document(finalDay);
        } else {
            saveReport.setVisibility(View.GONE);
        }

        //set up date picker
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(fromCaregiverMainActivity);
                datePickerDialog.show();
            }
        });

        //what to do when save report button is clicked
        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create array of completed tasks and map of incomplete task
                Map<String, Object> taskReport = new HashMap<>();
                taskReport.put("Completed", completedTasks);
                taskReport.put("Incomplete", incompleteTasks);
                careReportDay.set(taskReport).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //what to do when successfully insert data to database
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ViewReportActivity.this, "Report Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        // Back Button - returns user to CaregiverMainActivity
        back.setOnClickListener(view -> {
            Intent intent;
            if (fromCaregiverMainActivity) {
                intent = new Intent(ViewReportActivity.this, CaregiverMainActivity.class);
            } else {
                intent = new Intent(ViewReportActivity.this, ContactMainActivity.class);
            }
            startActivity(intent);
        });
    }

    //get today's date and format it a certain way
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    //format the passed in date
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

    //format month
    private String getMonthFormat(int month) {
        if (month == 1) return "JAN";
        if (month == 2) return "FEB";
        if (month == 3) return "MAR";
        if (month == 4) return "APR";
        if (month == 5) return "MAY";
        if (month == 6) return "JUN";
        if (month == 7) return "JUL";
        if (month == 8) return "AUG";
        if (month == 9) return "SEP";
        if (month == 10) return "OCT";
        if (month == 11) return "NOV";
        if (month == 12) return "DEC";

        //default should never happen
        return "JAN";
    }

    //what to do when datePickerBtn is clicked
    private void initDatePicker(Boolean fromCaregiverMainActivity) {
        //when datepicker date is set
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerBtn.setText(date);

                //retrieve report data based on selected date
                DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat formatWeekDay = new SimpleDateFormat("EEEE");
                Date dateObj;
                try {
                    dateObj = originalFormat.parse(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String selectedDocId = targetFormat.format(dateObj) + " (" + formatWeekDay.format(dateObj) + ")";

                //clear layout containing the completed and incomplete tasks, except the first child (the layout label)
                completedLayout.removeViews(1, completedLayout.getChildCount() - 1);
                incompleteLayout.removeViews(1, incompleteLayout.getChildCount() - 1);

                //if user selected the date of today, show save report button. otherwise, hide save report button
                if (fromCaregiverMainActivity) {
                    saveReport.setVisibility(View.VISIBLE);
                }
                //if user selected today's date for report, retrieve info from task documents instead of CareReport in case caregiver didn't click Save Report
                if (date.equals(getTodayDate())) {
                    getCompletedTask(morning);
                    getCompletedTask(afternoon);
                    getCompletedTask(evening);

                    getIncompleteTask(morning);
                    getIncompleteTask(afternoon);
                    getIncompleteTask(evening);
                } else {
                    saveReport.setVisibility(View.GONE);
                    clientDoc.collection("CareReport").document(selectedDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                //read array of completed tasks and map of incomplete tasks from database
                                List<String> completedTasks = (List<String>) documentSnapshot.get("Completed");
                                Map<String, String> incompleteTasks = (Map<String, String>) documentSnapshot.get("Incomplete");

                                if (completedTasks != null) {
                                    //create text view for completed tasks
                                    for (String tasks : completedTasks) {
                                        createTaskTextView(tasks, completedLayout);
                                    }
                                }
                                if (incompleteTasks != null) {
                                    //create text view for incomplete tasks
                                    Set<String> incompleteKeySet = incompleteTasks.keySet();
                                    for (String key : incompleteKeySet) {
                                        String reasonVal = incompleteTasks.get(key);
                                        createTaskTextView(key + " : " + reasonVal, incompleteLayout);
                                    }
                                }

                            }
                            //display message if there is no report for selected date
                            else {
                                Toast.makeText(ViewReportActivity.this, "No report for selected date", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        //set initial value of datePickerDialog to today's date
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void getCompletedTask(CollectionReference taskCollection) {
        completedTasks = new ArrayList<>();
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    createTaskTextView(document.getId(), completedLayout);
                    completedTasks.add(document.getId());
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void getIncompleteTask(CollectionReference taskCollection) {
        incompleteTasks = new HashMap<>();
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "no").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String taskId = document.getId();
                    String reason = document.getString(KEY_REASON);
                    incompleteTasks.put(taskId, reason);

                    createTaskTextView(taskId + " : " + reason, incompleteLayout);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    //create task text view
    private void createTaskTextView(String text, LinearLayout layout) {
        TextView textView = new TextView(ViewReportActivity.this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        layout.addView(textView);
    }

}