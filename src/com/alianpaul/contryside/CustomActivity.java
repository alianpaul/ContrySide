package com.alianpaul.contryside;

import java.util.ArrayList;

import com.alianpaul.contryside.layout.AppInfoAdapter;
import com.alianpaul.contryside.model.AppInfo;
import com.alianpaul.contryside.model.AppInfoDataBaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class CustomActivity extends Activity {
	private GridView customAppsGV;
	private Button sureCustomBTN;
	
	private ArrayList<AppInfo> allAppsInfo = new ArrayList<AppInfo>();
	private AppInfoDataBaseHelper dbHelper;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_custom);
		
		dbHelper = new AppInfoDataBaseHelper(this, "AppStore.db", null, 3);
		db = dbHelper.getWritableDatabase();
		
		customAppsGV = (GridView) findViewById(R.id.customAppsGV);
		sureCustomBTN = (Button) findViewById(R.id.sureCustomBTN);
		
		
		initAppsData();
		
		AppInfoAdapter allAppsAdapter = new AppInfoAdapter(CustomActivity.this, R.layout.app_item, allAppsInfo);
		customAppsGV.setAdapter(allAppsAdapter);
		
		customAppsGV.setOnItemClickListener(new OnItemClickListener() {
			
			private AppInfo app = null;
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				app = allAppsInfo.get(position);
				if(app.isSelected()){
					app.setSelected(false);
					ContentValues values = new ContentValues();
					values.put("selected",false);
					db.update("AppInfo", values, "imageresid = ?", new String[] {app.getImageResourceID()+""});
					view.setBackgroundColor(Color.GRAY);
				}else{
					app.setSelected(true);
					ContentValues values = new ContentValues();
					values.put("selected",true);
					db.update("AppInfo", values, "imageresid = ?", new String[] {app.getImageResourceID()+""});
					view.setBackgroundColor(Color.WHITE);
				}
			}
		});
		
		
		sureCustomBTN.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			          case MotionEvent.ACTION_DOWN: {
			              Button view = (Button) v;
			              view.getBackground().setColorFilter(0x77000000, Mode.SRC_ATOP);
			              v.invalidate();
			              break;
			          }
			          case MotionEvent.ACTION_UP:{
			        	  onBackPressed();
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
	}

	private void initAppsData() {
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
				allAppsInfo.add(app);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
	}
}
