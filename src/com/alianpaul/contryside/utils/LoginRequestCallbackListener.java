package com.alianpaul.contryside.utils;

public interface LoginRequestCallbackListener {
	void onFinish(String responseText);
	
	void onError(Exception e);
}
