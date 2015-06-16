package com.universer.HustWhereToEat.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;

public class RegistActivity extends Activity {
	private Button registBtn;
	private EditText user;
	private EditText password;
	
	String factorOneStr;
	String factorTwoStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		
		init();
		bindEvent();
	}
	
	private void init() {
		user = (EditText) findViewById(R.id.user_edit);
		password = (EditText) findViewById(R.id.password_edit);
		registBtn = (Button) findViewById(R.id.regist_bt);
	}
	
	private void bindEvent() {
		registBtn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String factorOneStr = user.getText().toString();
				String factorTwoStr = password.getText().toString();
				if(factorOneStr.isEmpty()){
					Toast.makeText(RegistActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
				}else if(factorTwoStr.isEmpty()){
					Toast.makeText(RegistActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();;
				}else{
					requestLogin();
				}
			}
		});
	}
	
	private void requestLogin(){
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		client.post(RegistActivity.this, "", null, new HWResponseHandler(){

			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String result = jo.getString("result");
					if(result.equals("")){
						
					}else{
						SharedPreferences accountsPrefs = SharedPreferencesUtil.
								setUserSharedPreference(RegistActivity.this, factorOneStr);
						Editor editor=accountsPrefs.edit();
						editor.putString("passWord", factorTwoStr);
						editor.commit();
						Intent intent = new Intent();
						intent.putExtra("one", factorOneStr);
						intent.putExtra("two", factorTwoStr);
						intent.setClass(RegistActivity.this, SlidingActivity.class);
						RegistActivity.this.startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(jo);
			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub
				super.onFailure();
			}
			
		});
	}
}
