package com.universer.operation;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.activity.LoginActivity;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;

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
						SharedPreferences accountsPrefs = SharedPreferencesUtil.
								setUserSharedPreference(context, name);
						Editor editor=accountsPrefs.edit();
						editor.putString("password", password);
						editor.commit();
						accountsPrefs = SharedPreferencesUtil.setUserSharedPreference(context, name);
						editor=accountsPrefs.edit();
						editor.putString("password", password);
						editor.commit();
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
						SharedPreferences accountsPrefs = SharedPreferencesUtil.
								setUserSharedPreference(context, name);
						
						Editor editor=accountsPrefs.edit();
						editor.putString("password", password);
						editor.commit();
						accountsPrefs = SharedPreferencesUtil.setUserSharedPreference(context, name);
						editor=accountsPrefs.edit();
						editor.putString("password", password);
						editor.commit();
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
