package com.example.gankedbyhousing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updatePass;
    private Button updateLocation;
    private Button updatePhone;
    private Button updatePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        updatePass = findViewById(R.id.updatePass);
        updateLocation = findViewById(R.id.updateLocation);
        updatePhone = findViewById(R.id.updatePhone);
        updatePic = findViewById(R.id.updatePic);




        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUpdatePass = new Intent(SettingsActivity.this, updatePassActivity.class);
                startActivity(toUpdatePass);
            }
        });

        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUpdateLocation = new Intent(SettingsActivity.this, updateLocationActivity.class);
                startActivity(toUpdateLocation);
            }
        });

        updatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUpdatePhone = new Intent(SettingsActivity.this, updatePhoneActivity.class);
                startActivity(toUpdatePhone);
            }
        });

        updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toGallery,100);
            }
        });




    }
}
