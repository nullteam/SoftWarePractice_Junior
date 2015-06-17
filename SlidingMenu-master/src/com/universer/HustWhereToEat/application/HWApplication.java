package com.universer.HustWhereToEat.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class HWApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this); 
	}


}
