package com.example.gankedbyhousing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private TextView toMain;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passText);
        toMain = findViewById(R.id.toSignIn);
        register = findViewById(R.id.regButton);

        mAuth = FirebaseAuth.getInstance();


        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainActivity = new Intent(SignUpActivity.this, MainActivity.class);
                mAuth.signOut();
                startActivity(toMainActivity);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString();
                String mPass = password.getText().toString();

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
                            Toast.makeText(SignUpActivity.this, "User Account created successfully!",Toast.LENGTH_SHORT).show();
                            //Intent toMainActivity = new Intent(SignUpActivity.this, MainActivity.class);
                            //startActivity(toMainActivity);
                        }else{
                            Toast.makeText(SignUpActivity.this, "An error has occurred. Please try again.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}
