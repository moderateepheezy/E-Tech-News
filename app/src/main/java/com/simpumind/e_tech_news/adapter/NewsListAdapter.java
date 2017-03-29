package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsDetailActivity;
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.models.News;
import com.squareup.picasso.Picasso;

/**
 * Created by simpumind on 3/28/17.
 */

public class NewsListAdapter extends FirebaseRecyclerAdapter<News, RecyclerView.ViewHolder> {

    private Context context;
    private AppCompatActivity activity;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String vendorName;

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
    public  NewsListAdapter(Class<News> modelClass, int modelLayout, Class<RecyclerView.ViewHolder> viewHolderClass,
                            Query ref, Context context, AppCompatActivity activity, String vendorName) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.activity = activity;
        this.vendorName = vendorName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return super.onCreateViewHolder(parent, viewType);


        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_header, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if(viewType == TYPE_ITEM){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
            return new NewsListHolder(layoutView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
           // return TYPE_HEADER;
        }

        return TYPE_ITEM;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final News model = getItem(position);

        if(viewHolder instanceof HeaderViewHolder){
            ((HeaderViewHolder) viewHolder).title.setText(model.getCaption());
            //((HeaderViewHolder) viewHolder).dateTime.setText(model.getime());
            //Picasso.with(context).load(model.getThumbnail()).into(((HeaderViewHolder) viewHolder).imageView2);


            String encodedDataString = model.getThumbnail();
            encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");

            byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
            ((HeaderViewHolder) viewHolder).imageView2.setImageBitmap(BitmapFactory.decodeByteArray(
                    imageAsBytes, 0, imageAsBytes.length));
        }else {

            ((NewsListHolder) viewHolder).newsTitle.setText(model.getCaption());
            //Picasso.with(context).load(model.getThumbnail()).into(((NewsListHolder) viewHolder).newsImage);
            ((NewsListHolder) viewHolder).vendorName.setText(vendorName);

            String encodedDataString = model.getThumbnail();
            encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");

            byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
            ((NewsListHolder) viewHolder).newsImage.setImageBitmap(BitmapFactory.decodeByteArray(
                    imageAsBytes, 0, imageAsBytes.length));

            ((NewsListHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra(NewsDetailActivity.SINGLE_NEWS, getRef(position).getKey());
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, final News model, final int position) {


    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView2;
        public TextView title;
        public TextView dateTime;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);

            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }
}
