package com.example.gankedbyhousing;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements PopUpDialog.PopUpDialogListener {

    private Button logoutBtn;
    private Button makeListing;
    private Button viewListing;
    private Button changePass;
    private TextView welcome;
    private TextView profName;
    private TextView profPhone;
    private TextView profEmail;
    private ImageView profPic;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference mStorageRef;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutBtn = findViewById(R.id.logoutButton);
        makeListing = findViewById(R.id.makeListing);
        viewListing = findViewById(R.id.viewListing);
        changePass = findViewById(R.id.changePass);
        welcome = findViewById(R.id.welcome);
        profName = findViewById(R.id.profName);
        profEmail = findViewById(R.id.profEmail);
        profPhone = findViewById(R.id.profPhone);
        profPic = findViewById(R.id.profPic);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        userID = mAuth.getCurrentUser().getUid();

       //Restore profile picture, if there is any
        StorageReference profRef = mStorageRef.child("profile.jpg" + userID);
        profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profPic);
            }
        });

        //Retrieve user information
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String mName = documentSnapshot.getString("name");
                String mPhone = documentSnapshot.getString("phone");
                String mEmail = documentSnapshot.getString("email");

                welcome.setText("Welcome, " + mName + "!");
                profName.setText(mName);
                profPhone.setText(mPhone);
                profEmail.setText(mEmail);

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                mAuth.getInstance().signOut();
                startActivity(toLoginActivity);
            }
        });


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //toGallery.setType("image/*");
                startActivityForResult(toGallery,100);
            }
        });
    }



    public void openDialog() {
        PopUpDialog popup = new PopUpDialog();
        popup.show(getSupportFragmentManager(), "changePass");
    }

    @Override
    public void applyText(String newPass, String confirmPass) {
        if(newPass.equals(confirmPass)) {
            mAuth.getCurrentUser().updatePassword(newPass);
            Toast.makeText(getApplicationContext(), "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Entered passwords do not match.", Toast.LENGTH_SHORT).show();

        }
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
                Toast.makeText(MainActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Profile image failed to update. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
