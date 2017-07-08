package com.simpumind.e_tech_news.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
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
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.concurrent.TimeUnit;


public class OTPActivity extends AppCompatActivity {

    public static final String VERFICATION_ID = "verificationId";

    public static final String PHONE_NUMBER = "phoneNumber";

    private static final String TAG = OTPActivity.class.getSimpleName();

    ProgressDialog progress;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    Pinview pinview;

    private String verificationId;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();

        progress= new ProgressDialog(OTPActivity.this);

        Intent intent = getIntent();
        final String phone = intent.getStringExtra(PHONE_NUMBER);
        verificationId = intent.getStringExtra(VERFICATION_ID);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //phoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                mResendToken = forceResendingToken;
            }
        };


        pinview = (Pinview) findViewById(R.id.pinview);

        Button submit = (Button) findViewById(R.id.submit);

        Button resendButton = (Button) findViewById(R.id.resend_code);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pinview.getValue().isEmpty()) {

                    String code = pinview.getValue();

                    verifyPhoneNumberWithCode(verificationId, code, phone);
                }else{

                }
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phone, mResendToken);
            }
        });

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code, String phone) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential, phone);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String phone) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            progress.setCancelable(false);
                            progress.setMessage("Signing in...");
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.setIndeterminate(true);
                            progress.show();

                            mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                            Query query = mDatabaseRef.child("subscriber").orderByChild("msisdn").equalTo(phone);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.getChildrenCount() > 0){
                                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                            String subscriberKey = childSnapshot.getKey();
                                            PrefManager.saveUserKey(getApplicationContext(), subscriberKey);
                                        }

                                        progress.dismiss();
                                        Intent intent = new Intent(OTPActivity.this, NewsMainActivity.class);
                                        startActivity(intent);
                                        PrefManager.saveMSSIDN(getApplicationContext(), "identify", phone);
                                        finish();

                                    } else {
//
                                        final DatabaseReference innerChildRef =  FirebaseDatabase.getInstance().getReference().child("subscriber").push();
                                        innerChildRef.child("msisdn").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                PrefManager.saveUserKey(getApplicationContext(), innerChildRef.getKey());
                                                PrefManager.saveMSSIDN(getApplicationContext(), "identify", phone);
                                                progress.dismiss();
                                                Intent intent = new Intent(OTPActivity.this, NewsMainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]

                                Snackbar.make(findViewById(android.R.id.content), "Invalid code.",
                                        Snackbar.LENGTH_SHORT).show();

                               // mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                           // updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
}
