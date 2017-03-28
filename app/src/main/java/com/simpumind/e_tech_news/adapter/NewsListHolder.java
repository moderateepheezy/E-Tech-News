package com.simpumind.e_tech_news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpumind.e_tech_news.R;

import org.w3c.dom.Text;

/**
 * Created by simpumind on 3/28/17.
 */

public class NewsListHolder extends RecyclerView.ViewHolder{

    TextView timeDate;
    TextView newsTitle;
    ImageView newsImage;

    public NewsListHolder(View itemView) {
        super(itemView);

        timeDate = (TextView) itemView.findViewById(R.id.dateTime);
        newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
        newsImage = (ImageView) itemView.findViewById(R.id.newsImage);
    }
}
