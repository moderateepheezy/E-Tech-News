package com.simpumind.e_tech_news.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simplealertdialog.SimpleAlertDialogFragment;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.utils.PrefManager;

public class VendorSettingsActivity extends AppCompatActivity {

    private static final String TAG = VendorSettingsActivity.class.getSimpleName();

    public static final String SINGLE_NEWS = "Single_news";
    public static final String VENDOR_NAME = "vendor_name";
    public static final String VENDOR_ICON = "vendor_icon";
    public static final String VENDOR_ID = "vendor_id";


    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private DatabaseReference mDataRef;
    private DatabaseReference mDatabase;

    Button subscribe;

    static String vendor_id;
    static String news_id;


    private static boolean isUserSubscribed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_settings);

        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Vendor Settings");

        Intent intent = getIntent();
        news_id  = intent.getStringExtra(SINGLE_NEWS);

        vendor_id  = intent.getStringExtra(VENDOR_ID);

        subscribe = (Button) findViewById(R.id.subscribe);

        String userId = PrefManager.readUserKey(getApplicationContext());

        mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(userId).child("susbscriptions").child(vendor_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendor_id) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.button_back_color));
                    subscribe.setText("Unsubscribe");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isUserSubscribed){
                    isUserSubscribed = false;

                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                    subscribe.setText("Subscribe");

                    unSubscribeVednor(vendor_id);

                }else {

                    CharSequence[] x =  {"Daily","Weekly","Monthly"};
                    final CharSequence[] y = {"Direct Billing","Mobile Money"};



                    new MaterialDialog.Builder(VendorSettingsActivity.this)
                            .theme(Theme.LIGHT)
                            .title("Choose one")
                            .items(x)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {

                                    new MaterialDialog.Builder(VendorSettingsActivity.this)
                                            .theme(Theme.LIGHT)
                                            .title("Choose one")
                                            .items(y)
                                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                                @Override
                                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {
                                                    isUserSubscribed = true;
                                                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.button_back_color));
                                                    subscribe.setText("Unsubscribe");
                                                    subscribeVendor(vendor_id);
                                                    return true; // allow selection
                                                }
                                            })
                                            .positiveText("Ok")
                                            .show();


                                    return true; // allow selection
                                }
                            })
                            .positiveText("Ok")
                            .show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void unSubscribeVednor(final String vendorId){

        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendorId)){
                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager.readUserKey(getApplicationContext())).child("susbscriptions").child(vendorId).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void subscribeVendor(final String vendorId){

        final DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();

        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).orderByChild("susbscriptions").equalTo(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){

                }else {
                     FirebaseDatabase.getInstance().getReference()
                             .child("subscriber").child(PrefManager
                            .readUserKey(getApplicationContext()))
                             .child("susbscriptions").child(vendorId).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
