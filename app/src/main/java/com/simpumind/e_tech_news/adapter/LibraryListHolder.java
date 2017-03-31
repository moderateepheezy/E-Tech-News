package com.simpumind.e_tech_news.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpumind.e_tech_news.R;

/**
 * Created by simpumind on 3/31/17.
 */

public class LibraryListHolder  extends RecyclerView.ViewHolder{

    ImageView libraryImage;
    TextView libraryName;


    public LibraryListHolder(View itemView) {
        super(itemView);
        libraryImage = (ImageView) itemView.findViewById(R.id.vendorIcon);
        libraryName = (TextView) itemView.findViewById(R.id.vendorName);
    }
}
