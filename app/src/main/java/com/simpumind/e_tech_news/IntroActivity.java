package com.simpumind.e_tech_news;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
    public void onFinishButtonPressed() {
        Intent showSignUpIntent = new Intent(IntroActivity.this, ProcessUserActivity.class);
        startActivity(showSignUpIntent);
    }
}
