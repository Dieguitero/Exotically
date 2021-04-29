package com.example.exotically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingActivity extends AppCompatActivity {

    private Button nLogin, nRegister;
    private boolean changing;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //VARIABLES
        nLogin = (Button) findViewById(R.id.login);
        nRegister = (Button) findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        changing = false;

        //LOGIN BUTTON
        nLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nLogin.setEnabled(false);
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        //REGISTER BUTTON
        nRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                }
                nRegister.setEnabled(false);
                Intent intent = new Intent(LandingActivity.this, RegistrationActivity.class);
                startActivity(intent);
                return;
            }
        });

        checkGetPermissions();

        //CHECK FIREBASE LOGIN
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && !changing){
                    changing = true;
                    Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


    }

    //CHECK & GET LOCATION/CAMERA PERMISSIONS
    private void checkGetPermissions() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION}, 44);

        }
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        changing = false;
        nLogin.setEnabled(true);
        nRegister.setEnabled(true);
    }
}
