package com.simpumind.e_tech_news.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simpumind on 4/19/17.
 */

public class TypefaceUtil {

    public static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        if (isVersionGreaterOrEqualToLollipop()) {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put("sans-serif", newTypeface);
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isVersionGreaterOrEqualToLollipop() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            return true;
        } else{
            // do something for phones running an SDK before lollipop
            return false;
        }
    }
}
