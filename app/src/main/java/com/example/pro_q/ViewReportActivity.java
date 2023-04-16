package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    private Button back, saveReport, datePickerBtn, dateSelectBtn;
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
        dateSelectBtn = findViewById(R.id.dateSelectBtn);

        // Get client ID & currentDayDoc from previous activity
        String id = sharedPref.getString("clientId", "");
        String finalDay = sharedPref.getString("currentDayDoc", "");

        // Assign path to document & collection references
        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");

        // If task is marked complete - find the layout in the app and create a textview with text set to completed task title
        getCompletedTask(morning);
        getCompletedTask(afternoon);
        getCompletedTask(evening);

        // If task is marked incomplete - find the layout in the app and create a textview with text set to incomplete task title
        getIncompleteTask(morning);
        getIncompleteTask(afternoon);
        getIncompleteTask(evening);

        Boolean fromCaregiverMainActivity = sharedPref.getBoolean("fromCaregiverMainActivity", true);
        if (fromCaregiverMainActivity) {
            dateSelectBtn.setVisibility(View.GONE);
            saveReport.setVisibility(View.VISIBLE);
            datePickerBtn.setVisibility(View.GONE);
            careReportDay = clientDoc.collection("CareReport").document(finalDay);

        } else {
            dateSelectBtn.setVisibility(View.VISIBLE);
            saveReport.setVisibility(View.GONE);
            datePickerBtn.setVisibility(View.VISIBLE);
            datePickerBtn.setText(getTodayDate());
        }

        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completedLayout.removeViews(1, completedLayout.getChildCount() - 1);
                incompleteLayout.removeViews(1, incompleteLayout.getChildCount() - 1);

                DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat formatWeekDay = new SimpleDateFormat("EEEE");
                String selectedDate = datePickerBtn.getText().toString();
                Date date;
                try {
                    date = originalFormat.parse(selectedDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String selectedDocId = targetFormat.format(date) + " (" + formatWeekDay.format(date) + ")";

                clientDoc.collection("CareReport").document(selectedDocId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    List<String> completedTasks = (List<String>) documentSnapshot.get("Completed");
                                    Map<String, String> incompleteTasks = (Map<String, String>) documentSnapshot.get("Incomplete");

                                    for (String tasks : completedTasks) {
                                        TextView text = new TextView(ViewReportActivity.this);
                                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        text.setText(tasks);
                                        completedLayout.addView(text);
                                    }
                                    Set<String> incompleteKeySet = incompleteTasks.keySet();
                                    for (String key : incompleteKeySet) {
                                        String reasonVal = incompleteTasks.get(key);
                                        TextView text = new TextView(ViewReportActivity.this);
                                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        text.setText(key + " : " + reasonVal);
                                        incompleteLayout.addView(text);
                                    }

                                } else {
                                    Toast.makeText(ViewReportActivity.this, "No report for selected date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                datePickerDialog.show();
            }
        });
        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> taskReport = new HashMap<>();
                taskReport.put("Completed", completedTasks);
                taskReport.put("Incomplete", incompleteTasks);
                careReportDay.set(taskReport).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void getCompletedTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    TextView text = new TextView(ViewReportActivity.this);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    completedTasks.add(document.getId());
                    text.setText(document.getId());
                    completedLayout.addView(text);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void getIncompleteTask(CollectionReference taskCollection) {
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "no").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    TextView text = new TextView(ViewReportActivity.this);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    String taskId = document.getId();
                    String reason = document.getString(KEY_REASON);
                    text.setText(taskId + " : " + reason);
                    incompleteTasks.put(taskId, reason);
                    incompleteLayout.addView(text);
                }
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

}