package com.alianpaul.contryside;

import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

public class StartActivity extends Activity implements AMapLocalWeatherListener{

	private LocationManagerProxy mLocationManagerProxy;
	public static final int SETTING_ERROR_WIFI = 3;
	public static final int SETTING_ERROR_NFC = 4;
	
	private boolean internetChecked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_layout);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		Handler waithandler = new Handler();
		waithandler.postDelayed(new Runnable(){
		@Override
			public void run(){
				if(wifiInfo.isConnected() || mobileInfo.isConnected()){
					//connected, test internet;
					init();
				}else{
					//both don't connected;jump to LoginActivity to set up;
					Intent intent = new Intent(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}, 1000);
		
	}
		
	private void init() {
	    mLocationManagerProxy = LocationManagerProxy.getInstance(this);
	    mLocationManagerProxy.requestWeatherUpdates(
	    LocationManagerProxy.WEATHER_TYPE_LIVE, this);
	}

	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast arg0) {
		
	}

	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
		Intent intent = null;
		if(aMapLocalWeatherLive!=null && aMapLocalWeatherLive.getAMapException().getErrorCode() == 0){
			intent = new Intent(StartActivity.this, MainActivity.class);
	    }else{
	    	intent = new Intent(StartActivity.this, LoginActivity.class);
	    }
		startActivity(intent);
		finish();
		return;
	}
	
}
