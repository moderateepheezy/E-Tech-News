package com.simpumind.e_tech_news.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.models.Comment;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.User;
import com.simpumind.e_tech_news.utils.EmptyRecyclerView;
import com.simpumind.e_tech_news.utils.PrefManager;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Map;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsDetailActivity extends AppCompatActivity{

    private DatabaseReference databaseReference;

    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    public static final String SINGLE_NEWS = "Single_news";
    public static final String VENDOR_NAME = "vendor_name";
    public static final String VENDOR_ICON = "vendor_icon";
    public static final String VENDOR_ID = "vendor_id";

    private DatabaseReference mDatabaseRef;
    ExpandableTextView expandableTextView;
    ImageView newsImage;
    TextView titleNews;
    TextView vendorName;
    ImageView vendorIcon;
    Button subscribe;
    ViewGroup transitionsContainer;
    TextView badgeCount;

    Button buttonToggle;

    FloatingActionButton fab;

    static String vendor_id;
    static String news_id;

    private static boolean isUserSubscribed = false;


    private DatabaseReference mDatabase;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        news_id  = intent.getStringExtra(SINGLE_NEWS);
        vendor_id = intent.getStringExtra(VENDOR_ID);



        toolbar.setTitleMarginStart(0);

        final ImageView logo = (ImageView) toolbar.findViewById(R.id.logo);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("news");
        mDatabaseRef.child(news_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News n = dataSnapshot.getValue(News.class);
                updateViews(n, logo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        newsImage = (ImageView) findViewById(R.id.toolbarImage);

        vendorIcon = (ImageView) findViewById(R.id.vendorIcon);

        titleNews = (TextView) findViewById(R.id.titleNews);

        vendorName = (TextView) findViewById(R.id.vendorName);

        badgeCount = (TextView) findViewById(R.id.badgeCount);

         time = (TextView) findViewById(R.id.time);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(isUserSubscribed) {
                    onShowPopup(v);
                }else{
                    Toast.makeText(NewsDetailActivity.this, "You cannot comment as you are not subscribed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        subscribe = (Button) findViewById(R.id.subscribe);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        expandableTextView = (ExpandableTextView) this.findViewById(R.id.description);
        buttonToggle = (Button) this.findViewById(R.id.button_toggle);

        commentCount();

        vendor_id  = intent.getStringExtra(VENDOR_ID);

        String userId = PrefManager.readUserKey(getApplicationContext());
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(userId).child("susbscriptions").child(vendor_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendor_id) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    expandableTextView.expand();
                    buttonToggle.setVisibility(View.INVISIBLE);
                    TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.button_back_color));
                    subscribe.setBackground(getResources().getDrawable(R.drawable.round_corner));
                    subscribe.setText(getResources().getString(R.string.unsubscribe));

                    readByUser(news_id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        expandableTextView.setAnimationDuration(1000L);


        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (!isUserSubscribed)
                {

                    Toast.makeText(NewsDetailActivity.this, "You are not subscribed to this vendor", Toast.LENGTH_LONG).show();
                }
                else
                {
                    expandableTextView.expand();
                    buttonToggle.setVisibility(View.GONE);
                }
            }
        });


        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isUserSubscribed){
                    isUserSubscribed = false;
                    expandableTextView.collapse();
                    buttonToggle.setVisibility(View.VISIBLE);

                    TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                    subscribe.setBackground(getResources().getDrawable(R.drawable.round_corner));
                    subscribe.setText(getResources().getString(R.string.subscribe));

                    unSubscribeVendor(vendor_id);

                }else {

                    CharSequence[] x =  {"Daily","Weekly","Monthly"};
                    final CharSequence[] y = {"Direct Billing","Mobile Money"};



                    new MaterialDialog.Builder(NewsDetailActivity.this)
                            .theme(Theme.LIGHT)
                            .title("Choose one")
                            .items(x)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {

                                    new MaterialDialog.Builder(NewsDetailActivity.this)
                                            .theme(Theme.LIGHT)
                                            .title("Choose one")
                                            .items(y)
                                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                                @Override
                                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {
                                                    isUserSubscribed = true;
                                                    expandableTextView.expand();
                                                    buttonToggle.setVisibility(View.GONE);

                                                    readByUser(news_id);

                                                    subscribeVendor(vendor_id);
                                                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.button_back_color));
                                                    subscribe.setText(getResources().getString(R.string.unsubscribe));
                                                    subscribeVendor(vendor_id);
                                                    return true; // allow selection
                                                }
                                            })
                                            .positiveText("Ok")
                                            .show();


                                    return true; // allow selection
                                }
                            })
                            .positiveText("Ok")
                            .show();

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

    private void updateViews(News news, ImageView imageView){

        Intent intent = getIntent();

        if(intent.getStringExtra(VENDOR_NAME) != null){
            String vendName = intent.getStringExtra(VENDOR_NAME);
            String vendIcon = intent.getStringExtra(VENDOR_ICON);

            vendorName.setText(vendName);

            loadImage(imageView, vendIcon, getApplicationContext());

             loadImage(vendorIcon, vendIcon, getApplicationContext());
        }

        expandableTextView.setText(Html.fromHtml(news.content));

        titleNews.setText(news.caption);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = preferences.getString("lang", "en");
        Locale LocaleBylanguageTag = Locale.forLanguageTag(lang);
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();

        String text = TimeAgo.using(news.getCreated_on(), messages);
        time.setText(text);


        loadImage(newsImage, news.getThumbnail(), getApplicationContext());

    }

    public void subscribeVendor(final String vendorId){


        Query query = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).orderByChild("susbscriptions").equalTo(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){

                }else {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("subscriber").child(PrefManager
                            .readUserKey(getApplicationContext())).child("susbscriptions");
                    mDatabase.child(vendorId).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void unSubscribeVendor(final String vendorId){
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendorId)){
                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager.readUserKey(getApplicationContext())).child("susbscriptions").child(vendorId).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void readByUser(final String newsId){



        Query query = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(getApplicationContext())).orderByChild("read_news").equalTo(newsId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){

                }else {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("subscriber").child(PrefManager
                            .readUserKey(getApplicationContext())).child("read_news");
                    mDatabase.child(newsId).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadImage(final ImageView imageView, String imagePath, final Context context){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorage)
                .fitCenter()
                .placeholder(R.drawable.denews)
                .into(imageView);
    }



    public void onShowPopup(View v){

        databaseReference = FirebaseDatabase.getInstance().getReference();

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        View inflatedView = layoutInflater.inflate(R.layout.comment_popup_layout, null,false);
        // find the ListView in the popup layout
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) inflatedView.findViewById(R.id.commentsListView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        LinearLayout headerView = (LinearLayout)inflatedView.findViewById(R.id.headerLayout);

        Button close = (Button) inflatedView.findViewById(R.id.close);

        Button sendComment = (Button) inflatedView.findViewById(R.id.send);
        final EditText writeComment = (EditText) inflatedView.findViewById(R.id.writeComment);


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (writeComment.getText().toString().isEmpty()){

                }else{
                    User user = PrefManager.getStoredUser(getApplicationContext());
                    String text = writeComment.getText().toString();
                    String name = user.getUsername().equals("") ? "Anonymous" : user.getUsername();
                    String userImage = user.getUserProfile().equals("") ? "" : user.getUserProfile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    long timeSent = timestamp.getTime();
                    Comment comment = new Comment(text, name, timeSent, userImage);

                    Map<String, Object> userValues = comment.toMap();
                    databaseReference.child("comments").child(news_id).push().setValue(userValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            }
                        }
                    });

                    writeComment.setText("");
                }
            }
        });

        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
