package com.example.coolweather.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import org.dom4j.Element;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.baidu.a.a.a.c;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Day;
import com.example.coolweather.model.Environment;
import com.example.coolweather.model.Forecast;
import com.example.coolweather.model.Night;
import com.example.coolweather.model.Province;
import com.example.coolweather.model.Weather;
import com.example.coolweather.model.WeatherInfo;
import com.example.coolweather.model.ZhiShu;

public class Utility {
	/***
	 * 解析和处理服务器返回的数据
	 * 
	 * @param coolWeatherDB
	 * @param reponse
	 * @return
	 */
	// private static WeatherInfo weatherInfo ;

	public static final String WEATHER = "WEATHER";

	public static final String ENVIRONMENT = "ENVIRONMENT";

	public static final String FOERCAST = "FOERCAST";
	
	public static final String FOERCAST_DAY = "FOERCAST_DAY";
	
	public static final String FOERCAST_NIGHT = "FOERCAST_NIGHT";

	public static final String ZHISHU = "ZHISHU";

	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String reponse) {

		if (!TextUtils.isEmpty(reponse)) {

			String[] provinces = reponse.split(",");

			if (provinces != null && provinces.length > 0) {

				for (String item : provinces) {

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

	public synchronized static boolean handlerCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, Integer provinceId) {

		if (!TextUtils.isEmpty(response)) {

			String[] cities = response.split(",");

			if (cities != null && cities.length > 0) {

				for (String item : cities) {

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

	public synchronized static boolean handlerCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, Integer cityId) {

		if (!TextUtils.isEmpty(response)) {

			String[] counties = response.split(",");

			if (counties != null && counties.length > 0) {

				for (String item : counties) {

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

	// 解析xml数据
	public synchronized static void praseXmlWithPull(
			CoolWeatherDB coolWeatherDB, final String response) {

		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

			XmlPullParser parser = factory.newPullParser();

			parser.setInput(new StringReader(response));

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public synchronized static void praseXmlWithDom(
			final CoolWeatherDB coolWeatherDB, final String response) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					Document document = DocumentHelper.parseText(response); // 获取document对象,如果文档无节点，则会抛出Exception提前结束

					Element root = document.getRootElement();// 获取根节点

					Element memberElm = root.element("c");// "member"是节点名

					// Log.i("INFO","大小："+ memberElm.elements("d").size());

					List listAttr = memberElm.elements("d");// 当前节点的所有属性的list

					for (Iterator iterator = listAttr.iterator(); iterator
							.hasNext();) {// 遍历当前节点的所有属性
						Element elm = (Element) iterator.next();

						Attribute attribute = elm.attribute("d1");// 属性名name

						Attribute attribute1 = elm.attribute("d2");// 属性名name

						// Log.i("INFO", "city_code：" + attribute.getValue() +
						// " city_name："
						// + attribute1.getValue());

						City city = new City();

						city.setCity_code(attribute.getValue());

						city.setCity_name(attribute1.getValue());

						coolWeatherDB.saveCity(city);

						if (attribute.getValue().equals("101340406")) {
							break;
						}
					}
					Log.i("INFO", "数据存储成功！");
					//
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}).start();

	}

	public synchronized static void parseWeatherInfo(String response,
			Context context) {

		// 解析天气数据

		Document document;
		try {

			document = DocumentHelper.parseText(response);

			Element root = document.getRootElement();// 获取根节点

			setWeather(root, context);

			/**
			 * 获取环境信息
			 */

			Element elm = root.element("environment");

			setEnvironment(elm, context);

			/**
			 * 获取未来几天的天气
			 * 
			 */

			Element forecast = root.element("forecast");

			setForecast(forecast, context);

			/***
			 * 获取指数
			 */
			Element zhishus = root.element("zhishus");

			setzhishus(zhishus, context);

			Log.i("INFO", "解析成功");

		} catch (DocumentException e) {

			// 获取document对象,如果文档无节点，则会抛出Exception提前结束

			e.printStackTrace();

		}

		// saveWeatherInfo(weatherInfo, context, "WEATHERINFO", "weatherinfo");

	}

	private static void setWeather(Element root, Context context) {

		Weather weather = new Weather();
		// 城市信息
		weather.setCity(getText("city", root));

		// 更新时间
		weather.setUpdateTime(getText("updatetime", root));

		weather.setTemp1(getText("wendu", root));
		// 风力大小
		weather.setWind(getText("fengli", root));

		// 湿度
		weather.setSidu(getText("shidu", root));

		// 风向
		weather.setWindDir(getText("fengxiang", root));
		
		
		
		

		Log.i("INFO", "解析成功：" + weather.getUpdateTime());

		saveWeatherInfo(weather, context, WEATHER, WEATHER.toLowerCase());

	}

	private static void setEnvironment(Element elm, Context context) {

		Environment environment = new Environment();

		environment.setAqi(getText("aqi", elm));

		environment.setPm(getText("pm25", elm));

		environment.setSuggest(getText("suggest", elm));

		environment.setAirQuality(getText("quality", elm));

		environment.setO3(getText("o3", elm));

		environment.setCo(getText("co", elm));

		environment.setPm10(getText("pm10", elm));

		environment.setSo2(getText("so2", elm));

		environment.setNo2(getText("no2", elm));

		// Log.i("INFO","解析成功：" + environment.getAirQuality());

		// saveWeatherInfo(environment, context, name1, name2)

		saveWeatherInfo(environment, context, ENVIRONMENT,
				ENVIRONMENT.toLowerCase());
	}

	private static void setzhishus(Element zhishu, Context context) {

		int i = 0;

		List<Element> zhishusList = zhishu.elements();

		for (Element element : zhishusList) {

			ZhiShu zhiShu = new ZhiShu();

			zhiShu.setName(getText("name", element));

			zhiShu.setValue(getText("value", element));

			zhiShu.setDetail(getText("detail", element));

			saveWeatherInfo(zhiShu, context, ZHISHU + i++, ZHISHU.toLowerCase());
		}

		

		// weatherInfo.setZhiShu(zhiShu2);
		

	}

	/**
	 * 获取当天天气信息
	 * 
	 * @param name
	 * @param root
	 * @return
	 */
	private static String getText(String name, Element element) {

		Element elm = element.element(name);

		return elm.getText();
	}

	private static void setForecast(Element forecast, Context context) {

        int i = 0;

		List<Element> list = forecast.elements();

		for (Element element : list) {

			Weather weather3 = new Weather();

			weather3.setDate(getText("date", element));

			weather3.setHighWinDu(getText("high", element));

			weather3.setLowWenDu(getText("low", element));

			Day day = new Day();

			Element dayelm = element.element("day");

			day.setWeatherType(getText("type", dayelm));

			day.setWind(getText("fengxiang", dayelm));

			day.setWindDaXiao(getText("fengli", dayelm));

			Night night = new Night();

			Element nightelm = element.element("night");

			night.setWeatherType(getText("type", nightelm));

			night.setWind(getText("fengxiang", nightelm));

			night.setWindDaXiao(getText("fengli", nightelm));


			//saveWeatherInfo(day, context, FOERCAST_DAY + i, FOERCAST_DAY.toLowerCase());
			
			//saveWeatherInfo(night, context, FOERCAST_NIGHT + i, FOERCAST_NIGHT.toLowerCase());
			
			weather3.setDay(day);
			weather3.setNight(night);
			
			saveWeatherInfo(weather3, context, FOERCAST + i++, FOERCAST.toLowerCase());
		}
	}

	/***
	 * weatherInfo
	 */
	public static void saveWeatherInfo(Object obj, Context context,
			String name1, String name2) {

		SharedPreferences preferences = context.getSharedPreferences(name1,
				Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			// 创建对象输出流，并封装字节流
			out = new ObjectOutputStream(baos);
			// 将对象写入字节流

			if (obj instanceof Weather) {

				Weather weather = (Weather) obj;

				out.writeObject(weather);

			} else if (obj instanceof Environment) {

				Environment environment = (Environment) obj;

				out.writeObject(environment);

			} else if (obj instanceof ZhiShu) {

				ZhiShu zhiShu = (ZhiShu) obj;

				out.writeObject(zhiShu);

			} else if (obj instanceof Day) {

				Day day = (Day) obj;

				out.writeObject(day);

			} else if (obj instanceof Night) {

				Night night = (Night) obj;

				out.writeObject(night);

			}else {

				return;
			}

			// 将字节流编码成base64的字符窜
			String objectVal = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			Editor editor = preferences.edit();
			editor.putString(name2, objectVal);

			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i("INFO", "存储成功");
	}

	/***
	 * 
	 * @return
	 */

	public static <T> T getObject(String key, Class<T> clazz, Context context,
			String name) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);

		if (sp.contains(key)) {

			String objectVal = sp.getString(key, null);

			byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);

			// 一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

			ObjectInputStream ois = null;

			try {

				ois = new ObjectInputStream(bais);

				T t = (T) ois.readObject();

				return t;

			} catch (StreamCorruptedException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			} catch (ClassNotFoundException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bais != null) {

						bais.close();

					}
					if (ois != null) {

						ois.close();
					}

				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
