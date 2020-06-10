package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ViewSingleListingActivity extends AppCompatActivity {
    TextView title, listing_document_id, price, location, phoneNumber, email;
    ImageView image;
    StorageReference mStorageRef;
    FirebaseFirestore fStore;
    private static final String TAG = "ViewSingleListing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_listing);

        String listingDocumentId = getIntent().getStringExtra("Listing Document ID");

/*        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);*/
        fStore = FirebaseFirestore.getInstance();

        LayoutInflater inflater = getLayoutInflater();
        View card = inflater.inflate(R.layout.activity_view_listings, null);

        title = findViewById(R.id.item_name);
        price = findViewById(R.id.item_age);
        image = findViewById(R.id.item_image);
        location = findViewById(R.id.item_city);
        email = findViewById(R.id.item_email);
        phoneNumber = findViewById(R.id.item_number);
        listing_document_id = findViewById(R.id.listing_document_id);
        Log.d(TAG, "listingDocumentID: " + listingDocumentId);


        fStore.collection("Listings")
                .document(listingDocumentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> docObj = document.getData();
                                title.setText(docObj.get("title").toString());
                                price.setText("Price: " + docObj.get("price").toString());
                                location.setText("Location: " + docObj.get("city") + ", " + docObj.get("state"));
                                phoneNumber.setText("Phone Number: " + docObj.get("phone number"));
                                email.setText("Email: " + docObj.get("email"));


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profRef = mStorageRef.child("image"+listingDocumentId);
        //Log.d("CardStackAdapter", "value of userID" + data.getListingID());

        listing_document_id.setText("Listing ID: " + listingDocumentId);
        profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("CardStackAdapter", "onSuccessListener triggered");

                Picasso.get().load(uri).fit().centerCrop().into(image);
            }
        });



    }
}
