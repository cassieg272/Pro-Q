package com.example.pro_q;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ResetTasks extends BroadcastReceiver {
    public static final String KEY_CAREGIVER_COMPLETE = "caregiverComplete";
    public static final String KEY_REASON = "reason";
    private DocumentReference clientDoc;
    private CollectionReference morning, afternoon, evening;
    private SharedPreferences sharedPref;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "In the resetTasks class");

        String id = sharedPref.getString("clientId", "");

        clientDoc = FirebaseFirestore.getInstance().collection("Client").document(id);
        morning = clientDoc.collection("morning");
        afternoon = clientDoc.collection("afternoon");
        evening = clientDoc.collection("evening");
        Log.d(TAG, "reset started!");
        resetTask(morning);
        resetTask(afternoon);
        resetTask(evening);
    }

    private void resetTask(CollectionReference taskCollection) {
        Log.d(TAG, "finding the completed tasks");
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "yes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskRef = document.getId();
                            Log.d(TAG, "completed task: " + taskRef);
                            // Set tasks marked completed in database back to no (not complete)
                            taskCollection.document(taskRef).update(KEY_CAREGIVER_COMPLETE, "no");
                        }
                    }
                });
        Log.d(TAG, "finding the incompleted tasks");
        taskCollection.whereEqualTo(KEY_CAREGIVER_COMPLETE, "no").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskRef = document.getId();
                            Log.d(TAG, "incompleted task: " + taskRef);
                            // Remove any "reasons" in database - reset to ""
                            taskCollection.document(taskRef).update(KEY_REASON, "");
                        }
                    }
                });
    }
}

