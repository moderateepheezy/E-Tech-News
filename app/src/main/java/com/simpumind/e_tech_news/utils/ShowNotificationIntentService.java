package com.simpumind.e_tech_news.utils;

/**
 * Created by simpumind on 7/8/17.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;

public class ShowNotificationIntentService extends IntentService {
    private static final String ACTION_SHOW_NOTIFICATION = "my.app.service.action.show";
    private static final String ACTION_HIDE_NOTIFICATION = "my.app.service.action.hide";


    public ShowNotificationIntentService() {
        super("ShowNotificationIntentService");
    }

    public static void startActionShow(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        context.startService(intent);
    }

    public static void startActionHide(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_HIDE_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NOTIFICATION.equals(action)) {


                FirebaseDatabase.getInstance().getReference()
                        .child("newspapers").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        handleActionShow();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        handleActionShow();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else if (ACTION_HIDE_NOTIFICATION.equals(action)) {
                handleActionHide();
            }
        }
    }

    private void handleActionShow() {
        showStatusBarIcon(ShowNotificationIntentService.this);
    }

    private void handleActionHide() {
       // hideStatusBarIcon(ShowNotificationIntentService.this);
    }

    public static void showStatusBarIcon(Context ctx) {
        Context context = ctx;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setContentTitle("E-Newspaper")
                .setContentText("A new item has been added")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true);
        Intent intent = new Intent(context, NewsMainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 9999, intent, 0);
        builder.setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = builder.build();
        notif.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(9999, notif);
    }
}
