package com.simpumind.e_tech_news.activities;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.simpumind.e_tech_news.R;

import at.blogc.android.views.ExpandableTextView;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        CollapsingToolbarLayout coll = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        coll.setTitle("Trump News");


        coll.setExpandedTitleColor(Color.TRANSPARENT);
        coll.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        coll.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.description);
        final Button buttonToggle = (Button) this.findViewById(R.id.button_toggle);

        expandableTextView.setAnimationDuration(1000L);


        // toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                expandableTextView.toggle();
                buttonToggle.setText(expandableTextView.isExpanded() ? "Collapse" : "Expand");
            }
        });

// but, you can also do the checks yourself
        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (expandableTextView.isExpanded())
                {
                    expandableTextView.collapse();
                    buttonToggle.setText("Expand");
                }
                else
                {
                    expandableTextView.expand();
                    buttonToggle.setText("Collapse");
                }
            }
        });

// listen for expand / collapse events
        expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }
}
