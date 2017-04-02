package com.simpumind.e_tech_news.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by simpumind on 3/27/17.
 */

public class SubMethodFragment extends DialogFragment {

    final CharSequence  items [] = {"Direct Billing","Mobile Money"};

    String select = "Daily";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Subscription Type");

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int args) {
                switch (args){
                    case 0:  select= (String) items[args];
                        break;
                    case 1:  select= (String) items[args];
                        break;
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("Select Subscription Method");
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
