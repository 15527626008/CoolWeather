package com.example.coolweather.model;

import java.io.Serializable;

/***
 * 环境信息
 * @author join
 *
 */
public class Environment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String aqi;
	
	private String pm;
	
	private String suggest;
	
	private String airQuality;
	
	private String o3;
	
	private String co;
	
	private String pm10;
	
	private String no2;
	
	private String so2;

	public String getAqi() {
		
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getAirQuality() {
		return airQuality;
	}

	public void setAirQuality(String airQuality) {
		this.airQuality = airQuality;
	}

	public String getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}
	
}