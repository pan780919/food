package com.diet;

import java.util.ArrayList;
import java.util.HashMap;

import com.sqlite.SQLiteHelper;
import com.sqlite.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//android app進入點
public class diet extends Activity
{
    public static diet dt;


    public int selector;

    protected boolean _active = true;
    protected int _splashTime = 5000; // time to display the splash screen in ms

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    //startActivity(new Intent("com.diet."));
                    Intent intent = new Intent();
                    intent.setClass(diet.this, main.class);
                    startActivity(intent);

                }
            }
        };
        splashTread.start();
    }


    private void openOptionsDialog(String info)
    {
        new AlertDialog.Builder(this)
                .setTitle("about me")
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
}
