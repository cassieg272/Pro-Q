package com.example.pro_q;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Search extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<ClientModel> clientModels = new ArrayList<>();
    private SearchView searchView;
    private CollectionReference clientCollection;
    private RecyclerView resultList;
    private SharedPreferences sharedPref ;
    private SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        clientCollection = FirebaseFirestore.getInstance().collection("/Client");
        searchView = findViewById(R.id.searchView);
        resultList = findViewById(R.id.searchResultRecyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //execute code when user press Enter to search
            public boolean onQueryTextSubmit(String query) {
                validateSearch(query); //call getClientList and pass in the text entered in search bar
                searchView.clearFocus();
                resultList.setVisibility(View.GONE);//empty recycler view for the next search
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void validateSearch(String searchText){
        String firstName = "", lastName="";
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
//        if user does not enter lastName, only firstName (e.g. ", lastName")
        if (lastName.equalsIgnoreCase("")){
            Toast.makeText(this, "Please provide last name", Toast.LENGTH_SHORT).show();
            return;
        }else if (firstName.equalsIgnoreCase("")){
            Toast.makeText(this, "Please provide first name", Toast.LENGTH_SHORT).show();
            return;
        }
        //if there are more than two words separated by comma/whitespace, display message
        if(nameList.size()!=2){
            Toast.makeText(this, "Enter two words (first and last name)", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TAG", "first Name: "+ firstName+ " Last name: "+lastName);
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();

        //perform query on firestore index to get client info
        query = clientCollection.whereEqualTo("lastName", lastName).whereEqualTo("firstName", firstName);
        getClientList(query);
    }

    public void getClientList(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&& task.getResult().size() != 0){
                    ArrayList<ClientModel> clientList = new ArrayList<>();

                    for(QueryDocumentSnapshot document: task.getResult()){
                        String id = document.getId(); //get document ID aka client ID

                        //get values in database of the matched clients
                        String fullName = (String) document.get("firstName")+ " "+(String) document.get("lastName");

                        //get values from address map field in Firestore and concatenate them to make a complete address
                        Map<String, String> addressMap = (Map<String, String>) document.get("address");
                        String street = addressMap.get("street");
                        String city = addressMap.get("city");
                        String province = addressMap.get("province");
                        String postalCode = addressMap.get("postalCode");
                        String address = street+", "+city+", "+province+ " "+postalCode;

                        //create a clientModel object and add to clientList
                        clientList.add(new ClientModel(address, id, fullName));
                    }
                        resultList.setVisibility(View.VISIBLE);
                        clientModels = clientList;

                        //create result list with recyclerview adapter
                        ClientAdapter adapter = new ClientAdapter(Search.this, clientList, Search.this);
                        resultList.setAdapter(adapter);
                        resultList.setLayoutManager(new LinearLayoutManager(Search.this));
                }
                //if task is not successful and/or there is no result in the list returned by task, display message
                else{
                    Toast.makeText(Search.this, "This client does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        Intent intent = new Intent(Search.this, CaregiverMainActivity.class);
        startActivity(intent);
    }
}