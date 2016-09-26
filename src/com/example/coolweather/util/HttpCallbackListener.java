package com.example.coolweather.util;

public interface HttpCallbackListener {

	public void OnFinsh(String response);
	
	public void OnError(Exception e);
}
