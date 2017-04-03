package com.simpumind.e_tech_news.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.simpumind.e_tech_news.models.User;

/**
 * Created by simpumind on 3/24/17.
 */

public class PrefManager {

    public static final String USER_KEY = "user_key";
    private static final String PREFERENCES_FILE = "quiz_settings";

    private static final String PREF_NAME = "quiz_key";

    private static final String USER_ID = "user_id";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_PROFILR_IMAGE = "profile_image";
    private static final String KEY_MSSISDN = "key_mssisdn";
    private static final String KEY_ADDRESS = "key_address";


    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static void saveMSSIDN(Context ctx, String constName, String mssisdn){
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(constName, mssisdn);
        editor.apply();
    }

    public static String readUserKey(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(USER_KEY, "");
    }

    public static void saveUserKey(Context ctx, String key){
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_KEY, key);
        editor.apply();
    }

    public static String readMSSISDN(Context ctx, String constName) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(constName, "");
    }

    public static void storeUser(Context ctx, User user){
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_PROFILR_IMAGE, user.getUserProfile());
        editor.putString(KEY_MSSISDN, user.getMsisdn());
        editor.putString(KEY_ADDRESS, user.getPhysical_address());
        editor.apply();
        editor.apply();
    }

    public  static User getStoredUser(Context ctx){

        SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String name, email, password, profileImage, mssisdn, address;
        int id = pref.getInt(KEY_USER_ID, 0);
        name = pref.getString(KEY_USER_NAME, "");
        email = pref.getString(KEY_USER_EMAIL, "");
        password = pref.getString(KEY_USER_PASSWORD, "");
        profileImage = pref.getString(KEY_PROFILR_IMAGE, "");
        mssisdn = pref.getString(KEY_MSSISDN, "");
        address = pref.getString(KEY_ADDRESS, "");

        User user = new User(name, email,mssisdn, address, password, profileImage);
        return user;
    }
}
