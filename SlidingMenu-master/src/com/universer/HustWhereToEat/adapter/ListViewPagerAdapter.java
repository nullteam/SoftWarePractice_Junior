package com.universer.HustWhereToEat.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.activity.DetailActivity;
import com.universer.HustWhereToEat.activity.SurroundActivity;
import com.universer.HustWhereToEat.model.Restaurant;

public class ListViewPagerAdapter extends PagerAdapter {

	private static final String[] STRINGS = { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler",
			"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
			"Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
			"Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private Context mContext;
	private OnRefreshListener<ListView> listener;

	public ListViewPagerAdapter(OnRefreshListener<ListView> listener,
			Context mContext) {
		this.listener = listener;
		this.mContext = mContext;
		List<String> comments = new ArrayList<String>();
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		comments.add("好评");
		Restaurant res;
		{
			res = new Restaurant("麦芽芗", R.drawable.restaurant_laosichuan + "",
					Restaurant.SMALL, "华科生活门", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("西苑咖啡", R.drawable.restaurant_coffee + "",
					Restaurant.BIG, "华中科技大学西十一舍附近", "13098840196", comments);
			restaurantList.add(res);
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
			res = new Restaurant("好运来新川菜", R.drawable.restaurant_xinchuancai
					+ "", Restaurant.BIG, "华中科技大学西光谷体育馆对面", "13098840196",
					comments);
			restaurantList.add(res);
			res = new Restaurant("鸡蛋灌饼", R.drawable.restaurant_jidanguanbing
					+ "", Restaurant.SMALL, "华中科技大学南大门附近", "13098840196",
					comments);
			restaurantList.add(res);
			res = new Restaurant("蔡林记", R.drawable.restaurant_cailinji + "",
					Restaurant.SMALL, "光谷步行街对面", "13098840196", comments);
			restaurantList.add(res);
			res = new Restaurant("海底捞", R.drawable.restaurant_haidilao + "",
					Restaurant.SMALL, "珞喻路618号（烽火科技对面）", "13098840196",
					comments);
			restaurantList.add(res);
			res = new Restaurant("凯威啤酒屋", R.drawable.restaurant_kaiweipijiuwu
					+ "", Restaurant.BIG, "光谷大洋百货4楼", "13098840196", comments);
			restaurantList.add(res);

		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Context context = container.getContext();
		PullToRefreshListView plv = (PullToRefreshListView) LayoutInflater
				.from(context).inflate(R.layout.layout_listview_in_viewpager,
						container, false);
		if (position == 0) {
			RestaurantListAdapter allAdapter = new RestaurantListAdapter(
					mContext, restaurantList);
			plv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					if (id < -1) {
						return;
					}
					int realPosition = (int) id;
					Restaurant tempRes = restaurantList.get(realPosition);
					startActivity(tempRes);

				}
			});

			// ListAdapter adapter = new ArrayAdapter<String>(context,
			// android.R.layout.simple_list_item_1, Arrays.asList(STRINGS));
			plv.setAdapter(allAdapter);
		} else if (position == 1) {

			List<Restaurant> bigResList = new ArrayList<Restaurant>();
			Iterator<Restaurant> resIt = restaurantList.iterator();
			while (resIt.hasNext()) {
				Restaurant bigRes = resIt.next();
				if (bigRes.getType() == Restaurant.BIG) {
					bigResList.add(bigRes);
				}
			}
			RestaurantListAdapter bigAdapter = new RestaurantListAdapter(
					mContext, bigResList);
			plv.setAdapter(bigAdapter);
			plv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					if (id < -1) {
						return;
					}
					int realPosition = (int) id;
					Restaurant tempRes = restaurantList.get(realPosition);
					startActivity(tempRes);

				}
			});

		} else if (position == 2) {
			List<Restaurant> smallResList = new ArrayList<Restaurant>();
			Iterator<Restaurant> resIt = restaurantList.iterator();
			while (resIt.hasNext()) {
				Restaurant smallRes = resIt.next();
				if (smallRes.getType() == Restaurant.SMALL) {
					smallResList.add(smallRes);
				}
			}
			RestaurantListAdapter bigAdapter = new RestaurantListAdapter(
					mContext, smallResList);
			plv.setAdapter(bigAdapter);
			plv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					if (id < -1) {
						return;
					}
					int realPosition = (int) id;
					Restaurant tempRes = restaurantList.get(realPosition);
					startActivity(tempRes);

				}
			});
		}

		plv.setOnRefreshListener(listener);

		// Now just add ListView to ViewPager and return it
		container.addView(plv, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		return plv;
	}

	private void startActivity(Restaurant tempRes) {
		Intent i = new Intent(mContext, DetailActivity.class);
		i.putExtra("ADDRESS", tempRes.getAddress());
		i.putExtra("PHONE", tempRes.getPhone());
		i.putExtra("NAME", tempRes.getName());
		i.putExtra("IMAGE", tempRes.getImageUrl());
		i.putStringArrayListExtra("COMMENT",
				(ArrayList<String>) tempRes.getCommentList());
		mContext.startActivity(i);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
