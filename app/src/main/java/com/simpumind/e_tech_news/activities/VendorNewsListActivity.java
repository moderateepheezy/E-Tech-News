package com.simpumind.e_tech_news.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.adapter.NewsListAdapter;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.utils.EmptyRecyclerView;
import com.simpumind.e_tech_news.utils.PrefManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class VendorNewsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = VendorNewsListActivity.class.getSimpleName();
    public static final String NEWS_PAPER_ID = "news_paper_id";

    public static final String VENDOR_NAME = "vendor_name";
    public static final String VENDOR_ICON = "vendor_icon";
    public static final String VENDOR_ID = "vendor_id";

    private DatabaseReference mDatabaseRef;
    private Query childRef;

    private NewsListAdapter newsListAdapter;

    private Toolbar toolbar;

    private static boolean isUserSubscribed = false;

    Button subscribe;
    private static String vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        subscribe = (Button) findViewById(R.id.subscribe);

        EmptyRecyclerView myRecycler = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);

        Intent intent = getIntent();

        String vendorName = intent.getStringExtra(VENDOR_NAME);
        String vendorIcon = intent.getStringExtra(VENDOR_ICON);
        vendorId = intent.getStringExtra(VENDOR_ID);

        String id = intent.getStringExtra(NEWS_PAPER_ID);

        //loadImage(vendorIcon);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // getSupportActionBar().setTitle(vendorName);
        toolbar.setTitleMarginStart(0);

        ImageView logo = (ImageView) toolbar.findViewById(R.id.logo);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(vendorName);

        loadImage(logo, vendorIcon);

        String userId = PrefManager.readUserKey(getApplicationContext());
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(userId).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equals(vendorId) && dataSnapshot.getValue() != null) {
                    isUserSubscribed = true;
                    subscribe.setVisibility(View.GONE);

                } else {
                    subscribe.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("news").orderByChild("newspaper_id").equalTo(id);

        newsListAdapter = new NewsListAdapter(News.class, R.layout.vendor_news_item_list,
                RecyclerView.ViewHolder.class, childRef, getApplicationContext(), this, vendorName, vendorIcon, vendorId);

        myRecycler.setAdapter(newsListAdapter);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence[] x = {"Daily", "Weekly", "Monthly"};
                final CharSequence[] y = {"Direct Billing", "Mobile Money"};


                new MaterialDialog.Builder(VendorNewsListActivity.this)
                        .theme(Theme.LIGHT)
                        .title("Choose one")
                        .items(x)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                new MaterialDialog.Builder(VendorNewsListActivity.this)
                                        .theme(Theme.LIGHT)
                                        .title("Choose one")
                                        .items(y)
                                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                            @Override
                                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                isUserSubscribed = true;
                                                subscribeVendor(vendorId);
                                                subscribe.setVisibility(View.GONE);
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
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG, "--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vendor_news_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {

            Intent i = getIntent();

            String vendorName = i.getStringExtra(VENDOR_NAME);
            String vendorIcon = i.getStringExtra(VENDOR_ICON);
            String vendorId = i.getStringExtra(VENDOR_ID);

            Intent intent = new Intent(VendorNewsListActivity.this, VendorSettingsActivity.class);
            intent.putExtra(VendorSettingsActivity.VENDOR_NAME, vendorName);
            intent.putExtra(VendorSettingsActivity.VENDOR_ICON, vendorIcon);
            intent.putExtra(VendorSettingsActivity.VENDOR_ID, vendorId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        newsListAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void subscribeVendor(final String vendorId) {


        Query query = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).orderByChild("susbscriptions").equalTo(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {

                } else {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("subscriber").child(PrefManager
                            .readUserKey(getApplicationContext())).child("susbscriptions");
                    mDatabase.child(vendorId).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chechButton();
    }

    private void chechButton() {
        String userId = PrefManager.readUserKey(getApplicationContext());
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(userId).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendorId) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    subscribe.setVisibility(View.GONE);

                }else {
                    subscribe.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadImage(ImageView imageView, String imagePath){

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);

//        Glide.with(getApplicationContext())
//                .using(new FirebaseImageLoader())
//                .load(mStorage)
//                .fitCenter()
//                .placeholder(R.drawable.denews)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        getSupportActionBar().setLogo(resource);
//                    }
//                });


        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(mStorage)
                .fitCenter()
                .placeholder(R.drawable.denews)
                .into(imageView);

    }



}
