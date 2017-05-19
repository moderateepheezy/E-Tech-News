package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.models.News;
import com.simpumind.e_tech_news.models.NewsPaper;
import com.simpumind.e_tech_news.utils.Const;
import com.simpumind.e_tech_news.utils.PrefManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


/**
 * Created by simpumind on 3/26/17.
 */

public class VendorNewAdapter extends FirebaseRecyclerAdapter<NewsPaper, NewsPaperHolder>{


    private Context context;
    private AppCompatActivity activity;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabase;
    private DatabaseReference childRef;
    private DatabaseReference mDataRef;
    private StorageReference mStorage;

    private Query mRef;
    private Class<NewsPaper> mModelClass;
    private int mLayout;
    private LayoutInflater mInflater;
    private ChildEventListener mListener;

    List<NewsPaper> newsPaperList;

    private List<NewsPaper> posts = new ArrayList<>();
    private List<NewsPaper> postsCopy= new ArrayList<>();
    private List<String> mKeys;

    private static boolean isUserSubscribed = false;
    FirebaseAuth mAuth;

    static ViewGroup view;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public VendorNewAdapter(Class<NewsPaper> modelClass, int modelLayout,
                            Class<NewsPaperHolder> viewHolderClass, Query ref, AppCompatActivity activity,
                            boolean isViewWithList, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this. activity = activity;

        this.mRef = ref;
        this.mModelClass = modelClass;
        this.mLayout = modelLayout;
        mInflater = activity.getLayoutInflater();
        posts = new ArrayList<>();
        mKeys = new ArrayList<>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                NewsPaper model = dataSnapshot.getValue(VendorNewAdapter.this.mModelClass);
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    posts.add(0, model);
                    postsCopy.add(0, model);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == posts.size()) {
                        posts.add(model);
                        postsCopy.add(model);
                        mKeys.add(key);
                    } else {
                        posts.add(nextIndex, model);
                        postsCopy.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the posts changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                NewsPaper newModel = dataSnapshot.getValue(VendorNewAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                posts.set(index, newModel);
                postsCopy.set(index, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                posts.remove(index);
                postsCopy.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                NewsPaper newModel = dataSnapshot.getValue(VendorNewAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                posts.remove(index);
                postsCopy.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    posts.add(0, newModel);
                    postsCopy.add(0,newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == posts.size()) {
                        posts.add(newModel);
                        postsCopy.add(newModel);
                        mKeys.add(key);
                    } else {
                        posts.add(nextIndex, newModel);
                        postsCopy.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    @Override
    public NewsPaperHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void populateViewHolder(final NewsPaperHolder viewHolder, final NewsPaper model, final int position) {

        viewHolder.vendorName.setText(changeFont(model.getPaper_name()), TextView.BufferType.SPANNABLE);

        //Const.loadImage(model.getLogo(), true, context, viewHolder.vendorIcon, model.getPaper_name());
        loadImage(viewHolder.vendorIcon, model.getLogo(), context);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("news");

        final String oneChildRef = getRef(position).getKey();

        mDataRef = FirebaseDatabase.getInstance().getReference();
        childRef = FirebaseDatabase.getInstance().getReference();
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(context)).child("susbscriptions").child(oneChildRef);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(getRef(position).getKey()) && dataSnapshot.getValue() != null){
                    isUserSubscribed = true;
                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.button_back_color));
                    viewHolder.subscribe.setBackground(context.getResources().getDrawable(R.drawable.round_corner));
                    viewHolder.subscribe.setText(context.getResources().getString(R.string.unsubscribe));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseRef.orderByChild("newspaper_id")
                .equalTo(oneChildRef).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            News n = childSnapshot.getValue(News.class);

                            updateUI(viewHolder, n);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorNewsListActivity.class);
                intent.putExtra(VendorNewsListActivity.NEWS_PAPER_ID, getRef(position).getKey());
                intent.putExtra(VendorNewsListActivity.VENDOR_NAME, model.getPaper_name());
                intent.putExtra(VendorNewsListActivity.VENDOR_ICON, model.getLogo());
                intent.putExtra(VendorNewsListActivity.VENDOR_ID, getRef(position).getKey());
                context.startActivity(intent);
            }
        });


        viewHolder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isUserSubscribed){
                    isUserSubscribed = false;
                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorAccent));
                    viewHolder.subscribe.setText(context.getResources().getString(R.string.subscribe));
                    unSubscribeVendor(getRef(position).getKey());
                }else {

                    CharSequence[] x =  {"Daily","Weekly","Monthly"};
                    final CharSequence[] y = {"Direct Billing","Mobile Money"};



                    new MaterialDialog.Builder(activity)
                            .theme(Theme.LIGHT)
                            .title("Choose one")
                            .items(x)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {

                                    new MaterialDialog.Builder(activity)
                                            .theme(Theme.LIGHT)
                                            .title("Choose one")
                                            .items(y)
                                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                                @Override
                                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence                   text) {
                                                    isUserSubscribed = true;
                                                    TransitionManager.beginDelayedTransition(viewHolder.transitionsContainer, new AutoTransition());
                                                    viewHolder.subscribe.setBackgroundTintList(context.getResources().getColorStateList(R.color.button_back_color));
                                                    viewHolder.subscribe.setText(context.getResources().getString(R.string.unsubscribe));
                                                    subscribeVendor(getRef(position).getKey());
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

    public void setList(List<NewsPaper> list) {
         newsPaperList = list;
    }
    

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the posts
        mRef.removeEventListener(mListener);
        posts.clear();
        postsCopy.clear();
        mKeys.clear();
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public NewsPaper getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void filter(String text) {
        posts.clear();
        if(text.isEmpty()){
            posts.addAll(postsCopy);
        } else{
            text = text.toLowerCase();
            for(NewsPaper post : postsCopy){
                if(post.paper_name.toLowerCase().contains(text)){
                    posts.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }



    public void updateUI(NewsPaperHolder holder, News news){

        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append("Latest: ") // Bold this
                .append(news.getCaption()); // Default TextView font.
// Create the Typeface you want to apply to certain text
        //CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/Montserrat-Bold.ttf"));
// Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
        CalligraphyTypefaceSpan typefaceSpan2 = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/Montserrat-Regular.ttf"));
        //sBuilder.setSpan(typefaceSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.setSpan(typefaceSpan2, 7, news.getCaption().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.firstNewsTitle.setText(sBuilder, TextView.BufferType.SPANNABLE);

    }


    public SpannableStringBuilder changeFont(String text){
        SpannableStringBuilder sBuilder = new SpannableStringBuilder() // Bold this
                .append(text); // Default TextView font.
// Create the Typeface you want to apply to certain text
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/BodoniFLF-Bold.ttf"));
// Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
        sBuilder.setSpan(typefaceSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sBuilder;
    }

    public void subscribeVendor(final String vendorId){
        mAuth = FirebaseAuth.getInstance();



        Query query = mDatabaseRef.child("subscriber").child(PrefManager.readUserKey(context)).orderByChild("susbscriptions").equalTo(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){

                }else {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("subscriber").child(PrefManager
                            .readUserKey(context)).child("susbscriptions");
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
        Query query = mDataRef.child("subscriber").child(PrefManager.readUserKey(context)).child("susbscriptions").child(vendorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(vendorId)){
                    FirebaseDatabase.getInstance().getReference().child("subscriber")
                            .child(PrefManager.readUserKey(context)).child("susbscriptions").child(vendorId).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadImage(final ImageView imageView, String imagePath, final Context context){
        mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorage)
                .fitCenter()
                .placeholder(R.drawable.denews)
                .into(imageView);
    }


}
