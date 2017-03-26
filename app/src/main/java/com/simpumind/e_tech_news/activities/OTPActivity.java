package com.simpumind.e_tech_news.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.simpumind.e_tech_news.R;


public class OTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Pinview pinview = (Pinview) findViewById(R.id.pinview);

        Button submit = (Button) findViewById(R.id.submit);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {



                Toast.makeText(OTPActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTPActivity.this, NewsMainActivity.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(OTPActivity.this, NewsMainActivity.class);
                startActivity(intent);
            }
        });

    }
}
