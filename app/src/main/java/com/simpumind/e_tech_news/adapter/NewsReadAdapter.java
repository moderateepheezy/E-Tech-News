package com.simpumind.e_tech_news.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsDetailActivity;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.simpumind.e_tech_news.utils.PrefManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

                if (dataSnapshot.getValue() == null) {

                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager.readUserKey(context)).child("read_news").child(getRef(position).getKey()).removeValue();

                }else{

                News news = dataSnapshot.getValue(News.class);

                if(news != null) {
                    newsId = dataSnapshot.getKey();
                    vendorId = news.getNewspaper_id();

                    viewHolder.newsTitle.setText(news.getCaption().getEnglish());

                    loadImage(viewHolder.newsImage, news.getThumbnail(), context);


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
                }
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

    private void loadImage(final ImageView imageView, String imagePath, final Context context){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorage)
                .fitCenter()
                .placeholder(R.drawable.denews)
                .into(imageView);


    }
}
