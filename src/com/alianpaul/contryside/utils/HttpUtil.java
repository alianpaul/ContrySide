package com.alianpaul.contryside.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import android.util.Base64;
import android.util.Log;

public class HttpUtil {
	
	public static final String TAG = "HttpUtil";
	
	public static void sendLoginRequest(final String severAddress,final String RFID, final String  macAddress, final LoginRequestCallbackListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Log.d("sendLoginRequest", "Executed");
					HttpPost httpPost = new HttpPost(severAddress);
					//set the http timeout
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
					HttpConnectionParams.setSoTimeout(httpParams, 5000);
					//form the form-data
					
					String type = "0";
					byte[] typeBytes = type.getBytes("UTF-8");
					String typeBase64 = Base64.encodeToString(typeBytes, Base64.DEFAULT);
					Log.d(TAG, "type's " + typeBase64);
					
					String behavior = "0";
					byte[] behaviorBytes = behavior.getBytes("UTF-8");
					String behaviorBase64 = Base64.encodeToString(behaviorBytes, Base64.DEFAULT);
					Log.d(TAG, "behavior's " + behaviorBase64);
					
					
					byte[] uidBytes = RFID.getBytes("UTF-8");
					String uidBase64 = Base64.encodeToString(uidBytes, Base64.DEFAULT);
					Log.d(TAG, "uid's " +RFID +" "+ uidBase64);
					
					
					byte[] macBytes = macAddress.getBytes("UTF-8");
					String macBase64 = Base64.encodeToString(macBytes, Base64.DEFAULT);
					Log.d(TAG, "mac's " + macAddress+" "+ macBase64);
							
							
					//send out the Request
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type",typeBase64));
					params.add(new BasicNameValuePair("behavior", behaviorBase64));
					params.add(new BasicNameValuePair("uid", uidBase64));
					params.add(new BasicNameValuePair("mac", macBase64));
					UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpPost.setEntity(requestEntity);
					
					String responseText;
					
					HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpPost);
					Log.d(TAG,"execute return");
					if(httpResponse.getStatusLine().getStatusCode() == 200) {
						
						HttpEntity reponseEntity = httpResponse.getEntity();
						responseText = EntityUtils.toString(reponseEntity);
						Log.d("reponse text",responseText);
						if(listener != null) {
							//»Øµ÷onFinish()
							Log.d("listener.onFinish()","executed");
							listener.onFinish(responseText);
						}
						
					}else{
						responseText = "id=-1";
						if(listener != null){
							listener.onFinish(responseText);
						}
					}
					
				} catch (Exception e) {
					if(listener != null) {
						listener.onError(e);
					}
				}
			}
		}).start();
	}
	
	public static void sendWeatherRequest() {
		
	}
}
