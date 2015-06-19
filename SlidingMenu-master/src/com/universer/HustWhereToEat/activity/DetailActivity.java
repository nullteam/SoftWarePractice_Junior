package com.universer.HustWhereToEat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
import com.universer.operation.UserOperation;

public class DetailActivity extends Activity {
	private Button orderBtn;
	private TextView addressTxt;
	private TextView phoneTxt;
	private TextView nameTxt;
	private TextView numTxt;
	private Button addBtn;
	private Button reduceBtn;
	private ImageView restautantImg;
	private ListView commentListView;
	private Intent mIntent;
//	List<String> comments;
	private ListAdapter mListAdapter;
	private int num = 0;
	String restaurantName = null;
	String restaurantAddress = null;
	String restaurantId = null;
	String restaurantPhone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mIntent = getIntent();
		findView();
		if (mIntent != null) {
			initView();
		}

	}

	private void initView() {

		restaurantName = mIntent.getStringExtra("NAME");
//		String url = mIntent.getStringExtra("IMAGE");
		restaurantAddress = mIntent.getStringExtra("ADDRESS");
		restaurantId = mIntent.getStringExtra("UID");
		restaurantPhone = mIntent.getStringExtra("PHONE");
//		comments = mIntent.getStringArrayListExtra("COMMENT");
//		Log.v("URL", url);
//		restautantImg.setImageResource(Integer.parseInt(url));
		addressTxt.setText(restaurantAddress);
		phoneTxt.setText(restaurantPhone);
		nameTxt.setText(restaurantName);
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
				UserOperation userOper = new UserOperation();
				userOper.addOrder(SharedPreferencesUtil.getCurrentUserStringShare(DetailActivity.this,"userName",""),
						restaurantId,Integer.toString(num), 
						restaurantName, restaurantAddress, restaurantPhone, new OperationListener<String>(){

							@Override
							public void onSuccess() {
								Toast.makeText(DetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
