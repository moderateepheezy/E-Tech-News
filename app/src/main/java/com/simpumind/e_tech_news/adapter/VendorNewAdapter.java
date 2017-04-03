package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.simpumind.e_tech_news.fragments.SubMethodFragment;
import com.simpumind.e_tech_news.fragments.SubscribeChoiceFragment;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.simpumind.e_tech_news.utils.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 3/26/17.
 */

public class VendorNewAdapter extends FirebaseRecyclerAdapter<NewsPaper, NewsPaperHolder>{


    private static final java.lang.String CHOICE_DIALOG = "dialogTagChoice";

    private  boolean isColorsInverted = false;
    private Context context;
    private AppCompatActivity activity;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private DatabaseReference childRef;
    private DatabaseReference mDataRef;

    private static boolean isUserSubscribed = false;
    FirebaseAuth mAuth;

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
    protected void populateViewHolder(final NewsPaperHolder viewHolder, final NewsPaper model, final int position) {

        viewHolder.vendorName.setText(model.getPaper_name());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("news");

        final String oneChildRef = getRef(position).getKey();

        mDataRef = FirebaseDatabase.getInstance().getReference();
        childRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(context)).child("susbscriptions").child(oneChildRef);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(getRef(position).getKey()) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.button_back_color));
                    viewHolder.subscribe.setBackground(context.getResources().getDrawable(R.drawable.round_corner));
                    viewHolder.subscribe.setText("Unsubscribe");
                }else {
                    isUserSubscribed = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseRef.orderByChild("newspaper_id")
                .equalTo(oneChildRef).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            News n = childSnapshot.getValue(News.class);

                            updateUI(viewHolder, n);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("vmfmdcv", getRef(position).getKey());
                Toast.makeText(context, "id /n" + getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, VendorNewsListActivity.class);
                intent.putExtra(VendorNewsListActivity.NEWS_PAPER_ID, getRef(position).getKey());
                intent.putExtra(VendorNewsListActivity.VENDOR_NAME, model.getPaper_name());
                intent.putExtra(VendorNewsListActivity.VENDOR_ICON, model.getLogo());
                intent.putExtra(VendorNewsListActivity.VENDOR_ID, getRef(position).getKey());
                context.startActivity(intent);
            }
        });

        String encodedDataString = model.getLogo();
        encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");

//        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
//        viewHolder.vendorIcon.setImageBitmap(BitmapFactory.decodeByteArray(
//                imageAsBytes, 0, imageAsBytes.length));

        viewHolder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isColorsInverted = !isColorsInverted;

                if(isUserSubscribed){
                    isUserSubscribed = false;
                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorAccent));
                    viewHolder.subscribe.setText("Subscribe");
                    unSubscribeVendor(getRef(position).getKey());
                }else {
                    isUserSubscribed = true;
                    SubscribeChoiceFragment dialog = new SubscribeChoiceFragment();
                    dialog.show(activity.getFragmentManager(), CHOICE_DIALOG);
                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.button_back_color));
                    viewHolder.subscribe.setText("Unsubscribe");
                    subscribeVendor(getRef(position).getKey());
                }

            }
        });

//        byte[] imageByteArray = Base64.decode(model.getLogo(), Base64.DEFAULT);
//        Glide.with(context).
//                load(imageByteArray)
//                .asBitmap()
//                .into(viewHolder.vendorIcon);
    }


    public void updateUI(NewsPaperHolder holder, News news){

        holder.firstNewsTitle.setText(news.getCaption());

        //String encodedDataString = news.getThumbnail();
        //encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");

       // byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
        //holder.firstNewsImage.setImageBitmap(BitmapFactory.decodeByteArray(
         //       imageAsBytes, 0, imageAsBytes.length));


//        byte[] imageByteArray = Base64.decode(news.getThumbnail(), Base64.DEFAULT);
//        Glide.with(context).
//                load(imageByteArray)
//                .asBitmap()
//                .into(holder.firstNewsImage);

    }

    public void subscribeVendor(final String vendorId){
        mAuth = FirebaseAuth.getInstance();



        Query query = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(context)).orderByChild("susbscriptions").equalTo(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){

                }else {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("subscriber").child(PrefManager
                            .readUserKey(context)).child("susbscriptions");
                    mDatabase.child(vendorId).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void unSubscribeVendor(final String vendorId){
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(context)).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendorId)){
                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager.readUserKey(context)).child("susbscriptions").child(vendorId).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
