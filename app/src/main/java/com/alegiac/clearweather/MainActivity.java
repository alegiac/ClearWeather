package com.alegiac.clearweather;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alegiac.clearweather.adapter.LocationAdapter;
import com.alegiac.clearweather.data.ConnectionDetector;
import com.alegiac.clearweather.data.Constant;
import com.alegiac.clearweather.data.DatabaseManager;
import com.alegiac.clearweather.data.GlobalVariable;
import com.alegiac.clearweather.model.ForecastResponse;
import com.alegiac.clearweather.model.Location;
import com.alegiac.clearweather.model.WeatherResponse;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import java.util.ArrayList;

public class MainActivity extends SlidingActivity {

    private TextView tv_addres, tv_date, tv_temp,
            tv_pressure, tv_humidity, tv_wind,
            tv_sunset, tv_sunrise, tv_description;

    private TextView tv_temp_[] = new TextView[5];
    private ImageView img_small_[] = new ImageView[5];

    private TextView tv_day_1, tv_day_2, tv_day_3,
            tv_day_4,tv_day_5;

    private LinearLayout lyt_main;

    private Button bt_location, bt_about;
    private Button bt_addlocation;
    public ListView listview_location;
    private ScrollView scroll_view;
    //private WeatherResponse weather= new WeatherResponse();
    //private ForecastResponse forecast = new ForecastResponse();
    private GlobalVariable global;
    //private GPSTracker gps ;
    private ImageView img_weather;
    private DatabaseManager db;
    private ConnectionDetector cd;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    //SwipeRefreshLayout mSwipeRefreshLayout;

    //for ads
    //private InterstitialAd mInterstitialAd;

    private boolean isOnexecute=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu_location);
        getSlidingMenu().setBehindOffset(100);

    }

    public void displayData(WeatherResponse w, ForecastResponse f){
        try {
            tv_addres.setText(w.name+", "+w.sys.country);
            tv_date.setText(global.getLastUpdate(w.dt));
            tv_temp.setText(global.getTemp(w.main.temp));
            tv_pressure.setText(Constant.sSpiltter(w.main.pressure)+" hpa");
            tv_humidity.setText(Constant.sSpiltter(w.main.humidity)+" %");
            tv_wind.setText(Constant.sSpiltter(w.wind.speed)+" m/s");
            global.setDrawableIcon(w.weather.get(0).icon, img_weather);
            global.setLytColor(w.weather.get(0).icon, lyt_main);
            tv_description.setText(w.weather.get(0).description.toUpperCase());

            tv_sunset.setText(global.getTime(w.sys.sunset)+" sunset");
            tv_sunrise.setText(global.getTime(w.sys.sunrise)+" sunrise");
            for (int i = 0; i < f.list.size(); i++) {
                tv_temp_[i].setText(global.getTemp(f.list.get(i).temp.day));
            }

            for (int i = 0; i < f.list.size(); i++) {
                Log.d("icon" + i, f.list.get(i).weather.get(0).icon);
                global.setDrawableSmallIcon(f.list.get(i).weather.get(0).icon, img_small_[i]);
            }

            tv_day_1.setText(global.getDay(f.list.get(0).dt));
            tv_day_2.setText(global.getDay(f.list.get(1).dt));
            tv_day_3.setText(global.getDay(f.list.get(2).dt));
            tv_day_4.setText(global.getDay(f.list.get(3).dt));
            tv_day_5.setText(global.getDay(f.list.get(4).dt));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed read data", Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshList(){
        ArrayList<Location> itemsloc = new ArrayList<Location>();
        for (int i = 0; i < global.getListCode().size(); i++) {
            itemsloc.add(global.getLocation(global.getListCode().get(i)));
        }
        listview_location.setAdapter(new LocationAdapter(MainActivity.this, itemsloc));
    }

}
