package com.simpumind.e_tech_news.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.simpumind.e_tech_news.R;
import com.simpumind.e_tech_news.activities.NewsMainActivity;

/**
 * Created by simpumind on 7/10/17.
 */

public class NewsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        listToFirebaseChanges("news");
        listToFirebaseChanges("newspapers");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void showStatusBarIcon() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("E-Newspaper")
                .setContentText("A new item has been added")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true);
        Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 9999, intent, 0);
        builder.setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = builder.build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(9999, notif);

    }

    private void listToFirebaseChanges(String node){

        FirebaseDatabase.getInstance().getReference()
                .child(node).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                showStatusBarIcon();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                showStatusBarIcon();
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
    }


}
