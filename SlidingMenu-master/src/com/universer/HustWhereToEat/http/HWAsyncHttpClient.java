package com.universer.HustWhereToEat.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.application.HWApplication;
import com.universer.HustWhereToEat.util.Constant;
/*
 * 封装好的AsyncHttpClient
 */
public class HWAsyncHttpClient {
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	private ProgressDialog dialog;
	
	static {
		mClient.setTimeout(90000);
		mClient.setMaxConnections(20);
	}
	public HWAsyncHttpClient(){
		
	}
	public void post(Context mContext,String url,RequestParams params,final HWResponseHandler handler) {
		mClient.post(mContext, Constant.BASE_URL+url, params, new AsyncHttpResponseHandler() {
			
			
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
				dialog.dismiss();
			}

			@Override
			public void onStart() {
				handler.onStart();
				Log.e("start","start");
//				progressBar = new ProgressBar(HWApplication.getInstance());
				dialog = new ProgressDialog(HWApplication.getInstance());
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//				dialog.setContentView(progressBar);
				dialog.show();
//				WindowManager windowManager = (WindowManager)HWApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
//				WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//				params.width = LayoutParams.WRAP_CONTENT;
//				params.height = LayoutParams.WRAP_CONTENT;
//				params.gravity = Gravity.CENTER;
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				handler.onFailure();
				
			}
		});
	}
}
