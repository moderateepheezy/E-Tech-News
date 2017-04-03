package com.simpumind.e_tech_news.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsDetailActivity;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.NewsPaper;

/**
 * Created by simpumind on 3/31/17.
 */

public class NewsReadAdapter extends FirebaseRecyclerAdapter<Boolean, ReadListHolder> {

    private Context context;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mChildRef;
    private String vendorName;
    private String vendorIcon;
    private String newsId;
    private String vendorId;
    private AppCompatActivity activity;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public NewsReadAdapter(Class<Boolean> modelClass, int modelLayout,
                           Class<ReadListHolder> viewHolderClass, Query ref, Context context, AppCompatActivity activity) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.activity = activity;
    }


    @Override
    protected void populateViewHolder(final ReadListHolder viewHolder, Boolean model, final int position) {


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseRef.child("news").child(getRef(position).getKey());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);

                newsId = dataSnapshot.getKey();
                vendorId = news.newspaper_id;

                viewHolder.newsTitle.setText(news.caption);
                String encodedDataString = news.getThumbnail();
                encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");
                String[] dataString = encodedDataString.split("=");

//                byte[] imageAsBytes = Base64.decode(dataString[0].getBytes(), 0);
//               viewHolder.newsImage.setImageBitmap(BitmapFactory.decodeByteArray(
//                        imageAsBytes, 0, imageAsBytes.length));


                mChildRef = FirebaseDatabase.getInstance().getReference().child("newspapers");
                mChildRef.child(news.getNewspaper_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NewsPaper newsPaper = dataSnapshot.getValue(NewsPaper.class);
                        viewHolder.vendorName.setText(newsPaper.getPaper_name());

                        vendorName = newsPaper.getPaper_name();
                        vendorIcon = newsPaper.getLogo();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, NewsDetailActivity.class);
                View sharedView = viewHolder.newsImage;
                String transitionName = context.getString(R.string.blue_name);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, transitionName);
                intent.putExtra(NewsDetailActivity.SINGLE_NEWS, getRef(position).getKey());
                intent.putExtra(NewsDetailActivity.VENDOR_NAME, vendorName);
                intent.putExtra(NewsDetailActivity.VENDOR_ICON, vendorIcon);
                intent.putExtra(NewsDetailActivity.VENDOR_ID, vendorId);
                ActivityCompat.startActivity(context, intent, transitionActivityOptions.toBundle());
            }
        });

    }
}
