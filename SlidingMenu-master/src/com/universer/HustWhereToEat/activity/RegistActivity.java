package com.universer.HustWhereToEat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.operation.UserOperation;

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
				if (factorOneStr.isEmpty()) {
					Toast.makeText(RegistActivity.this, "请输入用户名",
							Toast.LENGTH_SHORT).show();
				} else if (factorTwoStr.isEmpty()) {
					Toast.makeText(RegistActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					;
				} else {
					requestLogin();
				}
			}
		});
	}

	private void requestLogin() {
		UserOperation op = new UserOperation();
		op.regist(this, user.getText().toString(), password.getText()
				.toString(), new OperationListener<String>() {
			@Override
			public void onSuccess() {
				Intent intent = new Intent();
				intent.putExtra("one", factorOneStr);
				intent.putExtra("two", factorTwoStr);
				intent.setClass(RegistActivity.this, SlidingActivity.class);
				RegistActivity.this.startActivity(intent);
			}

			@Override
			public void onFailure(String e) {
				if (e.equals("1")) {
					Toast.makeText(RegistActivity.this, "用户名或密码不合法",
							Toast.LENGTH_SHORT).show();
				} else if (e.equals("2")) {
					Toast.makeText(RegistActivity.this, "用户已存在",
							Toast.LENGTH_SHORT).show();
				} else {

					Toast.makeText(RegistActivity.this, "未知错误",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure() {
				Toast.makeText(RegistActivity.this, "网络请求失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
		// HWAsyncHttpClient client = new HWAsyncHttpClient();
		// client.post(RegistActivity.this, "", null, new HWResponseHandler(){
		//
		// @Override
		// public void onSuccess(JSONObject jo) {
		// try {
		// String result = jo.getString("result");
		// if(result.equals("")){
		//
		// }else{
		// SharedPreferences accountsPrefs = SharedPreferencesUtil.
		// setUserSharedPreference(RegistActivity.this, factorOneStr);
		// Editor editor=accountsPrefs.edit();
		// editor.putString("passWord", factorTwoStr);
		// editor.commit();
		// Intent intent = new Intent();
		// intent.putExtra("one", factorOneStr);
		// intent.putExtra("two", factorTwoStr);
		// intent.setClass(RegistActivity.this, SlidingActivity.class);
		// RegistActivity.this.startActivity(intent);
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// super.onSuccess(jo);
		// }
		//
		// @Override
		// public void onFailure() {
		// super.onFailure();
		// }
		//
		// });
	}
}
