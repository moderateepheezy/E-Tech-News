package com.simpumind.e_tech_news.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.models.User;
import com.simpumind.e_tech_news.utils.PrefManager;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Settings");

        TextView username = (TextView) findViewById(R.id.username);
        TextView userEmail = (TextView) findViewById(R.id.user_email);
        TextView userPhone = (TextView) findViewById(R.id.user_phone);

        User u = PrefManager.getStoredUser(getApplicationContext());

        if(!u.getUsername().equals("")){
            username.setText(u.getUsername());
            userEmail.setText(u.getEmail());
            userPhone.setText(u.getMsisdn());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }
}
