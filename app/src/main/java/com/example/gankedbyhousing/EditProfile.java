package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class EditProfile extends AppCompatActivity implements PopUpDialog.PopUpDialogListener {

    private Button backBtn;
    private Button editLocation;
    private Button changePass;
    private TextView welcome;
    private TextView profName;
    private TextView profPhone;
    private TextView profEmail;
    private TextView profLocation;
    private ImageView profPic;
    private String editMode;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference mStorageRef;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        backBtn = findViewById(R.id.backButton);
        editLocation = findViewById(R.id.editLocation);
        changePass = findViewById(R.id.changePass);
        welcome = findViewById(R.id.welcome1);
        profName = findViewById(R.id.profName1);
        profEmail = findViewById(R.id.profEmail1);
        profPhone = findViewById(R.id.profPhone1);
        profPic = findViewById(R.id.profPic1);
        profLocation = findViewById(R.id.profLocation);

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

                welcome.setText("Edit Your Profile");
                profName.setText(mName);
                profPhone.setText(mPhone);
                profEmail.setText(mEmail);

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainActivity = new Intent(EditProfile.this, MainActivity.class);
                startActivity(toMainActivity);
            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = "changeLocation";
                openDialog();
            }
        });



        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = "changePass";
                openDialog();
            }
        });

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,100);
            }
        });
    }



    public void openDialog() {
        PopUpDialog popup = new PopUpDialog(editMode);
        popup.show(getSupportFragmentManager(), "changePass");
    }

    @Override
    public void applyPass(String newPass, String confirmPass) {
        if(newPass.equals(confirmPass)) {
            mAuth.getCurrentUser().updatePassword(newPass);
            Toast.makeText(getApplicationContext(), "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Entered passwords do not match.", Toast.LENGTH_SHORT).show();

        }
    }

    public void applyLocation(String newLoc){
        profLocation.setText(newLoc);
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
                Toast.makeText(EditProfile.this, "Profile image updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Profile image failed to update. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

