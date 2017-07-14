package com.simpumind.e_tech_news.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends OnboarderActivity {

    private static final String TAG = IntroActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    List<OnboarderPage> onboarderPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onboarderPages = new ArrayList<>();

        OnboarderPage onboarderPage1 = new OnboarderPage("E-Newspaper", "Access news from different vendors", R.drawable.a);
        OnboarderPage onboarderPage2 = new OnboarderPage("E-Newspaper", "Instant news", R.drawable.b);
        OnboarderPage onboarderPage3 = new OnboarderPage("E-Newspaper", "Customize news to your taste.", R.drawable.c);

        onboarderPage1.setBackgroundColor(R.color.a1_color);
        onboarderPage2.setBackgroundColor(R.color.b1_color);
        onboarderPage3.setBackgroundColor(R.color.c1_color);

        onboarderPages.add(onboarderPage1);
        onboarderPages.add(onboarderPage2);
        onboarderPages.add(onboarderPage3);

        setOnboardPagesReady(onboarderPages);


//        String mssisdn = PrefManager.readMSSISDN(getApplicationContext(), "identify");
//        if(!mssisdn.equals("")){
//            Intent intent = new Intent(IntroActivity.this, NewsMainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(IntroActivity.this, NewsMainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
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
    public void onSkipButtonPressed() {
        // Optional: by default it skips onboarder to the end
        super.onSkipButtonPressed();
        // Define your actions when the user press 'Skip' button
    }

    @Override
    public void onFinishButtonPressed() {
        // Define your actions when the user press 'Finish' button
        finshed();
    }

    public void finshed() {
        Intent showSignUpIntent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(showSignUpIntent);
        finish();
    }

}
