/*
 * Copyright (C) 2012 zhangman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.universer.HustWhereToEat.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.activity.DetailActivity;
import com.universer.HustWhereToEat.activity.SlidingActivity;
import com.universer.HustWhereToEat.activity.SurroundActivity;
import com.universer.HustWhereToEat.adapter.ListViewPagerAdapter;
import com.universer.HustWhereToEat.adapter.RestaurantListAdapter;
import com.universer.HustWhereToEat.adapter.ScrollingTabsAdapter;
import com.universer.HustWhereToEat.model.Restaurant;
import com.universer.HustWhereToEat.view.ScrollableTabView;

public class AllFragment extends Fragment implements
		OnRefreshListener<ListView>,OnGetPoiSearchResultListener{
	private static final String TAG = "NewsFragment";

	private View showLeft;
	private TextView mTopTitleView;
	private ImageView mTopBackView;
	private PullToRefreshListView resListView;
	private RestaurantListAdapter resAdapter;
	private List<Restaurant> restaurants = new ArrayList<Restaurant>();
	private PoiSearch mPoiSearch = null;
	private LocationClient mLocationClient = null;
	private MyLocationData mLocationData;

	private Activity mActivity;

	public AllFragment() {
	}

	public AllFragment(Activity activity) {
		this.mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.resturants_layout, null);

		findView(mView);
		iniView();
		initLoc();
		
		return mView;
	}
	
	private void findView(View view) {
		showLeft = (View) view.findViewById(R.id.head_layout_showLeft);
		mTopTitleView = (TextView) showLeft.findViewById(R.id.head_layout_text);
		mTopBackView = (ImageView) showLeft.findViewById(R.id.head_layout_back);
		resListView = (PullToRefreshListView)view.findViewById(R.id.res_listview);
	}
	
	private void iniView() {
		mTopTitleView.setText(getString(R.string.tab_all));
		resAdapter = new RestaurantListAdapter(mActivity, restaurants);
		resListView.setAdapter(resAdapter);
	}

	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});

//		showRight.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((SlidingActivity) getActivity()).showRight();
//			}
//		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new GetDataTask(refreshView).execute();
		restaurants.clear();
		initLoc();
		searchPoi();
	}

	private static class GetDataTask extends AsyncTask<Void, Void, Void> {

		PullToRefreshBase<?> mRefreshedView;

		public GetDataTask(PullToRefreshBase<?> refreshedView) {
			mRefreshedView = refreshedView;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mRefreshedView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(TAG, "onDestroyView");
		mActivity = null;
		mPoiSearch.destroy();
	}
	
	private BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Log.v("LOC NULL", "noloc");
				return;
			}
			animateToLoc(location);
			mLocationClient.stop();

		}

		private void animateToLoc(BDLocation location) {
			mLocationData = new MyLocationData.Builder()
					.accuracy(location.getRadius()).direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			initPoi();
			searchPoi();

		}
	};
	
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

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		
	}

	@Override
	public void onGetPoiResult(final PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(mActivity, "未找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			for (int i = 0; i < result.getAllPoi().size(); i++) {
				PoiInfo info = result.getAllPoi().get(i);
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(info.uid));
				restaurants.add(new Restaurant(info.uid,info.name, "", info.address, info.phoneNum, false,null));
			}
			
			resAdapter.notifyDataSetChanged();
			return;
		}
	}
	
	private void initLoc() {
		//
		mLocationClient = new LocationClient(mActivity);
		mLocationClient.registerLocationListener(mLocationListener);
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

}
