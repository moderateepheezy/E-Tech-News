package com.simpumind.e_tech_news.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.models.User;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneModel;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

import static android.R.attr.country;
import static android.R.attr.start;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth.AuthStateListener authListener;

    private FirebaseAuth mAuth;

    private static String phoneText;

    PhoneNumberVerifier.Countries country;
    EditText phoneNumber;

    private ProgressDialog progress;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // super.onCodeSent(s, forceResendingToken);
                progress.dismiss();
                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                intent.putExtra(OTPActivity.VERFICATION_ID, s);
                intent.putExtra(OTPActivity.PHONE_NUMBER, phoneText);
                startActivity(intent);
                finish();
            }
        };

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


            String number = phoneNumber.getText().toString();

            if(phoneNumber.getText().toString().isEmpty()){
                phoneNumber.setError("Cannot be empty");
            }
            try {
                PhoneModel phoneModel = country.isNumberValid(country, number);
                if (phoneModel.isValidPhoneNumber()) {

                    progress=new ProgressDialog(this);
                    progress.setMessage("Signing in...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();

                    number = country.ToCountryCode(country,phoneModel.getPhoneNumber());
                    //outputTextView.setText(number);
                    Toast.makeText(LoginActivity.this, number, Toast.LENGTH_SHORT).show();

                    phoneText = number;

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, this, mCallbacks);


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

    @Override
    protected void onResume() {
        super.onResume();
    }

}
