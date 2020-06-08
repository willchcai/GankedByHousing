package com.example.gankedbyhousing;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private Button editProfile;
    private Button viewListings;
    private static final String TAG = "MainActivity";
    private FirebaseFirestore fStore;



    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewListings = findViewById(R.id.viewListings);
        fStore = FirebaseFirestore.getInstance();

        viewListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toViewListingsActivity = new Intent(MainActivity.this, ViewListingsActivity.class);
                startActivity(toViewListingsActivity);

                //DocumentReference docRef = fStore.collection("Listings").document("pkCk7uQ5MObdFaQTyX7G");
                DocumentReference docRef = fStore.collection("Listings").document("pkCk7uQ5MObdFaQTyX7G");
                Log.d(TAG, "value of userID: " + userID);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
                Log.d(TAG, "onCardSwiped: p=");

            }
        });

        editProfile = findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(toProfile);
            }
        });


        mAuth = FirebaseAuth.getInstance();



        userID = mAuth.getCurrentUser().getUid();

    }
}
