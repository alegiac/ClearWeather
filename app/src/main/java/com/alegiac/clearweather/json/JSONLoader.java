package com.alegiac.clearweather.json;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alegiac.clearweather.MainActivity;
import com.alegiac.clearweather.data.Constant;
import com.alegiac.clearweather.data.DatabaseManager;
import com.alegiac.clearweather.data.GlobalVariable;
import com.alegiac.clearweather.model.City;
import com.alegiac.clearweather.model.ForecastResponse;
import com.alegiac.clearweather.model.Location;
import com.alegiac.clearweather.model.WeatherResponse;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONLoader extends AsyncTask<String, String, Location>{
	private JSONParser jsonParser = new JSONParser();
	private String jsonWeather = null, 
			jsonForecast= null, 
			status="null";
	
	private Context ctx;
	private LinearLayout lyt_form;
	private LinearLayout lyt_progress;
	private TextView tv_message; 
	private Dialog dialog;
	private DatabaseManager db;
	private GlobalVariable global;
	private MainActivity act;
	
	public JSONLoader(MainActivity act, LinearLayout lyt_form, LinearLayout lyt_progress, TextView tv_message, Dialog dialog) {
		this.act=act;
		this.ctx=act.getApplicationContext();
		this.lyt_form=lyt_form;
		this.lyt_progress=lyt_progress;
		this.tv_message=tv_message;
		this.dialog=dialog;
		global 	= (GlobalVariable) act.getApplication();
		db = new DatabaseManager(act);
	}


	@Override
	protected void onPreExecute() {
		lyt_form.setVisibility(View.GONE);
		lyt_progress.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	@Override
	protected Location doInBackground(String... params) {
		Location  itemLocation 	= new Location();
		WeatherResponse weather		= new WeatherResponse();
		ForecastResponse forecast 	= new ForecastResponse();
		try {
			Thread.sleep(50);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			City city = db.getWordsFormAutocomplate(params[0]);
			if(city!=null){
				itemLocation.setId(city.getId());
				itemLocation.setName(city.getName());
				itemLocation.setCode(city.getCode());
				
				String url_weather 		= Constant.getURLweather(city.getId());
				String url_forecast 	= Constant.getURLforecast(city.getId());
				
				JSONObject json_weather 	= jsonParser.makeHttpRequest(url_weather,"POST", param);
				JSONObject json_forecast 	= jsonParser.makeHttpRequest(url_forecast,"POST", param);
				
				jsonWeather 	= json_weather.toString();
				jsonForecast	= json_forecast.toString();
			
				itemLocation.setJsonWeather(jsonWeather);
				itemLocation.setJsonForecast(jsonForecast);
				
				status="success";
			}else{
				status="Invalid city name";
			}
			
		} catch (Exception e) {
			status = e.getMessage();
			e.printStackTrace();
		}
		
		return itemLocation;
	}
	
	protected void onPostExecute(Location result) {
		lyt_form.setVisibility(View.VISIBLE);
		lyt_progress.setVisibility(View.GONE);
		if(status.equals("success")){
			global.saveLocation(result);
			act.refreshList();
			dialog.dismiss();
		}
		tv_message.setText(status);
		//Toast.makeText(ctx, status, Toast.LENGTH_LONG).show();
	};

}
