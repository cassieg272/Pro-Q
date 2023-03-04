package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditNoteActivity extends AppCompatActivity {
    private Button deleteButton;
    private Button saveButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the deletion
                openDialog();
            }
        });
    }
    public void openDialog()
    {
        CreateNoteDialog createNoteDialog = new CreateNoteDialog();
        createNoteDialog.show(getSupportFragmentManager(), "it worked!");
    }
}