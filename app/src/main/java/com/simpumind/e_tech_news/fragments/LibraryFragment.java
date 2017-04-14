package com.simpumind.e_tech_news.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;
import com.simpumind.e_tech_news.adapter.CustomAdapter;
import com.simpumind.e_tech_news.adapter.LibraryListAdapter;
import com.simpumind.e_tech_news.adapter.LibraryListHolder;
import com.simpumind.e_tech_news.models.MyList;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.simpumind.e_tech_news.utils.EmptyRecyclerView;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 3/23/17.
 */

public class LibraryFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = LibraryFragment.class.getSimpleName();

    private EmptyRecyclerView recyclerView;
    private EmptyRecyclerView.LayoutManager layoutManager;
    private EmptyRecyclerView.Adapter adapter;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;

    private List<MyList> list;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NewsMainActivity) getActivity()).getSupportActionBar().show();
        getActivity().setTitle("Library");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.library_fragment, container, false);

        setHasOptionsMenu(true);

        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        list = new ArrayList<>();

        loadRecyclerViewItem();

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getActivity(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadRecyclerViewItem() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(getActivity())).child("susbscriptions");

        adapter = new LibraryListAdapter(Boolean.class, R.layout.library_list_item,
                LibraryListHolder.class, childRef, getContext());
        recyclerView.setAdapter(adapter);
    }
}
