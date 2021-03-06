package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity{

    private ImageView editLocation, editPhone;

    private TextView email, phone, location, profName, profLocation;

    private CircleImageView profPic;
    private Button logout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference mStorageRef;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BottomNavigationView navbar;

        navbar = findViewById(R.id.navbar);
        Menu menu = navbar.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        break;
                    case R.id.my_listings:
                        Intent myListings = new Intent(ProfileActivity.this, myListings.class);
                        startActivity(myListings);
                        break;
                    case R.id.listings:
                        Intent toMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.view_listings:
                        Intent toViewListings = new Intent(ProfileActivity.this, ViewListingsActivity.class);
                        startActivity(toViewListings);
                        break;
                    case R.id.make_listings:
                        Intent toMakeListings = new Intent(ProfileActivity.this, MakeListings.class);
                        startActivity(toMakeListings);
                        break;

                }

                return true;
            }
        });



        profName = findViewById(R.id.profName);
        profPic = findViewById(R.id.profPic);
        editLocation = findViewById(R.id.editLocation);
        editPhone = findViewById(R.id.editPhone);
        email = findViewById(R.id.UserEmail);
        phone = findViewById(R.id.UserPhone);
        location = findViewById(R.id.UserLocation);
        profLocation = findViewById(R.id.topLocation);
        logout = findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        userID = mAuth.getCurrentUser().getUid();




        //Restore profile picture, if there is any
        StorageReference profRef = mStorageRef.child("profile.jpg" + userID);
        profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("ProfileActivity", "onSuccessListener triggered");

                Picasso.get().load(uri).into(profPic);
            }
        });



        //Retrieve user information
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String mLastName = documentSnapshot.getString("last name");
                            String mFirstName = documentSnapshot.getString("first name");
                            String mPhone = documentSnapshot.getString("phone");
                            String mEmail = documentSnapshot.getString("email");
                            String mLocation = documentSnapshot.getString("location");

                            profName.setText(mFirstName + " " + mLastName);
                            email.setText(mEmail);
                            location.setText(mLocation);
                            phone.setText(mPhone);
                            profLocation.setText(mLocation);

                        }
                    }
                });





        //OnClickListeners

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateLocation = new Intent(ProfileActivity.this, updateLocationActivity.class);
                startActivity(updateLocation);
            }
        });


        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePhone = new Intent(ProfileActivity.this, updatePhoneActivity.class);
                startActivity(updatePhone);
            }
        });


        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,100);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(ProfileActivity.this, LoginActivity.class);
                mAuth.getInstance().signOut();
                startActivity(toLoginActivity);
            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(profPic);

            uploadImageToFirebase(imageUri);

        }

    }

    public void uploadImageToFirebase(Uri imageUri){
        //upload image to firebase storage

        StorageReference fileRef = mStorageRef.child("profile.jpg" + userID);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Profile image failed to update. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

