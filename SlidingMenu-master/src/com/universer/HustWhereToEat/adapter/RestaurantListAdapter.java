package com.universer.HustWhereToEat.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.model.Restaurant;

public class RestaurantListAdapter extends BaseAdapter {
	private Context mContext;
	private List<com.universer.HustWhereToEat.model.Restaurant> restaurantList;

	public RestaurantListAdapter(Context mContext,
			List<Restaurant> restaurantList) {
		this.mContext = mContext;
		this.restaurantList = restaurantList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return restaurantList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return restaurantList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChildHolder childHolder;
		if (convertView == null) {
			childHolder = new ChildHolder();

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.fragment_restaurantlist_item, null);
			childHolder.restaurantNameTxt = (TextView) convertView
					.findViewById(R.id.fragment_restaurant_nameTxt);
			childHolder.restaurantImg = (ImageView) convertView
					.findViewById(R.id.fragment_restaurant_Img);
			childHolder.restaurantAddressTxt = (TextView) convertView
					.findViewById(R.id.fragment_restaurant_addressTxt);
			convertView.setTag(childHolder);

		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		String restaurantName = restaurantList.get(position).getName();
		String restaurantAddress = restaurantList.get(position).getAddress();
		if (restaurantList.get(position).getImageUrl() != null) {
			// Uri imageUri =
			// Uri.parse(restaurantList.get(position).getImageUrl());
			// childHolder.restaurantImg.setImageURI(imageUri);
			childHolder.restaurantImg.setImageResource(Integer
					.parseInt(restaurantList.get(position).getImageUrl()));
		} else {
//			childHolder.restaurantImg.setImageResource(null);
		}
		childHolder.restaurantAddressTxt.setText(restaurantAddress);
		childHolder.restaurantNameTxt.setText(restaurantName);
		return convertView;
	}

	final class ChildHolder {
		TextView restaurantNameTxt;
		ImageView restaurantImg;
		TextView restaurantAddressTxt;
	}
}
