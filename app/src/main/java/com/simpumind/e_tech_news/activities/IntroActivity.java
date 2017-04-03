package com.simpumind.e_tech_news.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AhoyOnboarderActivity {

    private static final String TAG = IntroActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String mssisdn = PrefManager.readMSSISDN(getApplicationContext(), "identify");
        if(mssisdn != null){
            Intent intent = new Intent(IntroActivity.this, NewsMainActivity.class);
            startActivity(intent);
        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(IntroActivity.this, NewsMainActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        AhoyOnboarderCard firstScreen = new AhoyOnboarderCard(getString(R.string.first_title), getString(R.string.first_description), R.drawable.bookone);
        firstScreen.setBackgroundColor(R.color.colorPrimaryDark);
        firstScreen.setTitleColor(R.color.colorAccent);
        firstScreen.setDescriptionColor(R.color.colorAccent);
        firstScreen.setTitleTextSize(dpToPixels(10, this));
        firstScreen.setDescriptionTextSize(dpToPixels(8, this));
        firstScreen.setIconLayoutParams(256, 256, 40, 0, 0, 40);


        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(firstScreen);
        pages.add(firstScreen);
        pages.add(firstScreen);

        setOnboardPages(pages);

        setColorBackground(R.color.colorPrimary);

        showNavigationControls(false);

        setInactiveIndicatorColor(R.color.colorAccent);
        setActiveIndicatorColor(R.color.pressedColor);

        setFinishButtonTitle("Get Started");

        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onFinishButtonPressed() {
        Intent showSignUpIntent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(showSignUpIntent);
    }
}
