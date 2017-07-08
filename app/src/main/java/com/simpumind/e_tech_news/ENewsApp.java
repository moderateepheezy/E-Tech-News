package com.simpumind.e_tech_news;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpumind.e_tech_news.utils.TypefaceUtil;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by simpumind on 3/28/17.
 */

public class ENewsApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Orkney-Regular.ttf");
        TypefaceUtil.replaceFont("", tf);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = preferences.getString("lang", "fr");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


        DatabaseReference newsPaperRef = FirebaseDatabase.getInstance().getReference("newspapers");
        newsPaperRef.keepSynced(true);

        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("news");
        newsRef.keepSynced(true);

        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments");
        commentRef.keepSynced(true);

        DatabaseReference subscriberRef = FirebaseDatabase.getInstance().getReference("subscriber");
        subscriberRef.keepSynced(true);

        DatabaseReference baseUrlRef = FirebaseDatabase.getInstance().getReference("baseurl");
        baseUrlRef.keepSynced(true);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        MultiDex.install(this);
    }
}
