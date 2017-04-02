package com.simpumind.e_tech_news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpumind.e_tech_news.R;

/**
 * Created by simpumind on 3/28/17.
 */


public class NewsPaperHolder extends RecyclerView.ViewHolder {
    public ImageView vendorIcon;
    public Button subscribe;
    public TextView vendorName;
    public TextView firstNewsTitle;
    ViewGroup transitionsContainer;
    Button sub;


    public NewsPaperHolder(View view) {
        super(view);
        firstNewsTitle = (TextView) view.findViewById(R.id.firstNewsTitle);
        vendorName = (TextView) view.findViewById(R.id.vendorName);
        vendorIcon = (ImageView) view.findViewById(R.id.vendorIcon);
        subscribe = (Button) view.findViewById(R.id.subscribe);
        transitionsContainer = (ViewGroup) itemView.findViewById(R.id.transitions_container);

    }
}

