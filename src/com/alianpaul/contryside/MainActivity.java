package com.alianpaul.contryside;

import java.util.ArrayList;
import java.util.List;

import com.alianpaul.contryside.layout.AppInfoAdapter;
import com.alianpaul.contryside.layout.ForecastAdapter;
import com.alianpaul.contryside.layout.NonScrollableGridView;
import com.alianpaul.contryside.model.AppInfo;
import com.alianpaul.contryside.model.AppInfoDataBaseHelper;
import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;

public class MainActivity extends FragmentActivity implements 
		ActionBar.TabListener{

	SectionPagerAdapter mSectionPagerAdapter;
	ViewPager mViewPager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(R.color.SeaGreen));
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionPagerAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for(int i = 0; i < mSectionPagerAdapter.getCount(); i++){
			actionBar.addTab(
					actionBar.newTab()
						.setText(mSectionPagerAdapter.getPageTitle(i))
						.setTabListener(this));
		}
		
	}
	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public static class SectionPagerAdapter extends FragmentPagerAdapter {

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i){
				case 0:
					//the launch weather section 
					Fragment wfragment = new WeatherSectionFragment();
					
					return wfragment; 
					
				case 1:
					//the apps section
					Fragment afragment = new AppsSectionFragment();
					
					return afragment;
				default:
					//NFC info section;
					Fragment tfragment = new TagSectionFragment();
					Bundle args = new Bundle();
                    args.putInt(TagSectionFragment.ARG_SECTION_NUMBER, i + 1);
                    tfragment.setArguments(args);
					return tfragment;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch(position){
				case 0:
					return "天气";
				case 1:
					return "应用";
				default:
					return "状态";
			}
		}
		
	}
	
	public static class WeatherSectionFragment extends Fragment implements
			AMapLocalWeatherListener,
			AMapLocationListener{
		
		Context context = null;
		
		public static final String TAG = "WeatherSectionFragment";
		
		private LocationManagerProxy mLocationManagerProxy;
		private TextView currentLocationTV = null;
		private TextView currentTemperatureTV = null;
		private TextView currentWeatherTV = null; 
		
		private GridView futureWeatherGV = null;
		private List<AMapLocalDayWeatherForecast> forcasts = null;
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_section_weather, container, false);
			
			currentLocationTV = (TextView) rootView.findViewById(R.id.currentLocationTV);
			currentTemperatureTV = (TextView) rootView.findViewById(R.id.currentTemperatureTV);
			currentWeatherTV = (TextView) rootView.findViewById(R.id.currentWeatherTV);
			
			futureWeatherGV = (GridView) rootView.findViewById(R.id.futureWeatherGV);
			
			context = getActivity().getApplicationContext();
			
			initMapWeatherAPI();
			return rootView;
		}
		
		private void initMapWeatherAPI() {
			Log.d(TAG,"initMapWeatherAPI executed");
	        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
	        mLocationManagerProxy.requestLocationData(
	                LocationProviderProxy.AMapNetwork, -1, 15, this);
	    }
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
			if(aMapLocalWeatherForecast != null && aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0){
				 
		        forcasts = aMapLocalWeatherForecast.getWeatherForecast();
		        ForecastAdapter forcastAdapter = new ForecastAdapter(context, R.layout.future_weather_item, forcasts);
				futureWeatherGV.setAdapter(forcastAdapter);
		        
		    }else{
		        Toast.makeText(context,"ERROR:"+ aMapLocalWeatherForecast.getAMapException().getErrorMessage(), Toast.LENGTH_SHORT).show();
		    }
		}


		@Override
		public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
			Log.d(TAG, "onWeatherLiveSerched executed");
			if(aMapLocalWeatherLive!=null && aMapLocalWeatherLive.getAMapException().getErrorCode() == 0){
				currentLocationTV.setText(aMapLocalWeatherLive.getCity());
				currentTemperatureTV.setText(aMapLocalWeatherLive.getTemperature()+"°");
				currentWeatherTV.setText(aMapLocalWeatherLive.getWeather()+" | "+aMapLocalWeatherLive.getWindDir()+"风 "+aMapLocalWeatherLive.getWindPower()+"级");
			}else{
				Toast.makeText(getActivity().getApplicationContext(), "ERROR:" + aMapLocalWeatherLive.getAMapException().getErrorMessage(), Toast.LENGTH_SHORT).show();
			}
		}


		@Override
		public void onLocationChanged(AMapLocation arg0) {
			mLocationManagerProxy.requestWeatherUpdates(
	                LocationManagerProxy.WEATHER_TYPE_LIVE, this);
			mLocationManagerProxy.requestWeatherUpdates(
	                LocationManagerProxy.WEATHER_TYPE_FORECAST, this);
		}
		
		
	}
	
	
	public static class AppsSectionFragment extends Fragment {
		
		private AppInfoDataBaseHelper dbHelper = null;
		private Context context;
		
		private List<AppInfo> agriApps = new ArrayList<AppInfo>();
		private List<AppInfo> socialApps = new ArrayList<AppInfo>();
		private List<AppInfo> shoppingApps = new ArrayList<AppInfo>();
		
		private AppInfoAdapter agriAppsAdapter;
		private AppInfoAdapter socialAppsAdapter;
		private AppInfoAdapter shoppingAppsAdapter;
		
		private TextView agriAppsTittleTV;
		private TextView socialAppsTittleTV;
		private TextView shoppingAppsTittleTV;
		
		private NonScrollableGridView agriAppsGV;
		private NonScrollableGridView socialAppsGV;
		private NonScrollableGridView shoppingAppsGV;
		private Button addAppBTN;
		private OnItemClickListener appClickListener;
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_section_apps, container, false);
			context = getActivity().getApplicationContext();
			dbHelper = new AppInfoDataBaseHelper(context, "AppStore.db", null, 3);
						
			//init views
			
			agriAppsTittleTV = (TextView) rootView.findViewById(R.id.agriAppsTittleTV);
			socialAppsTittleTV = (TextView) rootView.findViewById(R.id.socialAppsTittleTV);
			shoppingAppsTittleTV = (TextView) rootView.findViewById(R.id.shoppingAppsTittleTV);
			
			//custom button,add new apps
			rootView.findViewById(R.id.addAppBTN)
						.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								switch (event.getAction()) {
						          case MotionEvent.ACTION_DOWN: {
						              Button view = (Button) v;
						              view.getBackground().setColorFilter(0x77000000, Mode.SRC_ATOP);
						              v.invalidate();
						              break;
						          }
						          case MotionEvent.ACTION_UP:{
						        	  Intent intent = new Intent(getActivity(), CustomActivity.class);
						        	  startActivity(intent);
						        	  
						          }
						          case MotionEvent.ACTION_CANCEL: {
						              Button view = (Button) v;
						              view.getBackground().clearColorFilter();
						              view.invalidate();
						              break;
						          }
					          }
								return true;
							}
						});
			
			//prepare the app item click event
			appClickListener = new OnItemClickListener() {

				private AppInfo app = null;
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					if(parent.getId() == R.id.agriAppsGV){
						app = agriApps.get(position);
					}else if(parent.getId() == R.id.socialAppsGV){
						app = socialApps.get(position);
					}else if(parent.getId() == R.id.shoppingAppsGV){
						app = shoppingApps.get(position);
					}
					
					String targetPackageName = app.getPackageName();
					if(isPackageExist(targetPackageName)){
						Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(targetPackageName);
						startActivity(launchIntent);
					}else{
						Toast.makeText(context, "您还未安装"+app.getTittle()+",前往豌豆荚进行安装", Toast.LENGTH_LONG).show();
						String url = "http://www.wandoujia.com/apps/"+targetPackageName;
						Intent launchBrowserIntent = new Intent(Intent.ACTION_VIEW);
						launchBrowserIntent.setData(Uri.parse(url));
						startActivity(launchBrowserIntent);
					}
				}
				
				private boolean isPackageExist(String targetPackage) {
					List<ApplicationInfo> packages;
			        PackageManager pm;

			        pm = getActivity().getPackageManager();        
			        packages = pm.getInstalledApplications(0);
			        for (ApplicationInfo packageInfo : packages) {
			            if(packageInfo.packageName.equals(targetPackage))
			                return true;
			        }
			        return false;
				}
			};
			
			
			agriAppsAdapter = new AppInfoAdapter(getActivity(), R.layout.app_item, agriApps);
			agriAppsGV = ((NonScrollableGridView) rootView.findViewById(R.id.agriAppsGV));
			
			
			socialAppsAdapter = new AppInfoAdapter(getActivity(), R.layout.app_item, socialApps);
			socialAppsGV = ((NonScrollableGridView) rootView.findViewById(R.id.socialAppsGV));
			
			
			shoppingAppsAdapter = new AppInfoAdapter(getActivity(), R.layout.app_item, shoppingApps);
			shoppingAppsGV = ((NonScrollableGridView) rootView.findViewById(R.id.shoppingAppsGV));
			
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			
			agriAppsAdapter.clear();
			socialAppsAdapter.clear();
			shoppingAppsAdapter.clear();
			
			//every time we see,we need to refresh the data;
			
			initAppData();
			if(agriApps.isEmpty()){
				agriAppsTittleTV.setVisibility(View.GONE);
			}else{
				agriAppsTittleTV.setVisibility(View.VISIBLE);
			}
			
			if(socialApps.isEmpty()){
				socialAppsTittleTV.setVisibility(View.GONE);
			}else{
				socialAppsTittleTV.setVisibility(View.VISIBLE);
			}
			
			if(shoppingApps.isEmpty()){
				shoppingAppsTittleTV.setVisibility(View.GONE);
			}else{
				shoppingAppsTittleTV.setVisibility(View.VISIBLE);
			}
			agriAppsGV.setAdapter(agriAppsAdapter);
			agriAppsGV.setOnItemClickListener(appClickListener);
			socialAppsGV.setAdapter(socialAppsAdapter);
			socialAppsGV.setOnItemClickListener(appClickListener);
			shoppingAppsGV.setAdapter(shoppingAppsAdapter);
			shoppingAppsGV.setOnItemClickListener(appClickListener);
		}
		
		
		
		private void initAppData(){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AppInfo app = null;
			Cursor cursor = db.query("AppInfo", null, null, null, null, null, null);
			if(cursor.moveToFirst()){
				do{
					
					String title = cursor.getString(cursor.getColumnIndex("title"));
					int imageresid = cursor.getInt(cursor.getColumnIndex("imageresid"));
					String packagename = cursor.getString(cursor.getColumnIndex("packagename"));
					boolean selected = (cursor.getInt(cursor.getColumnIndex("selected")) == 1);
					int type = cursor.getInt(cursor.getColumnIndex("type"));
					app = new AppInfo(imageresid, title, packagename, selected, type);
					if(!app.isSelected()) continue;
					if(app.getType() == AppInfoDataBaseHelper.AGRI_TYPE){
						agriApps.add(app);
					}else if(app.getType() == AppInfoDataBaseHelper.SOCIAL_TYPE){
						socialApps.add(app);
					}else if(app.getType() == AppInfoDataBaseHelper.SHOPING_TYPE){
						shoppingApps.add(app);
					}
					
				}while(cursor.moveToNext());
			}
			cursor.close();			
		}
		
	}
	
	public static class TagSectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "1";
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_section_tag, container, false);
			Bundle args = getArguments();
			return rootView;
		}
	}



}
