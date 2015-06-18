package com.universer.HustWhereToEat.activity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.universer.HustWhereToEat.R;

import android.app.Activity;
import android.os.Bundle;

public class RoutelineActivity extends Activity {
	private BaiduMap mBaiduMap = null;
	private MapView mapView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeline_layout);
		findView();
		initMap();
	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.activity_routline_MapView);
		
	}
	private void initMap() {
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	}
}
