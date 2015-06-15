package com.universer.HustWhereToEat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.universer.HustWhereToEat.R;

public class LoginActivity extends Activity {
	private Button login_Button;
	private Button registButton;
	private Button forwardseeBtn;
	private EditText user;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		login_Button = (Button) findViewById(R.id.login);
		registButton = (Button) findViewById(R.id.regist_button);
		forwardseeBtn = (Button) findViewById(R.id.forwardsee);
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

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String factorOneStr = user.getText().toString();
				String factorTwoStr = password.getText().toString();
				System.out.println("cat");
				if ("universer".equals(factorOneStr)
						&& "123456".equals(factorTwoStr)) {

					Intent intent = new Intent();
					intent.putExtra("one", factorOneStr);
					intent.putExtra("two", factorTwoStr);
					intent.setClass(LoginActivity.this, SlidingActivity.class);
					LoginActivity.this.startActivity(intent);
				}
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
