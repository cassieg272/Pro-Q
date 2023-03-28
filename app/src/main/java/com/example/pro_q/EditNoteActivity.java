package com.example.pro_q;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditNoteActivity extends AppCompatActivity {
    private Button deleteButton;
    private Button saveButton;
    private EditText noteTitle;
    private EditText noteDescription;

    // Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Path to database reference
    private DocumentReference noteRef = db.collection("ContactPerson")
            .document("TMBNDIqG23KA6woWXF2L")
            .collection("Notes")
            .document("rToL21GWjwEkIqPUvA9P");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        FirebaseApp.initializeApp(this);

//        TODO - pull info from database (persons document, Notes collection, document and put fields within the form)
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.updateButton);
        noteTitle = findViewById(R.id.titleInput);
        noteDescription = findViewById(R.id.descriptionInput);

        // Get the document you want to reference
        noteRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // get the existing values in the database and display them in the app
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
                    noteTitle.setText(title);
                    noteDescription.setText(description);
                }
                else {
                    Toast.makeText(EditNoteActivity.this, "No data exists", Toast.LENGTH_LONG).show();
                }
            }
        })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        saveButton.setOnClickListener(new View.OnClickListener() {
//            TODO - save edits to the database
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the deletion
                deleteNoteDialog();
            }
        });

    }

    private void updateNote() {

//TODO - how do I do the update?
//        String updateTitle = noteTitle.getText().toString();
//        String updateDescription = noteDescription.getText().toString();
//        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        noteRef.update();
//
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, updateTitle);
//        note.put(KEY_DESCRIPTION, updateDescription);
//        note.put(String.valueOf(KEY_DATETIME), today);

        editNoteDialog();
    }

    public void deleteNoteDialog()
    {
        DeleteNoteDialog deleteNoteDialog = new DeleteNoteDialog();
        deleteNoteDialog.show(getSupportFragmentManager(), "it worked!");
    }

    public void editNoteDialog()
    {
        EditNoteDialog editNoteDialog = new EditNoteDialog();
        editNoteDialog.show(getSupportFragmentManager(), "it worked!");
    }
}