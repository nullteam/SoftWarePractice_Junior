package com.universer.HustWhereToEat.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.http.HWAsyncHttpClient;
import com.universer.HustWhereToEat.http.HWResponseHandler;
import com.universer.HustWhereToEat.util.SharedPreferencesUtil;

public class LoginActivity extends Activity {
	private Button login_Button;
	private Button registButton;
	private Button forwardseeBtn;
	private EditText user;
	private EditText password;
	
	private String factorOneStr;
	private String factorTwoStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		init();
		bindEvent();
		
	}
	
	private void init() {
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		login_Button = (Button) findViewById(R.id.login);
		registButton = (Button) findViewById(R.id.regist_button);
		forwardseeBtn = (Button) findViewById(R.id.forwardsee);
	}
	
	@SuppressLint("NewApi")
	private void autoLogin() {
		SharedPreferences curreentAccountsPrefs = SharedPreferencesUtil.
									setSettingSharedPreferences(getApplicationContext());
		factorOneStr = curreentAccountsPrefs.getString("account","");
		factorTwoStr = curreentAccountsPrefs.getString("password","");
		if(!factorOneStr.isEmpty() && factorTwoStr.isEmpty()){
			requestLogin();
		}
	}
	
	private void bindEvent() {
		registButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "该功能即将开放哦",
						Toast.LENGTH_SHORT).show();
			}
		});
		forwardseeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this, SlidingActivity.class));
			}
		});
		login_Button.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				factorOneStr = user.getText().toString();
				factorTwoStr = password.getText().toString();
				if(factorOneStr.isEmpty()){
					Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
				}else if(factorTwoStr.isEmpty()){
					Toast.makeText(LoginActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();;
				}else{
					requestLogin();
				}
				System.out.println("cat");
				
			}
		});
	}
	
	private void requestLogin(){
		HWAsyncHttpClient client = new HWAsyncHttpClient();
		client.post(LoginActivity.this, "", null, new HWResponseHandler(){

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(JSONObject jo) {
				try {
					String result = jo.getString("result");
					if(result.equals("")){
						
					}else{
						SharedPreferences accountsPrefs = SharedPreferencesUtil.
								setUserSharedPreference(LoginActivity.this, factorOneStr);
						
						Editor editor=accountsPrefs.edit();
						editor.putString("passWord", factorTwoStr);
						editor.commit();
						accountsPrefs = SharedPreferencesUtil.setUserSharedPreference(LoginActivity.this, factorOneStr);
						editor=accountsPrefs.edit();
						editor.putString("passWord", factorTwoStr);
						editor.commit();
						Intent intent = new Intent();
						intent.putExtra("one", factorOneStr);
						intent.putExtra("two", factorTwoStr);
						intent.setClass(LoginActivity.this, SlidingActivity.class);
						LoginActivity.this.startActivity(intent);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

}
