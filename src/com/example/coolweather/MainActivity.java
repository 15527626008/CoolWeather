package com.example.coolweather;

import java.util.List;
import java.util.zip.Inflater;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Province;
import com.example.coolweather.slideMenu.SlideMenu;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private CoolWeatherDB coolWeatherDB;
	
	private SlideMenu slideMenu;
	
	private LayoutInflater layoutInflater;
	
	private GridView gridView;
	
	private final int PROVINCE = 1;
	
	private final int CITY = 2 ;
	
	private final int COUNTY = 3 ;
	
	//存放省份的list
	private List<Province> provinces;
	
	//获取所有城市
	private List<City> cityies;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
		
		
		
		loadProvince();
		
		
		
	}
	
	//初始化数据
	
    private void init() {
		
    	coolWeatherDB = CoolWeatherDB.getCoolWeatherDB(this);
    	
    	slideMenu = (SlideMenu) this.findViewById(R.id.slideMenu1);
    	
    	layoutInflater = LayoutInflater.from(this);
    	
    	View mainView = layoutInflater.inflate(R.layout.layout_main, slideMenu);
    	
    	gridView = (GridView) mainView.findViewById(R.id.gridView1);
    	
    	String[] arrayStrings = getResources().getStringArray(R.array.citys);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.text_item,arrayStrings );
    	
    	gridView.setAdapter(adapter);
    	
	}
	/***
     * 加载省份信息
     */
	private void loadProvince() {
		
		provinces = coolWeatherDB.getProvinces();
		
		
		
		/***
		 * 先从数据库查数据，
		 * 如果数据库查不到就从服务器上面查询
		 *  
		 */
		
		if(provinces.size() > 0){
			
			
			
			
		}else {
			
			qureyFromService(null,PROVINCE);
		}
		
	}
    
	/***
	 * 
	 * @param code   查询天气时的code
	 * @param type   查询的级别
	 */
	
	private void qureyFromService(String code,int type) {
		
		//判断从服务器获取数据的类型
		
		switch (type) {
		case 1: //表示要查询省份的信息
			String address = "";
			if(code == null){
				
				address = "http://www.weather.com.cn/data/list3/city.xml";
				
				qureyFromServiceForProvince(address);
			} 
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		default:
			break;
		}
		
		
	}
    
	/***
	 * 从服务器获取省份信息
	 * @param address
	 */
	private void qureyFromServiceForProvince(String address) {

		HttpUtil.sendHttpRequest(address , new HttpCallbackListener() {
			
			@Override
			public void OnFinsh(String response) {
				
				boolean result = Utility.handleProvincesResponse(coolWeatherDB, response);
				
				//当从服务器获取数据成功时
				
				if(result){
					
					
				}else{
					
					Toast.makeText(MainActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void OnError(Exception e) {
				
				Log.i("TAG", e.toString());
			}
		});
		
		
	}
	private void initDate(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
