package com.example.pro_q;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity {
    ArrayList<ClientModel> clientModels = new ArrayList<>();
    private SearchView searchView;
    private CollectionReference clientCollection;
    private RecyclerView resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        clientCollection = FirebaseFirestore.getInstance().collection("/Client");
        searchView = findViewById(R.id.searchView);
//        resultList = findViewById(R.id.searchResultRecyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //execute code when user press Enter to search
            public boolean onQueryTextSubmit(String query) {
                validateSearch(query); //call getClientList and pass in the text entered in search bar
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void validateSearch(String searchText){
        String firstName, lastName;
        Query query;
        searchText = searchText.toLowerCase().trim(); //turn the search text to lowercase and remove spaces around the text
        List<String> nameList = new ArrayList<>(); //list to store separated words

        //if words are separated by comma, firstName is the second word and lastName is first word
        if(searchText.contains(",")){
            nameList = Arrays.asList(searchText.split("\\s*,\\s*")); //separate words by comma and white space
            firstName = nameList.get(nameList.size()-1);
            lastName = nameList.get(0);
        }
        //if words are separated by whitespaces then firstName is first word and lastName is last word
        else if (searchText.contains(" ")){
            nameList = Arrays.asList(searchText.split("\\s+")); //separate words by comma and white space
            firstName = nameList.get(0);
            lastName = nameList.get(nameList.size()-1);
        }
        //if there is only one word or words are separated not by comma/whitespace, display message
        else{
            Toast.makeText(this, "Enter first and last name separated by comma and/or whitespace", Toast.LENGTH_SHORT).show();
            return;
        }
        //if user does not enter lastName, only firstName (e.g. ", lastName")
        if (lastName.equalsIgnoreCase("")){
            Toast.makeText(this, "Please provide last name", Toast.LENGTH_SHORT).show();
        }
        //if there are more than two words separated by comma/whitespace, display message
        if(nameList.size()>2){
            Toast.makeText(this, "Enter only two words (first and last name)", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TAG", "first Name: "+ firstName+ " Last name: "+lastName);
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        //perform query on firestore index
        query = clientCollection.whereEqualTo("lastName", lastName)
                .whereEqualTo("firstName", firstName);
        getClientList(query);
    }

    private void getClientList(Query query) {
        ArrayList<ClientModel> clientList = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&& task.getResult().size() != 0){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        ClientModel client = document.toObject(ClientModel.class);
                        clientList.add(client);
                    }
                }else{
                    Toast.makeText(Search.this, "This client does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("TAG", "getClientList: "+clientList.toString());
    }


    private void setUpClientModels(String text){

    }
}