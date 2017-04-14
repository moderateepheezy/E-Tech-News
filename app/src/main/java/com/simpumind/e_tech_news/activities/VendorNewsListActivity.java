package com.simpumind.e_tech_news.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.adapter.NewsListAdapter;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.utils.EmptyRecyclerView;


public class VendorNewsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = VendorNewsListActivity.class.getSimpleName();
    public static final String NEWS_PAPER_ID = "news_paper_id";

    public static final String VENDOR_NAME = "vendor_name";
    public static final String VENDOR_ICON = "vendor_icon";
    public static final String VENDOR_ID = "vendor_id";

    private DatabaseReference mDatabaseRef;
    private Query childRef;

    private NewsListAdapter newsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_news_list);

        getSupportActionBar().setHomeButtonEnabled(true);

        EmptyRecyclerView myRecycler = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);

        Intent intent = getIntent();

        String vendorName = intent.getStringExtra(VENDOR_NAME);
        String vendorIcon = intent.getStringExtra(VENDOR_ICON);
        String vendorId = intent.getStringExtra(VENDOR_ID);

        setTitle(vendorName);

        String id = intent.getStringExtra(NEWS_PAPER_ID);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("news").orderByChild("newspaper_id").equalTo(id);

        newsListAdapter = new NewsListAdapter(News.class,  R.layout.vendor_news_item_list,
                RecyclerView.ViewHolder.class, childRef, getApplicationContext(), this, vendorName, vendorIcon, vendorId);

        myRecycler.setAdapter(newsListAdapter);
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
}
