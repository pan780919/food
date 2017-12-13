package com.diet;

//import java.util.ArrayList;

import java.io.File;
import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.Toast;

import com.sqlite.SQLiteHelper;
import com.sqlite.account;
import com.sqlite.recdata;

public class WalKStepActivity extends Activity {
    private static final int MSG_DIALOG_REFRESH = 1;
    private static final int MSG_UPDATE_KM = 2;

    private static final int MENU_MAP_INC = Menu.FIRST;
    private static final int MENU_MAP_DEC = Menu.FIRST + 1;
    private static final int MENU_MAP_TYPE = Menu.FIRST + 2;
    private static final int MENU_QUERY = Menu.FIRST + 3;
    private static final int MENU_LOGIN = Menu.FIRST + 4;
    private static final int MENU_USERMGR = Menu.FIRST + 5;
    private static final int MENU_EXIT = Menu.FIRST + 6;

    private String TAG = "mapweight";

    static public WalKStepActivity my;

    private WalKStepActivity mWalKStepActivity = this;

    private Cursor cursor;
    private int intZoomLevel = 0;//geoLatitude,geoLongitude;

    public boolean mshow;

    public TextView label;
    public TextView distance;

    public int maptype;

    public double whot;

    int dcounter = 0;

    Timer timer;

    int counter = 0;

    int steps = 0;

    ScrollView sview;

    EditText sname, s1, s2, s3;
    Spinner s4, s5;

    TextView ttimer, mmode, mneed, km, shows, ssteps, ntime;

    Button tpause, treset;
    Button running, end, showmap, goal;

    String name;

    int pause = 0;

    int age, weight, tall, tsex;

    double now_km;

    int start = 0;
    double now_shows;

    int section;

    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    //private Cursor cursor;
    SimpleDateFormat sdf;

    Calendar mCalendar;
    private String str;
    private SimpleDateFormat df;
    private Handler handler = new Handler();

    private static int DB_VERSION = 1;

    ArrayList<account> data = null;

    int mchildid;

    SensorManager sensorMgr;
    Sensor sensor;


    boolean gps = false;

    Location slocation;

    ImageView iv;
    private SoundPool soundPool;
    private int alertId;

    private SharedPreferences settings;
    private static final String mydata = "DATA";
    private static final String nameField = "NAME";

    EditText ddate;

    File myvoice;
    Uri uri;      //儲存影音檔案的 Uri
    MediaPlayer mper;         //用來參照 MediaPlayer 物件

    int mygoal;
    private int[] image = {
            R.drawable.p01, R.drawable.p02, R.drawable.p03,
            R.drawable.p05, R.drawable.p06, R.drawable.p07,
            R.drawable.p08, R.drawable.p09, R.drawable.p10,
            R.drawable.p12, R.drawable.p13, R.drawable.p14,
            R.drawable.p15, R.drawable.p16, R.drawable.p17, R.drawable.p18,
            R.drawable.p19, R.drawable.p20, R.drawable.p21, R.drawable.p22,

    };

