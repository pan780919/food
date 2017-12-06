package com.diet;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JackPan on 2017/12/6.
 */

public class BackgroundSoundService extends Service  implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mMediaPlayer;
    private int length = 0;
    private static final String TAG = "BackgroundSoundService";
    private  Uri uri ;
    public BackgroundSoundService() {
    }

    public class ServiceBinder extends Binder {
        BackgroundSoundService getService() {
            return BackgroundSoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer();           //建立 MediaPlayer 物件
        try {
            mMediaPlayer.reset();       //如果之前有播過, 必須 reset 後才能更換
            mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" +getApplication().getPackageName() + "/" + R.raw.likey));  //指定影音檔來源
            mMediaPlayer.setLooping(true); //設定是否重複播放
            mMediaPlayer.prepareAsync();  //要求 MediaPlayer 準備播放指定的影音檔

            Log.d(TAG, "handleMessage: "+Uri.parse("android.resource://" +getApplication().getPackageName() + "/" + R.raw.likey));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "handleMessage: "+e.getMessage());
        }
        mMediaPlayer.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+intent.getExtras());
        if (intent != null && intent.getExtras() != null){
            Log.d(TAG, "onStartCommand: "+Uri.parse(intent.getStringExtra("uri")));
            uri = Uri.parse(intent.getStringExtra("uri"));
        }
        if(uri== null){
            uri =  Uri.parse("android.resource://" +getApplication().getPackageName() + "/" + R.raw.likey);
        }
        if(uri.equals("")){
            uri =  Uri.parse("android.resource://" +getApplication().getPackageName() + "/" + R.raw.likey);

        }

        if(mMediaPlayer!=null){
            mMediaPlayer.start();
        }else {
            mMediaPlayer = new MediaPlayer();           //建立 MediaPlayer 物件
            try {
                mMediaPlayer.reset();       //如果之前有播過, 必須 reset 後才能更換
                mMediaPlayer.setDataSource(getApplicationContext(), uri);  //指定影音檔來源
                mMediaPlayer.setLooping(true); //設定是否重複播放
                mMediaPlayer.prepareAsync();  //要求 MediaPlayer 準備播放指定的影音檔

                Log.d(TAG, "handleMessage: "+Uri.parse("android.resource://" +getApplication().getPackageName() + "/" + R.raw.likey));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "handleMessage: "+e.getMessage());
            }
            mMediaPlayer.start();
        }


        return START_REDELIVER_INTENT;
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            length = mMediaPlayer.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (mMediaPlayer.isPlaying() == false) {
            mMediaPlayer.seekTo(length);
            mMediaPlayer.start();
        }
    }

    public void stopMusic() {
        if(mMediaPlayer==null) return;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
        return false;
    }
}