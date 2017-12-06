package com.diet;

import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;

public class MyApplication extends MultiDexApplication {
    private static final String TAG = "MyApplication";
    private static FirebaseAnalytics mFirebaseAnalytics;
    private WindowManager.LayoutParams mWParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getmWParams() {


        return mWParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }






}
