package com.universer.HustWhereToEat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;

public class MyOrdersFragment extends Fragment {
	
	private View showLeft;
	private TextView mTopTitleView;
	private ImageView mTopBackView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.mylove, null);
		showLeft = (View) view.findViewById(R.id.head_layout_showLeft);
		
		mTopTitleView = (TextView) showLeft.findViewById(R.id.head_layout_text);
		mTopTitleView.setText(getString(R.string.tab_orders));
		mTopBackView = (ImageView) showLeft.findViewById(R.id.head_layout_back);
//		mTopBackView.setBackgroundResource(R.drawable.biz_ties_main_back_normal);
		return view;
	}


}
