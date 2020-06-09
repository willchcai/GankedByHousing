package com.example.gankedbyhousing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MakeListings extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private StorageReference fStorage;

    private String userID;
    private String listKey;

    private ImageView listPic;
    private EditText listName, price, state, city, email, phoneNumber;
    private Button confirmButton;
    private Uri currentImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_listings);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fStorage = FirebaseStorage.getInstance().getReference();

        listPic = findViewById(R.id.listPic);
        price = findViewById(R.id.price);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        listName = findViewById(R.id.listName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumberEdit);

        confirmButton = findViewById(R.id.confirmButton);


        listPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,69);
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPrice = "$"+price.getText().toString();
                String mState = state.getText().toString();
                String mCity = city.getText().toString();
                String mListName = listName.getText().toString();
                String mEmail = email.getText().toString();
                String mPhoneNumber = phoneNumber.getText().toString();

                Map<String, Object> listingData = new HashMap<>();
                listingData.put("title", mListName);
                listingData.put("price", mPrice);
                listingData.put("city", mCity);
                listingData.put("state", mState);
                listingData.put("uid", userID);
                listingData.put("phone number", mPhoneNumber);
                listingData.put("email", mEmail);



                //make new listing document in fireStore collections
                CollectionReference fileRef = fStore.collection("Listings");
                fileRef.add(listingData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MakeListings.java", "DocumentSnapshot written with ID: " + documentReference.getId());
                        uploadImageToFirebase(currentImageUri, documentReference.getId());

                    }
                });


            }

        });




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
                    case R.id.view_listings:
                        Intent toNotifications = new Intent(MakeListings.this, ViewListingsActivity.class);
                        startActivity(toNotifications);
                        break;
                    case R.id.make_listings:
                        break;

                }

                return true;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 69 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(listPic);
            currentImageUri = imageUri;

        }

    }


    public void uploadImageToFirebase(Uri imageUri, String documentID){
        //upload image to firebase storage

        StorageReference fileRef = fStorage.child("image"+documentID);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MakeListings.this, "listing image updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MakeListings.this, "Profile image failed to update. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}