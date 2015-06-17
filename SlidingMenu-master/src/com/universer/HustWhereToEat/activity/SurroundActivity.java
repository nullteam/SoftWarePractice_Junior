package com.universer.HustWhereToEat.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.model.Restaurant;
//假如用到位置提醒功能，需要import该类
//如果使用地理围栏功能，需要import如下类
public class SurroundActivity extends Activity implements
		OnGetPoiSearchResultListener{
	private BaiduMap mBaiduMap = null;
	private MapView mapView = null;
	private LocationClient mLocationClient = null;
	private PoiSearch mPoiSearch = null;
	// private MyLocationOverlay mOverlay;
	
	private MyLocationData mLocationData;
//	private SurroundOverlay overLays;
	// private List<OverlayItem> itemList = new ArrayList<OverlayItem>();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	// private PopupOverlay itemPopLay;
	// public static final String MAPKEY = "2yEzSULUyevqo4nCvyRFY5Hd";
	// private View restautrantPopView;
	private BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Log.v("LOC NULL", "noloc");
				return;
			}
			Log.v("LOC suc", "haveloc");
			initRestaurantData(location);
			
			animateToLoc(location);
			mLocationClient.stop();

		}

		private void animateToLoc(BDLocation location) {
			double mlatitude = location.getLatitude();
			double mlongitude = location.getLongitude();
			mLocationData = new MyLocationData.Builder()  
		    .accuracy(location.getRadius())  
		    // 此处设置开发者获取到的方向信息，顺时针0-360  
		    .direction(100).latitude(location.getLatitude())  
		    .longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(mLocationData); 
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			// Log.v("LOC", mLocationData.latitude + " " +
			// mLocationData.longitude);
			// for (int i = 0; i < restaurantList.size(); i++) {
			// OverlayItem surroundItem = new OverlayItem(restaurantList
			// .get(i).getPoint(), "item" + i, "item" + i);
			// itemList.add(surroundItem);
			// }
			// overLays.addItem(itemList);
			// mOverlay.setData(mLocationData);
			// mapView.getOverlays().add(mOverlay);
			mapView.refreshDrawableState();
			initPoi();
			searchPoi();
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_surround);
		findView();
		initMap();
		initLoc();
