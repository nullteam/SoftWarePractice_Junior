package com.universer.HustWhereToEat.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.activity.DetailActivity;
import com.universer.HustWhereToEat.activity.SlidingActivity;
import com.universer.HustWhereToEat.adapter.RestaurantListAdapter;
import com.universer.HustWhereToEat.model.Restaurant;

public class MyLoveFragment extends BaseFragment {

	private View showLeft;
	private TextView mTopTitleView;
	private ImageView mTopBackView;
	// private View showRight;
	private ListView mListView;
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.mylove, null);
		initData();
		findView(view);
		initView();

		// 原本应采用数据库的访问方式 暂时只是以样本的形式展现
		// mTopBackView.setBackgroundResource(R.drawable.biz_local_news_main_back_normal);
		return view;
	}

	private void initView() {
		mTopTitleView.setText(getString(R.string.tab_mylove));
		mListView.setAdapter(new RestaurantListAdapter(getActivity(),
				restaurantList));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				if (id < -1) {
					return;
				}
				int realPosition = (int) id;
				Restaurant tempRes = restaurantList.get(realPosition);
				startActivity(tempRes);

			}
		});
	}

	protected void startActivity(Restaurant tempRes) {

		Intent i = new Intent(getActivity(), DetailActivity.class);
		i.putExtra("ADDRESS", tempRes.getAddress());
		i.putExtra("PHONE", tempRes.getPhone());
		i.putExtra("NAME", tempRes.getName());
		i.putExtra("IMAGE", tempRes.getImageUrl());
		i.putStringArrayListExtra("COMMENT",
				(ArrayList<String>) tempRes.getCommentList());
		getActivity().startActivity(i);

	}

	private void findView(View view) {
		showLeft = (View) view.findViewById(R.id.head_layout_showLeft);
		mTopTitleView = (TextView) showLeft.findViewById(R.id.head_layout_text);
		mTopBackView = (ImageView) showLeft.findViewById(R.id.head_layout_back);
		mListView = (ListView) view.findViewById(R.id.listview);
	}

	private void initData() {
		Restaurant res;
		List<String> comments = new ArrayList<String>();
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		{
			res = new Restaurant("鸭血粉丝", R.drawable.restaurant_yaxuefensi + "",
					Restaurant.SMALL, "华中科技大学南三门", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("简朴田园寨(光谷航母店) ",
					R.drawable.restaurant_tianyuan + "", Restaurant.BIG,
					"华中科技大学南大门", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("氧气层", R.drawable.restaurant_o2 + "",
					Restaurant.BIG, "华中科技大学西园食堂附近", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("鸡蛋灌饼", R.drawable.restaurant_jidanguanbing
					+ "", Restaurant.SMALL, "华中科技大学南大门附近", "13098840196",
					comments);
			restaurantList.add(res);
			res = new Restaurant("蔡林记", R.drawable.restaurant_cailinji + "",
					Restaurant.SMALL, "光谷步行街对面", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("凯威啤酒屋", R.drawable.restaurant_kaiweipijiuwu
					+ "", Restaurant.BIG, "光谷大洋百货4楼", "13098840196", comments);
			restaurantList.add(res);

		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});

	}
}
