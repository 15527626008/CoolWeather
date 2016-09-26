package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				HttpURLConnection connection = null;

				try {

					URL url = new URL(address);

					connection = (HttpURLConnection) url.openConnection();

					connection.setRequestMethod("GET");

					connection.setConnectTimeout(8000);

					connection.setReadTimeout(8000);

					InputStream inputStream = connection.getInputStream();

					BufferedReader br = new BufferedReader(
							new InputStreamReader(inputStream, "utf-8"));

					StringBuilder sb = new StringBuilder();

					String readLine;

					while ((readLine = br.readLine()) != null) {

						sb.append(readLine);

					}
					
					if (sb.toString().contains("html")) {
						
						String sInt = "null";
						
						Integer.parseInt(sInt);
					}

					if (listener != null) {

						listener.OnFinsh(sb.toString());
					}

				} catch (Exception e) {

					if (listener != null) {

						listener.OnError(e);

					}
				} finally {

					connection.disconnect();
				}
			}
		}).start();

	}

}