//		initPoi();
//		searchPoi();
//		initMapSet();

	}
	
	private void initPoi() {
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}
	
	private void searchPoi() {
//		PoiNearbySearchOption option = new PoiNearbySearchOption();
//		option.location(new LatLng(mLocationData.latitude, mLocationData.longitude));
//		mPoiSearch.searchInCity((new PoiCitySearchOption())  
//			    .city("武汉")  
//			    .keyword("美食"));
//		mPoiSearch.searchNearby(option.keyword("餐厅"));
		mPoiSearch.searchNearby(new PoiNearbySearchOption().location(
				new LatLng(mLocationData.latitude, mLocationData.longitude)).
				keyword("美食"));
	}

	private void initRestaurantData(BDLocation location) {
		List<String> comments = new ArrayList<String>();
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");

		// for (int i = 0; i < 5; i++) {
		// Restaurant res;
		// if (i % 2 == 0) {
		// res = new Restaurant("麦芽芗" + i, null, Restaurant.BIG, "华科生活门",
		// "13098840196", null);
		//
		// } else {
		// res = new Restaurant("鸡蛋灌饼" + i, null, Restaurant.SMALL,
		// "华科南大门", "13098840196", null);
		//
		// }
		Restaurant res;
		{
//			res = new Restaurant("麦芽芗", R.drawable.restaurant_laosichuan + "",
//					Restaurant.SMALL, "华科生活门", "13098840196", comments);
//			res.setPoint(new GeoPoint(
//					(int) ((location.getLatitude() + 0 * 0.002) * 1e6),
//					(int) ((location.getLongitude() + 0 * 0.002) * 1e6)));
//			restaurantList.add(res);
//			res = new Restaurant("西苑咖啡", R.drawable.restaurant_coffee + "",
//					Restaurant.BIG, "华中科技大学西十一舍附近", "13098840196", comments);
//			res.setPoint(new GeoPoint(
//					(int) ((location.getLatitude() + 1 * 0.02) * 1e6),
//					(int) ((location.getLongitude() + 1 * 0.02) * 1e6)));
//			restaurantList.add(res);
//			res = new Restaurant("鸭血粉丝", R.drawable.restaurant_yaxuefensi + "",
//					Restaurant.SMALL, "华中科技大学南三门", "13098840196", comments);
//			res.setPoint(new GeoPoint(
//					(int) ((location.getLatitude() + 2 * 0.02) * 1e6),
//					(int) ((location.getLongitude()) * 1e6)));
//			restaurantList.add(res);
//			res = new Restaurant("简朴田园寨(光谷航母店) ",
//					R.drawable.restaurant_tianyuan + "", Restaurant.BIG,
//					"华中科技大学南大门", "13098840196", comments);
//			res.setPoint(new GeoPoint(
//					(int) ((location.getLatitude() + 3 * 0.02) * 1e6),
//					(int) ((location.getLongitude()) * 1e6)));
//			restaurantList.add(res);
//			res = new Restaurant("氧气层", R.drawable.restaurant_o2 + "",
//					Restaurant.BIG, "华中科技大学西园食堂附近", "13098840196", comments);
//			res.setPoint(new GeoPoint(
//					(int) ((location.getLatitude() + 4 * 0.02) * 1e6),
//					(int) ((location.getLongitude() - 4 * 0.002) * 1e6)));
//			restaurantList.add(res);

		}

	}

	private void initLoc() {
		//
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(mLocationListener);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setAddrType("all");
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else 
			Log.d("LocSDK5", "locClient is null or not started");
		// mOverlay = new MyLocationOverlay(mapView);
		// overLays = new SurroundOverlay(getResources().getDrawable(
		// R.drawable.map_loc2), mapView);
		// mOverlay.setData(mLocationData);
		// mOverlay.enableCompass();
		// mOverlay.setMarker(getResources().getDrawable(R.drawable.icon_geo));
		// mapView.getOverlays().add(mOverlay);
		// mapView.getOverlays().add(overLays);
//		mapView.setZoomControlsPosition(arg0);
//		mapView.getController().setZoom(13);
//		mapView.refresh();
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
		mPoiSearch.destroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

//	private void initMapSet() {
//
//		mapView.setBuiltInZoomControls(true);
//		MapController mMapController = mapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
//		// GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
//		// (int) (116.404 * 1E6));// 用给定的经纬度构造一个GeoPoint，单位是微度(度 * 1E6
//		// mMapController.setCenter(point);// 设置地图中心点
//		mMapController.setZoom(14);// 设置地图zoom级别
//
//	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.activity_surroundMapView);
	}

	private void initMap() {
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	}

	public static Bitmap convertViewToBitmap(View view) {
		view.setDrawingCacheEnabled(false);
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap cacheBitmap = view.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		view.destroyDrawingCache();
		return bitmap;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(SurroundActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(SurroundActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
			.show();
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(SurroundActivity.this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(SurroundActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}

	// class SurroundOverlay extends ItemizedOverlay<OverlayItem> {
	//
	// public SurroundOverlay(Drawable mark, MapView mapView) {
	// super(mark, mapView);
	// }
	//
	// @Override
	// protected boolean onTap(int index) {
	// // 跳转至详细
	// final Restaurant tempRes = restaurantList.get(index);
	// itemPopLay = new PopupOverlay(mapView, new PopupClickListener() {
	//
	// @Override
	// public void onClickedPopup(int index) {
	// // TODO Auto-generated method stub
	// Intent i = new Intent(SurroundActivity.this,
	// DetailActivity.class);
	// i.putExtra("ADDRESS", tempRes.getAddress());
	// i.putExtra("PHONE", tempRes.getPhone());
	// i.putExtra("NAME", tempRes.getName());
	// i.putExtra("IMAGE", tempRes.getImageUrl());
	// i.putStringArrayListExtra("COMMENT",
	// (ArrayList<String>) tempRes.getCommentList());
	// startActivity(i);
	// }
	// });
	// initPopLayView(index);
	// Bitmap[] bmps = new Bitmap[1];
	// bmps[0] = convertViewToBitmap(restautrantPopView);
	// itemPopLay.showPopup(bmps, tempRes.getPoint(), 32);
	// return true;
	// }
	//
	// @Override
	// public boolean onTap(GeoPoint arg0, MapView arg1) {
	// // TODO Auto-generated method stub
	// if (itemPopLay != null) {
	// itemPopLay.hidePop();
	// }
	// return false;
	// }
	//
	// }
	//
	// private void initPopLayView(int index) {
	// Restaurant res = restaurantList.get(index);
	// String address = res.getAddress();
	// String name = res.getName();
	// restautrantPopView = getLayoutInflater().inflate(
	// R.layout.activity_surround_popview, null);
	// ((TextView) restautrantPopView.findViewById(R.id.popView_addressTxt))
	// .setText(address);
	// ((TextView) restautrantPopView.findViewById(R.id.popView_nameTxt))
	// .setText(name);
	// }
}
