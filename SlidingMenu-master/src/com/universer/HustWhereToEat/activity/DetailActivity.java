package com.universer.HustWhereToEat.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.adapter.RestaurantListAdapter;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.model.Restaurant;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
import com.universer.operation.OrderOperation;
import com.universer.operation.RestaurantOperation;
/*
 * 餐馆详情界面
 */
public class DetailActivity extends Activity implements OnGetPoiSearchResultListener{
	private Button orderBtn;
	private TextView addressTxt;
	private TextView phoneTxt;
	private TextView nameTxt;
	private TextView numTxt;
	private TextView priceTxt;
	private Button addBtn;
	private Button reduceBtn;
	private ImageView restautantImg;
	private ImageView likeImg;
	private ListView commentListView;
	private Intent mIntent;
//	List<String> comments;
	private ListAdapter mListAdapter;
	private int num = 1;
	String restaurantName = null;
	String restaurantAddress = null;
	String restaurantId = null;
	String restaurantPhone = null;
	double price = 0;
	private boolean isLike;
	
	private PoiSearch mPoiSearch = null;
	private LocationClient mLocationClient = null;
	private MyLocationData mLocationData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mIntent = getIntent();
		findView();
		if (mIntent != null) {
			initView();
		}
		bindEvents();
		
		initLoc();
		initPoi();
		
		queryLike(restaurantId);

	}

	private void initView() {

		restaurantName = mIntent.getStringExtra("NAME");
//		String url = mIntent.getStringExtra("IMAGE");
		restaurantAddress = mIntent.getStringExtra("ADDRESS");
		restaurantId = mIntent.getStringExtra("ID");
		restaurantPhone = mIntent.getStringExtra("PHONE");
		price = mIntent.getDoubleExtra("PRICE", 0.0);
//		comments = mIntent.getStringArrayListExtra("COMMENT");
//		Log.v("URL", url);
//		restautantImg.setImageResource(Integer.parseInt(url));
		addressTxt.setText(restaurantAddress);
		phoneTxt.setText(restaurantPhone);
		nameTxt.setText(restaurantName);
		priceTxt.setText(price+"元");
		restautantImg.setImageDrawable(getResources().getDrawable(mIntent.getIntExtra("IMG",RestaurantListAdapter.drawable[0])));
	}
	
	private void bindEvents() {
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				num++;
				numTxt.setText(Integer.toString(num));
			}
		});
		
		reduceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(num > 0){
					num--;
					numTxt.setText(Integer.toString(num));
				}
			}
		});
		
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderOperation orderOper = new OrderOperation();
				orderOper.addOrder(SharedPreferencesUtil.getCurrentUserStringShare(DetailActivity.this,"userName",""),
						restaurantId,Integer.toString(num), 
						restaurantName, restaurantAddress, restaurantPhone, new OperationListener<String>(){

							@Override
							public void onSuccess() {
//								Toast.makeText(DetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(DetailActivity.this,OrderDetailActivity.class);
								intent.putExtra("restaurantName", restaurantName);
								intent.putExtra("restaurantAddress", restaurantAddress);
								intent.putExtra("restaurantPhone", restaurantPhone);
								intent.putExtra("num", num);
								
//								Time t = new Time();
//								Log.e("time",t.toString()+"");
//								t.toMillis(true);
								intent.putExtra("time",Long.toString(System.currentTimeMillis()));
								startActivity(intent);
								super.onSuccess();
							}

							@Override
							public void onFailure() {
								Toast.makeText(DetailActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
								super.onFailure();
							}
					
				});
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//						+ phone));
//				DetailActivity.this.startActivity(intent);
			}
		});
		
		likeImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RestaurantOperation resOperation = new RestaurantOperation();
				List<String> commentList = new ArrayList<String>();
				String userId = SharedPreferencesUtil.getCurrentUserStringShare(DetailActivity.this,"userName","");
				Restaurant res = new Restaurant(restaurantId, restaurantName," ", restaurantAddress, restaurantPhone, isLike,commentList,price);
//				Restaurant res = new Restaurant("5ea1aa1c0dcid","KFC"," ","KFC","18202720293", isLike,commentList);
				if(isLike){
					resOperation.deleteMyLove(res, userId, new OperationListener<String>(){
						public void onSuccess(String e) {
							likeImg.setImageDrawable(getResources().getDrawable(R.drawable.zan_img));
							isLike = false;
						}
						public void onFailure(String e) {
							Toast.makeText(DetailActivity.this,e,Toast.LENGTH_SHORT).show();
						};
					});
				}else{
					resOperation.setMyLove(res, userId, new OperationListener<String>(){
						public void onSuccess(String e) {
							Log.e("manm","man");
							likeImg.setImageDrawable(getResources().getDrawable(R.drawable.zan_img_new));
							isLike = true;
						}
						public void onFailure(String e) {
							Toast.makeText(DetailActivity.this,e,Toast.LENGTH_SHORT).show();
						};
					});
				}
			}
		});
	}

	private void findView() {
		orderBtn = (Button) findViewById(R.id.activity_detail_orderBtn);
		restautantImg = (ImageView) findViewById(R.id.restautant_Img);
		addressTxt = (TextView) findViewById(R.id.activity_detail_addressTxt);
		phoneTxt = (TextView) findViewById(R.id.activity_detail_phoneTxt);
		nameTxt = (TextView) findViewById(R.id.detail_nameTxt);
		commentListView = (ListView) findViewById(R.id.activity_detail_commentListView);
		numTxt = (TextView)findViewById(R.id.num_txt);
		addBtn = (Button)findViewById(R.id.add_bt);
		reduceBtn = (Button)findViewById(R.id.reduce_bt);
		likeImg = (ImageView)findViewById(R.id.like_img);
		priceTxt = (TextView)findViewById(R.id.editText2);
	}
	
	/*
	 * 搜索我的收藏的餐馆
	 */
	public void queryLike(String restaurantId){
		String userId = SharedPreferencesUtil.getCurrentUserStringShare(DetailActivity.this,"userName","");
		RestaurantOperation resOperation = new RestaurantOperation();
		resOperation.getResLike(restaurantId, userId, new OperationListener<String>(){
			@Override
			public void onSuccess(String e) {
				super.onSuccess(e);
				if(e.equals("1")){
					DetailActivity.this.isLike = true;
					likeImg.setImageDrawable(getResources().getDrawable(R.drawable.zan_img_new));
				}else{
					DetailActivity.this.isLike = false;
					likeImg.setImageDrawable(getResources().getDrawable(R.drawable.zan_img));
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			
			searchPoi();

		}
	};
	
	private void initPoi() {
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}
	/*
	 * POI检索
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener#onGetPoiDetailResult(com.baidu.mapapi.search.poi.PoiDetailResult)
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
	        //详情检索失败
	        // result.error请参考SearchResult.ERRORNO 
	    }else {
	        //检索成功
	    	price = result.getPrice();
	    	priceTxt.setText(Double.toString(price));
//	    	Log.e("detailPrice",result.getPrice()+""+result.getUid()+":"+restaurantId);
	    }
		Log.e("detail","detail");
	}

	/*
	 * (non-Javadoc)
	 * @see com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener#onGetPoiResult(com.baidu.mapapi.search.poi.PoiResult)
	 */
	@Override
	public void onGetPoiResult(final PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(DetailActivity.this, "未找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(restaurantId));
			Log.e("result", result.getAllPoi().size()+"");
			return;
		}
	}
	
	/*
	 * 初始化定位
	 */
	private void initLoc() {
		//
		mLocationClient = new LocationClient(DetailActivity.this);
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
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK5", "locClient is null or not started");
	}
}
