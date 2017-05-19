package com.simpumind.e_tech_news.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simpumind.e_tech_news.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by simpumind on 4/2/17.
 */

public class Const {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void loadImage(String  imagePath, boolean isBase64,
                                 Context context, ImageView imageView, String name){

        StringBuilder sb = new StringBuilder();
        sb.append(imagePath);

        if (isBase64){
//
//            byte[] imageBytes = sb.toByteArray();
//            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//            //decode base64 string to image
//            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
//            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            imageView.setImageBitmap(decodedImage);

            String imageBytes = sb.toString();
            byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);

            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    .placeholder(R.drawable.denews)
                    //.using(new FirebaseImageLoader())//For offline syncing.
                    .into(imageView);

        }else {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(imagePath);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mStorage)
                    .fitCenter()
                    .placeholder(R.drawable.denews)
                    .into(imageView);

        }

    }

    public String fileToBuffer(InputStream is, StringBuffer strBuffer) throws IOException {
        StringBuilder sb = new StringBuilder(strBuffer);
        try (BufferedReader rdr = new BufferedReader(new InputStreamReader(is))) {
            for (int c; (c = rdr.read()) != -1;) {
                sb.append((char) c);
            }
        }    return sb.toString();
    }
}
