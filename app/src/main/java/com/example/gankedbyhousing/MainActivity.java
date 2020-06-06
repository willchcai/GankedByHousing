package com.example.gankedbyhousing;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        BottomNavigationView navbar;

        navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        Intent toProfile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(toProfile);
                        break;
                    case R.id.my_listings:
                        Intent myListings = new Intent(MainActivity.this, myListings.class);
                        startActivity(myListings);
                        break;
                    case R.id.listings:
                        break;
                    case R.id.notifications:
                        Intent toNotifications = new Intent(MainActivity.this, notificationsActivity.class);
                        startActivity(toNotifications);
                        break;
                    case R.id.make_listing:
                        Intent toUpload = new Intent(MainActivity.this, MakeListings.class);
                        startActivity(toUpload);
                        break;

                }

                return true;
            }
        });

    }
}
