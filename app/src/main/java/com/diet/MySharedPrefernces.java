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
}