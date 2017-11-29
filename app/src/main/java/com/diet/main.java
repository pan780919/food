package com.diet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sqlite.RecWeight;
import com.sqlite.SQLiteHelper;
import com.sqlite.hotdiary;
import com.sqlite.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//主選單
public class main extends Activity
{
    private ListView listview;
    private String abouttp;

    //public ProgressDialog myDialog = null;
    private ArrayList<HashMap<String, Object>> menu;
    private int result_check;

    private String murl = "";
    public static String account;

    private int year, month, day;

    public ProgressDialog myDialog = null;

    public ArrayList<member> memberlist;

    private TextView tname, tsex, twe, the, twa, tage;
    private EditText name, we, he, wa, age;
    private Spinner sex;
    String n1, n2, n3, n4, n5, n6;

    public String tips [] = null;

    String mtime;

    int selector = 0;
    int selector1 = 0;
    int selector2;

    EditText ddate;

    TextView msg;

    final Calendar c = Calendar.getInstance();

    String url_list;

    static main mymain;

    member mydata;

    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private Cursor cursor;

    private static int DB_VERSION = 1;

    int tips_size=6;
    int tips_selector=0;

    String pic;
    String picfilename;

    int myYear, myMonth, myDay;

    CharSequence[] child_id = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        mymain=this;

        tips = this.getResources().getStringArray(R.array.diet_tips);

        tips_size = tips.length;

        final Calendar c = Calendar.getInstance();
        myYear = c.get(Calendar.YEAR);
        myMonth = c.get(Calendar.MONTH);
        myDay = c.get(Calendar.DAY_OF_MONTH);

