package com.example.coolweather.model;

import java.io.Serializable;

public class WeatherInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Weather weather;

	private Environment environment;

	private Yesterday yesterday;

	private Forecast forecast;

	private ZhiShu zhiShu;

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Yesterday getYesterday() {
		return yesterday;
	}

	public void setYesterday(Yesterday yesterday) {
		this.yesterday = yesterday;
	}

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public ZhiShu getZhiShu() {
		return zhiShu;
	}

	public void setZhiShu(ZhiShu zhiShu) {
		this.zhiShu = zhiShu;
	}

	
}
