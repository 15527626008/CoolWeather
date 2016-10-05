package com.example.coolweather.model;

import java.io.Serializable;

public class Night implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String wind;
	
	private String weatherType;
	
	private String windDaXiao;

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	

	public String getWindDaXiao() {
		return windDaXiao;
	}

	public void setWindDaXiao(String windDaXiao) {
		this.windDaXiao = windDaXiao;
	}

	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}

}
