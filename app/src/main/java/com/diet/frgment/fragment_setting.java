package com.diet.frgment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.diet.DBSQL;
import com.diet.MemberData;
import com.diet.MySharedPrefernces;
import com.diet.QAActivity;
import com.diet.R;
import com.diet.ResultData;
import com.diet.UserActivity;
import com.diet.main;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.jackpan.libs.mfirebaselib.MfiebaselibsClass;
import com.jackpan.libs.mfirebaselib.MfirebaeCallback;
import com.sqlite.SQLiteHelper;
import com.sqlite.hotdiary;
import com.sqlite.member;
import com.weather.MainActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by JackPan on 2017/12/6.
 */

public class fragment_setting extends Fragment implements MfirebaeCallback {
    private View v;
    private ListView listview;
    private ArrayList<HashMap<String, Object>> menu;
    TextView msg;
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private Cursor cursor;
    member mydata;
    public ArrayList<member> memberlist;
    private static int DB_VERSION = 1;
    int selector = 0;
    private TextView tname, tsex, twe, the, twa, tage;
    private EditText name, we, he, wa, age;
    private Spinner sex;
    int myYear, myMonth, myDay;
    String n1, n2, n3, n4, n5, n6;
    private int year, month, day;
    public static main mymain;
    public String NAME;
    public String SEX;
    public String HEIGHT;
    public String WEIGHT;
    public String BIRTHDAY;
    public String BMR;
    public String BMI;
    public String STANDARDWEIGHT;
    public String WEIGHTRANGE;
    public String RECOMMENDEDGEAT;
    public String TODAY_HOT;
    public String TODAY_DHOT;
    public String TODAY_KM;
    public String TODAY_STEPS;
    public String MEMBER_PHOTO;
    public static String account;
    private MfiebaselibsClass mfiebaselibsClass;
    private  boolean b;
    MemberData mMemberData =null;
    private  main mMain;
    String url = "https://food-4997e.firebaseio.com/";
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    private ProgressDialog progressDialog;
    private  String object ;
    public fragment_setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this vfragment




        v = inflater.inflate(R.layout.fragment_sport2, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        Log.d(TAG, "onCreateView: " + MySharedPrefernces.getUserId(getActivity()));

        menu = new ArrayList<HashMap<String, Object>>();
        msg = (TextView) v.findViewById(R.id.rrmsg);
        memberlist = new ArrayList<member>();
        HashMap<String, Object> map = new HashMap<String, Object>();

//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "虛擬教練" );
//        map.put("ItemText", "sport man");
//        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "卡路里計算" );
//        map.put("ItemText", "sport item");
//        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "真人影片教學" );
//        map.put("ItemText", "training video");
//        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "基本資料設定");
        map.put("ItemText", "setup");
        menu.add(map);

//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "計步器" );
//        map.put("ItemText", "Pedometer");
//        menu.add(map);
        map = new HashMap<String, Object>();
        map.put("ItemTitle", "天氣預報");
        map.put("ItemText", "weather");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "常見問題");
        map.put("ItemText", "QANDA");
        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "音樂播放器" );
