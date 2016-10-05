package com.example.coolweather.model;

import java.io.Serializable;


public class Weather implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String date;

	private String week;

	private String city;

	private String temp1; // 气温

	private String weather; // 天气

	private String img; // 图片

	private String wind; // 风力

	private String updateTime; // 更新时间

	private String sidu; // 湿度

	private String windDir;

	private String tip; // 其他信息

	private String highWinDu;

	private String lowWenDu;

	private Day day;

	private Night night;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getSidu() {
		return sidu;
	}

	public void setSidu(String sidu) {
		this.sidu = sidu;
	}

	public String getWindDir() {
		return windDir;
	}

	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}

	public String getHighWinDu() {
		return highWinDu;
	}

	public void setHighWinDu(String highWinDu) {
		this.highWinDu = highWinDu;
	}

	public String getLowWenDu() {
		return lowWenDu;
	}

	public void setLowWenDu(String lowWenDu) {
		this.lowWenDu = lowWenDu;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public Night getNight() {
		return night;
	}

	public void setNight(Night night) {
		this.night = night;
	}

}
