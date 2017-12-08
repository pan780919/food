package com.diet;

import android.os.Bundle;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        if (MySharedPrefernces.getMyCardTime(getApplicationContext()).equals("")) {
            System.currentTimeMillis();
            Log.d(TAG, "onCreate: "+"1");
            MySharedPrefernces.saveMyCardTime(getApplicationContext(),System.currentTimeMillis() + "");


//依照設定格式取得字串
        } else {
            Log.d(TAG, "onCreate: "+"2");
            Long last = Long.valueOf(MySharedPrefernces.getMyCardTime(getApplicationContext()));
            Long now = System.currentTimeMillis();
            Log.d(TAG, "onCreate: "+last+"");
            Log.d(TAG, "onCreate: "+ System.currentTimeMillis());
            if(now-last>=24* 60 * 60 * 1000){
                MySharedPrefernces.saveMyCardTime(getApplicationContext(),"");
                MySharedPrefernces.saveUserKm(getApplicationContext(),"");
                MySharedPrefernces.saveUserStep(getApplicationContext(),"");
                MySharedPrefernces.saveUserDhot(getApplicationContext(),"");
                MySharedPrefernces.saveUserhot(getApplicationContext(),"");


            }


        }





    }


}
