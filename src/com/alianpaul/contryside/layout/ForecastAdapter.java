package com.alianpaul.contryside.layout;

import java.util.List;

import com.alianpaul.contryside.R;
import com.amap.api.location.AMapLocalDayWeatherForecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ForecastAdapter extends ArrayAdapter<AMapLocalDayWeatherForecast> {

	private int resourceID;
	
	public ForecastAdapter(Context context, int resource, List<AMapLocalDayWeatherForecast> objects) {
		super(context, resource, objects);
		resourceID = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AMapLocalDayWeatherForecast forcast = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
		TextView dayTV = (TextView) view.findViewById(R.id.dayTV);
		TextView futureWeatherTV = (TextView) view.findViewById(R.id.futureWeatherTV);
		TextView futureTemperatureTV = (TextView) view.findViewById(R.id.futureTemperatureTV);
		dayTV.setText(forcast.getDate());
		futureTemperatureTV.setText(forcast.getDayTemp()+"бу/"+forcast.getNightTemp()+"бу");
		futureWeatherTV.setText(forcast.getDayWeather()+"/"+forcast.getNightWeather());
		return view;
	}

}
