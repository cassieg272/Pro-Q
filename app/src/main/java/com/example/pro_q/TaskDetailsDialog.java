package com.example.pro_q;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

public class TaskDetailsDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ProQ - Task Details")

                // Once user clicks "create", this dialog box will pop up with message:
                .setCancelable(true)
                .setMessage("Please select from options below:")
                .setNegativeButton("NOT COMPLETED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("Task Details", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        startActivity(intent);
                    }
                })
                // Click "OK" to go back to Dashboard
                .setPositiveButton("COMPLETED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), CaregiverMainActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
