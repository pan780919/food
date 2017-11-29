package com.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteHelper extends SQLiteOpenHelper 
{
  public static final String DB_NAME = "info.db";
	public static final String TB_NAME = "member";
	public static final String SPORT_NAME = "sport";
	public static final String DIARY_NAME = "diary";
	public static final String DIARYWEIGHT_NAME = "diaryheight";
	public static final String MYDIARY_NAME = "mydiary";
	public static final String USER_REC_TABLE = "user_rec_table";
	public static final String INFO_TABLE = "info_table";

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_NAME + "(" +
				member.ID + " integer primary key autoincrement," +
				member.NAME + " varchar," + 
				member.SEX + " varchar," + 
				member.WEIGHT + " varchar,"+
				member.HEIGHT + " varchar,"+
				member.WAIST + " varchar,"+
				member.AGE + " varchar,"+
				member.RDATE + " varchar"+
				");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				SPORT_NAME + "(" +
				sport.ID + " integer primary key autoincrement," +
				sport.SPORT + " varchar," + 
				sport.ITEM + " varchar," + 
				sport.RDATE + " varchar"+
				");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				DIARY_NAME + "(" +
				hotdiary.ID + " integer primary key autoincrement," +
				hotdiary.DITEM + " varchar," + 
				hotdiary.SITEM + " varchar," + 
				hotdiary.DHOT + " varchar," + 
				hotdiary.SHOT + " varchar," + 
				hotdiary.RDATE + " varchar"+
				");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				DIARYWEIGHT_NAME + "(" +
				RecWeight.ID + " integer primary key autoincrement," +
				RecWeight.WEIGHT + " varchar," + 
				RecWeight.PIC + " varchar," + 
				RecWeight.RDATE + " varchar"+
				");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				MYDIARY_NAME + "(" +
				hotdiary.ID + " integer primary key autoincrement," +
				hotdiary.DITEM + " varchar," + 
				hotdiary.SITEM + " varchar," + 
				hotdiary.DHOT + " varchar," + 
				hotdiary.SHOT + " varchar," + 
				hotdiary.RDATE + " varchar"+
				");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
			    INFO_TABLE + "(" +
					account.ID + " integer primary key," +
					account.NAME + " varchar," +
					account.SEX + " varchar," +
					account.AGE + " varchar," +
					account.HEIGHT + " varchar," +
					account.WEIGHT + " varchar" +
					");");
			
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
		        USER_REC_TABLE + "(" +
		        recdata.ID + " integer primary key," +
		        recdata.NAME + " varchar," +
		        recdata.MODE + " varchar," +
		        recdata.KL + " varchar," +
		        recdata.STEP + " varchar," +
		        recdata.DISTANCE + " varchar," +
		        recdata.DETAIL + " varchar," +
		        recdata.DETAIL2 + " varchar," +
		        recdata.CTIME + " varchar"+
        ");");
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
