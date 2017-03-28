package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsDetailActivity;
import com.simpumind.e_tech_news.activities.NewsMainActivity;
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.fragments.SubscribeChoiceFragment;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 3/26/17.
 */

public class VendorNewAdapter extends FirebaseRecyclerAdapter<NewsPaper, NewsPaperHolder>{



    private  boolean isColorsInverted = false;
    private Context context;
    private AppCompatActivity activity;

    private DatabaseReference mDatabaseRef;
    private Query oneQuery;
    private DatabaseReference childRef;


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
    public VendorNewAdapter(Class<NewsPaper> modelClass, int modelLayout,
                            Class<NewsPaperHolder> viewHolderClass, Query ref, AppCompatActivity activity,
                            boolean isViewWithList, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this. activity = activity;
        this.isColorsInverted = isViewWithList;
    }


    @Override
    protected void populateViewHolder(final NewsPaperHolder viewHolder, NewsPaper model, int position) {

        viewHolder.vendorName.setText(model.getPaper_name());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

       String oneChildRef = getRef(position).getKey();

        Log.d("fgmmddfmm", getRef(position).getKey());

        mDatabaseRef.child("news").orderByChild("newspaper_id")
                //.equalTo(oneChildRef)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Datasnapshot",dataSnapshot.getValue().toString());

//                for (DataSnapshot newsDataSnapshot : dataSnapshot.getChildren()) {
////                    News news = newsDataSnapshot.getValue(News.class);
//                    News news = newsDataSnapshot.getValue(News.class);
//                }
                  //  News news = dataSnapshot.getValue(News.class);
               // updateUI(viewHolder, news);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Picasso.with(context).load(model.getLogo()).into(viewHolder.vendorIcon);
    }


    public void updateUI(NewsPaperHolder holder, News news){

        holder.firstNewsTitle.setText(news.getCaption());
        Picasso.with(context).load(news.getThumbnail()).into(holder.firstNewsImage);

    }

}
