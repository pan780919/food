package com.diet;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DiaryEdit extends Activity implements OnClickListener
{
	private EditText title,content;
	private TextView mdate;
	
	private int did= 0;
	private Boolean isEdit;
	private final int saveDiary=Menu.FIRST;
	private final int listDiary=Menu.FIRST + 1;
	private final int mClose=Menu.FIRST + 2;
	
	int myYear, myMonth, myDay;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.diaryedit);
	    
	    did = getIntent().getIntExtra("dedit", 0);
	    isEdit = (did > 0);	    
	    
	    title = (EditText)findViewById(R.id.mtitle);
	    content = (EditText)findViewById(R.id.mcontent);
	    mdate = (TextView)findViewById(R.id.mdate);
	    
	    Button A = (Button)findViewById(R.id.button);
        A.setOnClickListener(this);
	    
	}
	
    public void onClick(View v) 
	{
	    	switch (v.getId()) {
	    		//click
		        case R.id.button: // Do something when click button1
		        	final Calendar c = Calendar.getInstance();
		            myYear = c.get(Calendar.YEAR);
		            myMonth = c.get(Calendar.MONTH);
		            myDay = c.get(Calendar.DAY_OF_MONTH);
		            showDialog(1);
		        	break;
	    	}
    }
    
    private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){

     @Override
     public void onDateSet(DatePicker view, int year, 
       int monthOfYear, int dayOfMonth) {
      // TODO Auto-generated method stub
      String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
       mdate.setText(date);
     }
   };
       
    
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
	
	private void getContent(int id)
	{
		Cursor c=DBSQL.selectOne(this,id);
		c.moveToFirst();   
        while (!c.isAfterLast()) { 
        	title.setText(c.getString(0));
        	content.setText(c.getString(1));
        	c.moveToNext();  
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, saveDiary, Menu.NONE, "Save")
			.setIcon(R.drawable.sava);
		menu.add(0, listDiary, Menu.NONE, "List")
			.setIcon(R.drawable.undo);
		menu.add(0, mClose, Menu.NONE, "Close")
			.setIcon(R.drawable.error_blue);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case saveDiary: {
	  		String t = title.getText().toString();
			String c = content.getText().toString();
			String dd = mdate.getText().toString();
			
			if(isEdit && t.trim().length()>0 && c.trim().length()>0)
				DBSQL.updateSport(DiaryEdit.this, t,  dd, c, did);
			else if(t.trim().length()>0 && c.trim().length()>0)
			{
				DBSQL.insertSport(DiaryEdit.this, t, dd, c);
				Intent i = new Intent(this, DiaryList.class);
		  		startActivity(i);
		  		finish();
			}else
				Toast.makeText(DiaryEdit.this, "", 
						Toast.LENGTH_LONG).show();
	  		return true;
	  	}
	  	case listDiary:{
	  		Intent i = new Intent(this, DiaryList.class);
	  		startActivity(i);
	  		finish();
	  		return true;
	  	}
	  	case mClose:
	  		finish();
	  		return true;
	  	default:
		  return false;
	  }   
	} 
}
