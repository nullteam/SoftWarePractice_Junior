package com.universer.HustWhereToEat.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HWAsyncHttpClient {
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	
	static {
		mClient.setTimeout(90000);
		mClient.setMaxConnections(20);
	}
	public HWAsyncHttpClient(){
		
	}
	public void post(Context mContext,String url,RequestParams params,final HWResponseHandler handler) {
		mClient.post(mContext, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				try {
					String json = new String(responseBody);
					if (json.substring(0, 1).equals("[")) {
						JSONArray data = new JSONArray(json);
						handler.onSuccess(data);
					} else {
						JSONObject data = new JSONObject(json);
						if (!data.has("error")) {
							handler.onSuccess(data);
						} else {
							
							handler.onFailure();
						}
					}
				} catch (JSONException e) {
					
				}
							
			}
			@Override
			public void onCancel() {
				handler.onFinish();
			}

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onStart() {
				handler.onStart();
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				handler.onFailure();
				
			}
		});
	}
}
