package com.diet.frgment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.diet.R;
import com.diet.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JackPan on 2017/12/6.
 */

public class fragment_teach extends Fragment{
    private View v;
    private ListView listview;
    private ArrayList<HashMap<String, Object>> menu;
    int tips_selector=0;
    int selector1 = 0;
    CharSequence[] child_id = null;
    int selector2;
    public fragment_teach() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =inflater.inflate(R.layout.fragment_sport, container, false);
        listview = (ListView)v.findViewById(R.id.listview);

        menu = new ArrayList<HashMap<String, Object>>();

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
        map = new HashMap<String, Object>();
        map.put("ItemTitle", "真人影片教學" );
        map.put("ItemText", "training video");
        menu.add(map);

//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "基本資料設定" );
//        map.put("ItemText", "setup");
//        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "計步器" );
//        map.put("ItemText", "Pedometer");
//        menu.add(map);
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "天氣預報" );
//        map.put("ItemText", "weather");
//        menu.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "常見問題" );
//        map.put("ItemText", "QANDA");
//        menu.add(map);
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
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "會員中心" );
//        map.put("ItemText","membercenter");
//        menu.add(map);
//        map = new HashMap<String, Object>();
//        map.put("ItemTitle", "結束程式" );
//        map.put("ItemText", "login out");
//        menu.add(map);

        //然後加入項目之後就準備接下來的工作
        SimpleAdapter listitemAdapter=new SimpleAdapter(getActivity(),
                menu,
                R.layout.no_listview_style,
                new String[]{"ItemTitle","ItemText"},
                new int[]{R.id.topTextView,R.id.bottomTextView}
        );
        listview.setAdapter(listitemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                //看使用者選什麼，就會去開啟服務
                Intent intent = null;

                switch (arg2)
                {
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
                    case 0:
                        tips_selector = 0;
                        choice();
                        break;
//                    case 0:
////
//                        fixmember();
//                        break;
//                    case 4:
//                        intent = new Intent();
//                        intent.setClass(main.this, WalKStepActivity.class);
//
//                        startActivity(intent);
//                        break;
//                    case 1:
//                        startActivity(new Intent(main.this, MainActivity.class));
//                        break;
//                    case 2:
//                        startActivity(new Intent(main.this,QAActivity.class));
//                        break;
//                    case 7:
//                        startActivity(new Intent(main.this,MusicActivity.class));
//                        break;
//                    case 8:
//                        startActivity(new Intent(main.this, com.diet.MainActivity.class));
//                        break;
//                    case 9:
//                        startActivity(new Intent(main.this, UserActivity.class));
//
//                        break;
//                    case 10:
//                        finish();
//                        break;
                }
            }
        });
        return v;
    }
    void choice()
    {
        final CharSequence[] child_id = new String[]{"全身","腹","臂","腿"};
        int checked = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


}
