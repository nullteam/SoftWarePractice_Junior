package com.universer.operation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.database.HWDataBaseHelper;
import com.universer.HustWhereToEat.database.HWDatabaseHelperManager;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.model.Order;

public class OrderOperation {

	public void addOrder(String userID, String restaurantId,
			String orderNum,String restaurantName,String restaurantAddress,
			String restaurantPhone,final OperationListener<String> listener) {
		String url = "/processOrder";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("code","1");
		params.put("userID",userID);
		params.put("restaurantId",restaurantId);
		params.put("orderNum",orderNum);
		params.put("restaurantName",restaurantName);
		params.put("restaurantAddress",restaurantAddress);
		params.put("restaurantPhone",restaurantPhone);
		client.post(null, url, params, new HWResponseHandler() {
			public void onSuccess(JSONObject jo) {
				String result;
				try {
					result = jo.getString("result");
					if(result.equals("1")){
						listener.onSuccess();
					}else{
						listener.onFailure();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
			
			@Override
			public void onFailure() {
				super.onFailure();
			}
		});
	}
	
	public void queryMyOrders(String userID,final OperationListener<Order> listener) {
		String url = "/processOrder";
		HWDataBaseHelper helper;
		List<Order> orderList = null;
		try {
			helper = HWDatabaseHelperManager.getInstance().getHelper();
			Dao<Order, String> orderDao = helper.getOrderDao();
			orderList = orderDao.queryForAll();
			if (orderList != null && !orderList.isEmpty()) {
				listener.onSuccess(orderList);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("code", "4");
		params.put("userID",userID);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
			public void onSuccess(JSONObject jo) {
				List<Order> orderList = new ArrayList<Order>();
				try {
					JSONArray js = jo.getJSONArray("result");
					for (int i = 0; i < js.length(); i++) {
						JSONObject json = js.getJSONObject(i);
						orderList.add(new Order(json.getString("orderID"),json.getString("userID"), 
								json.getString("restaurantId"),json.getString(" orderNum"), json.getString("restaurantName"), 
								json.getString("restaurantAddress"),json.getString("restaurantPhone"),json.getString("orderTime")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(!orderList.isEmpty()){
					listener.onSuccess(orderList);
					try {
						HWDataBaseHelper helper = HWDatabaseHelperManager.getInstance().getHelper();
						Dao<Order, String> orderDao = helper.getOrderDao();
						for (Order order : orderList) {
							orderDao.createOrUpdate(order);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					listener.onFailure();
				}
			}
		});
	}
}