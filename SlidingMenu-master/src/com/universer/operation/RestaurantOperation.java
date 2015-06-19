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
import com.universer.HustWhereToEat.model.Restaurant;

public class RestaurantOperation {
	
	public void getMyLove(String userId,
			final OperationListener<Restaurant> listener) {
		String url = "/processFavorite";
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("code", "4");
		List<Restaurant> restaurants = null;
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		try {
			HWDataBaseHelper helper = HWDatabaseHelperManager
					.getInstance().getHelper();
			Dao<Restaurant, String> resDao = helper
					.getDao(Restaurant.class);
			
			restaurants = resDao.queryForEq("isLike", true);
			if(restaurants!=null&&!restaurants.isEmpty()) {
				listener.onSuccess(restaurants);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		client.post(null, url, params, new HWResponseHandler() {

			@Override
			public void onSuccess(JSONObject jo) {
				List<Restaurant> restaurants = null;
				try {
					JSONArray jArray = jo.getJSONArray("result");
					restaurants = new ArrayList<Restaurant>();
					Restaurant res;
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject js = jArray.getJSONObject(i);
						String restaurantName = js.getString("restaurantName");
						String restaurantPhone = js
								.getString("restaurantPhone");
						String restaurantId = js.getString("restaurantId");
						String address = js.getString("restaurantAddress");
						res = new Restaurant(restaurantId, restaurantName,
								null, address, restaurantPhone, true, null);
						restaurants.add(res);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 写入数据库
				if (restaurants != null) {
					listener.onSuccess(restaurants);
					try {
						HWDataBaseHelper helper = HWDatabaseHelperManager
								.getInstance().getHelper();
						Dao<Restaurant, String> resDao = helper
								.getDao(Restaurant.class);
						for (Restaurant restaurant : restaurants) {
							resDao.createOrUpdate(restaurant);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure() {
				super.onFailure();
			}

		});
	}

}
