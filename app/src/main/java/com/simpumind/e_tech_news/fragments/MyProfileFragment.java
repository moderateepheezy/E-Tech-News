package com.simpumind.e_tech_news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;

/**
 * Created by simpumind on 3/23/17.
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = MyProfileFragment.class.getSimpleName();


    View view;

    CollapsingToolbarLayout coll;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((NewsMainActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);

      //  Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
  //      ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        coll = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbar);


        coll.setExpandedTitleColor(Color.TRANSPARENT);
        coll.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        coll.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.vendor_news_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(getActivity(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
