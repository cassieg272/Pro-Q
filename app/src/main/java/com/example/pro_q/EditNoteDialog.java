package com.example.pro_q;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class EditNoteDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ProQ")
                // When user clicks "UPDATE", will bring dialog box up with this message:
                .setMessage("Note updated successfully!")

                // User clicks "OK" and will return to dashboard
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), ContactMainActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
