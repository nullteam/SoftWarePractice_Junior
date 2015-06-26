package com.universer.HustWhereToEat.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.activity.DetailActivity;
import com.universer.HustWhereToEat.activity.OrderDetailActivity;
import com.universer.HustWhereToEat.adapter.MyOrderAdapter;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.model.Order;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
import com.universer.operation.OrderOperation;

public class MyOrdersFragment extends Fragment {
	
	private View showLeft;
	private TextView mTopTitleView;
	private ImageView mTopBackView;
	private ListView orderListView;
	private List<Order> orderList = new ArrayList<Order>();
	private MyOrderAdapter orderAdapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.my_orders, null);
		
		findView(view);
		iniView();
		bindEvents();
		queryOrders();
		
		return view;
	}

	private void findView(View view) {
		showLeft = (View) view.findViewById(R.id.head_layout_showLeft);
		mTopTitleView = (TextView) showLeft.findViewById(R.id.head_layout_text);
		mTopBackView = (ImageView) showLeft.findViewById(R.id.head_layout_back);
		orderListView = (ListView)view.findViewById(R.id.order_listview);
	}
	
	private void iniView() {
		mTopTitleView.setText(getString(R.string.tab_orders));
		orderAdapter = new MyOrderAdapter(getActivity(), orderList);
		orderListView.setAdapter(orderAdapter);
	}
	
	private void queryOrders() {
		String userID = SharedPreferencesUtil.getCurrentUserStringShare(getActivity(),"userName","");
		OrderOperation orderOperation = new OrderOperation();
		orderOperation.queryMyOrders(userID, new OperationListener<Order>(){

			@Override
			public void onSuccess(List<Order> list) {
				super.onSuccess(list);
				orderList.clear();
				orderList.addAll(list);
				orderAdapter.notifyDataSetChanged();
				Log.e("order",orderList.get(5).getRestaurantName());
			}

			@Override
			public void onFailure() {
				super.onFailure();
				Toast.makeText(getActivity(),"获取订单失败",Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void bindEvents() {
		orderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
				intent.putExtra("restaurantName", orderList.get(position).getRestaurantName());
				intent.putExtra("restaurantAddress", orderList.get(position).getRestaurantAddress());
				intent.putExtra("restaurantPhone", orderList.get(position).getRestaurantPhone());
				intent.putExtra("num", orderList.get(position).getOrderNum());
				intent.putExtra("time", orderList.get(position).getOrderTime());
				startActivity(intent);
			}
			
		});
	}
}
