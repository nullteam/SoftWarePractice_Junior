package com.universer.HustWhereToEat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.listener.OperationListener;
import com.universer.operation.FeedBackOperation;

public class FeedbackActivity extends Activity {
	private EditText editText;
	private Button commitButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		findView();
		bindEvents();
	}
	
	private void findView(){
		editText = (EditText)findViewById(R.id.feedback_txt);
		commitButton = (Button)findViewById(R.id.commit_feedback);
	}
	
	private void bindEvents() {
		commitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!editText.getText().toString().isEmpty()){
					FeedBackOperation operation = new FeedBackOperation();
					operation.commitFeedback(editText.getText().toString(), FeedbackActivity.this,new OperationListener<String>(){
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void onFailure() {
							// TODO Auto-generated method stub
						}
					});
				}
			}
		});
	}
}
