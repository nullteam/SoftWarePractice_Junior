package com.universer.HustWhereToEat.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class HWApplication extends Application {

	private static HWApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		SDKInitializer.initialize(this); 
	}

	public static HWApplication getInstance() {
		return instance;
	}

}
