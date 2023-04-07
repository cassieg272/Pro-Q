package com.example.pro_q;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class Alarm {
    private Context context;
    public Alarm(Context context) {
        this.context = context;
    }

    public void activateAlarm() {
        Intent intent = new Intent(context, ResetTasks.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, FLAG_IMMUTABLE);
        Log.d(TAG, "Alarm: intent and pendingintent set");

        // Create the alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d(TAG, "Alarm: alarm manager created");

        // Set the time alarm should go off
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 33);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "Alarm: time set for alarm");


        // Set the interval for the alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d(TAG, "Alarm: interval set for alarm");
    }

}
