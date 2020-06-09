package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class ViewListingsActivity extends AppCompatActivity {

    private static final String TAG = "ViewListingsActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listings);

        BottomNavigationView navbar;

        navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        Intent toProfile = new Intent(ViewListingsActivity.this, ProfileActivity.class);
                        startActivity(toProfile);
                        break;
                    case R.id.my_listings:
                        Intent myListings = new Intent(ViewListingsActivity.this, myListings.class);
                        startActivity(myListings);
                        break;
                    case R.id.listings:
                        Intent toMainActivity = new Intent(ViewListingsActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.view_listings:
                        break;
                    case R.id.make_listings:
                        Intent toMakeListings = new Intent(ViewListingsActivity.this, MakeListings.class);
                        startActivity(toMakeListings);
                        break;

                }

                return true;
            }
        });

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
/*                if (direction == Direction.Right){
                    Toast.makeText(ViewListingsActivity.this, "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Top){
                    Toast.makeText(ViewListingsActivity.this, "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left){
                    Toast.makeText(ViewListingsActivity.this, "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom){
                    Toast.makeText(ViewListingsActivity.this, "Direction Bottom", Toast.LENGTH_SHORT).show();
                }*/

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", title: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", title: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }

    private void paginate() {
        List<Listing> old = adapter.getItems();
        List<Listing> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    private List<Listing> addList() {
        List<Listing> items = new ArrayList<>();
        items.add(new Listing(R.drawable.sample1, "Two Bed, Two Bath", "$2400", "San Francisco, CA", "pkCk7uQ5MObdFaQTyX7G"));
        items.add(new Listing(R.drawable.sample2, "Three Floor Penthouse", "$2200", "Santa Barbara, CA", "asdf"));
/*        items.add(new Listing(R.drawable.sample3, "Chinatown Single Room", "$2700", "New York City, NY", documentID));
        items.add(new Listing(R.drawable.sample4, "House In The Projects", "$1900", "Seattle, WA", documentID));
        items.add(new Listing(R.drawable.sample5, "Trash", "$2500", "Atlanta, GA", documentID));

        items.add(new Listing(R.drawable.sample1, "Markonah", "$1500", "Sacramento, CA", documentID));
        items.add(new Listing(R.drawable.sample2, "Marpuah", "$3200", "Los Angeles, CA", documentID));
        items.add(new Listing(R.drawable.sample3, "Sukijah", "$7200", "Carson City, NV", documentID));
        items.add(new Listing(R.drawable.sample4, "Markobar", "$420", "Reno, NV", documentID));
        items.add(new Listing(R.drawable.sample5, "Marmut", "$2550", "Salem, OR", documentID));*/
        return items;
    }
}
