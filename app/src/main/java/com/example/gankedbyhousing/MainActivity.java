package com.example.gankedbyhousing;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
                    case R.id.view_listings:
                        Intent toViewListings = new Intent(MainActivity.this, ViewListingsActivity.class);
                        startActivity(toViewListings);
                        break;
                    case R.id.make_listings:
                        Intent toUpload = new Intent(MainActivity.this, MakeListings.class);
                        startActivity(toUpload);
                        break;

                }

                return true;
            }
        });


    }
}
