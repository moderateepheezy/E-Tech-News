package com.simpumind.e_tech_news.activities;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.SpinnerAdapter;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.OptionalPendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;
        import com.simpumind.e_tech_news.R;
        import com.simpumind.e_tech_news.models.User;
        import com.simpumind.e_tech_news.utils.PrefManager;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Locale;
        import java.util.Map;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    public Switch unsubscribeAll;
    public Switch socialSwitch;

    public LinearLayout userLoginLayout;
    public LinearLayout userNotLoginLayout;

    private static final int RC_SIGN_IN = 9001;

    TextView username;
    TextView userEmail;
    TextView userPhone;
    TextView textView;

    private ProgressDialog progressDialog;

    CallbackManager mFacebookCallbackManager;


    private static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("General Settings");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(SettingsActivity.this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        username = (TextView) findViewById(R.id.username);
        userEmail = (TextView) findViewById(R.id.user_email);
        userPhone = (TextView) findViewById(R.id.user_phone);
        textView = (TextView) findViewById(R.id.type);
        socialSwitch = (Switch) findViewById(R.id.sign_out_of_social_switch);
        unsubscribeAll = (Switch) findViewById(R.id.unsubscribe_switch);

        userLoginLayout = (LinearLayout) findViewById(R.id.userLogin);
        userNotLoginLayout = (LinearLayout) findViewById(R.id.userNoLogin);

        Button facebookLogin = (Button) findViewById(R.id.loginWithFacebook);
        Button googleLogin = (Button) findViewById(R.id.loginWithGoogle);
        facebookLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);


        initCustomSpinner();
        checkSubscriber();



        final User u = PrefManager.getStoredUser(getApplicationContext());

        boolean isSwitched = !u.getSigninType().equals("");
        if(!isSwitched){
            socialSwitch.setEnabled(false);
        }else {
            socialSwitch.setEnabled(true);
        }

        if(!u.getUsername().isEmpty()){

            updateUI();

            userLoginLayout.setVisibility(View.VISIBLE);
            userNotLoginLayout.setVisibility(View.GONE);
        }else{
            Query query = FirebaseDatabase.getInstance().getReference().child("subscriber")
                    .child(PrefManager.readUserKey(getApplicationContext())).child("users");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        PrefManager.storeUser(getApplicationContext(), user);
                        username.setText(user.getUsername());
                        userEmail.setText(user.getEmail());
                        userPhone.setText(user.getMsisdn());

                        userLoginLayout.setVisibility(View.VISIBLE);
                        userNotLoginLayout.setVisibility(View.GONE);
                    }else{

                        userNotLoginLayout.setVisibility(View.VISIBLE);
                        userLoginLayout.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        unsubscribeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unsubscribeAll.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager
                                    .readUserKey(getApplicationContext())).child("susbscriptions").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                unsubscribeAll.setEnabled(false);

                                FirebaseDatabase.getInstance().getReference().child("subscriber")
                                        .child(PrefManager
                                                .readUserKey(getApplicationContext())).child("read_news").removeValue();
                            }
                        }
                    });
                }
            }
        });

        socialSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u.getSigninType().equals("Google")){
                    signOut();
                }else {
                    LoginManager.getInstance().logOut();
                }
            }
        });

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // ...
                        if (status.isSuccess()){
                            userLoginLayout.setVisibility(View.GONE);
                            userNotLoginLayout.setVisibility(View.VISIBLE);
                            socialSwitch.setEnabled(false);

                            SharedPreferences preferences = getSharedPreferences(PrefManager.PREF_NAME, 0);
                            preferences.edit().clear().apply();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

    private void initCustomSpinner() {

        Spinner spinnerCustom= (Spinner)findViewById(R.id.spinner);
        // Spinner Drop down elements
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Select");
        languages.add("English");
        languages.add("French");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(getApplicationContext(),languages);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                if(position == 1) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().putString("lang", "en").commit();
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "Locale in English !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else if(position == 2){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().putString("lang", "fr").commit();
                    Locale locale = new Locale("fr");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "Locale in French !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                Toast.makeText(parent.getContext(),  item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.loginWithGoogle){
            onGoogleLogin();
        }else if(id == R.id.loginWithFacebook){
            onFblogin();
        }
    }


    public static class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }

    public void checkSubscriber()
    {
        FirebaseDatabase.getInstance().getReference().child("subscriber")
                .child(PrefManager
                        .readUserKey(getApplicationContext())).child("susbscriptions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    unsubscribeAll.setEnabled(false);
                }else {
                    unsubscribeAll.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String mssisdn = PrefManager.readMSSISDN(getApplicationContext(), "identify");

            User user = new User(acct.getDisplayName(), acct.getEmail(), mssisdn,
                    "address", "password", acct.getPhotoUrl().toString(), "Google");

            pushUserToFirebase(user);
            PrefManager.storeUser(getApplicationContext(), user);


            progressDialog = ProgressDialog.show(getApplicationContext(), "", "");

            new Thread() {

                public void run() {

                    try{

                        sleep(500);

                        onGoogleLogin();

                    } catch (Exception e) {

                        Log.e("tag", e.getMessage());

                    }

                    progressDialog.dismiss();

                }

            }.start();


        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }


    protected void getUserInfo(final LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.d("Hessd", "complete" + "ss");
                        try {
                            String facebook_id = object.getString("id");
                            String f_name = object.getString("name");
                            String email_id = object.getString("email");
                            String token = login_result.getAccessToken().getToken();
                            String picUrl = "https://graph.facebook.com/me/picture?type=normal&method=GET&access_token="+ token;

                            String mssisdn = PrefManager.readMSSISDN(getApplicationContext(), "identify");

                            User user = new User(f_name, email_id, mssisdn,  "address", "password", picUrl, "Facebook");
                            pushUserToFirebase(user);

                            PrefManager.storeUser(getApplicationContext(), user);

                            updateUI();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
        data_request.executeAsync();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        User u = PrefManager.getStoredUser(getApplicationContext());
        if (!u.getUsername().isEmpty()){
            updateUI();
        }else{
            userNotLoginLayout.setVisibility(View.VISIBLE);
            userLoginLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(SettingsActivity.this);
        mGoogleApiClient.disconnect();
    }

    public void updateUI(){

        User user = PrefManager.getStoredUser(getApplicationContext());

        username.setText(user.getUsername());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getMsisdn());

        userLoginLayout.setVisibility(View.VISIBLE);
        userNotLoginLayout.setVisibility(View.GONE);
    }


    public void pushUserToFirebase(User user){

        Map<String, Object> userValues = user.toMap();
        DatabaseReference innerMDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference innerChildRef = innerMDatabaseRef.child("subscriber").child(PrefManager
                .readUserKey(getApplicationContext()));
        innerChildRef.child("users").setValue(userValues);
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();
    }

    private void onFblogin()
    {

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        getUserInfo(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                    }
                });
    }



    private void onGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
}

