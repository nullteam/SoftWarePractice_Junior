package com.universer.operation;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;

public class UserOperation {
	public void regist(String name, String password,
			final OperationListener<String> listener) {
		String url = null;
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("name", name);
		params.put("password", password);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String code = jo.getString("result");
					if (code.equals("0")) {
						listener.onSuccess();
					} else {
						listener.onFailure(code);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub
				super.onFailure();
			}
		});

	}
}
