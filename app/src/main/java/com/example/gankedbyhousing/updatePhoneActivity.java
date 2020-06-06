package com.example.gankedbyhousing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class updatePhoneActivity extends AppCompatActivity {

    private TextView newPhone;
    private Button confirm, back;

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);

        newPhone = findViewById(R.id.newPhone);
        confirm = findViewById(R.id.confirmButton);
        back = findViewById(R.id.backbtn);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(updatePhoneActivity.this, ProfileActivity.class);
                startActivity(toProfile);
            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhone = newPhone.getText().toString();

                DocumentReference docRef = fStore.collection("Users").document(userID);
                docRef.update("phone", mPhone);


                if(!mPhone.isEmpty()){
                    Intent toProfile = new Intent(updatePhoneActivity.this, ProfileActivity.class);
                    startActivity(toProfile);
                }
            }
        });

    }

}
