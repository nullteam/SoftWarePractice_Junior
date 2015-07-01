package com.universer.operation;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;
/*
 * 封装好的网络请求，用语反馈提交
 */
public class FeedBackOperation {
	public void commitFeedback(String content,Context context,final OperationListener<String> listener){
		String url = "/processFeedback";
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("code","1");
		params.put("userId",SharedPreferencesUtil.getCurrentUserStringShare(context,"userName",""));
		params.put("content",content);
		client.post(null, url, params, new HWResponseHandler() {
			@Override
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
			}
			
			@Override
			public void onFailure() {
				listener.onFailure();
			}
		});
	}
}