    @Override
    protected void onCreate(Bundle icicle) {
        // TODO Auto-generated method stub
        super.onCreate(icicle);
        setContentView(R.layout.main2);
        my = this;
        uri = Uri.parse("android.resource://" + //預設會播放程式內的音樂檔
                getPackageName() + "/" + R.raw.goal);
        Log.d(TAG, "onCreate: " + uri);
        mper = new MediaPlayer();           //建立 MediaPlayer 物件
        try {
            mper.reset();       //如果之前有播過, 必須 reset 後才能更換
            mper.setDataSource(WalKStepActivity.this, uri);  //指定影音檔來源
            mper.setLooping(false); //設定是否重複播放
            mper.prepareAsync();  //要求 MediaPlayer 準備播放指定的影音檔
        } catch (Exception e) {
            e.printStackTrace();
        }
        start = 0;

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        try {
            dbHelper = new SQLiteHelper(this, SQLiteHelper.DB_NAME, null, DB_VERSION);
            if (dbHelper != null)
                db = dbHelper.getWritableDatabase();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ++DB_VERSION;
            if (dbHelper != null)
                dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
        }

        name = MySharedPrefernces.getUserName(WalKStepActivity.this);
        age = 20;
        tsex = MySharedPrefernces.getUserSex(WalKStepActivity.this);
        weight =MySharedPrefernces.getUserWeight(WalKStepActivity.this);
        tall = MySharedPrefernces.getUserTall(WalKStepActivity.this);

        sview = (ScrollView) findViewById(R.id.sview);
        ssteps = (TextView) findViewById(R.id.textView1);
        ttimer = (TextView) findViewById(R.id.textView7);
        mmode = (TextView) findViewById(R.id.textView8);
        mneed = (TextView) findViewById(R.id.textView9);
        km = (TextView) findViewById(R.id.textView5);
        shows = (TextView) findViewById(R.id.k1);
        //ntime = (TextView)findViewById(R.id.t1);

        iv = (ImageView) findViewById(R.id.imageView1);

        int imageid = MySharedPrefernces.getId(WalKStepActivity.this);
        iv.setImageResource(image[imageid]);
        tpause = (Button) findViewById(R.id.button1);
        treset = (Button) findViewById(R.id.button2);
        running = (Button) findViewById(R.id.button3);
        end = (Button) findViewById(R.id.button6);
        goal = (Button) findViewById(R.id.button4);

        tpause.setEnabled(false);
        treset.setEnabled(false);

        tpause.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (pause == 0) {
                    tpause.setText("繼續");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;

                        String k = km.getText().toString();
                        String s = ttimer.getText().toString();
                        String kr = shows.getText().toString();
                        String stepss = ssteps.getText().toString();
//                        SQLHandler.insert_data(WalKStepActivity.my, name, Integer.toString(section),
//                                kr, Integer.toString(steps),k, now_status, "暫停");
//                        if(MySharedPrefernces.getUserKm(getApplicationContext()).equals("")){
//                            MySharedPrefernces.saveUserKm(getApplicationContext(), String.valueOf(kr));
//
//                        }else {
//                            Double k1= Double.parseDouble(MySharedPrefernces.getUserKm(getApplication()));
//                            Double k2 = Double.parseDouble(k);
//                            MySharedPrefernces.saveUserKm(getApplicationContext(),String.valueOf(k1+k2));
//                        }
//                        if(MySharedPrefernces.getUserStep(getApplicationContext()).equals("")){
//                            MySharedPrefernces.saveUserStep(getApplicationContext(),String.valueOf(steps));
//                        }else {
//                            Integer s1 =Integer.parseInt(MySharedPrefernces.getUserStep(getApplicationContext())) ;
//                            Integer s0 = s1+steps;
//                            MySharedPrefernces.saveUserStep(getApplicationContext(),String.valueOf(s0));
//
//                        }
//                        double dhot =time*weight;
//
                        String now_status = "跑了 " + k + "- 花了 " + s + "- 消耗" + kr + "- 目前 " + stepss;

                        SQLHandler.insert_data(WalKStepActivity.my, name, Integer.toString(section), kr, Integer.toString(steps), k, now_status, "完成");

                        double dhot = time * weight;
                        Log.d(TAG, "onClick: " + dhot);

//                        DBSQL.insertDiary(WalKStepActivity.this, String.valueOf(dhot), "-1", "計步器", kr);                        pause = 1;
                        pause = 1;
                        start = 0;                 }
                } else {
                    tpause.setText("暫停");
                    pause = 0;
                    timer = new Timer();
                    timer.schedule(new DateTask(), 1000, 1000);
                    start = 1;
                }
            }

        });
        Button listbutton = (Button) findViewById(R.id.button5);
        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent app = new Intent(WalKStepActivity.this, Query.class);
                Bundle rdata = new Bundle();
                rdata.putString("name", name);
                app.putExtras(rdata);
                startActivity(app);

            }
        });

        treset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                String k = km.getText().toString();
                String s = ttimer.getText().toString();
                String kr = shows.getText().toString();
                String stepss = ssteps.getText().toString();

                String now_status = "跑了 " + k + "- 花了 " + s + "- 消耗" + kr + "- 目前 " + stepss;

