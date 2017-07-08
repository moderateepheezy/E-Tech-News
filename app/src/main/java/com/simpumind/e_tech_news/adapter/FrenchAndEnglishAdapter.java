package com.simpumind.e_tech_news.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsDetailActivity;
import com.simpumind.e_tech_news.models.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by simpumind on 7/2/17.
 */

public class FrenchAndEnglishAdapter extends RecyclerView.Adapter<NewsListHolder> {

    private HashMap<String, News> newsList;

    private String vendorName;
    private String vendorIcon;
    private String vendorId;
    private String newsKey;
    private List<String> keySet;
    private AppCompatActivity context;

    private StorageReference mStorage;

    public FrenchAndEnglishAdapter(AppCompatActivity context, HashMap<String, News> newsList, String newsKey,
                                   String vendorName, String vendorIcon, String vendorId) {
        this.newsList = newsList;
        this.context = context;
        this.vendorName = vendorName;
        this.vendorIcon = vendorIcon;
        this.vendorId = vendorId;
        this.newsKey = newsKey;
        this.keySet = new ArrayList<>(newsList.keySet());
    }

    @Override
    public NewsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_news_item_list, null);
        NewsListHolder viewHolder = new NewsListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsListHolder holder, final int position) {
        News news  = newsList.get(position);

        if(news.status) {
            holder.itemView.setVisibility(View.VISIBLE);
        }else{
            holder.itemView.setVisibility(View.GONE);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = preferences.getString("lang", "fr");

        if (lang.equals("fr")){
            if(!news.getCaption().getFrench().equals("")) {
                holder.newsTitle.setText(news.getCaption().getFrench());
            }else{
                holder.newsTitle.setText(news.getCaption().getEnglish());
            }
        }else{
            if (!news.getCaption().getEnglish().equals("")) {
                holder.newsTitle.setText(news.getCaption().getEnglish());
            }else {
                holder.newsTitle.setText(news.getCaption().getFrench());
            }
        }
        holder.vendorName.setText(vendorName);

        commentCount(holder, position);
        Locale LocaleBylanguageTag = Locale.forLanguageTag(lang);
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();

        String text = TimeAgo.using(news.getCreated_on(), messages);

        holder.timeDate.setText(text);


        loadImage(holder.newsImage, news.getThumbnail(), context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                View sharedView = holder.newsImage;
                String transitionName = context.getString(R.string.blue_name);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(context, sharedView, transitionName);
                intent.putExtra(NewsDetailActivity.SINGLE_NEWS, keySet.get(position));
                intent.putExtra(NewsDetailActivity.VENDOR_NAME, vendorName);
                intent.putExtra(NewsDetailActivity.VENDOR_ICON, vendorIcon);
                intent.putExtra(NewsDetailActivity.VENDOR_ID, vendorId);
                ActivityCompat.startActivity(context, intent, transitionActivityOptions.toBundle());
            }
        });

    }

    private void commentCount(final NewsListHolder holder, int position){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("comments").child(keySet.get(position));

//You can use the single or the value.. depending if you want to keep track
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    holder.commentCount.setText(String.valueOf(dataSnapshot.getChildrenCount() + ""));
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                }else{
                    holder.commentCount.setText(String.valueOf(0));
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadImage(final ImageView imageView, String imagePath, final Context context){
        mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorage)
                .fitCenter()
                .placeholder(R.drawable.denews)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (null != newsList ? newsList.size() : 0);
    }
}
