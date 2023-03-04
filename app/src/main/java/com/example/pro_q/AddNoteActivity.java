package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {
private Button createButton;
private Button discardButton;

private EditText title;
private EditText description;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        createButton = (Button) findViewById(R.id.saveButton);
        discardButton = (Button) findViewById(R.id.deleteButton);

        title = findViewById(R.id.titleInput);
        description = findViewById(R.id.descriptionInput);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("");
                description.setText("");
                Intent intent = new Intent(AddNoteActivity.this, ContactMainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void openDialog()
    {
        CreateNoteDialog createNoteDialog = new CreateNoteDialog();
        createNoteDialog.show(getSupportFragmentManager(), "it worked!");
    }
}