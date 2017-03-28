package com.simpumind.e_tech_news.activities;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.adapter.NewsListAdapter;
import com.simpumind.e_tech_news.models.News;

import java.util.ArrayList;
import java.util.List;

public class VendorNewsListActivity extends AppCompatActivity {

    private static final String TAG = VendorNewsListActivity.class.getSimpleName();

    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;

    private NewsListAdapter newsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_news_list);

        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Cartoon Network");

        RecyclerView myRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);

        //ImageView imageView = (ImageView) findViewById(R.id.imageView2);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToNewsDetails();
//            }
//        });

//        final List<String> content = new ArrayList<>();
//        for (int i = 0; i < 12; i++)
//            content.add(getListString(i));
//
//
//        ParallaxRecyclerAdapter<String> stringAdapter = new ParallaxRecyclerAdapter<String>(content) {
//            @Override
//            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter parallaxRecyclerAdapter, int i) {
//               // ((TextView) viewHolder.itemView).setText(content.get(i));
//                final ImageView image = ((SimpleViewHolder) viewHolder).newsImage;
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goToNewsDetails(image);
//                    }
//                });
//            }
//
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter parallaxRecyclerAdapter, int i) {
//                return new SimpleViewHolder(getLayoutInflater().inflate(R.layout.vendor_news_item_list, viewGroup, false));
//            }
//
//            @Override
//            public int getItemCountImpl(ParallaxRecyclerAdapter parallaxRecyclerAdapter) {
//                return content.size();
//            }
//        };
//
//
//        stringAdapter.setParallaxHeader(getLayoutInflater().inflate(R.layout.my_header, myRecycler, false), myRecycler);
//        stringAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//                //TODO: implement toolbar alpha. See README for details
//            }
//        });
//        myRecycler.setAdapter(stringAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("news");


        newsListAdapter = new NewsListAdapter(News.class,  R.layout.vendor_news_item_list,
                RecyclerView.ViewHolder.class, childRef, getApplicationContext(), this);

        myRecycler.setAdapter(newsListAdapter);
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            newsImage = (ImageView) itemView.findViewById(R.id.newsImage);
        }
    }

    public String getListString(int position) {
        return position + " - android";
    }

    private void goToNewsDetails(ImageView image){

        Intent i = new Intent(VendorNewsListActivity.this, NewsDetailActivity.class);

        View sharedView = image;
        String transitionName = getString(R.string.blue_name);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(VendorNewsListActivity.this, sharedView, transitionName);
        ActivityCompat.startActivity(VendorNewsListActivity.this, i, transitionActivityOptions.toBundle());
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
