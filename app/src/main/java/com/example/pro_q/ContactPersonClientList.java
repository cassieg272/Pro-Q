package com.example.pro_q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ContactPersonClientList extends AppCompatActivity implements RecyclerViewInterface{
    private SearchView searchBar;
    private TextView clientListTitle;
    ArrayList<ClientModel> clientModels = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clientCollection = db.collection("Client");
    private DocumentReference contactPersonDoc;
    private RecyclerView resultList;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //get contact person ID passed in by Intent from previous activity
        String contactPersonId = getIntent().getStringExtra("contactPersonID");

        //declare and assign Firestore contact person document reference and activity views
        searchBar = findViewById(R.id.searchView);
        resultList = findViewById(R.id.searchResultRecyclerView);
        clientListTitle = findViewById(R.id.clientListTitle);
        contactPersonDoc = db.collection("/ContactPerson").document(contactPersonId);

        searchBar.setVisibility(View.INVISIBLE); //make searchBar invisible because reusing the same activity_search layout for this activity
        clientListTitle.setVisibility(View.VISIBLE); //make the title visible to make it appear that user is on a different page

        //retrieve ids from clientList array in the contactPerson document
        contactPersonDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ArrayList<String> clientListArr = (ArrayList<String>) document.get("clientList"); //get clientList array from ContactPerson document

                //get client info based on each id iterated through clientList array in ContactPerson document
                for (String id : clientListArr) {
                    Query query = clientCollection.whereEqualTo("id", id);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //get values in database of the matched clients
                                String fullName = (String) document.get("firstName") + " " + (String) document.get("lastName");

                                //get values from address map field in Firestore and concatenate them to make a complete address
                                Map<String, String> addressMap = (Map<String, String>) document.get("address");
                                String street = addressMap.get("street");
                                String city = addressMap.get("city");
                                String province = addressMap.get("province");
                                String postalCode = addressMap.get("postalCode");
                                String address = street + ", " + city + ", " + province + " " + postalCode;

                                Log.d("TAG", "onComplete: " + id + " " + fullName + " " + address);
                                //create a clientModel object and add to clientModels array list
                                clientModels.add(new ClientModel(address, id, fullName));
                            }

                            //create result list with recyclerview adapter
                            ClientAdapter adapter = new ClientAdapter(ContactPersonClientList.this, clientModels, ContactPersonClientList.this);
                            resultList.setAdapter(adapter);
                            resultList.setLayoutManager(new LinearLayoutManager(ContactPersonClientList.this));
                        }
                    });


                }
            }
        });
    }
    //onItemClick is executed when user click on a result
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ContactPersonClientList.this, CaregiverMainActivity.class);
        intent.putExtra("ID", clientModels.get(position).getId());
        intent.putExtra("Address", clientModels.get(position).getAddress());
        intent.putExtra("Name", clientModels.get(position).getFullName());
        startActivity(intent);
    }
}