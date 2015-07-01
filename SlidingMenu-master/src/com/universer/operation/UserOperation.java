package com.universer.operation;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.activity.LoginActivity;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
/*
 * 封装了跟注册和登录有关的网络请求
 */
public class UserOperation {
	public void regist(final Context context,final String name, final String password,
			final OperationListener<String> listener) {
		String url = "/userRegister";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("username", name);
		params.put("password", password);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String code = jo.getString("registerResult");
					if (code.equals("0")) {
						listener.onSuccess();
						SharedPreferencesUtil.setUserSharedPreference(context, name);
						SharedPreferencesUtil.setUserStringShare(context,name,password);
						SharedPreferencesUtil.setCurrentAccountSharedPreferences(context);
						SharedPreferencesUtil.setCurrentUserStringShare(context, name, password);
					} else {
						listener.onFailure(code);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure() {
				listener.onFailure();
			}
		});

	}
	
	public void login(final Context context,final String name, final String password,
			final OperationListener<String> listener) {
		String url = "/userLogin";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("username", name);
		params.put("password", password);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String code = jo.getString("loginResult");
					if (code.equals("0")) {
						listener.onSuccess();
						SharedPreferencesUtil.setUserSharedPreference(context, name);
						SharedPreferencesUtil.setUserStringShare(context,name,password);
						SharedPreferencesUtil.setCurrentAccountSharedPreferences(context);
						SharedPreferencesUtil.setCurrentUserStringShare(context, name, password);
					} else {
						listener.onFailure(code);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure() {
				super.onFailure();
			}
		});

	}
}
