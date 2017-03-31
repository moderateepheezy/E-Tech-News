package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.models.NewsPaper;

/**
 * Created by simpumind on 3/31/17.
 */

public class LibraryListAdapter extends FirebaseRecyclerAdapter<String, LibraryListHolder>{

    private Context context;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private String vendotName;
    private String vendorIcon;

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
    public LibraryListAdapter(Class<String> modelClass, int modelLayout,
                              Class<LibraryListHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final LibraryListHolder viewHolder, final String model, int position) {



        Log.d("ggmgmgmg", model);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("newspapers");
        mDatabaseRef.child(model).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewsPaper newsPaper = dataSnapshot.getValue(NewsPaper.class);
                viewHolder.libraryName.setText(newsPaper.getPaper_name());
                String encodedDataString = newsPaper.getLogo();
                encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");

//                byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
//                viewHolder.libraryImage.setImageBitmap(BitmapFactory.decodeByteArray(
//                        imageAsBytes, 0, imageAsBytes.length));

                vendotName = newsPaper.getPaper_name();
                vendorIcon = newsPaper.getLogo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorNewsListActivity.class);
                intent.putExtra(VendorNewsListActivity.NEWS_PAPER_ID, model);
                intent.putExtra(VendorNewsListActivity.VENDOR_NAME, vendotName);
                intent.putExtra(VendorNewsListActivity.VENDOR_ICON, vendorIcon);
                context.startActivity(intent);
            }
        });


    }

}
