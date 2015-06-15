package com.universer.HustWhereToEat.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.model.Restaurant;

public class SurroundActivity extends Activity {
	private BMapManager mapManager = null;
	private MapView mapView = null;
	private LocationClient mLocationClient = null;
	private MyLocationOverlay mOverlay;
	private LocationData mLocationData;
	private SurroundOverlay overLays;
	private List<OverlayItem> itemList = new ArrayList<OverlayItem>();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private PopupOverlay itemPopLay;
	public static final String MAPKEY = "E3466274d2406295cc220c9b43e516d5";
	private View restautrantPopView;
	private BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation location) {

		}

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
			mLocationData.latitude = location.getLatitude();
			mLocationData.longitude = location.getLongitude();
			mLocationData.direction = location.getDerect();
			Log.v("LOC", mLocationData.latitude + " " + mLocationData.longitude);
			for (int i = 0; i < restaurantList.size(); i++) {
				OverlayItem surroundItem = new OverlayItem(restaurantList
						.get(i).getPoint(), "item" + i, "item" + i);
				itemList.add(surroundItem);
			}
			overLays.addItem(itemList);
			// mOverlay.setData(mLocationData);
			// mapView.getOverlays().add(mOverlay);
			mapView.refresh();
			mapView.getController().animateTo(
					new GeoPoint((int) (mLocationData.latitude * 1e6),
							(int) (mLocationData.longitude * 1e6)));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initMap();
		setContentView(R.layout.activity_surround);

		findView();
		initLoc();
		initMapSet();

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
			res = new Restaurant("麦芽芗", R.drawable.restaurant_laosichuan + "",
					Restaurant.SMALL, "华科生活门", "13098840196", comments);
			res.setPoint(new GeoPoint(
					(int) ((location.getLatitude() + 0 * 0.002) * 1e6),
					(int) ((location.getLongitude() + 0 * 0.002) * 1e6)));
			restaurantList.add(res);
			res = new Restaurant("西苑咖啡", R.drawable.restaurant_coffee + "",
					Restaurant.BIG, "华中科技大学西十一舍附近", "13098840196", comments);
			res.setPoint(new GeoPoint(
					(int) ((location.getLatitude() + 1 * 0.02) * 1e6),
					(int) ((location.getLongitude() + 1 * 0.02) * 1e6)));
			restaurantList.add(res);
			res = new Restaurant("鸭血粉丝", R.drawable.restaurant_yaxuefensi + "",
					Restaurant.SMALL, "华中科技大学南三门", "13098840196", comments);
			res.setPoint(new GeoPoint(
					(int) ((location.getLatitude() + 2 * 0.02) * 1e6),
					(int) ((location.getLongitude()) * 1e6)));
			restaurantList.add(res);
			res = new Restaurant("简朴田园寨(光谷航母店) ",
					R.drawable.restaurant_tianyuan + "", Restaurant.BIG,
					"华中科技大学南大门", "13098840196", comments);
			res.setPoint(new GeoPoint(
					(int) ((location.getLatitude() + 3 * 0.02) * 1e6),
					(int) ((location.getLongitude()) * 1e6)));
			restaurantList.add(res);
			res = new Restaurant("氧气层", R.drawable.restaurant_o2 + "",
					Restaurant.BIG, "华中科技大学西园食堂附近", "13098840196", comments);
			res.setPoint(new GeoPoint(
					(int) ((location.getLatitude() + 4 * 0.02) * 1e6),
					(int) ((location.getLongitude() - 4 * 0.002) * 1e6)));
			restaurantList.add(res);
			// res = new Restaurant("好运来新川菜", R.drawable.restaurant_xinchuancai
			// + "", Restaurant.BIG, "华中科技大学西光谷体育馆对面","13098840196", null);
			// restaurantList.add(res);
			// res = new Restaurant("鸡蛋灌饼", R.drawable.restaurant_jidanguanbing
			// + "", Restaurant.SMALL, "华中科技大学南大门附近", "13098840196",null);
			// restaurantList.add(res);
			// res = new Restaurant("蔡林记", R.drawable.restaurant_cailinji + "",
			// Restaurant.SMALL, "光谷步行街对面", "13098840196",null);
			// restaurantList.add(res);
			// res = new Restaurant("海底捞", R.drawable.restaurant_haidilao + "",
			// Restaurant.SMALL, "珞喻路618号（烽火科技对面）","13098840196",null);
			// restaurantList.add(res);
			// res = new Restaurant("凯威啤酒屋", R.drawable.restaurant_kaiweipijiuwu
			// + "", Restaurant.BIG, "光谷大洋百货4楼","13098840196", null);
			// restaurantList.add(res);

		}

	}

	private void initLoc() {
		//
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.setAK(MAPKEY);
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mOverlay = new MyLocationOverlay(mapView);
		mLocationData = new LocationData();
		overLays = new SurroundOverlay(getResources().getDrawable(
				R.drawable.map_loc2), mapView);
		mOverlay.setData(mLocationData);
		mOverlay.enableCompass();
		mOverlay.setMarker(getResources().getDrawable(R.drawable.icon_geo));
		mapView.getOverlays().add(mOverlay);
		mapView.getOverlays().add(overLays);
		mapView.getController().setZoom(13);
		mapView.refresh();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		if (mapManager != null) {
			mapManager.start();
		}
		// if (mLocationClient != null && mLocationClient.isStarted()) {
		// mLocationClient.requestLocation();
		// } else {
		// Log.d("LocSDK4", "locClient is null or not started");
		// }
		super.onResume();
	}

	@Override
	protected void onDestroy() {

		mapView.destroy();
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		mapView.onPause();

		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	private void initMapSet() {

		mapView.setBuiltInZoomControls(true);
		MapController mMapController = mapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		// GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
		// (int) (116.404 * 1E6));// 用给定的经纬度构造一个GeoPoint，单位是微度(度 * 1E6
		// mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(14);// 设置地图zoom级别

	}

	private void findView() {
		mapView = (MapView) findViewById(R.id.activity_surroundMapView);
	}

	private void initMap() {
		mapManager = new BMapManager(getApplication());

		mapManager.init(MAPKEY, null);

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

	class SurroundOverlay extends ItemizedOverlay<OverlayItem> {

		public SurroundOverlay(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}

		@Override
		protected boolean onTap(int index) {
			// 跳转至详细
			final Restaurant tempRes = restaurantList.get(index);
			itemPopLay = new PopupOverlay(mapView, new PopupClickListener() {

				@Override
				public void onClickedPopup(int index) {
					// TODO Auto-generated method stub
					Intent i = new Intent(SurroundActivity.this,
							DetailActivity.class);
					i.putExtra("ADDRESS", tempRes.getAddress());
					i.putExtra("PHONE", tempRes.getPhone());
					i.putExtra("NAME", tempRes.getName());
					i.putExtra("IMAGE", tempRes.getImageUrl());
					i.putStringArrayListExtra("COMMENT",
							(ArrayList<String>) tempRes.getCommentList());
					startActivity(i);
				}
			});
			initPopLayView(index);
			Bitmap[] bmps = new Bitmap[1];
			bmps[0] = convertViewToBitmap(restautrantPopView);
			itemPopLay.showPopup(bmps, tempRes.getPoint(), 32);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			// TODO Auto-generated method stub
			if (itemPopLay != null) {
				itemPopLay.hidePop();
			}
			return false;
		}

	}

	private void initPopLayView(int index) {
		Restaurant res = restaurantList.get(index);
		String address = res.getAddress();
		String name = res.getName();
		restautrantPopView = getLayoutInflater().inflate(
				R.layout.activity_surround_popview, null);
		((TextView) restautrantPopView.findViewById(R.id.popView_addressTxt))
				.setText(address);
		((TextView) restautrantPopView.findViewById(R.id.popView_nameTxt))
				.setText(name);
	}
}
