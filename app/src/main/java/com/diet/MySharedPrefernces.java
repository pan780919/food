package com.diet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefernces {


    public static final String NAME = "MySharedPrefernces";

    // 儲存
    public static final String KEY_ID = "id";

    public static void saveId(Context context, int userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putInt(KEY_ID, userid).commit();


    }

    public static int getId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getInt(KEY_ID, 0);
    }

    // 儲存 userid
    public static final String KEY_USERID = "userid";

    public static void saveUserId(Context context, String userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERID, userid).commit();


    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERID, "");
    }


    // 儲存 userid
    public static final String KEY_MUSIC = "music";

    public static void saveMusicState(Context context, int userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putInt(KEY_MUSIC, userid).commit();


    }

    public static int getMusicState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getInt(KEY_MUSIC, 0);
    }

    // 儲存 userpic
    public static final String KEY_USERPIC = "userpic";

    public static void saveUserPic(Context context, String userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERPIC, userid).commit();


    }

    public static String getUserPic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERPIC, "");
    }

    // 儲存 username
    public static final String KEY_USERNAME = "username";

    public static void saveUserName(Context context, String userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERNAME, userid).commit();


    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERNAME, "");
    }

    // 儲存 useremail
    public static final String KEY_USERMAIL = "usermail";

    public static void saveUserMail(Context context, String userid) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERMAIL, userid).commit();


    }

    public static String getUserMail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERMAIL, "");
    }
}