package com.example.pro_q;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateNoteDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ProQ")

                // Once user clicks "create", this dialog box will pop up with message:
                .setMessage("Note created successfully!")

                // Click "OK" to go back to Dashboard
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), CaregiverMainActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
