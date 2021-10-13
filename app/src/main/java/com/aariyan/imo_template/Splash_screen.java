package com.aariyan.imo_template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aariyan.imo_template.Authentication.Authentication;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class Splash_screen extends AppCompatActivity {

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Splash_screen.this, Authentication.class));
                } else if (userAuth.getCurrentUser() != null) {
                    startActivity(new Intent(Splash_screen.this,MainActivity.class));
                }

                finish();
            }
        },1000);

    }
}