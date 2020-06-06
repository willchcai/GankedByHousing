package com.example.gankedbyhousing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MakeListings extends AppCompatActivity {

    private TextView makeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_listings);

        makeList = findViewById(R.id.makeList);
        makeList.setText("Make Listings");

        BottomNavigationView navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        Intent toProfile = new Intent(MakeListings.this, ProfileActivity.class);
                        startActivity(toProfile);
                        break;
                    case R.id.my_listings:
                        Intent myListings = new Intent(MakeListings.this, myListings.class);
                        startActivity(myListings);
                        break;
                    case R.id.listings:
                        Intent viewListings = new Intent(MakeListings.this, MainActivity.class);
                        startActivity(viewListings);
                        break;
                    case R.id.notifications:
                        Intent toNotifications = new Intent(MakeListings.this, notificationsActivity.class);
                        startActivity(toNotifications);
                        break;
                    case R.id.make_listing:
                        break;

                }

                return true;
            }
        });

    }
}
