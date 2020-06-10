package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class myListings extends AppCompatActivity {
    FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    List<Listing> items;
    private ArrayList<String> listingArray;
    private ArrayList<String> documentIdArray;
    private ArrayAdapter adapter;
    private ListView cardList;
    private static final String TAG = "myListings.java";
    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        BottomNavigationView navbar;

        navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        fStore = FirebaseFirestore.getInstance();
        listingArray = new ArrayList<>();
        documentIdArray = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        Intent toProfile = new Intent(myListings.this, ProfileActivity.class);
                        startActivity(toProfile);
                        break;
                    case R.id.my_listings:
                        break;
                    case R.id.listings:
                        Intent toMainActivity = new Intent(myListings.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.view_listings:
                        Intent toViewListings = new Intent(myListings.this, ViewListingsActivity.class);
                        startActivity(toViewListings);
                        break;
                    case R.id.make_listings:
                        Intent toMakeListings = new Intent(myListings.this, MakeListings.class);
                        startActivity(toMakeListings);
                        break;

                }

                return true;
            }
        });

                fStore.collection("Listings").whereEqualTo("uid", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> docObj = document.getData();
                                //items.add(new Listing(R.drawable.sample1, docObj.get("title").toString(),docObj.get("price").toString(), docObj.get("city") + " " + docObj.get("state"), document.getId()));
                                String msg = docObj.get("title").toString() + "\n" + "Price: " + docObj.get("price").toString() + "\n" + "Location: " + docObj.get("city") + ", " + docObj.get("state") + "\n"+
                                        "Listing ID: " + document.getId();
                                listingArray.add(msg);
                                documentIdArray.add(document.getId());
                                /*listingArray.add(document.getId());*/
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        cardList = findViewById(R.id.cardList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listingArray);
        cardList.setAdapter(adapter);

        cardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int posID = (int) id;
                String msg = listingArray.get(posID);

                List<String> arr = Arrays.asList(msg.split("\n"));
                Log.d(TAG, "listingID: " + arr.get(arr.size()-1) + "EDWARD");
                String documentIdToDelete = arr.get(arr.size()-1);
                Log.d(TAG, "documentIdToDelete: " + documentIdToDelete);

                Log.d(TAG, "listingArray:  " + listingArray);
                Log.d(TAG, "documentIdArray: " + documentIdArray);


                fStore.collection("Listings").document(documentIdArray.get(posID))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                listingArray.remove(posID);
                                documentIdArray.remove(posID);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                //Log.d(TAG, "Contents of lines: " + lines);
                Log.d(TAG, "OnItemLongClickListener triggered!");

                return false;
            }
        });

        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int posID = (int) id;
                Intent ViewSingleListingActivityIntent = new Intent(myListings.this, ViewSingleListingActivity.class);
                ViewSingleListingActivityIntent.putExtra("Listing Document ID", documentIdArray.get(posID));
                startActivity(ViewSingleListingActivityIntent);
            }
        });
    }
}
