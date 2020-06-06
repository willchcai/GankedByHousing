package com.example.gankedbyhousing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

public class updateLocationActivity extends AppCompatActivity {

    private TextView newLocation;
    private Button confirm, back;

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);

        newLocation = findViewById(R.id.newLocation);
        confirm = findViewById(R.id.comfirmButton);
        back = findViewById(R.id.backbtn);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(updateLocationActivity.this, ProfileActivity.class);
                startActivity(toProfile);
            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mLocation = newLocation.getText().toString();

                DocumentReference docRef = fStore.collection("Users").document(userID);
                docRef.update("location", mLocation);


                if(!mLocation.isEmpty()){
                    Intent toProfile = new Intent(updateLocationActivity.this, ProfileActivity.class);
                    startActivity(toProfile);
                }
            }
        });

    }
}
