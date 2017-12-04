package com.diet;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    @Override
    public void onCreate() {
        //在 Service 開啟時顯示訊息。
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }


    @Override // 目前還不會用到的方法，先不管。
    public IBinder onBind(Intent intent) {
        return null;
    }
}