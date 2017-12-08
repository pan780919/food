package com.diet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MusicActivity extends AppCompatActivity implements
        MediaPlayer.OnPreparedListener,   //實作 MediaPlayer 的 3 個的事件監聽介面
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    Uri uri;      //儲存影音檔案的 Uri
    TextView txvName, txvUri;  //參照到畫面中的元件
    boolean isVideo = false;   //記錄是否為影片檔
    Button btnPlay, btnStop;  //用來參照播放鈕、停止鈕
    CheckBox ckbLoop;         //用來參照重複播放多選鈕
    MediaPlayer mper;         //用來參照 MediaPlayer 物件
    Toast tos;                //用來參照 Toast 物件 (顯示訊息之用)
    int state;
    private static final String TAG = "MusicActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_music);
        super.onCreate(savedInstanceState);

        doBindService();
        //設定螢幕不隨手機旋轉、以及畫面直向顯示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//設定螢幕不進入休眠

        txvName = (TextView)findViewById(R.id.txvName); //參照到第1個文字元件
        txvUri = (TextView)findViewById(R.id.txvUri);   //參照到第2個文字元件
        btnPlay = (Button)findViewById(R.id.btnPlay);   //參照到播放鈕
        btnStop = (Button)findViewById(R.id.btnStop);   //參照到停止鈕
        ckbLoop = (CheckBox)findViewById(R.id.ckbLoop); //參照到重複播放多選鈕

        uri = Uri.parse("android.resource://" + //預設會播放程式內的音樂檔
                getPackageName() + "/" + R.raw.likey);
        txvName.setText("Likey.mp3");         //在畫面中顯示檔名
        txvUri.setText("程式內的樂曲：" + uri.toString());//顯示 Uri

        mper = new MediaPlayer();           //建立 MediaPlayer 物件
        mper.setOnPreparedListener(this);   //設定 3 個事件監聽器
        mper.setOnErrorListener(this);
        mper.setOnCompletionListener(this);
        tos = Toast.makeText(this, "", Toast.LENGTH_SHORT); //建立 Toast 物件
        prepareMedia();   //準備播放指定的影音檔

    }

    void prepareMedia() {
        btnPlay.setText("播放");    //將按鈕文字恢復為 "播放"
        btnPlay.setEnabled(true);   //使播放鈕不能按 (要等準備好才能按)
        btnStop.setEnabled(true);   //使停止鈕不能按
//        createView(uri,"123",ckbLoop.isChecked());
//        finish();
//        try {
//            mper.reset();       //如果之前有播過, 必須 reset 後才能更換
//            mper.setDataSource(this, uri);  //指定影音檔來源
//            mper.setLooping(ckbLoop.isChecked()); //設定是否重複播放
//            mper.prepareAsync();  //要求 MediaPlayer 準備播放指定的影音檔
//        } catch (Exception e) {    //攔截錯誤並顯示訊息
//            tos.setText("指定影音檔錯誤！" + e.toString());
//            tos.show();
//        }
//        MySharedPrefernces.saveMusicState(MusicActivity.this,1);
    }

    public void onPick(View v) {
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);    //建立動作為 "選取" 的 Intent
        if (v.getId() == R.id.btnPickAudio) {  //如果是 "選取歌曲" 鈕的 ID
            it.setType("audio/*");     //要選取所有音樂類型
            startActivityForResult(it, 100);
        } else {  //否則就是 "選取影片" 鈕
            it.setType("video/*");     //要選取所有影片類型
            startActivityForResult(it, 101);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            isVideo = (requestCode == 101); //記錄是否選取了影片檔 (當識別碼為101時)
            uri = data.getData();  //取得選取檔案的 Uri
            txvName.setText(getFilename(uri));  //顯示檔名
            txvUri.setText("檔案URI：" + uri.toString()); //顯示檔案的 URI
            prepareMedia();  //重新準備播放剛選擇的影音檔
        }
    }

    String getFilename(Uri uri) { //以 URL 向內容資料庫查詢檔名
        String fileName = null;
        String[] colName = {MediaStore.MediaColumns.DISPLAY_NAME};    //宣告要查詢的欄位
        Cursor cursor = getContentResolver().query(uri, colName,  //以 uri 進行查詢
                null, null, null);
        cursor.moveToFirst();      //移到查詢結果的第一筆記錄
        fileName = cursor.getString(0);
        cursor.close();     //關閉查詢結果
        return fileName;   //傳回檔名
    }

    //********************************************************

    @Override
    public void onPrepared(MediaPlayer mp) {
        btnPlay.setEnabled(true);  //當準備好時, 只需讓【播放】鈕可以按即可
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mper.seekTo(0);             //將播放位置歸 0
        btnPlay.setText("播放");    //讓播放鈕顯示 "播放"
        btnStop.setEnabled(true);  //讓停止鈕不能按

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        tos.setText("發生錯誤，停止播放");  //顯示錯誤訊息
        tos.show();
        return true;
    }

    //********************************************************

    public void onMpPlay(View v) {
//        finish();//按下【播放】鈕時
        if(isVideo) {   //如果是影片
            Intent it = new Intent(this, MediaStore.Video.class); //建立開啟 Video Activity 的 Intent
            it.putExtra("uri", uri.toString());   //將影片的 Uri 以 "uri" 為名加入 Intent 中
            startActivity(it);    //啟動 Video Activity
            return;
        }
        Intent svc=new Intent(this, BackgroundSoundService.class);
        svc.putExtra("uri", uri.toString());
        startService(svc);//OR stopService(svc);
//        if (mper.isPlaying()) {  //如果正在播, 就暫停
//            mper.pause();   //暫停播放
//            btnPlay.setText("繼續");
//        }
//        else {  //如果沒有在播, 就開始播
//            mper.start();   //開始播放
//            btnPlay.setText("暫停");
//            btnStop.setEnabled(true);
//        }
    }

    public void onMpStop(View v) {   //按下【停止】鈕時
        mper.pause();   //暫停播放
        mper.seekTo(0); //移到音樂中 0 秒的位置
        btnPlay.setText("播放");
        btnStop.setEnabled(true);
        backgroundSoundService.stopMusic();
        MySharedPrefernces.saveMusicState(MusicActivity.this,0);
    }

    public void onMpLoop(View v) {   //按下【重複播放】多選鈕時
        if (ckbLoop.isChecked())
            mper.setLooping(true);   //設定要重複播放
        else
            mper.setLooping(false);  //設定不要重複播放
    }

    public void onMpBackward(View v) {   //按下倒退圖形鈕時
        if(!btnPlay.isEnabled()) return; //如果還沒準備好(播放鈕不能按), 則不處理
        int len = mper.getDuration();       //讀取音樂長度
        int pos = mper.getCurrentPosition();//讀取目前播放位置
        pos -= 10000;		                //倒退 10 秒 (10000ms)
        if(pos <0) pos = 0;                 //不可小於 0
        mper.seekTo(pos);                   //移動播放位置
        tos.setText("倒退10秒：" + pos/1000 + "/" + len/1000);  //顯示訊息
        tos.show();
    }

    public void onMpForward(View v) {   //按下前進圖形鈕時
        if(!btnPlay.isEnabled()) return; //如果還沒準備好(播放鈕不能按), 則不處理
        int len = mper.getDuration();       //讀取音樂長度
        int pos = mper.getCurrentPosition();//讀取目前播放位置
        pos += 10000;		                //前進 10 秒 (10000ms)
        if(pos > len) pos = len;            //不可大於總秒數
        mper.seekTo(pos);                   //移動播放位置
        tos.setText("前進10秒：" + pos/1000 + "/" + len/1000);  //顯示訊息
        tos.show();
    }

    //********************************************************

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
//        mper.release();  //釋放 MediaPlayer 物件
        super.onDestroy();
    }
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private MyFloatView myFV = null;
    @TargetApi(26)
    @RequiresApi(api = 26)
    private void createView(Uri url, String title, boolean b) {
        myFV = new MyFloatView(getApplicationContext(), url, title,b, new myFloatViewListener() {
            @Override
            public void onTaskComplete(boolean b) {
//                isPlay = b;
            }
        });
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //設置LayoutParams(全局變數）相關參數
        wmParams = ((MyApplication) getApplication()).getmWParams();        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;   //設置window type
            wmParams.format = PixelFormat.RGBA_8888;
            //設置Window flag
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //調整懸浮視窗至左上角
            //以屏幕左上角為原點，設置x、y初始值
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            wmParams.x = (width / 2) - 130;
            wmParams.y = height - 330;
            //設置懸浮視窗長寬數據
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //顯示myFloatView圖像
            wm.addView(myFV, wmParams);
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;   //設置window type
            wmParams.format = PixelFormat.RGBA_8888;
            //設置Window flag
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //調整懸浮視窗至左上角
            //以屏幕左上角為原點，設置x、y初始值
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            wmParams.x = (width / 2) - 130;
            wmParams.y = height - 330;
            //設置懸浮視窗長寬數據
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //顯示myFloatView圖像
            wm.addView(myFV, wmParams);
        }

        //獲取WindowManager






    }
//    public void permission(){
//        if (Build.VERSION.SDK_INT >= 23) {
//            if(!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                startActivity(intent);
//                return;
//            } else {
//                //Android6.0以上
//                if (mFloatView!=null && mFloatView.isShow()==false) {
//                    mFloatView.show();
//                }
//            }
//        } else {
//            //Android6.0以下，不用动态声明权限
//            if (mFloatView!=null && mFloatView.isShow()==false) {
//                mFloatView.show();
//            }
//        }
//    }

    private boolean mIsBound = false;
    private  BackgroundSoundService backgroundSoundService;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            backgroundSoundService = ((BackgroundSoundService.ServiceBinder) binder).getService();

        }

        public void onServiceDisconnected(ComponentName name) {
            backgroundSoundService = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,BackgroundSoundService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

}
