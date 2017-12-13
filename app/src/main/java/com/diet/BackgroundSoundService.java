package com.diet;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
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



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+intent.getExtras());
        if (intent != null && intent.getExtras() != null){
            uri = Uri.parse(intent.getStringExtra("uri"));
        }
        if(uri== null){
            return START_REDELIVER_INTENT;
        }
        if(uri.equals("")){
            return START_REDELIVER_INTENT;
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

    public void onMpBackward() {   //按下倒退圖形鈕時
        if(mMediaPlayer==null) return;
        int len = mMediaPlayer.getDuration();       //讀取音樂長度
        int pos = mMediaPlayer.getCurrentPosition();//讀取目前播放位置
        pos -= 10000;		                //倒退 10 秒 (10000ms)
        if(pos <0) pos = 0;                 //不可小於 0
        mMediaPlayer.seekTo(pos);                   //移動播放位置
//        tos.setText("倒退10秒：" + pos/1000 + "/" + len/1000);  //顯示訊息
//        tos.show();

//        tos.setText("前進10秒：" + pos/1000 + "/" + len/1000);  //顯示訊息
    }
    public void onMpForward() {

        if(mMediaPlayer==null) return;

        int len = mMediaPlayer.getDuration();       //讀取音樂長度
        int pos = mMediaPlayer.getCurrentPosition();//讀取目前播放位置
        Log.d(TAG, "onMpForward: " + len);
        Log.d(TAG, "onMpForward: " + pos);
        pos += 10000;                        //前進 10 秒 (10000ms)
        if (pos > len) pos = len;            //不可大於總秒數
        mMediaPlayer.seekTo(pos);                   //移動播放位置


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