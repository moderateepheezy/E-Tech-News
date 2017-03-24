package com.simpumind.e_tech_news;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.simpumind.e_tech_news.fragments.LibraryFragment;
import com.simpumind.e_tech_news.fragments.SettingsFragment;
import com.simpumind.e_tech_news.fragments.SubscriptionFragment;

public class NewsMainActivity extends AppCompatActivity {

    private static final String TAG = NewsMainActivity.class.getSimpleName();
    public static final String CAT_NAME = "category";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.home:
                                selectedFragment = new SubscriptionFragment();
                                setTitle("Vendors");
                                break;
                            case R.id.library:
                                selectedFragment = new LibraryFragment();
                                setTitle("Library");
                                break;
                            case R.id.settings:
                                selectedFragment = new SettingsFragment();
                                setTitle("Settings");
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.framelayout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new SubscriptionFragment());
        transaction.commit();
    }


}
