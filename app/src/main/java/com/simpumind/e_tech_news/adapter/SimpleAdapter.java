package com.simpumind.e_tech_news.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.VendorNewsListActivity;
import com.simpumind.e_tech_news.fragments.SubscribeChoiceFragment;



public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleItemViewHolder>{

    private static final java.lang.String CHOICE_DIALOG = "dialogTagChoice";
    private boolean isViewWithList = true;
    private  boolean isColorsInverted = false;
    private Context context;
    private AppCompatActivity activity;

    public SimpleAdapter(AppCompatActivity activity, boolean isViewWithList) {
        this.activity = activity;
        this.isViewWithList = isViewWithList;
    }


    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isViewWithList ? R.layout.vendor_item_card : R.layout.vendor_grid_item_card, parent, false);

        context = parent.getContext();

        return new SimpleItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, int position) {

        if(!isViewWithList) {

            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isColorsInverted = !isColorsInverted;
                    TransitionManager.beginDelayedTransition(holder.transitionsContainer, new AutoTransition());
                    holder.sub.setBackgroundTintList(isColorsInverted ? context.getResources().getColorStateList(R.color.button_back_color) : context.getResources().getColorStateList(R.color.colorAccent));
                    holder.sub.setBackground(context.getResources().getDrawable(R.drawable.round_corner));
                    holder.sub.setText(isColorsInverted ? "Subscribed" : "Subscribe");

                    SubscribeChoiceFragment dialog = new SubscribeChoiceFragment();

                    dialog.show(activity.getFragmentManager(), CHOICE_DIALOG);
                }
            });

        }else{
            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isColorsInverted = !isColorsInverted;
                    TransitionManager.beginDelayedTransition(holder.transitionsContainer, new AutoTransition());
                    holder.sub.setBackgroundTintList(isColorsInverted ? context.getResources().getColorStateList(R.color.button_back_color) : context.getResources().getColorStateList(R.color.pressedColor));
                    holder.sub.setText(isColorsInverted ? "Subscribed" : "Subscribe");

                    SubscribeChoiceFragment dialog = new SubscribeChoiceFragment();

                    dialog.show(activity.getFragmentManager(), CHOICE_DIALOG);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class SimpleItemViewHolder extends RecyclerView.ViewHolder {

        Button sub;

        ImageView newsImage;

        ViewGroup transitionsContainer;
        SimpleItemViewHolder(View itemView) {
            super(itemView);

            transitionsContainer = (ViewGroup) itemView.findViewById(R.id.transitions_container);
            sub = (Button) transitionsContainer.findViewById(R.id.subscribe);

            newsImage = (ImageView) itemView.findViewById(R.id.newsImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VendorNewsListActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}