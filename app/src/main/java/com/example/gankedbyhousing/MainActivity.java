package com.example.gankedbyhousing;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private Button logoutBtn;
    private Button makeListing;
    private Button viewListing;
    private Button editProfile;
    private TextView welcome;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutBtn = findViewById(R.id.logoutButton);
        makeListing = findViewById(R.id.makeListing);
        viewListing = findViewById(R.id.viewListing);
        editProfile = findViewById(R.id.editProfile);
        welcome = findViewById(R.id.welcome);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        Log.d("TAG", "userID is: " + userID);

        //Retrieve user information
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String mName = documentSnapshot.getString("name");
                welcome.setText("Welcome, " + mName + "!");
            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                mAuth.getInstance().signOut();
                startActivity(toLoginActivity);
            }
        });
    }

}
