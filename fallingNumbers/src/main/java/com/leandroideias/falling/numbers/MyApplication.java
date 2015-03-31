package com.leandroideias.falling.numbers;

import android.app.Application;
import android.content.res.Configuration;

import com.leandroideias.falling.numbers.util.CustomLayout;

public class MyApplication extends Application {
	private CustomLayout customLayout;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		customLayout = new CustomLayout(getApplicationContext());
	}
	
	public CustomLayout getCustomLayout(){
		return customLayout;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
