package com.universer.HustWhereToEat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.activity.AboutActivity;
import com.universer.HustWhereToEat.activity.FeedbackActivity;
import com.universer.HustWhereToEat.activity.LoginActivity;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
/*
 * 设置界面
 */
public class SettingFragment extends Fragment {
	
	private View showLeft;
	private TextView mTopTitleView;
	private ImageView mTopBackView;
	private Button quitButton;
	private Button suggestButton;
	private Button aboutButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.set, null);
		
		findView(view);
		initView();
		bindEvents();
		return view;
	}
	
	private void findView(View view) {
		quitButton = (Button)view.findViewById(R.id.quit);
		suggestButton = (Button)view.findViewById(R.id.suggest);
		aboutButton = (Button)view.findViewById(R.id.about);
		showLeft = view.findViewById(R.id.head_layout_showLeft);
		mTopTitleView = (TextView) showLeft.findViewById(R.id.head_layout_text);
		mTopBackView = (ImageView) showLeft.findViewById(R.id.head_layout_back);
	}
	
	private void initView() {
		mTopTitleView.setText(getString(R.string.tab_settings));
		mTopBackView.setBackgroundResource(R.drawable.biz_ugc_main_back_normal);
	}
	
	private void bindEvents() {
		quitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferencesUtil.setCurrentUserStringShare(getActivity(),"","");
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		
		aboutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AboutActivity.class);
				startActivity(intent);
			}
		});
		
		suggestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FeedbackActivity.class);
				startActivity(intent);
			}
		});
	}

}
