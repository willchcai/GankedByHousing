package com.example.gankedbyhousing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class ViewListingsActivity extends AppCompatActivity {

    private ArrayList<String> listingArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listings);

        listingArray.add("Listing 1");
        listingArray.add("Listing 2");
        listingArray.add("Listing 3");
        listingArray.add("Listing 4");

    }
}
