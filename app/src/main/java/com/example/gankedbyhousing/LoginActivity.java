package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView email;
    private TextView password;
    private TextView toSignUp;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            toHomeActivity();
        }

        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passwordText);
        toSignUp = findViewById(R.id.signUpText);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if login credentials are valid, go to home activity.
                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();

                if(mEmail.isEmpty()){
                    email.setError("Please enter your email.");
                    return;
                }

                if(mPassword.isEmpty()){
                    password.setError("Please enter your password.");
                    return;
                }

                mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent toHomeActivity = new Intent(LoginActivity.this, MainActivity.class);
                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            startActivity(toHomeActivity);
                        }else{
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSignUpActivity();
            }
        });
    }

    private boolean validateForm(String mEmail, String mPassword){
        boolean valid = true;

        if(mEmail.isEmpty()){
            email.setError("A valid email address is required.");
            valid = false;
        }else{
            email.setError(null);
        }


        if(mPassword.isEmpty()){
            password.setError("A valid password is required.");
            valid = false;
        }else{
            password.setError(null);
        }

        return valid;
    }

    private void toSignUpActivity(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void toHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent toActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(toActivity);

    }
}
