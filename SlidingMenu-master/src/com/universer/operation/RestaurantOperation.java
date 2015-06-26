package com.universer.operation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
			HWDataBaseHelper helper = HWDatabaseHelperManager.getInstance()
					.getHelper();
			Dao<Restaurant, String> resDao = helper.getDao(Restaurant.class);

			restaurants = resDao.queryForEq("isLike", true);
			if (restaurants != null && !restaurants.isEmpty()) {
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
						String restaurantId = js.getString("id");
						String address = js.getString("restaurantAddress");
						String imgUrl = js.getString("imgUrl");
						String price = js.getString("price");
						res = new Restaurant(restaurantId, restaurantName,
								imgUrl, address, restaurantPhone, true, null,Double.parseDouble(price));
						restaurants.add(res);
					}

				} catch (JSONException e) {
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
						DeleteBuilder<Restaurant, String> deleteBuilder = resDao.deleteBuilder();
						deleteBuilder.where().isNotNull("id");
						deleteBuilder.delete();
						for (Restaurant restaurant : restaurants) {
							resDao.createOrUpdate(restaurant);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure() {
				listener.onFailure();
			}

		});
	}

	public void setMyLove(final Restaurant res, String userId,
			final OperationListener<String> listener) {
		String url = "/processFavorite";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		String restaurantId = res.getId();
		String restaurantName = res.getName();
		String restaurantPhone = res.getPhone();
		String restaurantAddress = res.getAddress();
		String imgUrl = res.getImageUrl();
		String price = Double.toString(res.getPrice());
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("code", "1");
		params.put("restaurantId", restaurantId);
		params.put("restaurantName", restaurantName);
		params.put("restaurantAddress", restaurantAddress);
		params.put("restaurantPhone", restaurantPhone);
		params.put("imgUrl",imgUrl);
		params.put("price",price);
		client.post(null, url, params, new HWResponseHandler() {

			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String result = jo.getString("result");
					if (result.equals("1")) {
						// 数据库写入
						HWDataBaseHelper helper = HWDatabaseHelperManager
								.getInstance().getHelper();
						Dao<Restaurant, String> resDao = helper
								.getDao(Restaurant.class);
						resDao.createOrUpdate(res);
						listener.onSuccess("success");
					} else if (result.equals("2")) {
						listener.onFailure("提交失败");
					} else {
						listener.onFailure("未知错误");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure() {
				listener.onFailure("网络请求失败");
			}

		});
	}

	public void deleteMyLove(final Restaurant res, String userId,
			final OperationListener<String> listener) {
		String url = "/processFavorite";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		String restaurantId = res.getId();
		RequestParams params = new RequestParams();
		params.put("userName", userId);
		params.put("code", "2");
		params.put("restaurantId", restaurantId);
		client.post(null, url, params, new HWResponseHandler(){

			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String result = jo.getString("result");
					if(result.equals("1")) {
						listener.onSuccess("删除成功");
						HWDataBaseHelper helper = HWDatabaseHelperManager
								.getInstance().getHelper();
						Dao<Restaurant, String> resDao = helper
								.getDao(Restaurant.class);
						resDao.delete(res);
					}else if(result.equals("0")) {
						listener.onFailure("删除失败");
					}else {
						listener.onFailure("未知错误");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure() {
				listener.onFailure("网络请求失败");
			}
			
		});
	}
	
	public void getResLike(String restaurantId,String userId,final OperationListener<String> listener){
		String url = "/processFavorite";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("code","3");
		params.put("userId", userId);
		params.put("restaurantId", restaurantId);
		client.post(null, url, params, new HWResponseHandler() {
			public void onSuccess(JSONObject jo) {
				try {
					String result = jo.getString("result");
					if(result.equals("0")){
						String isLike = jo.getString("isLike");
						listener.onSuccess(isLike);
					}else{
						listener.onFailure();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
