package com.universer.HustWhereToEat.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.model.Restaurant;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
import com.universer.operation.OrderOperation;
import com.universer.operation.RestaurantOperation;
import com.universer.operation.UserOperation;

public class DetailActivity extends Activity {
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
								intent.putExtra("time", new SimpleDateFormat("HH:mm:ss").format(new Date()).toString());
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
}
