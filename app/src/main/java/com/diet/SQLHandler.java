package com.diet;

import java.util.ArrayList;
import java.util.Date;



import com.sqlite.SQLiteHelper;
import com.sqlite.account;
import com.sqlite.recdata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


public class SQLHandler {
  
  static ArrayList<account> ndata = null;
	
	public static void insert_register(Context context, String title, String sex, String age, String height, String weight){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		String mdate = dateFormat.format(new Date());
		
	    java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("HH:mm");
	    String mtime = dateFormat2.format(new Date());
			String sql = "insert into "+ SQLiteHelper.INFO_TABLE + "(" +
				account.NAME+", " + account.SEX+", "+account.AGE+", " + account.HEIGHT+", "+account.WEIGHT +")" +
				"values('"+ title+"', '" + sex + "', '" + age + "', '" + height + "', '" + weight + "');";
		db.execSQL(sql);
		db.close();
		//Toast.makeText(context, "", Toast.LENGTH_LONG).show();
	}
	
	public static void insert_data(Context context, String title, String mode, String kl, String step, String distance, String detail, String detail2 ){
	    SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
	    String mdate = dateFormat.format(new Date());
	    
	    java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("HH:mm");
	    String mtime = dateFormat2.format(new Date());
	    String sql = "insert into "+ SQLiteHelper.USER_REC_TABLE + "(" +
	      recdata.NAME+", " +  recdata.MODE+", " + recdata.KL+", " + recdata.STEP+", " + recdata.DISTANCE+", "+ recdata.DETAIL+", "+ recdata.DETAIL2+", "+recdata.CTIME +")" +
	      "values('"+ title+"', '" +  mode + "', '"  + kl + "', '" + step + "', '"  + distance + "', '" + detail + "', '" + detail2 + "', '" + mdate+"');";
	    db.execSQL(sql);
	    db.close();
	    //Toast.makeText(context, sql, Toast.LENGTH_LONG).show();
	    Log.i("TAG", sql);
	}
	
	public static void update(Context context, String title, String content, int id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		String mtime = dateFormat.format(new Date());
		/*
		String sql = "UPDATE "+SQLiteHelper.USER_REC_TABLE+" SET "+
				account.NAME+" = '"+title+"',"+
				account.PWD+" = '"+content+"',"+
				account.DEFAULT_LANGUAGE+" = '"+mtime+
			"' WHERE id = "+id;
		db.execSQL(sql);
		db.close();
		*/
		//Toast.makeText(context, "嚙賣頛��哄���", Toast.LENGTH_LONG).show();
	}
	
	public static void deleteOne(Context context, int id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "DELETE FROM "+SQLiteHelper.DB_NAME +" WHERE id = "+ id;
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "", Toast.LENGTH_LONG).show();
	}
	
	public static void deleteAll(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "DELETE FROM "+SQLiteHelper.DB_NAME;
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "", Toast.LENGTH_LONG).show();
	}
	
	public static Cursor getdata(Context context, String table, String condition){
	    SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
	    SQLiteDatabase db = helper.getReadableDatabase();
	    Cursor c = db.query(table, null, condition, null, null, null, null);
	    return c;
	}
	
	public static ArrayList<account> getAllAccount(Context context)
	{
    SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
    SQLiteDatabase db = helper.getWritableDatabase();
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
    String mdate = dateFormat.format(new Date());
    
    java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("HH:mm");
    String mtime = dateFormat2.format(new Date());
   // Cursor c = db.rawQuery("select id, "+SQLiteHelper.D_TITLE+","+SQLiteHelper.D_TIME+" from "+SQLiteHelper.DIARY_TABLE+" order by id desc", null);

    ndata = new ArrayList<account>();
   
    try{
      //cursor = db.query(SQLiteHelper.FILE_TABLE, null, file.OWER + "='" + LoginActivity.rent.email + "'", null, null, null, null);
      Cursor cursor = SQLHandler.getdata(context, SQLiteHelper.INFO_TABLE, null);
      cursor.moveToFirst();
      
      //no data
      if (cursor.isAfterLast())
      {
        return null;
      }
      
        
        while(!cursor.isAfterLast())
        {
          account newfile = new account();
          
          newfile.id= cursor.getString(0);
          newfile.name= cursor.getString(1);
          newfile.sex= cursor.getString(2);
          newfile.age= cursor.getString(3);
          newfile.height= cursor.getString(4);
          newfile.weight= cursor.getString(5);
          
          ndata.add(newfile);

          cursor.moveToNext();
        }
        cursor.close();
  }catch(IllegalArgumentException e){
      e.printStackTrace();
  }    //db.close();
    //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
    db.close();
    return ndata;
  }
	
	 public static void delete(Context context, String name){
	    SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    String sql = "DELETE FROM "+SQLiteHelper.INFO_TABLE +" WHERE " + account.NAME + " = '"+ name + "'";
	    db.execSQL(sql);
      sql = "DELETE FROM "+SQLiteHelper.USER_REC_TABLE +" WHERE " + recdata.NAME + " = '"+ name + "'";
      db.execSQL(sql);
	    db.close();
	    Toast.makeText(context, "", Toast.LENGTH_LONG).show();
	  }

	
/*	
	public static Cursor selectAll(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select id, "+SQLiteHelper.D_TITLE+","+SQLiteHelper.D_TIME+" from "+SQLiteHelper.DIARY_TABLE+" order by id desc", null);
		return c;
	}
	
	public static Cursor search(Context context, String time){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select id, "+SQLiteHelper.D_TITLE+","+SQLiteHelper.D_TIME+" from "+
					SQLiteHelper.DIARY_TABLE+" where "+
					SQLiteHelper.D_TIME+" like '"+time+"%' order by id desc", null);
		return c;
	}
	
	public static Cursor selectNormal(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select id, "+SQLiteHelper.D_TITLE+","+SQLiteHelper.D_TIME+" from "+SQLiteHelper.DIARY_TABLE+" order by id desc limit 20", null);
		return c;
	}
	
	public static Cursor selectOne(Context context, int id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] whereValue={Integer.toString(id)};
		Cursor c=db.query(SQLiteHelper.DIARY_TABLE, new String[]{
				SQLiteHelper.D_TITLE, 
				SQLiteHelper.D_CONTENT, 
				SQLiteHelper.D_TIME
		       }, "id=?", whereValue, null, null, null);
		return c;
	}*/
}