//                SQLHandler.insert_data(WalKStepActivity.my, name, Integer.toString(section), kr, Integer.toString(steps), k, now_status, "reset");
//                if(MySharedPrefernces.getUserKm(getApplicationContext()).equals("")){
//                    MySharedPrefernces.saveUserKm(getApplicationContext(), String.valueOf(kr));
//
//                }else {
//                    Double k1= Double.parseDouble(MySharedPrefernces.getUserKm(getApplication()));
//                    Double k2 = Double.parseDouble(k);
//                    MySharedPrefernces.saveUserKm(getApplicationContext(),String.valueOf(k1+k2));
//                }
//                if(MySharedPrefernces.getUserStep(getApplicationContext()).equals("")){
//                    MySharedPrefernces.saveUserStep(getApplicationContext(),String.valueOf(steps));
//                }else {
//                    Integer s1 =Integer.parseInt(MySharedPrefernces.getUserStep(getApplicationContext())) ;
//                    Integer s0 = s1+steps;
//                    MySharedPrefernces.saveUserStep(getApplicationContext(),String.valueOf(s0));
//
//                }
//                double dhot =time*weight;
//
//                SQLHandler.insert_data(WalKStepActivity.my, name, Integer.toString(section), kr, Integer.toString(steps), k, now_status, "完成");

                double dhot = time * weight;
                Log.d(TAG, "onClick: " + dhot);

//                DBSQL.insertDiary(WalKStepActivity.this, String.valueOf(dhot), "-1", "計步器", kr);
                counter = 0;
                running.setEnabled(true);
                ttimer.setText("0:0");
                tpause.setEnabled(false);
                treset.setEnabled(false);
                km.setText("0");
                start = 0;
                steps = 0;
                ssteps.setText("0");

            }

        });

        end.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                String k = km.getText().toString();
                String s = ttimer.getText().toString();
                String kr = shows.getText().toString();
                String stepss = ssteps.getText().toString();

                String now_status = "跑了 " + k + "- 花了 " + s + "- 消耗" + kr + "- 目前 " + stepss;

                double dhot = time * weight;
                Log.d(TAG, "onClick: " + dhot);

                SQLHandler.insert_data(WalKStepActivity.my, name, Integer.toString(section), kr, Integer.toString(steps), k, now_status, "完成");

                DBSQL.insertDiary(WalKStepActivity.this, "-1", "-1", "計步器", kr);


                if(MySharedPrefernces.getUserDhot(getApplicationContext()).equals("")){
                    MySharedPrefernces.saveUserDhot(getApplicationContext(),kr);

                }else {
                    double dhot11 = Double.parseDouble(MySharedPrefernces.getUserDhot(getApplicationContext()));
                    double dhot2 = dhot11+Double.parseDouble(kr);
                    MySharedPrefernces.saveUserDhot(getApplicationContext(),dhot2+"");
                }




                if(MySharedPrefernces.getUserDhot(getApplicationContext()).equals("")){
                    MySharedPrefernces.saveUserDhot(getApplicationContext(),kr);

                }else {
                    double dhot11 = Double.parseDouble(MySharedPrefernces.getUserDhot(getApplicationContext()));
                    Log.d(TAG, "onClick: "+dhot11);
                    double dhot2 = dhot11+Double.parseDouble(kr);
                    Log.d(TAG, "onClick: "+dhot2);
                    MySharedPrefernces.saveUserDhot(getApplicationContext(),dhot2+"");
                }

                if(MySharedPrefernces.getUserKm(getApplicationContext()).equals("")){
                    MySharedPrefernces.saveUserKm(getApplicationContext(),k);

                }else {
                    double km  = Double.parseDouble(MySharedPrefernces.getUserKm(getApplicationContext()));
                    Log.d(TAG, "onClick: "+km);
                    double kmAll =km+Double.parseDouble(k);
                    Log.d(TAG, "onClick: "+kmAll);
                    MySharedPrefernces.saveUserKm(getApplicationContext(),kmAll+"");

                }
                if(MySharedPrefernces.getUserStep(getApplicationContext()).equals("")){
                    MySharedPrefernces.saveUserStep(getApplicationContext(),stepss);

                }else {
                    int step =Integer.parseInt(MySharedPrefernces.getUserStep(getApplicationContext())) ;
                    int stepAll = step+Integer.parseInt(stepss);
                    MySharedPrefernces.saveUserStep(getApplicationContext(),stepAll+"");

                }
                counter = 0;
                running.setEnabled(true);
                ttimer.setText("0:0");
                tpause.setEnabled(false);
                km.setText("0");
                start = 0;
                steps = 0;
                ssteps.setText("0");
                shows.setText("0");


