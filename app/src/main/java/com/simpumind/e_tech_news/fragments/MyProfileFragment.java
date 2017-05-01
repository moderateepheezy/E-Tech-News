package com.simpumind.e_tech_news.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;
import com.simpumind.e_tech_news.activities.SettingsActivity;
import com.simpumind.e_tech_news.adapter.NewsReadAdapter;
import com.simpumind.e_tech_news.adapter.ReadListHolder;
import com.simpumind.e_tech_news.adapter.SimpleAdapter;
import com.simpumind.e_tech_news.models.MyList;
import com.simpumind.e_tech_news.models.User;
import com.simpumind.e_tech_news.utils.EmptyRecyclerView;
import com.simpumind.e_tech_news.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by simpumind on 3/23/17.
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = MyProfileFragment.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;

    private DatabaseReference innerMDatabaseRef;
    private DatabaseReference innerChildRef;

    View view;

    CollapsingToolbarLayout coll;

    private EmptyRecyclerView recyclerView;
    private EmptyRecyclerView.LayoutManager layoutManager;
    private EmptyRecyclerView.Adapter adapter;

    CallbackManager mFacebookCallbackManager;

    private ProgressDialog progressDialog;



    CircleImageView profileImage;
    TextView profileName;
    TextView profileEmail;
    TextView phoneNumber;

    RelativeLayout main;
    RelativeLayout main1;

    private static GoogleApiClient mGoogleApiClient;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((NewsMainActivity) getActivity()).getSupportActionBar().hide();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facebookSDKInitialize();
        view = inflater.inflate(R.layout.settings_fragment, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.read_news_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getActivity(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(getActivity())).child("read_news");



         profileImage = (CircleImageView) view.findViewById(R.id.profilePicture);
         profileName = (TextView) view.findViewById(R.id.profileName);
         profileEmail = (TextView) view.findViewById(R.id.profileEmail);
         phoneNumber = (TextView) view.findViewById(R.id.mssisdn);

        Button facebookLogin = (Button) view.findViewById(R.id.facebookLogin);
        Button googleLogin = (Button) view.findViewById(R.id.googleLogin);
        facebookLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);

        coll = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbar);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.recycler_view);



        loadRecyclerViewItem();

         main = (RelativeLayout) view.findViewById(R.id.main);
        main1 = (RelativeLayout) view.findViewById(R.id.main1);

        User u = PrefManager.getStoredUser(getActivity());

        if(!u.getUsername().equals("")){
            updateUI();
            coll.setTitle(u.getUsername());
            main.setVisibility(View.GONE);
            main1.setVisibility(View.VISIBLE);
        }else{
            Query query = FirebaseDatabase.getInstance().getReference().child("subscriber")
                    .child(PrefManager.readUserKey(getActivity())).child("users");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        PrefManager.storeUser(getActivity(), user);
                        profileName.setText(user.getUsername());
                        profileEmail.setText(user.getEmail());
                        phoneNumber.setText(user.getMsisdn());

                        Glide.with(getActivity()).
                                load(user.getUserProfile())
                                .asBitmap()
                                .into(profileImage);

                        main1.setVisibility(View.VISIBLE);
                        main.setVisibility(View.GONE);
                    }else{

                        coll.setTitle("Sign in");
                        main.setVisibility(View.VISIBLE);
                        main1.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        coll.setExpandedTitleColor(Color.TRANSPARENT);
        coll.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        coll.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        setHasOptionsMenu(true);


        return view;
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getActivity());
        mFacebookCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.googleLogin){
            onGoogleLogin();
        }else if(id == R.id.facebookLogin){
            onFblogin();
        }

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

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String mssisdn = PrefManager.readMSSISDN(getActivity(), "identify");

            User user = new User(acct.getDisplayName(), acct.getEmail(), mssisdn,
                    "address", "password", acct.getPhotoUrl().toString(), "Google");

            pushUserToFirebase(user);
            PrefManager.storeUser(getActivity(), user);






        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        if(getActivity() == null)
            return;
        getActivity().runOnUiThread(Timer_Tick);
        progressDialog.dismiss();
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            onGoogleLogin();

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

        }
    };


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

                            String mssisdn = PrefManager.readMSSISDN(getActivity(), "identify");

                            User user = new User(f_name, email_id, mssisdn,  "address", "password", picUrl, "Facebook");
                            pushUserToFirebase(user);

                           PrefManager.storeUser(getActivity(), user);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.read_news_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(getActivity(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        User u = PrefManager.getStoredUser(getActivity());
        if (!u.getUsername().isEmpty()){
            updateUI();
        }else {
            main1.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    public void updateUI(){

        User user = PrefManager.getStoredUser(getActivity());

        profileName.setText(user.getUsername());
        profileEmail.setText(user.getEmail());
        phoneNumber.setText(user.getMsisdn());

        Glide.with(getActivity()).
                load(user.getUserProfile())
                .asBitmap()
                .into(profileImage);

        main1.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
    }


    private void loadRecyclerViewItem() {

        adapter = new NewsReadAdapter(Boolean.class, R.layout.read_item_card,
                ReadListHolder.class, childRef, getActivity(), (AppCompatActivity) getActivity());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    public void pushUserToFirebase(User user){

        Map<String, Object> userValues = user.toMap();
        innerMDatabaseRef = FirebaseDatabase.getInstance().getReference();
        innerChildRef = innerMDatabaseRef.child("subscriber").child(PrefManager
                .readUserKey(getActivity()));
        innerChildRef.child("users").setValue(userValues);
    }

}
