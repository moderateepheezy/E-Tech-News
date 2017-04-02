package com.simpumind.e_tech_news.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class VendorNewsListActivity extends AppCompatActivity {

    private static final String TAG = VendorNewsListActivity.class.getSimpleName();
    public static final String NEWS_PAPER_ID = "news_paper_id";

    public static final String VENDOR_NAME = "vendor_name";
    public static final String VENDOR_ICON = "vendor_icon";

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

        setTitle(vendorName);

        String id = intent.getStringExtra(NEWS_PAPER_ID);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("news").orderByChild("newspaper_id").equalTo(id);

        newsListAdapter = new NewsListAdapter(News.class,  R.layout.vendor_news_item_list,
                RecyclerView.ViewHolder.class, childRef, getApplicationContext(), this, vendorName, vendorIcon);

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
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(VendorNewsListActivity.this, VendorSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