        //資料庫
        try{
            dbHelper = new SQLiteHelper(this, SQLiteHelper.DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            ++ DB_VERSION;
            dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
        }


        int nodata=0;

        memberlist = new ArrayList<member>();

        //first login
        try{
            cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

            cursor.moveToFirst();

            //no data
            if (cursor.isAfterLast())
            {
                //openOptionsDialog("查無data, 請更新database");
                nodata=1;
                //return;
            }


            while(!cursor.isAfterLast())
            {
                member sitem = new member();
                sitem.id = cursor.getString(0);
                sitem.name = cursor.getString(1);
                sitem.sex = cursor.getString(2);
                sitem.weight = cursor.getString(3);
                sitem.height = cursor.getString(4);
                sitem.waist = cursor.getString(5);
                sitem.age = cursor.getString(6);
                sitem.rdate = cursor.getString(7);

                memberlist.add(sitem);
                cursor.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        //登入後, 顯示下列主選單
        msg = (TextView)findViewById(R.id.rrmsg);
        listview = (ListView)findViewById(R.id.listview);

        menu = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map = new HashMap<String, Object>();
        map.put("ItemTitle", "虛擬教練" );
        map.put("ItemText", "sport man");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "卡路里計算" );
        map.put("ItemText", "sport item");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "真人影片教學" );
        map.put("ItemText", "training video");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "基本資料設定" );
        map.put("ItemText", "setup");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "計步器" );
        map.put("ItemText", "Pedometer");
        menu.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemTitle", "結束程式" );
        map.put("ItemText", "login out");
        menu.add(map);

        //然後加入項目之後就準備接下來的工作
        SimpleAdapter listitemAdapter=new SimpleAdapter(this,
                menu,
                R.layout.no_listview_style,
                new String[]{"ItemTitle","ItemText"},
                new int[]{R.id.topTextView,R.id.bottomTextView}
        );
        listview.setAdapter(listitemAdapter);
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                //看使用者選什麼，就會去開啟服務
                Intent intent = null;

                switch (arg2)
                {
                    case 0:
                        intent = new Intent();
                        intent.setClass(main.this, food.class);

                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent();
                        intent.setClass(main.this, sport.class);

                        startActivity(intent);
                        break;
                    case 2:
                        tips_selector = 0;
                        choice();
                        break;
                    case 3:
                        modifymember();
                        break;
                    case 4:
                        intent = new Intent();
                        intent.setClass(main.this, WalKStepActivity.class);

                        startActivity(intent);
                        break;
                    case 5:
                        finish();
                        break;
                }
            }
        });


        Log.i("TAG", Integer.toString(memberlist.size()));

        if (nodata == 1)
        {
            addmember();
        }
        else
        {

            final CharSequence[] child_id = new String[memberlist.size()];

            selector = 0;

            for(int i = 0 ;i<memberlist.size(); i++)
            {
                child_id[i] = memberlist.get(i).name;
                //Log.i(TAG, childlist.get(i).name);
            }

            account = child_id[selector].toString();

            Log.i("TAG", "account: " + account);

            refresh_msg();

        }

    }



    public void refresh_msg()
    {
        String rmsg = "";

        //selector = diet.dt.selector;
        mydata = memberlist.get(selector);

        double bmr = 0, bmi = 0, standrdweight = 0;
        int weight = Integer.valueOf(mydata.weight);
        int height = Integer.valueOf(mydata.height);
        int age = Integer.valueOf(mydata.age);

        double waist = Double.valueOf(mydata.waist);
        String rwaist = "";

        //cal bmr
        if (mydata.sex.equals("0"))
        {
            bmr = (13.7*weight)+(5.0*height)-(6.8*age)+66;
            //標準體重好像我公式有給錯 正確公式＝身高(m)×身高(m)×22
            rwaist = (waist <= 94)?"正常":"異常";
        }
        else
        {
            bmr = (9.6*weight)+(1.8*height)-(4.7*age)+655;
            rwaist = (waist <= 80)?"正常":"異常";
        }

        //cal bmi
        //bmi = (double) weight / (height*height);
        double h2 = (double) height/100;

        standrdweight = (h2 * h2) * 22;

        //計算BMI
        bmi = (double) weight / (h2 * h2);

        double standrdweightratio	= weight/standrdweight;

        double sResult=standrdweight*30;

        DecimalFormat mDecimalFormat = new DecimalFormat("#.##");

        if(standrdweightratio<=0.9)sResult=standrdweight*35;
        if(standrdweightratio>=1.1)sResult=standrdweight*25;

        //1公斤=2.2046磅
        //增重:體重磅數*18
        double inc = weight * 2.2046 * 18;
        //保持:體重磅數*15
        double keep = weight * 2.2046 * 15;
        //減重:體重磅數*12
        double dec = weight * 2.2046 * 12;

        //rmsg += "目前的熱量/消耗熱量:" + food.hot + "/" + sport.hot + "\n";
        rmsg += "體重" + weight + "\n";
        rmsg += "基礎代謝率(BMR):" + mDecimalFormat.format(bmr) +	"\n";
        String rbmi =(bmi > 8.5 && bmi <  24)?"(BMI正常)":"(異常BMI)";
        rmsg += "BMI:" + mDecimalFormat.format(bmi) + rbmi + "\n";
        //String rst =(standrdweight < (weight*0.1))?"(體重正常)":"(體重太重)";
        rmsg += "標準體重:" + ((Math.round(standrdweight)/10)*10) + "\n";
        rmsg += "理想體重範圍:" + Math.round(standrdweight*.9*10)/10 + " ~ " + Math.round(standrdweight*1.1*10)/10 + "\n";
        rmsg += "建議熱量:" + mDecimalFormat.format(sResult) + "\n";

        final Calendar c = Calendar.getInstance();
        myYear = c.get(Calendar.YEAR);
        myMonth = c.get(Calendar.MONTH);
        myDay = c.get(Calendar.DAY_OF_MONTH);

        String date = String.valueOf(year) + "-" + String.valueOf(myMonth+1) + "-" + String.valueOf(myDay);

        String[] whereValue={date};

        double getdata = 0;
        double costdata = 0;

        try{
            cursor = db.query(SQLiteHelper.DIARY_NAME, null, hotdiary.RDATE + "=?", whereValue, null, null, null);

            cursor.moveToFirst();

            //no data
            if (cursor.isAfterLast())
            {
                //openOptionsDialog("找不到資料");
                //return;
            }


            while(!cursor.isAfterLast())
            {
                hotdiary sitem = new hotdiary();
                sitem.id = cursor.getString(0);
                sitem.ditem = cursor.getString(1);
                sitem.sitem = cursor.getString(2);
                sitem.dhot = cursor.getString(3);
                sitem.shot = cursor.getString(4);
                sitem.rdate = cursor.getString(5);

                if (!sitem.dhot.equals("-1"))
                {
                    getdata += Double.valueOf(sitem.dhot);
                }

                costdata += Double.valueOf(sitem.dhot);

                cursor.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        rmsg += "今日總共攝取熱量:" + getdata + "\n";
        rmsg += "今日總共消耗熱量:" + costdata+ "\n";

        msg.setText(rmsg);
    }


    private void addmember()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("新增姓名");
        alert.setMessage("請輸入帳號 和 密碼");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        tname = new TextView(this);
        tname.setText("姓名: ");
        name = new EditText(this);
        name.setText("");
        ll.addView(tname);
        ll.addView(name);

        tsex = new TextView(this);
        tsex.setText("性別: ");
        sex = new Spinner(this);
        String sexs[] = {"男","女"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sexs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        sex.setAdapter(spinnerArrayAdapter);

        ll.addView(tsex);
        ll.addView(sex);

        twe = new TextView(this);
        twe.setText("體重: ");
        we = new EditText(this);
        we.setText("");
        ll.addView(twe);
        ll.addView(we);

        the = new TextView(this);
        the.setText("身高: ");
        he = new EditText(this);
        he.setText("");
        ll.addView(the);
        ll.addView(he);

        tage = new TextView(this);
        tage.setText("年齡: ");
        age = new EditText(this);
        age.setText("");
        ll.addView(tage);
        ll.addView(age);
        // Set an EditText view to get user input
        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                n1 = name.getText().toString();
                n2 = Integer.toString(sex.getSelectedItemPosition());
                n3 = we.getText().toString();
                n4 = he.getText().toString();
                n5 = "28";
                n6 = age.getText().toString();

                if (n1.equals("") || n2.equals("") ||n3.equals("") ||n4.equals("") ||n5.equals("")||n6.equals(""))
                {
                    openOptionsDialog("有值沒填");
                    finish();
                    return;
                }
                else
                {
                    DBSQL.insert(main.this, n1, n2, n4, n3, n6, n5);
                    memberlist.clear();
                    try{
                        cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

                        cursor.moveToFirst();

                        while(!cursor.isAfterLast())
                        {
                            member sitem = new member();
                            sitem.id = cursor.getString(0);
                            sitem.name = cursor.getString(1);
                            sitem.sex = cursor.getString(2);

                            sitem.weight = cursor.getString(3);
                            sitem.height = cursor.getString(4);
                            sitem.waist = cursor.getString(5);
                            sitem.age = cursor.getString(6);
                            sitem.rdate = cursor.getString(7);

                            memberlist.add(sitem);
                            cursor.moveToNext();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    selector = memberlist.size()-1;

                    account = n1;
                    refresh_msg();

                }



            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });

        alert.show();



    }


    private void modifymember()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("修改");
        alert.setMessage("請輸入");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        tname = new TextView(this);
        tname.setText("姓名: ");
        name = new EditText(this);
        name.setText(memberlist.get(selector).name);
        ll.addView(tname);
        ll.addView(name);

        tsex = new TextView(this);
        tsex.setText("性別: ");
        sex = new Spinner(this);
        String sexs[] = {"男","女"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sexs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        sex.setAdapter(spinnerArrayAdapter);

        Log.i("TAG", memberlist.get(selector).sex);

        if (memberlist.get(selector).sex.equals("0"))
            sex.setSelection(0);
        else
            sex.setSelection(1);

        ll.addView(tsex);
        ll.addView(sex);

        twe = new TextView(this);
        twe.setText("體重: ");
        we = new EditText(this);
        we.setText(memberlist.get(selector).weight);
        ll.addView(twe);
        ll.addView(we);

        the = new TextView(this);
        the.setText("身高: ");
        he = new EditText(this);
        he.setText(memberlist.get(selector).height);
        ll.addView(the);
        ll.addView(he);

        tage = new TextView(this);
        tage.setText("年齡: ");
        age = new EditText(this);
        age.setText(memberlist.get(selector).age);
        ll.addView(tage);
        ll.addView(age);
        // Set an EditText view to get user input
        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String id = memberlist.get(selector).id;
                n1 = name.getText().toString();
                n2 = Integer.toString(sex.getSelectedItemPosition());
                n3 = we.getText().toString();
                n4 = he.getText().toString();
                n5 = wa.getText().toString();
                n6 = age.getText().toString();

                Log.i("TAG", "n2: " + n2);

                if (n3.equals("") ||n4.equals("") ||n5.equals("")||n6.equals("")||n1.equals(""))
                {
                    openOptionsDialog("有值沒填");
                    return;
                }
                else
                {
                    DBSQL.update(main.this, n1, n2, n3, n4, n5, n6, id);
                    memberlist.clear();
                    try{
                        cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

                        cursor.moveToFirst();

                        while(!cursor.isAfterLast())
                        {
                            member sitem = new member();
                            sitem.id = cursor.getString(0);
                            sitem.name = cursor.getString(1);
                            sitem.sex = cursor.getString(2);
                            sitem.weight = cursor.getString(3);
                            sitem.height = cursor.getString(4);
                            sitem.waist = cursor.getString(5);
                            sitem.age = cursor.getString(6);
                            sitem.rdate = cursor.getString(7);

                            memberlist.add(sitem);
                            cursor.moveToNext();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    selector = memberlist.size()-1;

                    refresh_msg();

                }



            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });

        alert.show();



    }


    public void insertweight()
    {
        result_check = 0;

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String mtime = dateFormat.format(new Date());


        String[] whereValue={mtime};
        //first login
        try{
            cursor = db.query(SQLiteHelper.DIARYWEIGHT_NAME, null, RecWeight.RDATE + "=?", whereValue, null, null, null);

            cursor.moveToFirst();

            //no data
            if (cursor.isAfterLast())
            {
                inputweight();

                return;
            }
            else
            {
                openOptionsDialog("今日已經寫入");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String getRealPathFromUri(Activity activity, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK)
            {
                DBSQL.insertDiaryWeight(main.this, n1, picfilename);
                Toast.makeText(main.this, "insert OK", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Uri customizedFilePath() {
        pic = System.currentTimeMillis() + ".jpg";
        File sdDir = Environment.getExternalStorageDirectory();
        File theDir = new File(sdDir,"");
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        File picFile = new File(theDir, pic);
        picfilename = theDir + "/" + pic;
        Uri uri = Uri.fromFile(picFile);
        Log.i("TAG", "picture path：" + uri.getPath());
        return uri;
    }

    public void inputweight()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("今日體重");
        alert.setMessage("input weight?");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        mtime = dateFormat.format(new Date());

        TextView tdate = new TextView(this);
        tdate.setText("今日的體重: ");
        ddate = new EditText(this);
        ddate.setText(mydata.weight);
        ll.addView(tdate);
        ll.addView(ddate);

        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                n1 = ddate.getText().toString();
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, customizedFilePath());
                startActivityForResult(it, 1);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });

        alert.show();

    }



    public void query_hot()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Query");
        alert.setMessage("input date?");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        mtime = dateFormat.format(new Date());

        TextView tdate = new TextView(this);
        tdate.setText("日期: ");
        ddate = new EditText(this);
        ddate.setText(mtime);
        ll.addView(tdate);
        ll.addView(ddate);

        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                n1 = ddate.getText().toString();

                if (n1.equals(""))
                {
                    openOptionsDialog("有值沒填");
                    return;
                }
                else
                {


                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });

        alert.show();

    }


    public void query_weight()
    {
        //n1 = mydate;

        if (n1.equals(""))
        {
            openOptionsDialog("有值沒填");
            return;
        }
        else
        {
            String[] whereValue={n1};
            //first login
            try{
                cursor = db.query(SQLiteHelper.DIARYWEIGHT_NAME, null, RecWeight.RDATE + "=?", whereValue, null, null, null);
                cursor.moveToFirst();

                //no data
                if (cursor.isAfterLast())
                {
                    return;
                }

                RecWeight sitem = new RecWeight();
                while(!cursor.isAfterLast())
                {
                    sitem.id = cursor.getString(0);
                    sitem.weight = cursor.getString(1);
                    sitem.pic = cursor.getString(2);
                    sitem.rdate = cursor.getString(3);

                    cursor.moveToNext();
                }

                String rmsg = sitem.rdate  + " \n";
                rmsg +=  "那天的體重: " + sitem.weight  + " \n";

                Log.i("TAG", sitem.pic);

                ShowQueryWeight(rmsg, sitem.pic);


            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch(id){
            case 1:

                return new DatePickerDialog(this,
                        myDateSetListener,
                        myYear, myMonth, myDay);
        }

        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);

            n1 = date;

            String[] whereValue={date};
            //first login
            try{
                cursor = db.query(SQLiteHelper.DIARY_NAME, null, hotdiary.RDATE + "=?", whereValue, null, null, null);

                cursor.moveToFirst();

                //no data
                if (cursor.isAfterLast())
                {
                    ShowQueryDialog("找不到資料");
                    return;
                }

                String rmsg = "";

                hotdiary sitem = new hotdiary();
                while(!cursor.isAfterLast())
                {
                    sitem.id = cursor.getString(0);
                    sitem.ditem = cursor.getString(1);
                    sitem.sitem = cursor.getString(2);
                    sitem.dhot = cursor.getString(3);
                    sitem.shot = cursor.getString(4);
                    sitem.rdate = cursor.getString(5);

                    rmsg = sitem.rdate  + " \n";
                    if (!sitem.dhot.equals("-1"))
                    {
                        rmsg +=  "熱量: " + sitem.dhot  + " \n";
                        rmsg +=  "項目: " + sitem.ditem  + " \n";
                    }
                    rmsg +=  "消耗熱量: " + sitem.shot  + " \n";
                    rmsg +=  "項目: " + sitem.sitem  + " \n";

                    cursor.moveToNext();
                }



                ShowQueryDialog(rmsg);

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    private void openOptionsDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("msg")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        }
                )
                .show();
    }

    private void ShowQueryDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("msg")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                query_weight();
                            }
                        }
                )
                .show();
    }

    private void ShowQueryWeight(String msg, String pic)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(n1);
        alert.setMessage(msg);

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        ImageView t1 = new ImageView(this);
        ll.addView(t1);

        Bitmap bMap = BitmapFactory.decodeFile(pic);
        t1.setImageBitmap(bMap);

        alert.setView(sv);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });

        alert.show();

    }

    //按下上鍵
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            openOptionsDialog();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void openOptionsDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Exit?")
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                            }
                        }
                )

                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                finish();
                            }

                        }
                )

                .show();
    }

    void choice2()
    {

        if (selector1 == 0)
        {
            child_id = new String[]{"全身1 側彈跳","全身2 鳥狗式"};
        }
        else if (selector1 == 1)
        {
            child_id = new String[]{"腹1 船式轉底船式","腹2 登山者"};
        }
        else if (selector1 == 2)
        {
            child_id = new String[]{"臂1 板式交替撐地"};
        }
        else if (selector1 == 3)
        {
            child_id = new String[]{"腿1 原地跑步", "腿2 直腿腳踏車式", "腿3 分腿跳","腿4 自重深蹲", "腿5 跨欄步", "腿6 側弓箭步", "腿7 下落屈蹲"};
        }

        int checked = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
        builder.setTitle("選擇想看什麼?");

        selector2 = 0;

        builder.setSingleChoiceItems(child_id, checked, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                selector2 = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                String newVideoPath = "http://163.13.201.96:8081/video/" + selector1 + "/" + selector2 + ".mp4";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newVideoPath));
                intent.setDataAndType(Uri.parse(newVideoPath), "video/mp4");
                startActivity(intent);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    void choice()
    {
        final CharSequence[] child_id = new String[]{"全身","腹","臂","腿"};
        int checked = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
        builder.setTitle("選擇想看的部位?");

        selector1 = 0;

        builder.setSingleChoiceItems(child_id, checked, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                selector1 = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                choice2();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    void showvideo()
    {

        new AlertDialog.Builder(this)
                .setTitle("真人影片教學")
                .setMessage("去健身房才能長肌肉嗎 那是你不了解徒手訓練 方便又省時\n運動只是輔助 想減肥還是要靠飲食的控制\n每個動作盡量已8~12下為一組 共3組\n一天選擇一個部位做就好，做多少動作依自己的體能狀況\n同個部位做的休息間隔時間為24小時 因為肌肉組織被破壞需要修復的時間\n每個動作消耗的熱量:\n是以組來算 一組18卡 所以運動項目的運動耗量健身的部分 要以組來計算不能用分鐘")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                choice();
                            }
                        }
                )
                .show();



    }

    private void ShowfoodTipsOptionsDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("Diet Tips")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        }
                )
                .setNegativeButton("下一個",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                if (tips_selector == 0)
                                    ShowfoodTipsOptionsDialog("一般成年男性每日需要的熱量約2000大卡，女性約1800大卡，若要減重，攝取的熱量必需比需要量少500~1000大卡，才會有預期的每週減輕0.5~1公斤的效果，因此1200大卡的減重餐是一般常見的低熱量減肥餐，在此告訴您1200大卡營養均衡的減重餐飲食內容。");
                                else if (tips_selector == 1)
                                    ShowfoodTipsOptionsDialog("早餐（主食2份、蛋白質2份）\n菜單1\n早餐\n土司2片\n 低脂鮮奶240cc\n茶葉蛋1個\n\n\n午餐\n飯1/2碗（100克）\n雞腿1隻\n燙青菜約1碗 拌蒜頭醬油\n芭樂1/2個\n\n晚餐\n\n水餃6個\n青菜豆腐湯 豆腐1塊 小白菜\n燙青小蘋果1個可加蒜頭醬油 或鹽調味 \n菜1碗\n");
                                else if (tips_selector == 2)
                                    ShowfoodTipsOptionsDialog("菜單2\n早餐\n粥1碗 \n涼拌豆腐半盒\n燙青菜半碗(可加蒜頭醬油或鹽調味)\n滷蛋1個\n\n\n午餐\n水煮麵 \n麵條40公克（煮熟約1碗）\n里雞肉絲1兩 文蛤15個 青菜\n葡萄13個\n註：水滾後直接加入材料。利用文蛤鮮味，材料不需爆炒。\n\n\n晚餐\n小碗糙米或五穀飯 燙青菜只加醬油膏  滷白豆腐一塊  滷雞腿去皮\n");
                                else if (tips_selector == 3)
                                    ShowfoodTipsOptionsDialog("菜單3\n早餐\n三角飯糰1個\n脫脂奶粉3平匙\n\n\n午餐\n陽春麵1碗\n滷味 厚黑豆干1塊 海帶1塊\n蓮霧2個\n註：陽春麵不可喝湯以去部分油脂，乾麵含油量高，不可點。滷味只加蔥花醬油不可拌肉燥。\n\n\n晚餐\n地瓜130~160克糙米飯一碗  燙青菜只加醬油膏  水煮蛋，滷蛋或茶葉蛋一顆  100g的水煮雞肉或蒸魚肉");
                                else if (tips_selector == 4)
                                    ShowfoodTipsOptionsDialog("菜單4\n早餐\n無糖豆漿類300c.c\n堅果類1把\n奇異果1顆\n\n\n午餐\n牛肉麵\n\n\n晚餐\n木耳炒蔥蛋\n滷雞腿去皮\n燙A菜(不可加肉燥，加少許醬油即可)\n糙米飯1碗");
                                else if (tips_selector == 5)
                                    ShowfoodTipsOptionsDialog("菜單5\n早餐   2顆水煮蛋  牛奶250ml  蘋果1顆\n午餐   糙米130g  青椒炒牛肉  燙青菜(不可加肉燥)\n晚餐   草莓3顆  吐司兩片  燕麥1碗  玉米1根");
                                else if (tips_selector == 6)
                                    ShowfoodTipsOptionsDialog("菜單6\n早餐  麥片一碗 加牛奶250ml\n午餐  100克滷豆干 滷排骨1塊  炒高麗菜70g 糙米飯130g\n晚餐  乾燒白蝦250g(可以依喜好加些不同蔬菜)\n  九層塔炒蛋  清炒蛤蜊義大利麵  燙青菜70g");

                                tips_selector++;
                                if (tips_selector >=7) tips_selector=0;

                            }
                        }
                )
                .show();
    }


    private void ShowTipsOptionsDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("Diet Tips")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        }
                )
                .setNegativeButton("下一個小常識",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                if (tips_selector == 0)
                                    ShowTipsOptionsDialog("吃水果減肥 沒變瘦反變胖?不少人嘗試水果減肥法，有鳳梨減肥法、香蕉減肥法，但是不但沒瘦，反而變胖，營養師提醒，現在的水果甜度高，光吃水果可能沒變瘦，還會吃出反效果。夏天是減肥的季節，網路上流傳水果瘦身法的妙用，「三餐吃鳳梨，一個月瘦8公斤」，「光吃水果，餓不著，又快瘦」，減肥文立即發燒，網友謠傳，水果減肥法可以吃到飽，不但不用餓肚子，還能成功瘦");
                                else if (tips_selector == 1)
                                    ShowTipsOptionsDialog("任何食物都可以多吃嗎?雖然想要增重的人要增加熱量的攝取，但是並不表示可以毫無忌憚的大吃大喝，也不是任何零食都可以盡量的吃。目標是要增'重'，而不是增'肥'，希望增加的是肌肉，而不是肥油，仍然應該避免脂肪含量高的食物，因為吃進去的脂肪不會轉換成肌肉，只有蛋白質才會。要少吃油炸的食物、太肥的肉，各式糖果、餅乾、洋芋片也不是好的點心，雖然他們提供了醣類，但是也含有不少的脂肪，而且幾乎不含有任何維他命和礦物質");
                                else if (tips_selector == 2)
                                    ShowTipsOptionsDialog("怎麼吃都吃不胖怎麼辦?有些人先天代謝機能比較旺盛，比常人消耗比較多的熱量，所以比較難增重。但是還有一個很重要的原因就是某些營養素不足。充足的維他命和礦物質對肌肉增長有絕對的影響，同時要有足夠的胺基酸提供肌肉合成的原料。總而言之，體重多半是經由先天遺傳和小時候的營養所決定，但是後天充足而均衡的營養，加上適度的運動，仍然可以得到很好的增重效果");
                                else if (tips_selector == 3)
                                    ShowTipsOptionsDialog("睡覺也可以達到減肥的目的嗎？想要減肥成功，首先就要保証睡眠。在深度睡眠中，大腦會悄悄分泌大量成長荷爾蒙，它會指導身體把脂肪轉化為能量。 如果減少深度睡眠時間，同時又囤積了大量能夠轉化為脂肪的熱量，荷爾蒙的分泌跟不上熱量的囤積，你的身體就會自動把這些脂肪轉化到臀部，大腿和肚子上，肥胖就是這麼來的");
                                else if (tips_selector == 4)
                                    ShowTipsOptionsDialog("吃肉不吃飯無福消瘦 有些怕胖的人，想說少吃飯多吃肉就會瘦或不容易變胖！台灣民眾攝取太多的蛋白質，這些蛋白質大部分來自於肉類，然而，很多肉類含有豐富的脂肪，尤其是飽和脂肪，飽和脂肪為引起心血管疾病的危險因子，對身體健康的危害性相當大，所以想靠少吃飯多吃肉來瘦下來或保持身材的人可要特別當心。建議減少攝取肉類，用豆類取代部分肉類，不但營養又兼有低脂高纖的好處，這樣就可以增加攝取膳食纖維，同時也可減少攝取動物性脂肪。如果要通分獲得豆類的營養，可以烹煮毛豆炒酸菜、黃豆燉湯、黃豆糙米飯等料理，有助於留下豆類的營養");
                                else if (tips_selector == 5)
                                    ShowTipsOptionsDialog("我有著吃不胖的體質，如何做才能成功增重?吃不胖可能與飲食中所攝取的熱量、蛋白質不足有關，想要增重，應多吃米飯、麵食等澱粉類食物，並搭配富含熱量、蛋白質的雞蛋、魚肉、牛肉等食材。新鮮蔬菜、水果也應均衡攝取，這應有助於增重。在運動類型方面，欲增重者不宜採取慢跑、球類運動等有氧運動，因進行有氧運動時，會加速消耗身體中的脂肪量，反而易減重，想增重者應進行負重等類型的運動，如舉重、舉啞鈴等，這有助於肌肉訓練，也就是藉由增加肌肉質量的效果，來達到增重的目的");

                                tips_selector++;
                                if (tips_selector >=6) tips_selector=0;
                            }
                        }
                )
                .show();
    }


}
