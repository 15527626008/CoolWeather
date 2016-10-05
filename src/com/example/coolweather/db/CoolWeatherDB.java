package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CoolWeatherDB {

	/**
	 * 数据库名
	 */
	private static final String DB_NAME = "cool_weather";

	private SQLiteDatabase db;

	/***
	 * 数据库版本
	 * 
	 */
	private static final int version = 1;

	private static CoolWeatherDB coolWeatherDB = null;

	/**
	 * 单例模式，禁止实例化
	 */
	private CoolWeatherDB(Context context) {

		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(
				context, DB_NAME, null, version);

		db = coolWeatherOpenHelper.getWritableDatabase();

	}

	/**
	 * 获取coolWeather实例
	 * 
	 * @param context
	 * @return
	 */
	public static CoolWeatherDB getCoolWeatherDB(Context context) {

		if (coolWeatherDB == null) {

			synchronized (CoolWeatherDB.class) {

				if (coolWeatherDB == null)

					coolWeatherDB = new CoolWeatherDB(context);
			}

		}
		return coolWeatherDB;
	}

	/***
	 * 存信息
	 */
	public Boolean saveProvince(Province province) {

		boolean result = false;

		if (province != null) {

			ContentValues cValues = new ContentValues();

			cValues.put("province_name", province.getProvince_name());

			cValues.put("province_code", province.getProvince_code());

			try {

				db.insert("Province", null, cValues);

				result = true;

			} catch (Exception e) {

				Log.e("TAG", "insert province error!");
			}
		}

		return result;
	}

	/**
	 * 
	 * @param city
	 * @return
	 */
	public Boolean saveCity(City city) {
		boolean result = false;

		if (city != null) {

			ContentValues values = new ContentValues();

			values.put("city_name", city.getCity_name());

			values.put("city_code", city.getCity_code());

			values.put("province_id", city.getProvince_id());

			try {

				db.insert("City", null, values);

				result = true;

			} catch (Exception e) {

				Log.e("TAG", "insert city error!");
			}

		}
		return result;
	}

	/***
	 * 
	 * @param county
	 */
	public boolean saveCounty(County county) {
		boolean result = false;

		if (county != null) {

			ContentValues values = new ContentValues();

			values.put("county_name", county.getCounty_name());

			values.put("county_code", county.getCounty_code());

			values.put("city_id", county.getCity_id());

			try {

				db.insert("County", null, values);

				result = true;

			} catch (Exception e) {

				Log.e("TAG", "insert County error!");
			}

		}

		return result;
	}

	/***
	 * 获取信息
	 */
	public List<Province> getProvinces() {

		List<Province> provinces = new ArrayList<Province>();

		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();

				province.setId(cursor.getColumnIndex("id"));

				province.setProvince_name(cursor.getString(cursor
						.getColumnIndex("province_name")));

				province.setProvince_code(cursor.getString(cursor
						.getColumnIndex("province_code")));

				provinces.add(province);

			} while (cursor.moveToNext());

		}

		return provinces;
	}

	/***
	 * 
	 * @param provinceId
	 * @return
	 */
	public List<City> getCities(Integer provinceId) {

		List<City> citys = new ArrayList<City>();

		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);

		if (cursor.moveToFirst()) {
			do {

				City city = new City();

				city.setId(cursor.getColumnIndex("id"));

				city.setCity_code(cursor.getString(cursor
						.getColumnIndex("city_code")));

				city.setCity_name(cursor.getString(cursor
						.getColumnIndex("city_name")));

				city.setProvince_id(provinceId);

				citys.add(city);

			} while (cursor.moveToNext());

		}
		return citys;
	}
	
	public List<City> getCities() {

		List<City> citys = new ArrayList<City>();

		Cursor cursor = db.query("City", null,null,
				null, null, null, null);

		if (cursor.moveToFirst()) {
			do {

				City city = new City();

				city.setId(cursor.getColumnIndex("id"));

				city.setCity_code(cursor.getString(cursor
						.getColumnIndex("city_code")));

				city.setCity_name(cursor.getString(cursor
						.getColumnIndex("city_name")));

				citys.add(city);

			} while (cursor.moveToNext());

		}
		return citys;
	}


	/***
	 * 
	 * @param cityId
	 * @return
	 */
	public List<County> getCountys(Integer cityId) {

		List<County> counties = new ArrayList<County>();

		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);

		if (cursor.moveToFirst()) {

			do {

				County county = new County();

				county.setId(cursor.getColumnIndex("id"));

				county.setCounty_name(cursor.getString(cursor
						.getColumnIndex("county_name")));

				county.setCounty_code(cursor.getString(cursor
						.getColumnIndex("county_code")));

				county.setCity_id(cityId);

				counties.add(county);

			} while (cursor.moveToNext());

		}

		return counties;
	}
	
	public boolean haveDate(String TABLE_NAME){
		
		Cursor cursor = db.query(TABLE_NAME, null, null,
				null, null, null, null);
		
		if(cursor.getCount() > 0)
			return true;
		return false;
	}
	
	public City loadCityByCityName(String cityName){
		
		Cursor cursor = db.query("City", null, "city_name = ?",
				new String[] { cityName}, null, null, null);
		
		if(cursor.moveToFirst()) {
			
			City city = new City();

			city.setId(cursor.getColumnIndex("id"));

			city.setCity_code(cursor.getString(cursor
					.getColumnIndex("city_code")));

			city.setCity_name(cityName);

			return city;
		}
		
		return null;
		
	}
	

}
