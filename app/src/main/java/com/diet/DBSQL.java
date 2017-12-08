package com.diet;

import java.util.Date;


import com.sqlite.RecWeight;
import com.sqlite.SQLiteHelper;
import com.sqlite.hotdiary;
import com.sqlite.member;
import com.sqlite.sport;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class DBSQL {
	
	public static void insert(Context context, String name, String sex, String he, String we, String age, String wa)
	{
		com.sqlite.SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String mtime = dateFormat.format(new Date());
		String sql = "insert into "+ SQLiteHelper.TB_NAME + "(" +
			member.NAME+", "+
			member.SEX+", "+
			member.WEIGHT+", "+
			member.HEIGHT+", "+
			member.WAIST+", "+
			member.AGE+", "+
			member.RDATE +")" +
			"values('"+name+"', '" + sex + "', '" + we + "', '" + he + "', '" + wa + "', '" + age + "', '" + mtime+"');";
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "insert OK", Toast.LENGTH_LONG).show();
	}

	public static void insertSport(Context context, String ssport, String rdate, String item)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

		String sql = "insert into "+ SQLiteHelper.SPORT_NAME + "(" +
			sport.SPORT+", "+
			sport.ITEM+", "+
			sport.RDATE +")" +
			"values('"+ssport+"', '" + item + "', '" + rdate + "');";
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "Insert recorder", Toast.LENGTH_LONG).show();
	}

	public static void insertDiary(Context context, String ditem, String sitem, String dhot, String shot)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String mtime = dateFormat.format(new Date());

		String sql = "insert into "+ SQLiteHelper.DIARY_NAME + "(" +
			hotdiary.DITEM+", "+
			hotdiary.SITEM+", "+
			hotdiary.DHOT+", "+
			hotdiary.SHOT+", "+
			hotdiary.RDATE +")" +
			"values('"+ditem+"', '" + sitem + "', '"+dhot+"', '" + shot + "', '" + mtime + "');";
		db.execSQL(sql);
		db.close();
		
		Toast.makeText(context, "Insert recorder", Toast.LENGTH_LONG).show();
	}

	public static void insertDiaryWeight(Context context, String weight, String pic)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String mtime = dateFormat.format(new Date());

		String sql = "insert into "+ SQLiteHelper.DIARYWEIGHT_NAME + "(" +
			RecWeight.WEIGHT+", "+
			RecWeight.PIC+", "+
			RecWeight.RDATE +")" +
			"values('"+weight+"', '" +pic+"', '" + mtime + "');";
		db.execSQL(sql);
		db.close();
		
		Toast.makeText(context, "Insert recorder", Toast.LENGTH_LONG).show();
	}
	
	public static void insertMyDiary(Context context, String ditem, String sitem, String dhot, String shot)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String mtime = dateFormat.format(new Date());

		String sql = "insert into "+ SQLiteHelper.MYDIARY_NAME + "(" +
			hotdiary.DITEM+", "+
			hotdiary.SITEM+", "+
			hotdiary.DHOT+", "+
			hotdiary.SHOT+", "+
			hotdiary.RDATE +")" +
			"values('"+ditem+"', '" + sitem + "', '"+dhot+"', '" + shot + "', '" + mtime + "');";
		db.execSQL(sql);
		db.close();
		
		Toast.makeText(context, "Insert recorder", Toast.LENGTH_LONG).show();
	}
	
	public static void update(Context context, String cname, String sex, String weight, String tall, String waist, String age, String id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		String mtime = dateFormat.format(new Date());
		String sql = "UPDATE "+SQLiteHelper.TB_NAME+" SET "+
			member.NAME+" = '"+cname+"',"+
			member.SEX+" = '"+sex+"',"+
			member.WEIGHT+" = '"+weight+"',"+
			member.HEIGHT+" = '"+tall+"',"+
			member.WAIST+" = '"+waist+"',"+
			member.AGE+" = '"+age +
			"' WHERE " + member.ID + " = '"+id + "'";
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "update", Toast.LENGTH_LONG).show();
	}

	
	public static void updateSport(Context context, String ssport, String rdate, String item, int id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		String mtime = dateFormat.format(new Date());
		String sql = "UPDATE "+SQLiteHelper.SPORT_NAME+" SET "+
			sport.SPORT+" = '"+ssport+"',"+
			sport.ITEM+" = '"+item+"',"+
			sport.RDATE+" = '"+rdate +
			"' WHERE id = "+id;
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "update", Toast.LENGTH_LONG).show();
	}
	
	public static void deleteOne(Context context, int id){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "DELETE FROM "+SQLiteHelper.SPORT_NAME +" WHERE aid = "+ id;
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "delete OK", Toast.LENGTH_LONG).show();
	}
	
	public static void deleteAll(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);

		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "DELETE FROM "+SQLiteHelper.SPORT_NAME;
		db.execSQL(sql);
		db.close();
		Toast.makeText(context, "delete all", Toast.LENGTH_LONG).show();
	}


	public static void removeAll(Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);

		// db.delete(String tableName, String whereClause, String[] whereArgs);
		// If whereClause is null, it will delete all rows.
		SQLiteDatabase db = helper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
		db.delete(SQLiteHelper.TB_NAME, null, null);
		db.delete(SQLiteHelper.INFO_TABLE, null, null);
		db.delete(SQLiteHelper.USER_REC_TABLE, null, null);
		db.delete(SQLiteHelper.DIARY_NAME, null, null);
		db.delete(SQLiteHelper.SPORT_NAME, null, null);
	}

	public static Cursor selectAll(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select id, "+sport.SPORT+","+sport.ITEM+" from "+SQLiteHelper.SPORT_NAME+" order by aid desc", null);
		return c;
	}
	
	public static Cursor search(Context context, String time){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select aid, "+sport.SPORT+","+sport.RDATE+" from "+
					SQLiteHelper.SPORT_NAME+" where "+
					sport.RDATE+" like '"+time+"%' order by id desc", null);
		return c;
	}
	
	public static Cursor selectNormal(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+SQLiteHelper.SPORT_NAME+" order by aid desc limit 20", null);
		return c;
	}
	
	public static Cursor selectdata(Context context){
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+SQLiteHelper.MYDIARY_NAME+"", null);
		return c;
	}
	
	public static Cursor selectOne(Context context, int id)
	{
		SQLiteHelper helper = new SQLiteHelper(context, SQLiteHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] whereValue={Integer.toString(id)};
		Cursor c=db.query(SQLiteHelper.SPORT_NAME, new String[]{
				sport.SPORT, 
				sport.ITEM, 
				sport.RDATE
		       }, "id=?", whereValue, null, null, null);
		return c;
	}
}
