package com.universer.HustWhereToEat.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;

public class OrderDetailActivity extends Activity {
	private Intent mIntent;
	private TextView resNameTxt;
	private TextView resAddressTxt;
	private TextView resPhoneTxt;
	private TextView numTxt;
	private TextView timeTxt;
	private Button phoneBtn;
	private String phoneNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		
		mIntent = getIntent();
		findView();
		iniView();
		bindEvents();
	}
	
	private void findView() {
		resNameTxt = (TextView)findViewById(R.id.order_detail_name);
		resAddressTxt = (TextView)findViewById(R.id.detail_addressTxt);
		resPhoneTxt = (TextView)findViewById(R.id.detail_phoneTxt);
		numTxt = (TextView)findViewById(R.id.order_detail_num);
		timeTxt = (TextView)findViewById(R.id.order_time);
		phoneBtn = (Button)findViewById(R.id.order_detail_btn);
	}
	
	private void iniView() {
		phoneNum = mIntent.getStringExtra("restaurantPhone");
		resNameTxt.setText(mIntent.getStringExtra("restaurantName"));
		resAddressTxt.setText(mIntent.getStringExtra("restaurantAddress"));
		resPhoneTxt.setText(phoneNum);
		numTxt.setText(mIntent.getStringExtra("num"));
		long time = Long.parseLong(mIntent.getStringExtra("time"));
		Date date = new Date(time);
		timeTxt.setText(date.toLocaleString());
	}
	
	private void bindEvents() {
		phoneBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneNum));
				OrderDetailActivity.this.startActivity(intent);
			}
		});
	}
}
