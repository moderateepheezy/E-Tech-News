package com.simpumind.e_tech_news.fragments;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;
import com.simpumind.e_tech_news.adapter.NewsPaperHolder;
import com.simpumind.e_tech_news.adapter.SimpleAdapter;
import com.simpumind.e_tech_news.adapter.VendorNewAdapter;
import com.simpumind.e_tech_news.models.NewsPaper;

import static android.R.attr.button;

/**
 * Created by simpumind on 3/23/17.
 */

public class SubscriptionFragment extends Fragment implements View.OnClickListener{

    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;

    private static final String TAG = SubscriptionFragment.class.getSimpleName();

    GridLayoutManager gridLayoutManager;
    //SimpleAdapter simpleAdapter;

    RecyclerView recyclerView;

    VendorNewAdapter vendorNewAdapter;

    boolean isViewWithList = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NewsMainActivity) getActivity()).getSupportActionBar().show();
        getActivity().setTitle("Vendors");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vendor_fragment, container, false);

        setHasOptionsMenu(true);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.getItemAnimator().setChangeDuration(700);
        //simpleAdapter = new SimpleAdapter((AppCompatActivity) getActivity(), true);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("newspapers");
        Log.d("chilref", childRef.getKey());

        //isViewWithList ? R.layout.vendor_grid_item_card : R.l

        vendorNewAdapter = new VendorNewAdapter(NewsPaper.class,  R.layout.vendor_item_card,
                NewsPaperHolder.class, childRef, (AppCompatActivity) getActivity(), true, getActivity());

        recyclerView.setAdapter(vendorNewAdapter);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.vendor_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_list_to_grid) {
            isViewWithList = !isViewWithList;
            if (!((Animatable) item.getIcon()).isRunning()) {
                if (isViewWithList) {
                    item.setIcon(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_grid_to_list));
                    recyclerView.getItemAnimator().setChangeDuration(700);
                    vendorNewAdapter = new VendorNewAdapter(NewsPaper.class,  R.layout.vendor_item_card,
                            NewsPaperHolder.class, childRef, (AppCompatActivity) getActivity(), true, getActivity());
                    recyclerView.setAdapter(vendorNewAdapter);
                    gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);

                } else {
                    item.setIcon(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_list_to_grid));
                    recyclerView.getItemAnimator().setChangeDuration(700);
                    vendorNewAdapter = new VendorNewAdapter(NewsPaper.class,  R.layout.vendor_grid_item_card,
                            NewsPaperHolder.class, childRef, (AppCompatActivity) getActivity(), false, getActivity());

                    recyclerView.setAdapter(vendorNewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                ((Animatable) item.getIcon()).start();
                vendorNewAdapter.notifyItemRangeChanged(0, vendorNewAdapter.getItemCount());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
