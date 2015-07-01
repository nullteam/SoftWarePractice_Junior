package com.universer.HustWhereToEat.adapter;

import java.util.List;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.model.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/*
 * 我的订单适配器
 */
public class MyOrderAdapter extends BaseAdapter {
	private Context mContext;
	private List<Order> orderList;

	public MyOrderAdapter(Context mContext,List<Order> orderList) {
		this.mContext = mContext;
		this.orderList = orderList;
	}

	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.my_orders_item,null);
			viewHolder.resNameTxt = (TextView)convertView.findViewById(R.id.order_item_name);
			viewHolder.orderNumTxt = (TextView)convertView.findViewById(R.id.order_item_num);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.resNameTxt.setText(orderList.get(position).getRestaurantName());
		viewHolder.orderNumTxt.setText(orderList.get(position).getOrderNum());
		
		return convertView;
	}
	
	final class ViewHolder{
		TextView resNameTxt;
		TextView orderNumTxt;
	}

}
