package com.example.coolweather.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    //创建省份表
	private final String CREATE_PROVINCE = "create table Province(" +
			"id integer primary key autoincrement ," +
			"province_name text" +
			"province_code text )";
	//创建城市表
	private final String CREATE_CITY = "create table City(" +
	        "id integer primary key autoincrement" + 
			"city_name text" + 
	        "city_code text" +
			"province_id integer)";
	//创建乡镇表
	private final String CREATE_COUNTY = "create table County(" +
	        "id integer primary key autoincrement" +
			"county_name text" +
	        "county_code text" +
			"city_id integer )";
	
	
	
	public CoolWeatherOpenHelper(Context context, String name,CursorFactory factory, int version,
DatabaseErrorHandler errorHandler) {
		
		
		super(context, name, factory, version, errorHandler);
		
	}
    
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version){
		
		super(context, name, factory, version);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建省市县
         db.execSQL(CREATE_PROVINCE);
         db.execSQL(CREATE_CITY);
         db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