//        mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        // fill the data to the list items
        setComment(recyclerView);


        // set height depends on the device size
        final PopupWindow popWindow = new PopupWindow(inflatedView, width,height, true );
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.staight_edge_corner));

        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
    }


    void setComment(EmptyRecyclerView recyclerView){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseRef.child("comments").child(news_id);

        FirebaseRecyclerAdapter<Comment, CommentHolder> adapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.comment_layout,
                CommentHolder.class, query) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String lang = preferences.getString("lang", "en");
                Locale LocaleBylanguageTag = Locale.forLanguageTag(lang);
                TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();

                String text = TimeAgo.using(model.getTimeSent(), messages);

                viewHolder.text.setText(model.getText());
                viewHolder.dateTime.setText(text);
                viewHolder.username.setText(model.getUsername());

                Glide.with(getApplicationContext()).
                        load(model.getUserImage())
                        .asBitmap()
                        .placeholder(R.drawable.pp)
                        .error(R.drawable.pp)
                        .into(viewHolder.userImage);

            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class CommentHolder extends RecyclerView.ViewHolder{

        TextView text;
        TextView dateTime;
        TextView username;
        CircleImageView userImage;

        public CommentHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.comment);
            dateTime = (TextView) v.findViewById(R.id.date);
            username = (TextView) v.findViewById(R.id.username);
            userImage = (CircleImageView) v.findViewById(R.id.user_image);
        }
    }

    private void commentCount(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("comments").child(news_id);

//You can use the single or the value.. depending if you want to keep track
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    badgeCount.setText(String.valueOf(dataSnapshot.getChildrenCount() + ""));
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                }else{
                    badgeCount.setText(String.valueOf(0));
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkButton();
    }

    private void checkButton(){
        String userId = PrefManager.readUserKey(getApplicationContext());
        DatabaseReference mDataRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(userId).child("susbscriptions").child(vendor_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendor_id) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    expandableTextView.expand();
                    buttonToggle.setVisibility(View.INVISIBLE);
                    TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
                    subscribe.setBackgroundTintList(getResources().getColorStateList(R.color.button_back_color));
                    subscribe.setBackground(getResources().getDrawable(R.drawable.round_corner));
                    subscribe.setText(getResources().getString(R.string.unsubscribe));

                    readByUser(news_id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
