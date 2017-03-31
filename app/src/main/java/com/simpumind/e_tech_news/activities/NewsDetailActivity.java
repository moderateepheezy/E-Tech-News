package com.simpumind.e_tech_news.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.models.News;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    public static final String SINGLE_NEWS = "Single_news";

    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
     ExpandableTextView expandableTextView;
    ImageView newsImage;
    TextView titleNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");


//        Intent intent = getIntent();
//        String news_id  = intent.getStringExtra(SINGLE_NEWS);
//
//        Log.d("fdmfmdmdfdc", news_id);
//
////        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("news");
////        mDatabaseRef.child(news_id).addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                News n = dataSnapshot.getValue(News.class);
////                Log.d("dmfmdmdmf", n.content);
////                //updateViews(n);
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//
//
//        newsImage = (ImageView) findViewById(R.id.toolbarImage);
//
//        titleNews = (TextView) findViewById(R.id.titleNews);
////
////        coll = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
////
////
////        coll.setExpandedTitleColor(Color.TRANSPARENT);
////        coll.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
////        coll.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//
//        expandableTextView = (ExpandableTextView) this.findViewById(R.id.description);
//        final Button buttonToggle = (Button) this.findViewById(R.id.button_toggle);
//
//        expandableTextView.setAnimationDuration(1000L);
//
//
//        // toggle the ExpandableTextView
//        buttonToggle.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(final View v)
//            {
//                expandableTextView.toggle();
//                buttonToggle.setText(expandableTextView.isExpanded() ? "Collapse" : "Expand");
//            }
//        });
//
//// but, you can also do the checks yourself
//        buttonToggle.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(final View v)
//            {
//                if (expandableTextView.isExpanded())
//                {
//                    expandableTextView.collapse();
//                    buttonToggle.setText("Expand");
//                }
//                else
//                {
//                    expandableTextView.expand();
//                    buttonToggle.setText("Collapse");
//                }
//            }
//        });
//
//// listen for expand / collapse events
//        expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener()
//        {
//            @Override
//            public void onExpand(final ExpandableTextView view)
//            {
//                Log.d(TAG, "ExpandableTextView expanded");
//            }
//
//            @Override
//            public void onCollapse(final ExpandableTextView view)
//            {
//                Log.d(TAG, "ExpandableTextView collapsed");
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

//    private void updateViews(News news){
//
//
//        coll.setTitle(news.caption);
//        expandableTextView.setText(news.content);
//
//        titleNews.setText(news.caption);
//
//        String encodedDataString = news.getThumbnail();
//        encodedDataString = encodedDataString.replace("data:image/jpeg;base64,","");
//
//        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
//        newsImage.setImageBitmap(BitmapFactory.decodeByteArray(
//                imageAsBytes, 0, imageAsBytes.length));
//
//    }
}