//                getStoreList();
            }

        });

        running.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (section == 0) {

                    //sview.setBackgroundResource(R.drawable.back);
                    timer = new Timer();
                    timer.schedule(new DateTask(), 0, 1000);
                    running.setEnabled(false);
                    tpause.setEnabled(true);
                    treset.setEnabled(true);
                    start = 1;
                } else if (section == 1) {

                    //sview.setBackgroundResource(R.drawable.back);
                    //sview.setBackgroundResource(R.drawable.index);
                    //sview.setBackgroundColor(Color.LTGRAY);
                    timer = new Timer();
                    timer.schedule(new DateTask(), 0, 1000);
                    running.setEnabled(false);
                    tpause.setEnabled(true);
                    treset.setEnabled(true);
                    start = 1;
                }

            }

        });

        goal.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(WalKStepActivity.this);

                alert.setTitle("今日的目標");
                alert.setMessage("input steps?");

                ScrollView sv = new ScrollView(WalKStepActivity.this);
                LinearLayout ll = new LinearLayout(WalKStepActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                sv.addView(ll);

                TextView tdate = new TextView(WalKStepActivity.this);
                tdate.setText("(步)");
                ddate = new EditText(WalKStepActivity.this);
                ddate.setText("");
                ddate.setInputType(InputType.TYPE_CLASS_NUMBER);

                ll.addView(tdate);
                ll.addView(ddate);

                alert.setView(sv);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mygoal = Integer.valueOf(ddate.getText().toString());
                        mmode.setText("目標:" + mygoal);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }

        });

        section = 1;
        mchildid = 0;
//        name = MySharedPrefernces.getUserName(WalKStepActivity.this);
//        age = 20;
//        tsex = MySharedPrefernces.getUserSex(WalKStepActivity.this);
//        weight = MySharedPrefernces.getUserWeight(WalKStepActivity.this);
//        tall = MySharedPrefernces.getUserTall(WalKStepActivity.this);

        mmode.setText("未設定目標");

        double nk = 0;

        if (age <= 30) {
            if (tsex == 0)
                nk = 15.2 * weight + 680;
            else
                nk = 14.6 * weight + 450;

        } else if (age > 30 && age <= 60) {
            if (tsex == 0)
                nk = 11.5 * weight + 830;
            else
                nk = 8.6 * weight + 830;
        }
        if (age > 60) {
            if (tsex == 0)
                nk = 13.4 * weight + 490;
            else
                nk = 10.4 * weight + 600;
        }

        java.text.DecimalFormat nf = new java.text.DecimalFormat("###,##0.00");

        mneed.setText("");

        if (section == 1) {
            //mmode.setText("健走模式");
            //sview.setBackgroundResource(R.drawable.index);
//            sview.setBackgroundColor(Color.LTGRAY);

        } else {
            //mmode.setText("跑步模式");
//            sview.setBackgroundResource(R.drawable.back);
        }

        sensorMgr.registerListener(SensorL, sensor, SensorManager.SENSOR_DELAY_GAME);
        if (mygoal != 0) {


        }
    }


    private final SensorEventListener SensorL = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (pause == 0 && start == 1) {
                double x = event.values[0];
                double y = event.values[1];
                double z = event.values[2];

                double g = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

                java.text.DecimalFormat nf = new java.text.DecimalFormat("###,##0.000");
                //Log.i("TAG", "g: " + g + ", km " + nf.format(now_km/1000) + ", " + now_km);

                if (g > 1.5)
                    dcounter++;

                if (dcounter > 10) {
                    dcounter = 0;
                    steps++;
                    //體重（公斤）×距離（公里）× 1.036
                    now_km = (steps * 0.5);


                    now_shows = 0;

                    if (section == 0) {
                        now_shows = (now_km / 1000) * weight * 1.036;
                    } else {
                        now_shows = (now_km / 1000) * weight * 0.98;
                    }
                    Log.d(TAG, "onSensorChanged: "+weight);


//                    Message msg = new Message();
//                    msg.what = MSG_UPDATE_KM;
//                    myHandler.sendMessage(msg);
                    java.text.DecimalFormat nf1 = new java.text.DecimalFormat("###,##0.000");
                    km.setText(nf1.format(now_km / 1000));

                    Log.i("TAG", "data: " + nf1.format(now_km / 1000));

                    java.text.DecimalFormat nf2 = new java.text.DecimalFormat("###,##0.0000");
                    shows.setText(nf2.format(now_shows));
                    Log.i("TAG", "data: " + nf2.format(now_shows));

                    ssteps.setText(Integer.toString(steps));

                    Log.d(TAG, "onSensorChanged: "+mygoal);
                    if (mygoal != 0) {
                        if (steps == mygoal) {
                            mper.start();
                            mygoal = 0;
                        }

                    }
                }
            }
        }

        public void onAccuracyChanged(Sensor s, int accuracy) {
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Kill myself
        if (timer != null)
            timer.cancel();
    }

