package com.example.organizzeclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.organizzeclone.R;
import com.example.organizzeclone.activity.home.HomeActivity;
import com.example.organizzeclone.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_register)
                .canGoForward(false)
                .build());

    }

    @Override
    protected void onStart() {
        super.onStart();
        userIsLogged();
    }

    public void goToRegister(View view) {startActivity(new Intent(this, RegisterActivity.class)); }

    public void goToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void userIsLogged() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null) {
            goToHomePage();
        }
    }

    public void goToHomePage() {
        startActivity(new Intent(this, HomeActivity.class));
    }

}