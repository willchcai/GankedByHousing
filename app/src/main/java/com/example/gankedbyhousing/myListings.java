package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class myListings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        BottomNavigationView navbar;

        navbar = findViewById(R.id.navbar);
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
    }
}
