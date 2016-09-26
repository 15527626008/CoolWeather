package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

public class Utility {
    /***
     * 解析和处理服务器返回的数据
     * @param coolWeatherDB
     * @param reponse
     * @return
     */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String reponse){
		
		if(!TextUtils.isEmpty(reponse)){
			
			String[] provinces = reponse.split(",");
			
			if(provinces != null && provinces.length > 0){
				
				for(String item : provinces){
					
					String[] arrays = item.split("\\|");
					
					Province province = new Province();
					
					province.setProvince_code(arrays[0]);
					province.setProvince_name(arrays[1]);
					
					coolWeatherDB.saveProvince(province);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	public synchronized static boolean handlerCitiesResponse(CoolWeatherDB coolWeatherDB,String response,Integer provinceId){
		
		if(!TextUtils.isEmpty(response)){
			
			String[] cities = response.split(",");
			
			if(cities != null && cities.length > 0){
				
				for(String item : cities){
					
					String[] arrays = item.split("\\|");
					
					City city = new City();
					
					city.setCity_code(arrays[0]);
					city.setCity_name(arrays[1]);
					city.setProvince_id(provinceId);
					
					coolWeatherDB.saveCity(city);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public synchronized static boolean handlerCountiesResponse(CoolWeatherDB coolWeatherDB,String response,Integer cityId){
		
		if(!TextUtils.isEmpty(response)){
			
			String[] counties = response.split(",");
			
			if(counties != null && counties.length > 0){
				
				for(String item : counties){
					
					String[] arrays = item.split("\\|");
					
					County county = new County();
					
					county.setCounty_code(arrays[0]);
					county.setCounty_name(arrays[1]);
					county.setCity_id(cityId);
					
					coolWeatherDB.saveCounty(county);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	
	
}













