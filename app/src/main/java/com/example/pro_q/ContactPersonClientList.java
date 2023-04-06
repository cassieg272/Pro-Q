package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ContactPersonClientList extends AppCompatActivity implements RecyclerViewInterface {
    ArrayList<ClientModel> clientModels = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference clientCollection = db.collection("Client");
    private RecyclerView resultList;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //get contact person ID from sharedPreferences file
        String contactPersonId =sharedPref.getString("contactPersonId", "<Id>");

        //declare and assign Firestore contact person document reference and activity views
        SearchView searchBar = findViewById(R.id.searchView);
        resultList = findViewById(R.id.searchResultRecyclerView);
        TextView clientListTitle = findViewById(R.id.clientListTitle);
        DocumentReference contactPersonDoc = db.collection("/ContactPerson").document(contactPersonId);

        searchBar.setVisibility(View.INVISIBLE); //make searchBar invisible because reusing the same activity_search layout for this activity
        clientListTitle.setVisibility(View.VISIBLE); //make the title visible to make it appear that user is on a different page

        //retrieve ids from clientList array in the contactPerson document
        contactPersonDoc.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            ArrayList<String> clientListArr = (ArrayList<String>) document.get("clientList"); //get clientList array from ContactPerson document

            if (clientListArr != null && clientListArr.size() > 0) {
                //get client info based on each id iterated through clientList array in ContactPerson document
                for (String id : clientListArr) {

                    //Query for client info based on the id provided from clientListArr array
                    Query query = clientCollection.whereEqualTo("id", id);

                    query.get().addOnCompleteListener(task1 -> {
                        for (QueryDocumentSnapshot document1 : task1.getResult()) {

                            //get values in database of the matched clients
                            String fullName = document1.get("firstName") + " " + (String) document1.get("lastName");


                            //get values from address map field in Firestore and concatenate them to make a complete address
                            Map<String, String> addressMap = (Map<String, String>) document1.get("address");
                            if (addressMap != null) {
                                String street = addressMap.get("street");
                                String city = addressMap.get("city");
                                String province = addressMap.get("province");
                                String postalCode = addressMap.get("postalCode");
                                String address = street + ", " + city + ", " + province + " " + postalCode;

                                //create a clientModel object and add to clientModels array list
                                clientModels.add(new ClientModel(address, id, fullName));
                            }
                        }
                        //create result list with recyclerview adapter
                        ClientAdapter adapter = new ClientAdapter(ContactPersonClientList.this, clientModels, ContactPersonClientList.this);
                        resultList.setAdapter(adapter);
                        resultList.setLayoutManager(new LinearLayoutManager(ContactPersonClientList.this));
                    });
                }
            } else {
                Toast.makeText(this, "No clients matched for this Contact Person account", Toast.LENGTH_LONG).show();
            }
        });
    }

    //onItemClick is executed when user click on a result
    @Override
    public void onItemClick(int position) {
        sharedPref = getSharedPreferences("listOfId", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("clientId",  clientModels.get(position).getId());
        editor.putString("clientAddress", clientModels.get(position).getAddress());
        editor.putString("clientName",  clientModels.get(position).getFullName());
        editor.commit();
        Intent intent = new Intent(ContactPersonClientList.this, ContactMainActivity.class);
        startActivity(intent);
    }
}