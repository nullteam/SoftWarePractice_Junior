package com.universer.HustWhereToEat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.universer.HustWhereToEat.R;

/*
 * 路线界面
 */
public class RoutelineActivity extends Activity implements
		OnGetRoutePlanResultListener {
	private final static int ROUTE_BUS = 0;
	private final static int ROUTE_DRIVE = 1;
	private BaiduMap mBaiduMap = null;
	private MapView mapView = null;
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private MyLocationData mLocationData = null;
	private LatLng startLocation = null;
	private LatLng endLocation = null;
	BitmapDescriptor bd;
	private HashMap<LatLng, PoiInfo> poiLLMap = new HashMap<LatLng, PoiInfo>();
	private LocationClient mLocationClient;
	private PoiSearch mPoiSearch;
	private View restautrantPopView;
	private TransitRouteOverlay transitOverlay;
	private DrivingRouteOverlay driverOverlay;
	private OnGetPoiSearchResultListener onGetPoiListener = new OnGetPoiSearchResultListener() {

		@Override
		public void onGetPoiResult(final PoiResult result) {

			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				Toast.makeText(RoutelineActivity.this, "未找到结果",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				if(mBaiduMap != null){
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
								OverlayOptions option = new MarkerOptions()
										.position(info.location).icon(bd);
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
							if (info != null) {
								OnInfoWindowClickListener listener = null;
								restautrantPopView = getLayoutInflater().inflate(
										R.layout.activity_routeline_popview, null);
								((TextView) restautrantPopView
										.findViewById(R.id.popView_addressTxt))
										.setText(info.address);
								((TextView) restautrantPopView
										.findViewById(R.id.popView_nameTxt))
										.setText(info.name);
								View busButton = restautrantPopView
										.findViewById(R.id.bus_route);
								View driveButton = restautrantPopView
										.findViewById(R.id.drive_route);
								busButton.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										searchRoute(endLocation, ROUTE_BUS);
										mBaiduMap.hideInfoWindow();
									}
								});
								driveButton
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												searchRoute(endLocation,
														ROUTE_DRIVE);
												mBaiduMap.hideInfoWindow();
											}
										});
								// listener = new OnInfoWindowClickListener() {
								// public void onInfoWindowClick() {
								//
								// }
								// };
								endLocation = info.location;
								InfoWindow mInfoWindow = new InfoWindow(
										restautrantPopView, info.location, -47);
								mBaiduMap.showInfoWindow(mInfoWindow);
							}

							return true;
						}
					});
					return;
				}
				
			}

		}

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result) {

		}
	};
	private BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Log.v("LOC NULL", "noloc");
				return;
			}
			Log.v("LOC suc", "haveloc");
			animateToLoc(location);
			searchPoi();
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
		bd = BitmapDescriptorFactory.fromResource(R.drawable.map_loc2);
		findView();
		initMap();
		initLoc();
		initPoiSearchSet();
		initRoutPlanSet();
		bindEvents();
	}

	private void bindEvents() {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng ll) {
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	private void initPoiSearchSet() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiListener);
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

	public void searchRoute(LatLng end, int routeType) {
		PlanNode stNode = PlanNode.withLocation(startLocation);
		PlanNode enNode = PlanNode.withLocation(end);
		if (routeType == ROUTE_BUS) {
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("武汉").to(enNode));
		} else if (routeType == ROUTE_DRIVE) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		}

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
		bd.recycle();
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
			Toast.makeText(RoutelineActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if(driverOverlay!=null) {
				driverOverlay.removeFromMap();
			}
			driverOverlay = new MyDrivingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(driverOverlay);
			driverOverlay.setData(result.getRouteLines().get(0));
			
			if (transitOverlay != null) {
				transitOverlay.removeFromMap();
			}
			driverOverlay.addToMap();
			driverOverlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutelineActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if(transitOverlay!=null) {
				transitOverlay.removeFromMap();
			}
			transitOverlay = new MyTransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(transitOverlay);
			transitOverlay.setData(result.getRouteLines().get(0));
			if (driverOverlay != null) {
				driverOverlay.removeFromMap();
			}
			transitOverlay.addToMap();
			transitOverlay.zoomToSpan();
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