//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        menu.add(0, MENU_QUERY, 0, "每日查詢");
//        menu.add(0, MENU_LOGIN, 0, "切換使用者");
//        menu.add(0, MENU_USERMGR, 0, "使用者管理");
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case MENU_QUERY:
//                Intent app = new Intent(WalKStepActivity.my, Query.class);
//                Bundle rdata = new Bundle();
//                rdata.putString("name", name);
//                app.putExtras(rdata);
//                startActivity(app);
//                return true;
//            case MENU_LOGIN:
//                login();
//                return true;
//            case MENU_USERMGR:
//                app = new Intent(WalKStepActivity.my, UserMgr.class);
//                startActivity(app);
//                return true;
//            case MENU_EXIT:
//
//                return true;
//        }
//
//        return true;
//    }


    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

    private double time;
    public Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DIALOG_REFRESH:
                    int hours = counter / 3600;
                    int minutes = (counter % 3600) / 60;
                    int seconds = counter % 60;
                    time = hours + minutes + seconds;
                    ttimer.setText(minutes + ":" + seconds);
                    counter++;

                    break;
                case MSG_UPDATE_KM:


                    break;

                default:
                    label.setText("目前消耗熱量:");
            }
            super.handleMessage(msg);
        }
    };

    private Runnable updateTimer = new Runnable() {
        public void run() {
            handler.postDelayed(this, 500);
            mCalendar = Calendar.getInstance();
            df = new SimpleDateFormat("HH:mm:ss");
            str = df.format(mCalendar.getTime());
            ntime.setText(str);

        }
    };


    public class DateTask extends TimerTask {
        public void run() {
            Message msg = new Message();
            msg.what = MSG_DIALOG_REFRESH;
            myHandler.sendMessage(msg);
        }
    }


    void register() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("請輸入資料");


        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        TextView tname = new TextView(this);
        tname.setText("姓名: ");
        sname = new EditText(this);
        sname.setText("");
        ll.addView(tname);
        ll.addView(sname);

        TextView tlogin = new TextView(this);
        tlogin.setText("生日: ");
        s1 = new EditText(this);
        s1.setInputType(InputType.TYPE_CLASS_NUMBER);
        s1.setText("");
        ll.addView(tlogin);
        ll.addView(s1);

        TextView tpwd = new TextView(this);
        tpwd.setText("體重: ");
        s2 = new EditText(this);
        s2.setInputType(InputType.TYPE_CLASS_NUMBER);
        s2.setText("");
        ll.addView(tpwd);
        ll.addView(s2);

        TextView tpwd1 = new TextView(this);
        tpwd1.setText("身高: ");
        s3 = new EditText(this);
        s3.setInputType(InputType.TYPE_CLASS_NUMBER);
        s3.setText("");
        ll.addView(tpwd1);
        ll.addView(s3);

        TextView sex = new TextView(this);
        sex.setText("性別: ");
        s4 = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"男生", "女生"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s4.setAdapter(adapter);
        ll.addView(sex);
        ll.addView(s4);

        TextView type = new TextView(this);
        type.setText("模式: ");
        s5 = new Spinner(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"跑步模式", "健走模式"});
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s5.setAdapter(adapter2);
        ll.addView(type);
        ll.addView(s5);

        // Set an EditText view to get user input
        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                section = s5.getSelectedItemPosition();

                if (sname.getText().toString().equals("") || s1.getText().toString().equals("") || s2.getText().toString().equals("") ||
                        s3.getText().toString().equals("")) {
                    finish();
                }

                name = sname.getText().toString();
                age = 20;
                weight = Integer.valueOf(s2.getText().toString());
                tall = Integer.valueOf(s3.getText().toString());
                tsex = s4.getSelectedItemPosition();

                //double nk = 54.34+(13.88* weight)+(4.16*tall)-(3.43*age)-(112.4*tsex);
                SQLHandler.insert_register(WalKStepActivity.my, name, Integer.toString(tsex), Integer.toString(age),
                        Integer.toString(tall), Integer.toString(weight));


                double nk = 0;

                if (age <= 30) {
                    if (tsex == 0)
                        nk = 15.2 * weight + 680;
                    else
                        nk = 14.6 * weight + 450;

                } else if (age > 30 && age <= 60) {
                    if (tsex == 0)
                        nk = 11.5 * weight + 830;
                    else
                        nk = 8.6 * weight + 830;
                }
                if (age > 60) {
                    if (tsex == 0)
                        nk = 13.4 * weight + 490;
                    else
                        nk = 10.4 * weight + 600;
                }

                java.text.DecimalFormat nf = new java.text.DecimalFormat("###,##0.00");

                //mneed.setText("目前需消耗(卡) " + nf.format(nk));

                if (section == 1) {
                    //mmode.setText("健走模式");
                    //sview.setBackgroundResource(R.drawable.index);
//                    sview.setBackgroundColor(Color.LTGRAY);
                } else {
                    //mmode.setText("跑步模式");
//                    sview.setBackgroundResource(R.drawable.back);
                }

                sensorMgr.registerListener(SensorL, sensor, SensorManager.SENSOR_DELAY_GAME);

            }
        });

        alert.show();
    }

    void login() {
        data = SQLHandler.getAllAccount(this);

        if (data == null) {
            register();
            return;
        } else if (data.size() == 0) {
            register();
            return;
        }

        final CharSequence[] child_id = new String[data.size()];
        int checked = 0;

        for (int i = 0; i < data.size(); i++) {
            child_id[i] = data.get(i).name;
            Log.i(TAG, data.get(i).name);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mWalKStepActivity);
        builder.setCancelable(false);
        builder.setTitle("請選使用者");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        TextView type = new TextView(this);
        type.setText("模式: ");
        s5 = new Spinner(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"跑步模式", "健走模式"});
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s5.setAdapter(adapter2);
        ll.addView(type);
        ll.addView(s5);

        mchildid = 0;

        builder.setSingleChoiceItems(child_id, checked, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mchildid = which;
            }
        });

        builder.setPositiveButton("選好了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.setNegativeButton("建立帳號", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                register();
            }
        });

        AlertDialog alert = builder.create();
        alert.setView(sv);
        alert.show();

    }

    //show message
    public void openOptionsDialog(String info) {
        new AlertDialog.Builder(this)
                .setTitle("訊息")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                mshow = false;
                            }
                        }
                )
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mper!=null){
            mper.pause();
        }
    }

    public void getStoreList() {
//int search_list_size = MyGoogleMap.my.search_list.size();

        Date today = Calendar.getInstance().getTime();
        Date date;
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sdate = sdf.format(today);

        Calendar scalendar = new GregorianCalendar(2010, 1, 1);
        scalendar.setTime(today);

        int times = 0, times2 = 0;
        int steps = 0;
        //for (int i=0; i<=0; i++)
        {
            //Query DATABASE
            try {
                Log.i("TAG", recdata.NAME + "='" + name + "' and " + recdata.CTIME + "='" + sdate + "'");
                cursor = db.query(SQLiteHelper.USER_REC_TABLE, null, recdata.NAME + "='" + name + "' and " + recdata.CTIME + "='" + sdate + "'", null, null, null, null);

                cursor.moveToFirst();

                //no data
                times = 0;
                while (!cursor.isAfterLast()) {
                    recdata ndata = new recdata();
                    ndata.id = cursor.getString(0);
                    ndata.name = cursor.getString(1);
                    ndata.mode = cursor.getString(2);
                    ndata.kl = cursor.getString(3);
                    times += Double.valueOf(ndata.kl);
                    ndata.step = cursor.getString(4);
                    steps += Integer.valueOf(ndata.step);
                    ndata.distance = cursor.getString(5);
                    times2 += Double.valueOf(ndata.distance);
                    ndata.detail = cursor.getString(6);
                    ndata.detail2 = cursor.getString(7);
                    ndata.ctime = cursor.getString(8);
                    cursor.moveToNext();
                    Log.d(TAG, "getStoreList: " + steps);

                    Log.d(TAG, "getStoreList: " + times);
                    Log.d(TAG, "getStoreList: " + times2);
                    MySharedPrefernces.saveUserDhot(WalKStepActivity.this, String.valueOf(times));
                    MySharedPrefernces.saveUserKm(getApplicationContext(),String.valueOf(times2));
                    MySharedPrefernces.saveUserStep(getApplicationContext(), String.valueOf(steps));

                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

                ++DB_VERSION;
                dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
            }


        }

    }
}
