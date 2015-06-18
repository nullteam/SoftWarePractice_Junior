package com.universer.HustWhereToEat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.universer.HustWhereToEat.R;

public class RoutelineActivity extends Activity implements
		OnGetRoutePlanResultListener {
	private BaiduMap mBaiduMap = null;
	private MapView mapView = null;
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private MyLocationData mLocationData = null;
	private LatLng startLocation = null;
	private LatLng endLocation = null;
	private LocationClient mLocationClient;
	private BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Log.v("LOC NULL", "noloc");
				return;
			}
			Log.v("LOC suc", "haveloc");
			animateToLoc(location);
			POISearch(location);
			searchRoute(null);
			mLocationClient.stop();

		}

		private void animateToLoc(BDLocation location) {

			mLocationData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(mLocationData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			startLocation = ll;
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mapView.refreshDrawableState();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_routeline_layout);
		findView();
		initMap();
		initLoc();
		initPoiSearchSet();
		initRoutPlanSet();
	}

	private void initPoiSearchSet() {
		
	}

	protected void POISearch(BDLocation location) {
		
	}

	private void initLoc() {
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(mLocationListener);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setAddrType("all");
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK5", "locClient is null or not started");
	}

	public void searchRoute(View v) {
		PlanNode stNode = PlanNode.withLocation(startLocation);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("武汉", "光谷广场");
		 mSearch.transitSearch((new TransitRoutePlanOption())
                 .from(stNode)
                 .city("武汉")
                 .to(enNode));
	}

	private void initRoutPlanSet() {
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.activity_routline_MapView);

	}

	private void initMap() {
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	// listener override
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutelineActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutelineActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		// TODO Auto-generated method stub

	}
	private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
                return BitmapDescriptorFactory.fromResource(R.drawable.map_loc);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
                return BitmapDescriptorFactory.fromResource(R.drawable.map_loc2);
        }
    }

	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {

			return BitmapDescriptorFactory.fromResource(R.drawable.map_loc);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.map_loc2);
		}
	}
}
