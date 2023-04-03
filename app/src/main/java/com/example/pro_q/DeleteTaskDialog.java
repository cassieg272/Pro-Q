package com.example.pro_q;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteTaskDialog extends AppCompatDialogFragment {

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ProQ")

                // Once user clicks "DELETE", this dialog box pops up to confirm deletion
                .setMessage("Are you sure you want to delete this task?")

                // When user clicks "OK", will go to delete confirmation page
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(getActivity(), DeleteSuccessActivity.class);
                        startActivity(intent);
                    }
                })

                // If user clicks "CANCEL", will return to delete note activity
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
