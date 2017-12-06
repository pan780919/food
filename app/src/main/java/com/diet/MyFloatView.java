package com.diet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("AppCompatCustomView")
public class MyFloatView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "MyFloatView";
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private MediaPlayer mMediaPlayer;
    private ImageButton mPlayButton;
    private ImageButton mPauseButton;
    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private double startTime = 0;
    private double finalTime = 0;
    private ImageButton mExpandBtn;
    private RelativeLayout mRelativeLayout, mLargelayoutt;
    //此wmParams為獲取的全局變數，用以保存懸浮視窗的屬性
    private WindowManager.LayoutParams wmParams = ((MyApplication) getContext().getApplicationContext()).getmWParams();
    private Handler myHandler = new Handler();
    private myFloatViewListener myFloatViewListener;
    private TextView mFinalTimeTextView, mEndTimeTextView;
    private boolean isExpand = false;
    private SeekBar mLargeSeekbar;
    private TextView mTittleTextView;
    private  ImageButton mLargePlayBtn,mLargePauseBtn;
    private Context mContext;
    public MyFloatView(Context context, Uri url, String title, boolean b,myFloatViewListener myFloatViewListener) {
        super(context);
        initmedia(context,url,title,b);
        this.myFloatViewListener = myFloatViewListener;
        // TODO Auto-generated constructor stub
    }

    private void initmedia(Context context,final Uri url, final  String title, boolean b) {
        mMediaPlayer = new MediaPlayer();           //建立 MediaPlayer 物件
        try {
            mMediaPlayer.reset();       //如果之前有播過, 必須 reset 後才能更換
            mMediaPlayer.setDataSource(context, url);  //指定影音檔來源
            mMediaPlayer.setLooping(true); //設定是否重複播放
            mMediaPlayer.prepareAsync();  //要求 MediaPlayer 準備播放指定的影音檔

            Log.d(TAG, "handleMessage: "+Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.likey));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "handleMessage: "+e.getMessage());
        }
        mMediaPlayer.start();
//        try {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setDataSource(url);
//            mMediaPlayer.prepare();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//
//        }

        initToLarge(title);
//        myHandler.postDelayed(UpdateSongTime, 100);





    }

    //音黨播放器
    private void initToLarge(String title) {


        View view = inflate(getContext(), R.layout.large_layout, this);
        mLargelayoutt = (RelativeLayout) view.findViewById(R.id.largelayout);
        mFinalTimeTextView = (TextView) view.findViewById(R.id.finaltimetextview);
        mEndTimeTextView = (TextView) view.findViewById(R.id.endtimetexview);
        mLargeSeekbar = (SeekBar) view.findViewById(R.id.large_seekbar);
        mLargePlayBtn = (ImageButton) view.findViewById(R.id.largeplaybtn);
        mLargePauseBtn = (ImageButton) view.findViewById(R.id.largepausebtn);
        mTittleTextView = (TextView) view.findViewById(R.id.tittletextview);
        mTittleTextView.setText(title);
        mLargeSeekbar.setMax((int) finalTime);
        mLargeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setThumbOffset(15);
                seekBar.setThumb(getResources().getDrawable(R.drawable.slider_thumb_pressed));
                seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.play_seekbar_bg_2));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                seekBar.setThumbOffset(0);
                seekBar.setThumb(getResources().getDrawable(R.drawable.seekbarthumb));
                seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.play_seekbar_bg));



            }
        });
        findViewById(R.id.compressbtn).setOnClickListener(this);
        findViewById(R.id.largeplaybtn).setOnClickListener(this);
        findViewById(R.id.largepausebtn).setOnClickListener(this);
        findViewById(R.id.forwardroundbtn).setOnClickListener(this);
        findViewById(R.id.backbtn).setOnClickListener(this);
        findViewById(R.id.largeclosebtn).setOnClickListener(this);
        findViewById(R.id.compresslayout).setOnClickListener(this);
        findViewById(R.id.largecloselayout).setOnClickListener(this);
        mFinalTimeTextView.setText(progresstime((int) finalTime));

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //獲取相對屏幕的坐標，即以屏幕左上角為原點
        x = event.getRawX();
        y = event.getRawY()-25;   //25是系統狀態欄的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //獲取相對View的坐標，即以此View左上角為原點
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return true;
    }


    private void updateViewPosition() {
        //更新浮動視窗位置參數
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(this, wmParams);
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            double endtime = mMediaPlayer.getDuration() - mMediaPlayer.getCurrentPosition();
            mLargeSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
            mEndTimeTextView.setText("-" + progresstime((int) (endtime)));
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.compressbtn:
                mRelativeLayout.setVisibility(VISIBLE);
                mLargelayoutt.setVisibility(GONE);
                break;
            case R.id.largeplaybtn:
                mLargePauseBtn.setVisibility(VISIBLE);
                mLargePlayBtn.setVisibility(GONE);
                mMediaPlayer.start();
                break;
            case R.id.largepausebtn:
                mLargePauseBtn.setVisibility(GONE);
                mLargePlayBtn.setVisibility(VISIBLE);
                mMediaPlayer.pause();
                break;
            case R.id.forwardroundbtn:
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 10000);
                break;
            case R.id.backbtn:
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 10000);
                break;
            case R.id.largeclosebtn:
                mLargelayoutt.setVisibility(INVISIBLE);
                mMediaPlayer.reset();
                myHandler.removeCallbacks(UpdateSongTime);
                myFloatViewListener.onTaskComplete(false);

                break;
            case R.id.compresslayout:
                mRelativeLayout.setVisibility(VISIBLE);
                mLargelayoutt.setVisibility(GONE);
                break;
            case R.id.largecloselayout:
                mLargelayoutt.setVisibility(INVISIBLE);
                mMediaPlayer.reset();
                myHandler.removeCallbacks(UpdateSongTime);
                myFloatViewListener.onTaskComplete(false);
                break;
        }
    }

    // 将毫秒数转换为时间格式
    private String progresstime(int progress) {
        Date date = new Date(progress);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(date);
    }
    public void colseMediaPlayer(){
        mMediaPlayer.reset();

        myHandler.removeCallbacks(UpdateSongTime);
        myFloatViewListener.onTaskComplete(false);

    }


}