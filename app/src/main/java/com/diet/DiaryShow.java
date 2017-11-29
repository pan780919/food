package com.diet;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


public class DiaryShow extends Activity{
	private int did= 0;
	private PopupWindow pop;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    did = getIntent().getIntExtra("dedit", 0);
	    final Boolean isEdit = (did > 0);	    	    	
	    setContentView(R.layout.showdiary);
	    TextView show = (TextView)findViewById(R.id.show);
	    initPop();
	    if(isEdit){
	    	Cursor c=DBSQL.selectOne(this,did);
	    	c.moveToFirst();   
	    	while (!c.isAfterLast()) { 
	    		setTitle(c.getString(0)+'-'+ c.getString(2));
	    		show.setText(c.getString(3));
	    		c.moveToNext();   
	    	}
	    }else{
	    	setTitle(getResources().getString(R.string.app_name));
    		show.setText("Android");
	    }
	 }
	
	private void initPop(){
		View view = this.getLayoutInflater().inflate(R.layout.popup_window, null);
		((Button)view.findViewById(R.id.btnEdit)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				Intent i = new Intent(DiaryShow.this, DiaryEdit.class);
				i.putExtra("dedit", did);
				startActivity(i);
				finish();
			}
		});
		
		((Button)view.findViewById(R.id.btnChange)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				changeSreen();
			}
		});
		((Button)view.findViewById(R.id.btnClose)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				finish();
			}
		});
		
		pop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		//pop.setOutsideTouchable(true);
		
	}

	
	private void changeSreen(){
		if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
}
