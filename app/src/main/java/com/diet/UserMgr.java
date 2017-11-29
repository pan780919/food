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
import com.sqlite.account;

public class UserMgr extends Activity {


    private ArrayList<HashMap<String, Object>> cstore_list;
    private ArrayList<account> listdata;

    private ListView show_view;

    private static int DB_VERSION = 1;


    String TAG = "FTList";
    SimpleDateFormat sdf;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        Bundle rdata =this.getIntent().getExtras();

        listdata = new ArrayList<account>();


        //Display: create ListView class
        show_view = (ListView)findViewById(R.id.listview);

        refresh();

    }

    void refresh()
    {
        //取得目前找到的項目，並放到List中
        cstore_list = getStoreList();

        if (cstore_list != null)
        {
            if (cstore_list.size() == 0)
            {
                openOptionsDialog("no account data...");
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
                    String name = listdata.get(arg2).name;
                    SQLHandler.delete(getBaseContext(), name);
                    refresh();
                }
            });
        }
        else
        {
            openOptionsDialog("no account data...");
        }

    }

    public ArrayList<HashMap<String, Object>> getStoreList()
    {
//int search_list_size = MyGoogleMap.my.search_list.size();
        ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();

        listdata.clear();

        listdata = SQLHandler.getAllAccount(this);

        if (listdata == null)
        {
            return null;
        }

        for (int i=0; i<listdata.size(); i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", listdata.get(i).name);
            map.put("ItemText", "年齡: " + listdata.get(i).age);
            listitem.add(map);
        }

        return listitem;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

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