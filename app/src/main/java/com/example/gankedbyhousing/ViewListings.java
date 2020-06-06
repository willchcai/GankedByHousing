package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewListings extends AppCompatActivity {

    private TextView viewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listings);

        viewList = findViewById(R.id.viewList);
        viewList.setText("View Listings");


        BottomNavigationView navbar = findViewById(R.id.navbar);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        Intent toProfile = new Intent(ViewListings.this, ProfileActivity.class);
                        startActivity(toProfile);
                        break;
                    case R.id.my_listings:
                        Intent myListings = new Intent(ViewListings.this, myListings.class);
                        startActivity(myListings);
                        break;
                    case R.id.listings:
                        break;
                    case R.id.notifications:
                        Intent toNotifications = new Intent(ViewListings.this, notificationsActivity.class);
                        startActivity(toNotifications);
                        break;
                    case R.id.make_listing:
                        Intent toUpload = new Intent(ViewListings.this, MakeListings.class);
                        startActivity(toUpload);
                        break;

                }

                return true;
            }
        });



    }
}
