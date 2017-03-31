package com.simpumind.e_tech_news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpumind.e_tech_news.R;

/**
 * Created by simpumind on 3/31/17.
 */

public class ReadListHolder extends RecyclerView.ViewHolder{

    ImageView newsImage;
    TextView newsTitle;
    ImageView vendorIcon;
    TextView vendorName;


    public ReadListHolder(View itemView) {
        super(itemView);
        newsImage = (ImageView) itemView.findViewById(R.id.newsImage);
        newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
        vendorIcon = (ImageView) itemView.findViewById(R.id.vendorIcon);
        vendorName = (TextView) itemView.findViewById(R.id.vendorName);
    }
}