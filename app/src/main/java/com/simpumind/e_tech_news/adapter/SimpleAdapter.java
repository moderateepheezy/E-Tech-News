package com.simpumind.e_tech_news.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simpumind.e_tech_news.R;


public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>{

    private boolean isViewWithList = true;
    private  boolean isColorsInverted = false;
    private Context context;

    public SimpleAdapter(boolean isViewWithList) {

        this.isViewWithList = isViewWithList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isViewWithList ? R.layout.vendor_item_card : R.layout.vendor_grid_item_card, parent, false);

        context = parent.getContext();

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {

        if(!isViewWithList) {

            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isColorsInverted = !isColorsInverted;
                    TransitionManager.beginDelayedTransition(holder.transitionsContainer, new AutoTransition());
                    holder.sub.setBackgroundTintList(isColorsInverted ? context.getResources().getColorStateList(R.color.button_back_color) : context.getResources().getColorStateList(R.color.colorAccent));
                    holder.sub.setBackground(context.getResources().getDrawable(R.drawable.round_corner));
                    holder.sub.setText(isColorsInverted ? "Subscribed" : "Subscribe");
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
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        Button sub;

        ViewGroup transitionsContainer;
        SimpleViewHolder(View itemView) {
            super(itemView);

            transitionsContainer = (ViewGroup) itemView.findViewById(R.id.transitions_container);
            sub = (Button) transitionsContainer.findViewById(R.id.subscribe);
        }
    }
}