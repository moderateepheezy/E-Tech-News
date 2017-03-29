package com.simpumind.e_tech_news.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.models.User;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.ArrayList;
import java.util.StringTokenizer;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneModel;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

import static android.R.attr.country;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;

    PhoneNumberVerifier.Countries country;
    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button go = (Button) findViewById(R.id.go);
        Spinner spinner = (Spinner) findViewById(R.id.countrySpinner);
        go.setOnClickListener(this);

        final TextView textView = (TextView) findViewById(R.id.countryCode);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        CustomSpinnerAdapter customSpinnerAdapter= new CustomSpinnerAdapter(getApplicationContext(),PhoneNumberVerifier.Countries.values());
        spinner.setAdapter(customSpinnerAdapter);
        PhoneNumberVerifier verifier = new PhoneNumberVerifier();
        country = verifier.getUserCountry(LoginActivity.this);
        int index = 0;
        if (country != null) {

            for (PhoneNumberVerifier.Countries c : PhoneNumberVerifier.Countries.values()) {
                if (c == country) {
                    break;
                }
                index++;
            }

            spinner.setSelection(index);


            textView.setText("+" + String.valueOf(country.getCountryCode()));

            Button button = (Button) findViewById(R.id.go);
            button.setOnClickListener(this);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    country = PhoneNumberVerifier.Countries.values()[i];
                    textView.setText("+" + country.getCountryCode() + " |");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.go){
            //Intent intent = new Intent(LoginActivity.this, NewsMainActivity.class);
            //startActivity(intent);
            String number = phoneNumber.getText().toString();

            if(phoneNumber.getText().toString().isEmpty()){
                phoneNumber.setError("Cannot be empty");
            }
            try {
                PhoneModel phoneModel = country.isNumberValid(country, number);
                if (phoneModel.isValidPhoneNumber()) {
                    number = country.ToCountryCode(country,phoneModel.getPhoneNumber());
                    //outputTextView.setText(number);
                    Toast.makeText(LoginActivity.this, number, Toast.LENGTH_SHORT).show();

                    PrefManager.saveMSSIDN(getApplicationContext(), "identify", number);

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").push();

                    User user = new User("tolu","email@gmail.com",number, "some address", "passowrd");
                    mDatabase.setValue(user);


                    Explode explode = new Explode();
                    explode.setDuration(800);

                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    startActivity(intent, oc2.toBundle());
                } else {
                    //outputTextView.setText("Not a valid phone number");
                    phoneNumber.setError("Not a valid number");
                }
            } catch (PhoneFormatException e) {
                //outputTextView.setText(e.getMessage());
            }
        }
    }



    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private PhoneNumberVerifier.Countries [] asr;

        public CustomSpinnerAdapter(Context context, PhoneNumberVerifier.Countries[] asr) {
            this.asr = asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.length;
        }

        public Object getItem(int i)
        {
            return asr[i];
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getApplicationContext());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position].name());
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getApplicationContext());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
            txt.setText(asr[i].name());
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }
}
