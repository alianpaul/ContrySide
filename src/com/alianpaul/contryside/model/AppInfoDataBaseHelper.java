package com.alianpaul.contryside.model;

import com.alianpaul.contryside.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class AppInfoDataBaseHelper extends SQLiteOpenHelper {

	public static final String TAG = "AppInfoDataBaseHelper";
	
	public static final int AGRI_TYPE = 0;
	public static final int SHOPING_TYPE = 1;
	public static final int SOCIAL_TYPE = 2;
	
	public static final String CREATE_APP = "create table AppInfo ("
			+"id integer primary key autoincrement, "
			+"title text, "
			+"imageresid integer,"
			+"packagename text, "
			+"selected blob,"
			+"type integer )";
	
	private Context mContext;

	public AppInfoDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG,"onCreate exed");
		db.execSQL(CREATE_APP);
		
		ContentValues values = new ContentValues();
		values.put("title", "三农信息通");
		values.put("imageresid", R.drawable.ic_snxxt);
		values.put("packagename", "com.nxt.xxtzw");
		values.put("selected",true);
		values.put("type",AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "农管家");
		values.put("imageresid", R.drawable.ic_nongguanjia);
		values.put("packagename", "com.nongguanjia.doctorTian");
		values.put("selected",true);
		values.put("type", AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "农医生");
		values.put("imageresid", R.drawable.ic_peacantdoctor);
		values.put("packagename", "com.satan.peacantdoctor");
		values.put("selected",true);
		values.put("type", AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "掌农");
		values.put("imageresid", R.drawable.ic_zhangnong);
		values.put("packagename", "com.zhongsou.zhangnong1");
		values.put("selected",true);
		values.put("type", AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "天农网");
		values.put("imageresid", R.drawable.ic_tiannong);
		values.put("packagename", "com.tianong.app");
		values.put("selected",true);
		values.put("type", AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "农合网");
		values.put("imageresid", R.drawable.ic_nonghewang);
		values.put("packagename", "cn.b2cf.m.example");
		values.put("selected",false);
		values.put("type", AGRI_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "QQ");
		values.put("imageresid", R.drawable.ic_qq);
		values.put("packagename", "com.tencent.mobileqq");
		values.put("selected",true);
		values.put("type", SOCIAL_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "微信");
		values.put("imageresid", R.drawable.ic_weixin);
		values.put("packagename", "com.tencent.mm");
		values.put("selected",true);
		values.put("type", SOCIAL_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "陌陌");
		values.put("imageresid", R.drawable.ic_momo);
		values.put("packagename", "com.immomo.momo");
		values.put("selected",true);
		values.put("type", SOCIAL_TYPE);
		db.insert("AppInfo", null, values);
		
		
		
		values.clear();
		values.put("title", "京东");
		values.put("imageresid", R.drawable.ic_jd);
		values.put("packagename", "com.jingdong.app.mall");
		values.put("selected",true);
		values.put("type", SHOPING_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "淘宝");
		values.put("imageresid", R.drawable.ic_taobao);
		values.put("packagename", "com.taobao.taobao");
		values.put("selected",true);
		values.put("type", SHOPING_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "天猫");
		values.put("imageresid", R.drawable.ic_tianmao);
		values.put("packagename", "com.tmall.wireless");
		values.put("selected",true);
		values.put("type", SHOPING_TYPE);
		db.insert("AppInfo", null, values);
		
		values.clear();
		values.put("title", "一号店");
		values.put("imageresid", R.drawable.ic_yihaodian);
		values.put("packagename", "com.thestore.main");
		values.put("selected",true);
		values.put("type", SHOPING_TYPE);
		db.insert("AppInfo", null, values);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists AppInfo");
		onCreate(db);
	}

}
