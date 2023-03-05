package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {
    private Button createButton;
    private Button discardButton;
    private EditText title;
    private EditText description;
    private static DateTime createDate;

    // Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final DateTime KEY_DATETIME = createDate;

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        createButton = findViewById(R.id.saveButton);
        discardButton = findViewById(R.id.deleteButton);

        title = findViewById(R.id.titleInput);
        description = findViewById(R.id.descriptionInput);
        createButton.setOnClickListener(new View.OnClickListener() {
//            TODO - when create is pressed, go to persons document, then Notes collection, then create new document with fields createDate (timestamp), description, title and set the data inputed into those fields
            @Override
            public void onClick(View view) {
                String entertitle = title.getText().toString();
                String enterdescription = description.getText().toString();
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Map<String, Object> note = new HashMap<>();
                note.put(KEY_TITLE, entertitle);
                note.put(KEY_DESCRIPTION, enterdescription);
                note.put(String.valueOf(KEY_DATETIME), today);

                db.collection("ContactPerson")
                        .document( "TMBNDIqG23KA6woWXF2L")
                        .collection("Notes")
                        .document()
                        .set(note);

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