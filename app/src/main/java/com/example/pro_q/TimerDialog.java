package com.example.pro_q;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerDialog extends AppCompatDialogFragment {
    Date startTime, finishTime;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss  dd/MMM/yyyy");
    long elapsedTime;
    String startTimeStr, finishTimeStr, elapsedTimeStr;
    public TimerDialog(Date startTime, Date finishTime) {
        this.finishTime = finishTime;
        this.startTime = startTime;
        this.elapsedTime = finishTime.getTime() - startTime.getTime(); //convert finish and start time to milliseconds to do the subtraction

    }
    private String convertFormat(long time){
        //convert the total elapsed seconds to hours, minutes, seconds
        double totalSecs = (double) (time) / 1000.0;
        int hours = (int)totalSecs / 3600;
        int minutes = (int)(totalSecs % 3600) / 60;
        int seconds = (int)totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //convert date and time values to user-friendly format
        startTimeStr = "\n\t\tSTART: "+dateFormat.format(startTime)+"\n\n";
        finishTimeStr = "\t\tFINISH: "+dateFormat.format(finishTime)+"\n\n";
        elapsedTimeStr = "\t\tDURATION: "+convertFormat(elapsedTime);
        String printTime = startTimeStr+finishTimeStr+elapsedTimeStr;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Visit Duration")
                .setMessage(printTime)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return builder.create();
    }

}
