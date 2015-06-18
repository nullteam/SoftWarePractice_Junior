package com.universer.HustWhereToEat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.universer.HustWhereToEat.R;

public class SurroundActivity extends Activity implements
		OnGetPoiSearchResultListener {
	private BaiduMap mBaiduMap = null;
	private MapView mapView = null;
	private LocationClient mLocationClient = null;
	private PoiSearch mPoiSearch = null;
	private HashMap<LatLng, PoiInfo> poiLLMap = new HashMap<LatLng, PoiInfo>();
	BitmapDescriptor bd;
	View restautrantPopView;
	private MyLocationData mLocationData;
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
			mLocationData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(mLocationData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
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
		bd = BitmapDescriptorFactory.fromResource(R.drawable.map_loc2);
		findView();
		initMap();
		initLoc();
	}

	private void initPoi() {
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}

	private void searchPoi() {
		PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
		LatLng southwest = new LatLng(mLocationData.latitude - 0.01,
				mLocationData.longitude - 0.012);// 西南
		LatLng northeast = new LatLng(mLocationData.latitude + 0.01,
				mLocationData.longitude + 0.012);// 东北
		LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)
				.include(northeast).build();// 得到一个地理范围对象
		boundSearchOption.bound(bounds);// 设置poi检索范围
		boundSearchOption.keyword("美食");// 检索关键字
		mPoiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
		// boundSearchOption.pageNum(page);
		// mPoiSearch.searchNearby(new PoiNearbySearchOption().location(
		// new LatLng(mLocationData.latitude, mLocationData.longitude)).
		// keyword("美食"));
	}

	private void initRestaurantData(BDLocation location) {
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
		// mOverlay = new MyLocationOverlay(mapView);
		// overLays = new SurroundOverlay(getResources().getDrawable(
		// R.drawable.map_loc2), mapView);
		// mOverlay.setData(mLocationData);
		// mOverlay.enableCompass();
		// mOverlay.setMarker(getResources().getDrawable(R.drawable.icon_geo));
		// mapView.getOverlays().add(mOverlay);
		// mapView.getOverlays().add(overLays);
		// mapView.setZoomControlsPosition(arg0);
		// mapView.getController().setZoom(13);
		// mapView.refresh();
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
		bd.recycle();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.activity_surroundMapView);
	}

	private void initMap() {
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				mBaiduMap.hideInfoWindow();
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		Toast.makeText(SurroundActivity.this, result.getName()+"www", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetPoiResult(final PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(SurroundActivity.this, "未找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			OverlayManager m = new OverlayManager(mBaiduMap) {

				@Override
				public boolean onMarkerClick(Marker arg0) {

					return false;
				}

				@Override
				public List<OverlayOptions> getOverlayOptions() {
					List<OverlayOptions> options = new ArrayList<OverlayOptions>();
					for (int i = 0; i < result.getAllPoi().size(); i++) {
						PoiInfo info = result.getAllPoi().get(i);
						poiLLMap.put(info.location, info);
						OverlayOptions option = new MarkerOptions().position(
								info.location).icon(bd);
						options.add(option);
					}
					return options;
				}
			};
			m.addToMap();
			m.zoomToSpan();
			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {

					final PoiInfo info = poiLLMap.get(marker.getPosition());
					if(info != null) {
						OnInfoWindowClickListener listener = null;
						restautrantPopView = getLayoutInflater().inflate(
								 R.layout.activity_surround_popview, null);
								 ((TextView) restautrantPopView.findViewById(R.id.popView_addressTxt))
								 .setText(info.address);
								 ((TextView) restautrantPopView.findViewById(R.id.popView_nameTxt))
								 .setText(info.name);
						listener = new OnInfoWindowClickListener() {
								public void onInfoWindowClick() {
									 Intent i = new Intent(SurroundActivity.this,
									 DetailActivity.class);
									 i.putExtra("ADDRESS", info.address);
									 i.putExtra("PHONE", info.phoneNum);
									 i.putExtra("NAME", info.name);
									 i.putExtra("ID", info.uid);
									 mBaiduMap.hideInfoWindow();
									 startActivity(i);
								}
							};
						InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(restautrantPopView),info.location, -47, listener);
						mBaiduMap.showInfoWindow(mInfoWindow);
					}
					
					return true;
				}
			});
			return;
		}
	}}