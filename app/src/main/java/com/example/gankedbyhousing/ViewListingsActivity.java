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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewListingsActivity extends AppCompatActivity {

    private static final String TAG = "ViewListingsActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    FirebaseFirestore fStore;
    List<Listing> items;

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
        items = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<Listing> items) {
                Log.d(TAG,"During onCallback call");
                Log.d(TAG,"items" + items);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new CardStackAdapter(items);

        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }

    private void paginate() {
        List<Listing> old = adapter.getItems();
        List<Listing> baru = new ArrayList<>(getListOfListings());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    public static void getAllFromFireStore(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Listings").get().addOnCompleteListener(listener);
    }

    private List<Listing> getListOfListings() {

        fStore = FirebaseFirestore.getInstance();;
        items = new ArrayList<>();
        readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<Listing> items) {
                Log.d(TAG,"During onCallback call");
                Log.d(TAG,"items" + items);
            }
        });
        Log.d(TAG,"After readData call");
        Log.d(TAG,"items" + items);

        return items;

    }

    private void readData(final FireStoreCallback fireStoreCallBack){
        fStore.collection("Listings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> docObj = document.getData();
                                items.add(new Listing(R.drawable.sample1, docObj.get("title").toString(),docObj.get("price").toString(), "Location: " + docObj.get("city") + " " + docObj.get("state"), document.getId(), "Phone Number: " +docObj.get("phone number"), "Email: " + docObj.get("email")));
                            }
                            //Log.d(TAG, "items: " + items);
                            fireStoreCallBack.onCallback(items);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private interface FireStoreCallback {
        void onCallback(List<Listing> items);
    }

}
