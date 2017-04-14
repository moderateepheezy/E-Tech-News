package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by simpumind on 3/31/17.
 */

public class LibraryListAdapter extends FirebaseRecyclerAdapter<Boolean, LibraryListHolder>{

    private Context context;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private String vendotName;
    private String vendorIcon;
    private String vendorId;
    private StorageReference mStorage;

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
    public LibraryListAdapter(Class<Boolean> modelClass, int modelLayout,
                              Class<LibraryListHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final LibraryListHolder viewHolder, final Boolean model, final int position) {



        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("newspapers");
        mDatabaseRef.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewsPaper newsPaper = dataSnapshot.getValue(NewsPaper.class);
                viewHolder.libraryName.setText(newsPaper.getPaper_name());

                loadImage(viewHolder.libraryImage, newsPaper.getLogo(), context);

                vendotName = newsPaper.getPaper_name();
                vendorIcon = newsPaper.getLogo();
                vendorId = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorNewsListActivity.class);
                intent.putExtra(VendorNewsListActivity.NEWS_PAPER_ID, getRef(position).getKey());
                intent.putExtra(VendorNewsListActivity.VENDOR_NAME, vendotName);
                intent.putExtra(VendorNewsListActivity.VENDOR_ICON, vendorIcon);
                intent.putExtra(VendorNewsListActivity.VENDOR_ID, vendorId);
                context.startActivity(intent);
            }
        });


    }


    private void loadImage(final ImageView imageView, String imagePath, final Context context){
        mStorage = FirebaseStorage.getInstance().getReference();
        mStorage.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Picasso.with(context).load(uri.toString())
                        .fit()
                        .error(R.drawable.denews)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.denews)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(uri.toString())
                                        .fit()
                                        .error(R.drawable.denews)
                                        .placeholder(R.drawable.denews)
                                        .into(imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso","Could not fetch image");
                                            }
                                        });
                            }
                        });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failed", e.getMessage());


            }
        });
    }

}
