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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.adapter.SimpleAdapter;

import static android.R.attr.button;

/**
 * Created by simpumind on 3/23/17.
 */

public class SubscriptionFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = SubscriptionFragment.class.getSimpleName();

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    SimpleAdapter simpleAdapter;

    RecyclerView recyclerView;

    boolean isViewWithList = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Vendors");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vendor_fragment, container, false);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.getItemAnimator().setChangeDuration(700);
        simpleAdapter = new SimpleAdapter((AppCompatActivity) getActivity(), true);
        recyclerView.setAdapter(simpleAdapter);
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
                    simpleAdapter = new SimpleAdapter((AppCompatActivity) getActivity(), true);
                    recyclerView.setAdapter(simpleAdapter);
                    gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);

                } else {
                    item.setIcon(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_list_to_grid));
                    recyclerView.getItemAnimator().setChangeDuration(700);
                    simpleAdapter = new SimpleAdapter((AppCompatActivity) getActivity(), false);
                    recyclerView.setAdapter(simpleAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                ((Animatable) item.getIcon()).start();
                simpleAdapter.notifyItemRangeChanged(0, simpleAdapter.getItemCount());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
