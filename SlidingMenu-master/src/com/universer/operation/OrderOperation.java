package com.universer.operation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.database.HWDataBaseHelper;
import com.universer.HustWhereToEat.database.HWDatabaseHelperManager;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.model.Order;
import com.universer.HustWhereToEat.model.Restaurant;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
/*
 * 封装了跟订单有关的网络请求
 */
public class OrderOperation {

	public void addOrder(final String userID, final String restaurantId,
			final String orderNum,final String restaurantName,final String restaurantAddress,
			final String restaurantPhone,final OperationListener<String> listener) {
		String url = "/processOrder";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("code","1");
		params.put("userId",userID);
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
						String orderId = jo.getString("orderId");
						try {
							HWDataBaseHelper helper = HWDatabaseHelperManager.getInstance().getHelper();
							Dao<Order, String> orderDao = helper.getOrderDao();
							DeleteBuilder<Order, String> deleteBuilder = orderDao.deleteBuilder();
							deleteBuilder.where().isNotNull("orderId");
							deleteBuilder.delete();
							orderDao.createOrUpdate(new Order(orderId, userID, restaurantId, orderNum, restaurantName, restaurantAddress, restaurantPhone,""));
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
	
	public void queryMyOrders(final String userID,final OperationListener<Order> listener) {
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
		params.put("userId",userID);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
			public void onSuccess(JSONObject jo) {
				List<Order> orderList = new ArrayList<Order>();
				try {
					JSONArray js = jo.getJSONArray("result");
					for (int i = 0; i < js.length(); i++) {
						JSONObject json = js.getJSONObject(i);
						orderList.add(new Order(json.getString("orderId"),userID, 
								json.getString("restaurantId"),json.getString("orderNum"), json.getString("restaurantName"), 
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
						List<Order> tempList = orderDao.queryForAll();
						orderDao.delete(tempList);
//						DeleteBuilder<Order, String> deleteBuilder = orderDao.deleteBuilder();
//						deleteBuilder.where().isNotNull("orderId");
//						deleteBuilder.delete();
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
