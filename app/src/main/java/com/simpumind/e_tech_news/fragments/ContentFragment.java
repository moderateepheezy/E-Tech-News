package com.simpumind.e_tech_news.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simpumind.e_tech_news.R;


public class ContentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private int mParam1;

    public ContentFragment() {
        // Required empty public constructor
    }

    public static ContentFragment newInstance(int param1) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.indicator_fragment, container, false);;
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(mParam1);
        return view;
    }

    public int getmParam1() {
        return mParam1;
    }

}
