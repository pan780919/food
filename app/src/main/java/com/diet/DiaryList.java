package com.diet;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import com.sqlite.sport;	

public class DiaryList extends ListActivity
{
	private List<sport> list;
	private MListAdapter adapter;
	private final int newDiary = Menu.FIRST;
	private final int deleteDiary = Menu.FIRST + 1;
	private final int mClose = Menu.FIRST + 2;
	private final int mClear = Menu.FIRST + 3;
	private final int settings = Menu.FIRST + 4;
	private final int seeall = Menu.FIRST + 5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_list);
        list = getDiaryList(DBSQL.selectNormal(this));
        adapter = new MListAdapter(this, list);
        setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, DiaryShow.class);
		i.putExtra("dedit", list.get(position).id);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	

	private void seeAll(){
		list.clear();
		list.addAll(getDiaryList(DBSQL.selectAll(this)));
		adapter.notifyDataSetChanged();
	}
	
	private void searchDialog()
	{
		Calendar d = Calendar.getInstance(Locale.CHINA);
		Date myDate=new Date();
		d.setTime(myDate);
		int year=d.get(Calendar.YEAR);
		int month=d.get(Calendar.MONTH);
		int day=d.get(Calendar.DAY_OF_MONTH);
		final DatePicker dp = new DatePicker(this);
		dp.init(year, month, day, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("search");
		builder.setView(dp);
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String month,day;
				if(dp.getMonth()< 9)
					month = "0"+String.valueOf(dp.getMonth()+1);
				else
					month = String.valueOf(dp.getMonth()+1);
				
				if(dp.getDayOfMonth()< 9)
					day = "0"+String.valueOf(dp.getDayOfMonth());
				else
					day = String.valueOf(dp.getDayOfMonth());
					
				String searchString = String.valueOf(dp.getYear())+"-"+
					month+"-"+day;

				search(searchString);
				dialog.dismiss();
			}
		});
		builder.setNeutralButton("Cancel", new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String month;
				if(dp.getMonth()< 9)
					month = "0"+String.valueOf(dp.getMonth()+1);
				else
					month = String.valueOf(dp.getMonth()+1);
				
			String searchString = String.valueOf(dp.getYear())+"-"+
				month;

				search(searchString);
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("NO", new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	private void search(String s){
		list.clear();
		list.addAll(getDiaryList(DBSQL.search(this,s)));
		adapter.notifyDataSetChanged();  
	}
	
	private List<sport> getDiaryList(Cursor cursor)
	{
		List<sport> dl = new ArrayList<sport>();
		cursor.moveToFirst();   
        while (!cursor.isAfterLast()) {
        	
        	sport sport = new sport();
        	sport.id = cursor.getString(0);
        	sport.name = cursor.getString(1);
        	sport.item = cursor.getString(2);
        	sport.rdate = cursor.getString(3);
        	//sport.user = cursor.getString(4);
        	dl.add(sport);
        	
        	cursor.moveToNext();  
        }
		return dl;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, newDiary, Menu.NONE, "add new sport")
			;
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case newDiary: {
	  		Intent i = new Intent(this, DiaryEdit.class);
	  		startActivity(i);
	  		finish();
	  		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	  		return true;
	  	}
	  	case settings: {
	  		//Intent i = new Intent(this, Admin.class);
	  		//i.putExtra("user", 1);
	  		//startActivity(i);
	  		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	  		return true;
	  	}
	  	case deleteDiary: {
	  		deleteOneDialog();
	  		return true;
	  	}
	  	case seeall:{
	  		seeAll();
	  		return true;
	  	}
	  	case mClose:{
	  		finish();
	  		return true;
	  	}
	  	case mClear:
	  		deleteAllDialog();
	  		return true;
	  	default:
		  return false;
	  }   
	}
	
	private void deleteOneDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("Delete one?");  
        builder.setMessage("which");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		if(getSelectedItemPosition() >=0){
        			int id = Integer.valueOf(list.get(getSelectedItemPosition()).id);
        			DBSQL.deleteOne(DiaryList.this, id);
        			list.remove(getSelectedItemPosition());
        			adapter.notifyDataSetChanged();
    	  		}else
    	  			Toast.makeText(DiaryList.this, "error", 
    	  					Toast.LENGTH_LONG).show();
        	}
        });
        builder.setNegativeButton("NO", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        builder.show();
	}
	
	private void deleteAllDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("Delete?");  
        builder.setMessage("ALL?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("YES", new OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {

          		list.clear();
          		adapter.notifyDataSetChanged();
        	}
        });
        builder.setNegativeButton("NO", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        builder.show();
	}
}
