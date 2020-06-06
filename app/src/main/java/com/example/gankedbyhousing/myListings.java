package com.example.gankedbyhousing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class myListings extends AppCompatActivity {

    private TextView myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        myList = findViewById(R.id.myList);
        myList.setText("My Listings");

        BottomNavigationView navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

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
                        Intent viewListings = new Intent(myListings.this, MainActivity.class);
                        startActivity(viewListings);
                        break;
                    case R.id.notifications:
                        Intent toNotifications = new Intent(myListings.this, notificationsActivity.class);
                        startActivity(toNotifications);
                        break;
                    case R.id.make_listing:
                        Intent toUpload = new Intent(myListings.this, MakeListings.class);
                        startActivity(toUpload);
                        break;

                }

                return true;
            }
        });

    }
}