//        map.put("ItemText", "MusicPlayer");
//        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "討論區" );
//        map.put("ItemText","share");
//        menu.add(map);
        map = new HashMap<String, Object>();
        map.put("ItemTitle", "會員中心");
        map.put("ItemText", "membercenter");
        menu.add(map);
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "結束程式" );
//        map.put("ItemText", "login out");
//        menu.add(map);

        //然後加入項目之後就準備接下來的工作
        SimpleAdapter listitemAdapter = new SimpleAdapter(getActivity(),
                menu,
                R.layout.no_listview_style,
                new String[]{"ItemTitle", "ItemText"},
                new int[]{R.id.topTextView, R.id.bottomTextView}
        );
        listview.setAdapter(listitemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //看使用者選什麼，就會去開啟服務
                Intent intent = null;

                switch (arg2) {
//                    case 0:
//                        intent = new Intent();
//                        intent.setClass(main.this, GridViewActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 1:
//                        intent = new Intent();
//                        intent.setClass(main.this, sport.class);
//
//                        startActivity(intent);
//                        break;
//                    case 2:
//                        tips_selector = 0;
//                        choice();
//                        break;
                    case 0:
//
                        if (mMemberData==null) {
                            addmember();
                        } else {
                            fixmember();
                        }


                        break;
//                    case 4:
//                        intent = new Intent();
//                        intent.setClass(main.this, WalKStepActivity.class);
//
//                        startActivity(intent);
//                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), QAActivity.class));
                        break;
//                    case 7:
//                        startActivity(new Intent(main.this,MusicActivity.class));
//                        break;
//                    case 8:
//                        startActivity(new Intent(main.this, com.diet.MainActivity.class));
//                        break;
                    case 3:
//                        startActivity(new Intent(getActivity(), UserActivity.class));
                        Intent i = new Intent();

                        Bundle b = new Bundle();
                        b.putString("data",object);
                        i.putExtras(b);
                        i.setClass(getActivity(), UserActivity.class);
                        startActivity(i);


                        break;
//                    case 10:
//                        finish();
//                        break;
                }
            }
        });


        final Calendar c = Calendar.getInstance();
        myYear = c.get(Calendar.YEAR);
        myMonth = c.get(Calendar.MONTH);
        myDay = c.get(Calendar.DAY_OF_MONTH);

        //資料庫
        try {
            dbHelper = new SQLiteHelper(getActivity(), SQLiteHelper.DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ++DB_VERSION;
            dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
        }


        int nodata = 0;


        //first login
        try {
            cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

            cursor.moveToFirst();

            //no data
            if (cursor.isAfterLast()) {
//                openOptionsDialog("查無data, 請更新database");
                nodata = 1;
                //return;
            }


            while (!cursor.isAfterLast()) {
                member sitem = new member();
                sitem.id = cursor.getString(0);
                sitem.name = cursor.getString(1);
                sitem.sex = cursor.getString(2);
                sitem.weight = cursor.getString(3);
                sitem.height = cursor.getString(4);
                sitem.waist = cursor.getString(5);
                sitem.age = cursor.getInt(6);
                sitem.rdate = cursor.getString(7);

                memberlist.add(sitem);
                cursor.moveToNext();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + "in");
        Log.d(TAG, "onResume: "+mMemberData);
        refresh_msg();
        progressDialog.dismiss();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(getActivity(),"讀取中","請稍候");

        mfiebaselibsClass = new MfiebaselibsClass(getActivity(), fragment_setting.this);
        mfiebaselibsClass.getFirebaseDatabase(url +MySharedPrefernces.getUserId(getActivity()), "id");

    }

    private static final String TAG = "fragment_setting";

    public void refresh_msg() {
        String rmsg = "";
        double bmr = 0, bmi = 0, standrdweight = 0;
//
//        if (memberlist.size() == 0) {
//            addmember();
//            return;
//        }
//        boolean b = MySharedPrefernces.getIsBuyed(getActivity());
//        Log.d(TAG, "refresh_msg: "+b);
//        if(!b){
//            Log.d(TAG, "refresh_msg: "+"false");
//            addmember();
//            return;
//        }else {
//            Log.d(TAG, "refresh_msg: "+"true");

//        }


        if (mMemberData == null) {
            return;
        }
        //selector = diet.dt.selector;
//        mydata = memberlist.get(selector);
//        if(mMemberData==null){
//            addmember();
//            return;
//        }
        int weight = Integer.valueOf(mMemberData.weight);
        int height = Integer.valueOf(mMemberData.height);
        int age = 20;

        double waist = Double.valueOf(28);
        String rwaist = "";

        //cal bmr
        if (mMemberData.sex.equals("0")) {
            bmr = (13.7 * weight) + (5.0 * height) - (6.8 * age) + 66;
            //標準體重好像我公式有給錯 正確公式＝身高(m)×身高(m)×22
            rwaist = (waist <= 94) ? "正常" : "異常";
        } else {
            bmr = (9.6 * weight) + (1.8 * height) - (4.7 * age) + 655;
            rwaist = (waist <= 80) ? "正常" : "異常";
        }

        //cal bmi
        //bmi = (double) weight / (height*height);
        double h2 = (double) height / 100;

        standrdweight = (h2 * h2) * 22;

        //計算BMI
        bmi = (double) weight / (h2 * h2);


        double standrdweightratio = weight / standrdweight;

        double sResult = standrdweight * 30;

        DecimalFormat mDecimalFormat = new DecimalFormat("#.##");

        if (standrdweightratio <= 0.9) sResult = standrdweight * 35;
        if (standrdweightratio >= 1.1) sResult = standrdweight * 25;

        //1公斤=2.2046磅
        //增重:體重磅數*18
        double inc = weight * 2.2046 * 18;
        //保持:體重磅數*15
        double keep = weight * 2.2046 * 15;
        //減重:體重磅數*12
        double dec = weight * 2.2046 * 12;

//        rmsg += "目前的熱量/消耗熱量:" + food.hot + "/" + sport.hot + "\n";
        rmsg += "體重" + weight + "\n";
        rmsg += "基礎代謝率(BMR):" + mDecimalFormat.format(bmr) + "\n";
        String rbmi = (bmi > 18.5 && bmi < 24) ? "(BMI正常)" : "(異常BMI)";
        rmsg += "BMI:" + mDecimalFormat.format(bmi) + rbmi + "\n";
        //String rst =(standrdweight < (weight*0.1))?"(體重正常)":"(體重太重)";
        rmsg += "標準體重:" + ((Math.round(standrdweight) / 10) * 10) + "\n";
        rmsg += "理想體重範圍:" + Math.round(standrdweight * .9 * 10) / 10 + " ~ " + Math.round(standrdweight * 1.1 * 10) / 10 + "\n";
        rmsg += "建議熱量:" + mDecimalFormat.format(sResult) + "\n";
        NAME = mMemberData.name;
        HEIGHT = mMemberData.height;
        BIRTHDAY = mMemberData.birthday;


        WEIGHT = String.valueOf(weight);
        BMR = mDecimalFormat.format(bmr);
        BMI = mDecimalFormat.format(bmi) + rbmi;
        WEIGHTRANGE = String.valueOf(((Math.round(standrdweight) / 10) * 10));
        STANDARDWEIGHT = Math.round(standrdweight * .9 * 10) / 10 + " ~ " + Math.round(standrdweight * 1.1 * 10) / 10;
        RECOMMENDEDGEAT = mDecimalFormat.format(sResult);
        final Calendar c = Calendar.getInstance();
        myYear = c.get(Calendar.YEAR);
        myMonth = c.get(Calendar.MONTH);
        myDay = c.get(Calendar.DAY_OF_MONTH);

        String date = String.valueOf(year) + "-" + String.valueOf(myMonth + 1) + "-" + String.valueOf(myDay);

        String[] whereValue = {date};

        double getdata = 0;
        double costdata = 0;

        try {
            cursor = db.query(SQLiteHelper.DIARY_NAME, null, hotdiary.RDATE + "=?", whereValue, null, null, null);

            cursor.moveToFirst();

            //no data
            if (cursor.isAfterLast()) {
                //openOptionsDialog("找不到資料");
                //return;
            }


            while (!cursor.isAfterLast()) {
                hotdiary sitem = new hotdiary();
                sitem.id = cursor.getString(0);
                sitem.ditem = cursor.getString(1);
                sitem.sitem = cursor.getString(2);
                sitem.dhot = cursor.getString(3);
                sitem.shot = cursor.getString(4);
                sitem.rdate = cursor.getString(5);
                Log.d(TAG, "refresh_msg: " + sitem.dhot);
                if (!sitem.dhot.equals("-1")) {
                    getdata += Double.valueOf(sitem.dhot);
                }

                costdata += Double.valueOf(sitem.dhot);

                cursor.moveToNext();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

<<<<<<< HEAD

=======
>>>>>>> 3dd9cba2f66043f77344ae260d2bd88bfcb886fb
        TODAY_DHOT = MySharedPrefernces.getUserDhot(getActivity());
        TODAY_HOT = MySharedPrefernces.getUserhot(getActivity());
        TODAY_KM = MySharedPrefernces.getUserKm(getActivity());
        TODAY_STEPS = MySharedPrefernces.getUserStep(getActivity());
<<<<<<< HEAD
        if(!TODAY_DHOT.equals("")){
            double d = Double.parseDouble(TODAY_DHOT);
            rmsg += "今日總共消耗熱量:" +String.format("%.3f", d) + "\n";
        }


=======
>>>>>>> 3dd9cba2f66043f77344ae260d2bd88bfcb886fb
        rmsg += "今日總共攝取熱量:" +TODAY_HOT + "\n";
        rmsg += "今日總共消耗熱量:" +TODAY_DHOT + "\n";
        rmsg += "今日總公里數:" + TODAY_KM + "\n";
        rmsg += "今日總步數:" + TODAY_STEPS + "\n";
        if(mMemberData!=null){
            NAME = mMemberData.name;
            HEIGHT = mMemberData.height;
            BIRTHDAY = mMemberData.birthday;
            WEIGHT = mMemberData.weight;
            BMR = mMemberData.bmr;
            BMI = mMemberData.bmi;
            WEIGHTRANGE = mMemberData.Weightrange;
            STANDARDWEIGHT =mMemberData.StandardWeight;
            RECOMMENDEDGEAT = mMemberData.Recommendedheat;

        }
        Log.d(TAG, "refresh_msg: "+TODAY_KM);
        msg.setText(rmsg);
        Log.d(TAG, "refresh_msg: "+msg.getText().toString());
        setMemberlist();

    }

    private void addmember() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("個人資料設定");
//        alert.setMessage("請輸入帳號 和 密碼");
        ScrollView sv = new ScrollView(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        tname = new TextView(getActivity());
        tname.setText("姓名: ");
        name = new EditText(getActivity());
        name.setText("");
        ll.addView(tname);
        ll.addView(name);

        tsex = new TextView(getActivity());
        tsex.setText("性別: ");
        sex = new Spinner(getActivity());
        final String sexs[] = {"男", "女"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sexs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        sex.setAdapter(spinnerArrayAdapter);

        ll.addView(tsex);
        ll.addView(sex);

        twe = new TextView(getActivity());
        twe.setText("體重: ");
        we = new EditText(getActivity());
        we.setText("");
        ll.addView(twe);
        ll.addView(we);

        the = new TextView(getActivity());
        the.setText("身高: ");
        he = new EditText(getActivity());
        he.setText("");
        ll.addView(the);
        ll.addView(he);

        tage = new TextView(getActivity());
        tage.setText("生日: ");
        age = new EditText(getActivity());
        age.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });
        age.setText("");
        ll.addView(tage);
        ll.addView(age);
        // Set an EditText view to get user input
        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                n1 = name.getText().toString();
                n2 = Integer.toString(sex.getSelectedItemPosition());
                n3 = we.getText().toString();
                n4 = he.getText().toString();
                n5 = "28";
                n6 = age.getText().toString();

                if (n1.equals("") || n2.equals("") || n3.equals("") || n4.equals("") || n5.equals("") || n6.equals("")) {
                    openOptionsDialog("有值沒填");
                    getActivity().finish();
                    return;
                } else {
                    MySharedPrefernces.saveUserName(getActivity(), n1);
                    MySharedPrefernces.saveUserSex(getActivity(), sex.getSelectedItemPosition());
                    MySharedPrefernces.saveUserTall(getActivity(), Integer.parseInt(n4));
                    MySharedPrefernces.saveUserWeight(getActivity(), Integer.parseInt(n3));
                    NAME = n1;
                    SEX = String.valueOf(sex.getSelectedItemPosition());
                    HEIGHT = n4;
                    WEIGHT = n3;
                    BIRTHDAY = n6;


                    DBSQL.insert(getActivity(), n1, n2, n4, n3, n6, n5);
                    memberlist.clear();
                    try {
                        cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            member sitem = new member();
                            sitem.id = cursor.getString(0);
                            sitem.name = cursor.getString(1);
                            sitem.sex = cursor.getString(2);

                            sitem.weight = cursor.getString(3);
                            sitem.height = cursor.getString(4);
                            sitem.waist = cursor.getString(5);
                            sitem.age = cursor.getInt(6);
                            sitem.rdate = cursor.getString(7);

                            memberlist.add(sitem);
                            cursor.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    selector = memberlist.size() - 1;

                    account = n1;
<<<<<<< HEAD
                    setMemberlist();
=======
                        setMemberlist();
>>>>>>> 3dd9cba2f66043f77344ae260d2bd88bfcb886fb
                    refresh_msg();
                    MySharedPrefernces.saveIsBuyed(getActivity(), true);
                }


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();


    }

    private void setData() {
        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                age.setText(setDateFormat(year, month, day));
            }

        }, mYear, mMonth, mDay).show();
    }

    private String setDateFormat(int year, int monthOfYear, int dayOfMonth) {
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private void openOptionsDialog(String info) {
        new AlertDialog.Builder(getActivity())
                .setTitle("msg")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                            }
                        }
                )
                .show();
    }

    private void fixmember() {
        Log.d(TAG, "fixmember: "+"in");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("修改");
        alert.setMessage("請輸入");

        ScrollView sv = new ScrollView(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        tname = new TextView(getActivity());
        tname.setText("姓名: ");
        name = new EditText(getActivity());

        ll.addView(tname);
        ll.addView(name);

        tsex = new TextView(getActivity());
        tsex.setText("性別: ");
        sex = new Spinner(getActivity());
        final String sexs[] = {"男", "女"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sexs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        sex.setAdapter(spinnerArrayAdapter);


        ll.addView(tsex);
        ll.addView(sex);

        twe = new TextView(getActivity());
        twe.setText("體重: ");
        we = new EditText(getActivity());

        ll.addView(twe);
        ll.addView(we);

        the = new TextView(getActivity());
        the.setText("身高: ");
        he = new EditText(getActivity());

        ll.addView(the);
        ll.addView(he);

        tage = new TextView(getActivity());
        tage.setText("生日: ");
        age = new EditText(getActivity());
        age.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤

                setData();
            }
        });
        ll.addView(tage);
        ll.addView(age);
        // Set an EditText view to get user input

        if (mMemberData == null) {
            we.setText("有值沒填");
            he.setText("有值沒填");
            age.setText("有值沒填");
            name.setText("有值沒填");
            sex.setSelection(0);


        } else {
            name.setText(mMemberData.name);
            we.setText(mMemberData.weight);
            he.setText(mMemberData.height);
            age.setText(mMemberData.birthday + "");
            if (mMemberData.sex.equals("0"))
                sex.setSelection(0);
            else
                sex.setSelection(1);
        }
        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String id = "122345";
                n1 = name.getText().toString();
                n2 = Integer.toString(sex.getSelectedItemPosition());
                n3 = we.getText().toString();
                n4 = he.getText().toString();
                n5 = "28";
                n6 = age.getText().toString();

                Log.i("TAG", "n2: " + n2);

                if (n3.equals("") || n4.equals("") || n5.equals("") || n6.equals("") || n1.equals("")) {
                    openOptionsDialog("有值沒填");
                    return;
                } else {
                    MySharedPrefernces.saveUserName(getActivity(), n1);
                    MySharedPrefernces.saveUserSex(getActivity(), sex.getSelectedItemPosition());
                    MySharedPrefernces.saveUserTall(getActivity(), Integer.parseInt(n4));
                    MySharedPrefernces.saveUserWeight(getActivity(), Integer.parseInt(n3));
                    NAME = name.getText().toString();
                    SEX = Integer.toString(sex.getSelectedItemPosition());
                    HEIGHT = he.getText().toString();
                    WEIGHT = we.getText().toString();
                    BIRTHDAY = age.getText().toString();
                    mMemberData.name = NAME;
                    mMemberData.sex = SEX;
                    mMemberData.height = HEIGHT;
                    mMemberData.weight = WEIGHT;
                    mMemberData.birthday = BIRTHDAY;
//                    setMember(NAME,SEX,HEIGHT,WEIGHT,BIRTHDAY);
                    DBSQL.update(getActivity(), n1, n2, n3, n4, n5, n6, id);
                    memberlist.clear();
                    try {
                        cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            member sitem = new member();
                            sitem.id = cursor.getString(0);
                            sitem.name = cursor.getString(1);
                            sitem.sex = cursor.getString(2);
                            sitem.weight = cursor.getString(3);
                            sitem.height = cursor.getString(4);
                            sitem.waist = cursor.getString(5);
                            sitem.age = cursor.getInt(6);
                            sitem.rdate = cursor.getString(7);

                            memberlist.add(sitem);
                            cursor.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    selector = memberlist.size() - 1;

                    refresh_msg();

                }


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();


    }

    @Override
    public void getDatabaseData(Object o) {

        Gson gson = new Gson();
        String jsonInString = gson.toJson(o);
        object = gson.toJson(o);
        Log.d(TAG, "getDatabaseData: " + jsonInString);
        mMemberData = gson.fromJson(jsonInString, MemberData.class);
        Log.d(TAG, "getDatabaseData: " + mMemberData.toString());
        if (mMemberData == null) {
            addmember();
        } else {
            MySharedPrefernces.saveUserName(getActivity(),mMemberData.name);
            MySharedPrefernces.saveUserPic(getActivity(),mMemberData.member_photo);
            MySharedPrefernces.saveUserStep(getActivity(),mMemberData.today_steps);
            MySharedPrefernces.saveUserKm(getActivity(),mMemberData.today_km);
            MySharedPrefernces.saveUserhot(getActivity(),mMemberData.today_hot);
            MySharedPrefernces.saveUserDhot(getActivity(),mMemberData.today_dhot);
<<<<<<< HEAD
            MySharedPrefernces.saveUserName(getActivity(),mMemberData.name);
            MySharedPrefernces.saveUserTall(getActivity(),Integer.parseInt(mMemberData.height));
            MySharedPrefernces.saveUserWeight(getActivity(),Integer.parseInt(mMemberData.weight));
=======
>>>>>>> 3dd9cba2f66043f77344ae260d2bd88bfcb886fb
            refresh_msg();
            progressDialog.dismiss();

        }



    }

    @Override
    public void getDeleteState(boolean b, String s, Object o) {

    }

    @Override
    public void createUserState(boolean b) {

    }

    @Override
    public void useLognState(boolean b) {

    }

    @Override
    public void getuseLoginId(String s) {
        Log.d(TAG, "getuseLoginId: " + s);

    }

    @Override
    public void getuserLoginEmail(String s) {

    }

    @Override
    public void resetPassWordState(boolean b) {

    }

    @Override
    public void getFireBaseDBState(boolean b, String s) {
        Log.d(TAG, "getFireBaseDBState: " + b);
        Log.d(TAG, "getFireBaseDBState: " + s);
        if(mMemberData==null){
            Log.d(TAG, "getFireBaseDBState: "+"null");
        }else {
            mfiebaselibsClass = new MfiebaselibsClass(getActivity(), fragment_setting.this);


        }

    }

    @Override
    public void getFirebaseStorageState(boolean b) {

    }

    @Override
    public void getFirebaseStorageType(String s, String s1) {

    }

    @Override
    public void getsSndPasswordResetEmailState(boolean b) {

    }

    @Override
    public void getUpdateUserName(boolean b) {

    }

    @Override
    public void getUserLogoutState(boolean b) {

    }

    private  void setMember(String name,String sex , String h,String w,String b){
        HashMap<String, String> map = new HashMap<>();
        String key = MySharedPrefernces.getUserId(getActivity());
        map.put(MemberData.ID, key);
        map.put(MemberData.NAME, name);
        map.put(MemberData.SEX, sex);
        map.put(MemberData.HEIGHT, h);
        map.put(MemberData.WEIGHT, w);
        map.put(MemberData.BIRTHDAY, b);
        mfiebaselibsClass.setFireBaseDB(url + key, key, map);

    }
    private void setMemberlist() {

        HashMap<String, String> map = new HashMap<>();
        String key = MySharedPrefernces.getUserId(getActivity());
        map.put(MemberData.ID, key);
        map.put(MemberData.NAME, NAME);
        map.put(MemberData.SEX, SEX);
        map.put(MemberData.HEIGHT, HEIGHT);
        map.put(MemberData.WEIGHT, WEIGHT);
        map.put(MemberData.BIRTHDAY, BIRTHDAY);
        map.put(MemberData.BMI, BMI);
        map.put(MemberData.BMR, BMR);
        map.put(MemberData.WEIGHTRANGE, WEIGHTRANGE);
        map.put(MemberData.RECOMMENDEDGEAT, RECOMMENDEDGEAT);
        map.put(MemberData.STANDARDWEIGHT, STANDARDWEIGHT);
        map.put(MemberData.TODAY_DHOT, MySharedPrefernces.getUserDhot(getActivity()));
        map.put(MemberData.TODAY_HOT, MySharedPrefernces.getUserhot(getActivity()));
        map.put(MemberData.TODAY_KM, MySharedPrefernces.getUserKm(getActivity()));
        map.put(MemberData.TODAY_STEPS, MySharedPrefernces.getUserStep(getActivity()));
        map.put(MemberData.MEMBER_PHOTO, MySharedPrefernces.getUserPic(getActivity()));
        mfiebaselibsClass.setFireBaseDB(url + key, key, map);

    }

}
