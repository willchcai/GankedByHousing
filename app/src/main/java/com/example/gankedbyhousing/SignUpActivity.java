package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private TextView toLogin;
    private TextView name;
    private TextView phoneNum;
    private TextView location;
    private Button register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.SUemailText);
        password = findViewById(R.id.SUpassText);
        toLogin = findViewById(R.id.toSignIn);
        register = findViewById(R.id.regButton);
        name = findViewById(R.id.SUnameText);
        phoneNum = findViewById(R.id.SUphoneText);
        location = findViewById(R.id.SUlocation);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(SignUpActivity.this, LoginActivity.class);
                mAuth.signOut();
                startActivity(toLoginActivity);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail = email.getText().toString();
                final String mPass = password.getText().toString();
                final String mName = name.getText().toString();
                final String mPhoneNum = phoneNum.getText().toString();
                final String mLocation = location.getText().toString();

                if(mEmail.isEmpty()){
                    email.setError("A valid email is required.");
                    return;
                }

                if(mPass.isEmpty()){
                    password.setError("A valid password is required.");
                    return;
                }

                if(mPass.length() < 6){
                    password.setError("Password must be 7 or more characters in length.");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();

                            //create collection called Users, make document that saves the UserIDs inside the collection.
                            DocumentReference docRef = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", mName);
                            user.put("email", mEmail);
                            user.put("phone", mPhoneNum);
                            user.put("location", mLocation);
                            docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user Profile created for " + userID);
                                }
                            });

                            Toast.makeText(SignUpActivity.this, "User Account created successfully!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUpActivity.this, "An error has occurred. Please try again.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}
