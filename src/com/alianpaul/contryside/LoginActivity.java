package com.alianpaul.contryside;


import java.text.Format;

import com.alianpaul.contryside.R;
import com.alianpaul.contryside.R.id;
import com.alianpaul.contryside.R.layout;
import com.alianpaul.contryside.R.string;
import com.alianpaul.contryside.utils.HttpUtil;
import com.alianpaul.contryside.utils.LoginRequestCallbackListener;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	public static final String TAG = "LoginActivity";
	
	public static final int ACCESS_ERROR= 1;
	public static final int LOGIN_SUCCESS = 2;
	public static final int VARIFY_ERROR = 3;
	
	public static final int SETTING_ERROR_WIFI = 1;
	public static final int SETTING_ERROR_NFC = 2;
	
	private PendingIntent loginPendingIntent;
	private NfcAdapter nfcAdapter;
	private String tagId;
	private String macAddr;
	private String gatewayIPAddr;
	private TextView helloTextView;
	
	
	 
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			String response;
			switch (msg.what) {
			case ACCESS_ERROR:
				Toast.makeText(LoginActivity.this, "ERROR:" + "id=-1", Toast.LENGTH_LONG).show();
				helloTextView.setText("WLAN连接错误，请检查WLAN是否连接正确后重新刷卡");
			break;
			case VARIFY_ERROR:
				response = (String)msg.obj;
				Toast.makeText(LoginActivity.this, "ERROR:" + msg.obj, Toast.LENGTH_LONG).show();
				helloTextView.setText("身份验证错误");
			break;
			case LOGIN_SUCCESS:
				//Toast.makeText(LoginActivity.this, "SUCCESS:" + msg.obj, Toast.LENGTH_LONG).show();
				helloTextView.setText("验证成功");
				Handler waithandler = new Handler();
				waithandler.postDelayed(new Runnable(){
				@Override
				  public void run(){
				   // do something
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("tagID", tagId);
					startActivity(intent);
					finish();
				 }
				 }, 1000);
				
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		
		helloTextView = (TextView) findViewById(R.id.text);
		
		AlertDialog nfcNotSupportedDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
        	nfcNotSupportedDialog.setTitle(R.string.error);
        	nfcNotSupportedDialog.setMessage(getText(R.string.no_nfc_error));
        	nfcNotSupportedDialog.show();
  
        }
        
        loginPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        
        
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettingsDialog(SETTING_ERROR_NFC);
            }
            nfcAdapter.enableForegroundDispatch(this, loginPendingIntent, null, null);
            getMacAddressAndGateway();
		}
		
		
		
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
	
	@Override
    public void onNewIntent(Intent intent) {
		Log.d(TAG, "OnNewIntent "+intent.getAction());
        setIntent(intent);
        resolveNFCIntent(intent);
    }
	
	
	private void showWirelessSettingsDialog(int settingErrorType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(settingErrorType == SETTING_ERROR_NFC){
        	builder.setMessage(R.string.nfc_not_enabled_error);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            });
        }else if(settingErrorType == SETTING_ERROR_WIFI){
        	builder.setMessage(R.string.wifi_not_enabled_error);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
        }
        
        
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }
	
	private void getMacAddressAndGateway() {
		macAddr = "";
		WifiManager wifiMng = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
	    WifiInfo wifiInfor = wifiMng.getConnectionInfo();  
	    macAddr = wifiInfor.getMacAddress().replace(":", "");
	    DhcpInfo dhcpInfo = wifiMng.getDhcpInfo();
	    gatewayIPAddr = Formatter.formatIpAddress(dhcpInfo.gateway);
	    Log.d(TAG,"gateway:"+gatewayIPAddr);
	    if(gatewayIPAddr.equals("0.0.0.0"))
	    	showWirelessSettingsDialog(SETTING_ERROR_WIFI);
	    
	}
	
	private void resolveNFCIntent(Intent intent) {
		String action = intent.getAction();
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.d(TAG, "get Tag succ");
			byte[] idInBytes = tagFromIntent.getId();
			Log.d(TAG, " "+idInBytes.length);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= idInBytes.length - 1; i++) {
	            int b = idInBytes[i] & 0xff;
	            if (b < 0x10)
	                sb.append('0');
	            sb.append(Integer.toHexString(b));
	        }
	        tagId = sb.toString().toUpperCase();
	        Log.d(TAG, tagId);
	        helloTextView.setText("正在验证您的身份...");
	        HttpUtil.sendLoginRequest("http://"+ gatewayIPAddr +"/cgi-bin/luci/authuid", tagId, macAddr, new LoginRequestCallbackListener() {
				
				@Override
				public void onFinish(String responseText) {
					Log.d(TAG, "onFinish()");
					if(responseText.equalsIgnoreCase("id=0")) {
						System.out.println("login success: "+responseText);
						Message message = new Message();
						message.what = LOGIN_SUCCESS;
						message.obj = responseText;
						handler.sendMessage(message);
					}else {
						System.out.println("error: "+responseText);
						Message message = new Message();
						message.what = VARIFY_ERROR;
						message.obj = responseText;
						handler.sendMessage(message);
					}
				}
				
				@Override
				public void onError(Exception e) {
					Message message = new Message();
					message.what = ACCESS_ERROR;
					message.obj = e;
					handler.sendMessage(message);
				}
			});
		}
		
	}

}
