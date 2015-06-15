package com.universer.HustWhereToEat.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;

public class DetailActivity extends Activity {
	private Button orderBtn;
	private TextView addressTxt;
	private TextView phoneTxt;
	private TextView nameTxt;
	private ImageView restautantImg;
	private ListView commentListView;
	private Intent mIntent;
	List<String> comments;
	private ListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mIntent = getIntent();
		findView();
		if (mIntent != null) {
			initView();
		}

	}

	private void initView() {

		String name = mIntent.getStringExtra("NAME");
		String url = mIntent.getStringExtra("IMAGE");
		String address = mIntent.getStringExtra("ADDRESS");
		final String phone = mIntent.getStringExtra("PHONE");
		comments = mIntent.getStringArrayListExtra("COMMENT");
		Log.v("URL", url);
		restautantImg.setImageResource(Integer.parseInt(url));
		addressTxt.setText(address);
		phoneTxt.setText(phone);
		nameTxt.setText(name);
		mListAdapter = new ListAdapter() {

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return comments.size() == 0;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				convertView = new TextView(DetailActivity.this);
				;
				((TextView) convertView).setMinHeight(40);
				((TextView) convertView).setText(comments.get(position));
				return convertView;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return comments.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return comments.size();
			}

			@Override
			public boolean isEnabled(int position) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		commentListView.setAdapter(mListAdapter);
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				DetailActivity.this.startActivity(intent);
			}
		});
	}

	private void findView() {
		orderBtn = (Button) findViewById(R.id.activity_detail_orderBtn);
		restautantImg = (ImageView) findViewById(R.id.restautant_Img);
		addressTxt = (TextView) findViewById(R.id.activity_detail_addressTxt);
		phoneTxt = (TextView) findViewById(R.id.activity_detail_phoneTxt);
		nameTxt = (TextView) findViewById(R.id.detail_nameTxt);
		commentListView = (ListView) findViewById(R.id.activity_detail_commentListView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
