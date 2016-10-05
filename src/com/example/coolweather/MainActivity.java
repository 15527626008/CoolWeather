package com.example.coolweather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Day;
import com.example.coolweather.model.Environment;
import com.example.coolweather.model.Night;
import com.example.coolweather.model.Weather;
import com.example.coolweather.model.ZhiShu;
import com.example.coolweather.searchview.SearchView;
import com.example.coolweather.slideMenu.SlideMenu;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Toast toast;

	private CoolWeatherDB coolWeatherDB;

	private SlideMenu slideMenu;

	private ImageView refresh;

	boolean isLoading = false;

	private ImageView topImageBtn;

	private LayoutInflater layoutInflater;

	private GridView gridView;

	private final int PROVINCE = 1;

	private final int CITY = 2;

	private final int COUNTY = 3;

	// 获取所有城市
	private List<City> cityies;
	// 当前位置经纬度
	private double latitude;
	private double longitude;

	private String currentTime;

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	// 是否首次定位
	private boolean isFirstLoc = true;

	private String myAddress;

	// 定位SDK的核心类
	private LocationClient mLocationClient;

	// 定位SDK监听函数
	public MyLocationListener locListener = new MyLocationListener();

	private Weather weather;

	private TextView publish_text;

	private TextView current_date;

	private TextView publish_time;

	private TextView temp1;

	private TextView temp2;

	private TextView day;

	private TextView night;

	private TextView city;

	private GridView gridView2;

	private TextView zhishu1;

	private TextView zhishu2;

	private TextView zhishu3;

	private List<Map<String, Object>> list;

	/**
	 * 输入框
	 */
	private EditText etInput;

	/**
	 * 删除键
	 */
	private ImageView ivDelete;

	private SearchView searchView;

	/**
	 * 搜索按钮
	 */
	private Button search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();

		// 初始化标题
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setCustomView(R.layout.main_head);

		View headView = actionBar.getCustomView();

		refresh = (ImageView) headView.findViewById(R.id.refresh);

		topImageBtn = (ImageView) headView.findViewById(R.id.topimagebtn);

		weather = Utility.getObject(Utility.WEATHER.toLowerCase(), null,
				MainActivity.this, Utility.WEATHER);

		init();
		
		if(weather == null){
			
			initBaiduMap();
			
		}

		

		/***
		 * 加载数据
		 */
		loadInfo();

		/***
		 * 初始化搜索view
		 */
		initSearchView();
		
		

	}

	private void initSearchView() {

		searchView = (SearchView) findViewById(R.id.searchView1);

		etInput = (EditText) searchView.findViewById(R.id.search_et_input);

		etInput.addTextChangedListener(new EditChangedListener());

		ivDelete = (ImageView) searchView.findViewById(R.id.search_iv_delete);

		ivDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				etInput.setText("");
				ivDelete.setVisibility(View.GONE);
			}
		});

		search = (Button) searchView.findViewById(R.id.search);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!TextUtils.isEmpty(etInput.getText().toString())) {
					
					
					City city = coolWeatherDB.loadCityByCityName(etInput
							.getText().toString().trim());

					if (city != null) {
                      
						loadInfo();

					} else {

						myToastShow("未搜索到您的城市,请检查您的输入信息");
					}
				} else {

					myToastShow("请输入城市信息");
				}

			}

		});

	}


	private class EditChangedListener implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i2,
				int i3) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i2,
				int i3) {

			if (!"".equals(charSequence.toString())) {

				ivDelete.setVisibility(View.VISIBLE);
				
				

				// 更新autoComplete数据

			} else {

				ivDelete.setVisibility(View.GONE);

			}

		}

		@Override
		public void afterTextChanged(Editable editable) {
			
           
			City city = coolWeatherDB.loadCityByCityName(etInput.getText().toString());
			
			if(city != null){
				
				qureyFromService(city.getCity_code(), CITY);
	        	loadInfo();
			}
			
									
		}
	}

	/***
	 * 加载天气数据
	 */

	private void loadInfo() {

		list = new ArrayList<Map<String, Object>>();

		Environment environment = Utility.getObject(
				Utility.ENVIRONMENT.toLowerCase(), null, MainActivity.this,
				Utility.ENVIRONMENT);

		weather = Utility.getObject(Utility.WEATHER.toLowerCase(), null,
				MainActivity.this, Utility.WEATHER);

		Weather weather2 = Utility.getObject(Utility.FOERCAST.toLowerCase(),
				null, MainActivity.this, Utility.FOERCAST + 0);

		Weather weather3 = Utility.getObject(Utility.FOERCAST.toLowerCase(),
				null, MainActivity.this, Utility.FOERCAST + 1);

		Weather weather4 = Utility.getObject(Utility.FOERCAST.toLowerCase(),
				null, MainActivity.this, Utility.FOERCAST + 2);

		Weather weather5 = Utility.getObject(Utility.FOERCAST.toLowerCase(),
				null, MainActivity.this, Utility.FOERCAST + 3);

		Weather weather6 = Utility.getObject(Utility.FOERCAST.toLowerCase(),
				null, MainActivity.this, Utility.FOERCAST + 4);

		// 穿衣
		ZhiShu clothes = Utility.getObject(Utility.ZHISHU.toLowerCase(), null,
				MainActivity.this, Utility.ZHISHU + 2);

		// 旅游指数

		ZhiShu lvyou = Utility.getObject(Utility.ZHISHU.toLowerCase(), null,
				MainActivity.this, Utility.ZHISHU + 5);

		//
		ZhiShu xiche = Utility.getObject(Utility.ZHISHU.toLowerCase(), null,
				MainActivity.this, Utility.ZHISHU + 7);

		if (clothes != null) {

			zhishu1.setText(clothes.getName() + " " + clothes.getValue() + " "
					+ clothes.getDetail());
		}

		if (lvyou != null) {

			zhishu2.setText(lvyou.getName() + " " + lvyou.getValue() + " "
					+ lvyou.getDetail());
		}

		if (xiche != null) {

			zhishu3.setText(xiche.getName() + " " + xiche.getValue() + " "
					+ xiche.getDetail());
		}

		if (weather != null) {

			publish_text.setText(weather.getTemp1() + " ℃ ");

			publish_time.setText("更新时间 : " + weather.getUpdateTime());

			city.setText(weather.getCity());

		}

		if (weather2 != null) {

			temp1.setText(weather2.getLowWenDu());

			temp2.setText(weather2.getHighWinDu());

			if (weather2.getDay() != null) {

				Day baitian = weather2.getDay();

				String info = baitian.getWeatherType() + " "
						+ baitian.getWind() + " " + baitian.getWindDaXiao();

				day.setText(info);

			} else if (weather2.getNight() != null) {

				Night wanjian = new Night();

				String info = "晚间天气 " + wanjian.getWeatherType() + " "
						+ wanjian.getWind() + " " + wanjian.getWindDaXiao();

				night.setText(info);
			}

			if (weather3 != null) {

				addDate(weather3);
			}
			if (weather4 != null) {

				addDate(weather4);

			}
			if (weather5 != null) {

				addDate(weather5);
			}
			if (weather6 != null) {

				addDate(weather6);
			}
			createAdapter();

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd号");

		current_date.setText(sdf.format(new Date()));

		if (environment != null) {

		}

	}

	private void addDate(Weather weather) {

		Map<String, Object> map = new HashMap<String, Object>();

		String temp = weather.getLowWenDu().substring(
				weather.getLowWenDu().indexOf("温") + 1)
				+ "/"
				+ weather.getHighWinDu().substring(
						weather.getLowWenDu().indexOf("温") + 1);

		map.put("temp", temp);

		String week;

		if (weather.getDate().endsWith("天")) {

			week = "日";

		} else {

			int index = weather.getDate().indexOf("期") + 1;

			week = weather.getDate().substring(index);
		}

		map.put("week", "周" + week);

		list.add(map);
	}

	private void initBaiduMap() {
		// 获取地图控件引用

	

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(locListener); // 注册监听函数

		mLocationClient.start();

	}

	private void createAdapter() {

		SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,
				list, R.layout.grid_item,

				new String[] { "temp", "week" }, new int[] { R.id.temp,
						R.id.week });

		gridView2.setAdapter(simpleAdapter);

	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

				latitude = location.getLatitude();

				longitude = location.getLongitude();

				currentTime = location.getTime();

				LatLng ptCenter = new LatLng(latitude, longitude);

				mSearch = GeoCoder.newInstance();
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				mSearch.setOnGetGeoCodeResultListener(new MyReverseGeoCode());

			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				// sb.append("\ndescribe : ");
				// sb.append("离线定位成功，离线定位结果也是有效的");
				myToastShow("离线定位成功 !");

			} else if (location.getLocType() == BDLocation.TypeServerError) {
				// sb.append("\ndescribe : ");
				// sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				myToastShow("服务端网络定位失败!");

			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				// sb.append("\ndescribe : ");
				// sb.append("网络不同导致定位失败，请检查网络是否通畅");
				myToastShow("网络不同导致定位失败!");

			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				// sb.append("\ndescribe : ");
				// sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				myToastShow("无法获取有效定位依据导致定位失败!");
			}
			// sb.append("\nlocationdescribe : ");
			// sb.append(location.getLocationDescribe());// 位置语义化信息

			// Log.i("INFO", sb.toString());
		}
	}

	class MyReverseGeoCode implements OnGetGeoCoderResultListener {
		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
						Toast.LENGTH_LONG).show();
				return;
			}
			myAddress = result.getAddress();

			// city.setText(result.getAddress());
			// addressTextView.setText("您所在的位置是: " + );
			// 加载城市信息
			loadCities();

		}
	}

	private void init() {
		
		publish_text = (TextView) findViewById(R.id.publish_text);

		current_date = (TextView) findViewById(R.id.current_date);

		publish_time = (TextView) findViewById(R.id.publish_time);

		temp1 = (TextView) findViewById(R.id.temp1);

		temp2 = (TextView) findViewById(R.id.temp2);

		day = (TextView) findViewById(R.id.day);

		night = (TextView) findViewById(R.id.night);

		city = (TextView) findViewById(R.id.city);

		gridView2 = (GridView) findViewById(R.id.gridView2);

		zhishu1 = (TextView) findViewById(R.id.zhishu1);

		zhishu2 = (TextView) findViewById(R.id.zhishu2);

		zhishu3 = (TextView) findViewById(R.id.zhishu3);

		coolWeatherDB = CoolWeatherDB.getCoolWeatherDB(this);

		slideMenu = (SlideMenu) this.findViewById(R.id.slideMenu1);

		layoutInflater = LayoutInflater.from(this);

		initGridView();

		initHeaderView();
	}

	private void initHeaderView() {

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Animation animation = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.refresh);

				refresh.startAnimation(animation);

				myToastShow("正在加载数据...");

				if (!isLoading) {

					isLoading = true;

					String cityName = city.getText().toString();

					if (!TextUtils.isEmpty(cityName)) {

						City city = coolWeatherDB.loadCityByCityName(cityName);

						if (city != null) {

							qureyFromService(city.getCity_code(), CITY);

						}
					} else {

						initBaiduMap();
					}

					/***
					 * 加载数据
					 */
					loadInfo();

					isLoading = false;

				}

			}
		});

		topImageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				slideMenu.showOrHideMenu();

			}
		});

	}

	private void initGridView() {

		View mainView = layoutInflater.inflate(R.layout.layout_main, slideMenu);

		gridView = (GridView) mainView.findViewById(R.id.gridView1);

		String[] arrayStrings = getResources().getStringArray(R.array.citys);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.text_item, arrayStrings);

		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Animation animation = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.refresh);

				TextView item = (TextView) v;

				// myToastShow("you click: " + item.getText());

				refresh.startAnimation(animation);

				if (item.getText().equals("当前位置")) {

					/**
					 * 初始化地图数据
					 */
					initBaiduMap();

					/***
					 * 加载数据
					 */
					loadInfo();

				} else {

					City city = coolWeatherDB.loadCityByCityName(item.getText()
							.toString());

					if (city != null) {

						qureyFromService(city.getCity_code(), CITY);

						/***
						 * 加载数据
						 */

						loadInfo();
					}

				}
				refresh.clearAnimation();

			}
		});
	}

	/***
	 * 加载省份信息
	 */
	private void loadCities() {

		boolean haveDate = coolWeatherDB.haveDate("City");

		/***
		 * 先从数据库查数据， 如果数据库查不到就从服务器上面查询
		 * 
		 */

		if (haveDate) {

			// 数据库中存在数据

			int index = 0, last = 1;

			if (myAddress.contains("省"))

				index = myAddress.indexOf("省") + 1;

			if (myAddress.contains("市"))

				last = myAddress.indexOf("市");

			String cityName = myAddress.substring(index, last);

			City city = coolWeatherDB.loadCityByCityName(cityName);

			if (city != null) {

				qureyFromService(city.getCity_code(), CITY);
			}

		} else {

			myToastShow("获取城市信息！");

			qureyFromService(null, CITY);
		}

	}

	/***
	 * 
	 * @param code
	 *            查询天气时的code
	 * @param type
	 *            查询的级别
	 */

	private void qureyFromService(String code, int type) {

		// 判断从服务器获取数据的类型
		String address = "";
		switch (type) {
		case 1: // 表示要查询省份的信息

			if (code == null) {

			}
			break;
		case 2: // 表示查询城市信息
			if (code == null) {

				address = "http://mobile.weather.com.cn/js/citylist.xml";

				qureyFromServiceForCities(address, code);
			} else { // 查询城市天气

				address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + code;

				qureyFromServiceForCities(address, code);
			}

			break;
		case 3:

			break;
		default:
			break;
		}

	}

	/***
	 * 从服务器获取省份信息
	 * 
	 * @param address
	 */
	private void qureyFromServiceForCities(String address, final String code) {

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void OnFinsh(String response) {

				// 当从服务器获取数据成功时，解析数据并保存进数据库

				if (!TextUtils.isEmpty(response)) {

					if (code == null) {

						Utility.praseXmlWithDom(coolWeatherDB, response);

					} else {

						// Log.e("TAG",response);
						// 解析xml数据
						Utility.parseWeatherInfo(response, MainActivity.this);

						refresh.clearAnimation();
					}

				} else {

					refresh.clearAnimation();

					myToastShow("加载数据失败 ！");
				}

			}

			@Override
			public void OnError(Exception e) {

				//refresh.clearAnimation();

				Log.e("TAG", "加载信息错误！");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	public void myToastShow(String message) {

		if (toast == null) {
			toast = Toast.makeText(MainActivity.this, message,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.show();
	}
}
