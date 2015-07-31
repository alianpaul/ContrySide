package com.alianpaul.contryside.layout;

import java.util.List;

import com.alianpaul.contryside.R;
import com.alianpaul.contryside.model.AppInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfoAdapter extends ArrayAdapter<AppInfo> {
	private int resourceID;

	public AppInfoAdapter(Context context, int resource,  List<AppInfo> objects) {
		super(context, resource,  objects);
		resourceID = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appInfo = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
		ImageView appIconIV = (ImageView) view.findViewById(R.id.appIconIV);
		TextView appTittleTV = (TextView) view.findViewById(R.id.appTittleTV);
		appIconIV.setImageResource(appInfo.getImageResourceID());
		appTittleTV.setText(appInfo.getTittle());
		
		if(appInfo.isSelected()){
			view.setBackgroundResource(R.color.Snow);
		}else{
			view.setBackgroundColor(Color.GRAY);
		}
		
		return view;
		
		
	}
}
