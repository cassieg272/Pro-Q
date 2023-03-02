package com.example.pro_q;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class Search extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private RecyclerView resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBar = findViewById(R.id.searchBar);
        resultList = findViewById(R.id.resultList);
    }
}