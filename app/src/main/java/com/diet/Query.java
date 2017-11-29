package com.diet;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sqlite.SQLiteHelper;
import com.sqlite.account;
import com.sqlite.recdata;

public class Query extends Activity {


    private ArrayList<HashMap<String, Object>> cstore_list;
    private ArrayList<recdata> list_ft;

    private ListView show_view;

    private static int DB_VERSION = 1;

    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;

    String TAG = "FTList";
    SimpleDateFormat sdf;

    String name;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        Bundle rdata =this.getIntent().getExtras();

        name = rdata.getString("name");

        list_ft = new ArrayList<recdata>();

        //開資料庫
        try{
            dbHelper = new SQLiteHelper(this, SQLiteHelper.DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            ++ DB_VERSION;
            dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
        }


        //Display: create ListView class
        show_view = (ListView)findViewById(R.id.listview);

        //取得目前找到的項目，並放到List中
        cstore_list = getStoreList();

        if (cstore_list != null)
        {
            if (cstore_list.size() == 0)
            {
                openOptionsDialog("zero, no data...");
            }

            //加入到ListView中，顯示給使用者
            SimpleAdapter listitemAdapter=new SimpleAdapter(this,
                    cstore_list,
                    R.layout.no_listview_style,
                    new String[]{"ItemTitle","ItemText"},
                    new int[]{R.id.topTextView, R.id.bottomTextView}
            );

            show_view.setAdapter(listitemAdapter);
            show_view.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3)
                {

      /*
          Bundle bundle = new Bundle();
          //bundle.putString("date", list_ft.get(arg2));
           Intent intent = new Intent();
           //intent.setClass(query.this, ShowView.class);
           intent.putExtras(bundle);
           startActivity(intent);
           */
                }
            });
        }
        else{
            openOptionsDialog("no today data");
            //finish();
        }
    }

    public ArrayList<HashMap<String, Object>> getStoreList()
    {
//int search_list_size = MyGoogleMap.my.search_list.size();
        ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();

        list_ft.clear();

        Date today = Calendar.getInstance().getTime();
        Date date;
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sdate = sdf.format(today);

        Calendar scalendar = new GregorianCalendar(2010,1,1);
        scalendar.setTime(today);

        int times=0, times2 = 0;
        int steps=0;
        //for (int i=0; i<=0; i++)
        {
            //Query DATABASE
            try{
                Log.i("TAG", recdata.NAME + "='" + name + "' and " + recdata.CTIME + "='" + sdate + "'");
                cursor = db.query(SQLiteHelper.USER_REC_TABLE, null, recdata.NAME + "='" + name + "' and " + recdata.CTIME + "='" + sdate + "'", null, null, null, null);

                cursor.moveToFirst();

                //no data
                if (cursor.isAfterLast())
                {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemTitle", "今天沒運動記錄");
                    map.put("ItemText", "建議您去運動!!");

                    listitem.add(map);
                }

                times = 0;
                while(!cursor.isAfterLast())
                {
                    recdata ndata = new recdata();
                    ndata.id = cursor.getString(0);
                    ndata.name =cursor.getString(1);
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

                    list_ft.add(ndata);
                    cursor.moveToNext();
                }
            }catch(IllegalArgumentException e){
                e.printStackTrace();
                ++ DB_VERSION;
                dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
            }

            for (int i=0; i<list_ft.size(); i++)
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemTitle", list_ft.get(i).detail);
                map.put("ItemText", list_ft.get(i).ctime + " " + list_ft.get(i).detail2);

                listitem.add(map);
            }

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", "= 今日 總消耗 " + times + "卡 = ");
            map.put("ItemText",  "= 今日 公里數  " + times2 + "km =\n" + "= 今日 總步數  " + steps + "步 =");
            listitem.add(map);

            // scalendar.add(Calendar.DATE, -1);
            //date=scalendar.getTime();
            //term = sdf.format(date);
        }

        return listitem;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (dbHelper != null)
            dbHelper.close();
    }

    public void openOptionsDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("message")
                .setMessage(info)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                finish();
                            }
                        }
                )
                .show();
    }

}