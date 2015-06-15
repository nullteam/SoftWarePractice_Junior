package com.universer.HustWhereToEat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.universer.HustWhereToEat.R;


public class LeftAdapter extends BaseAdapter {
	private List<String> leftTxtList;
	private List<String> leftEngTxtList;
	private Context mContext;
	private int selectPosition;

	public LeftAdapter(List<String> leftTxtList, List<String> leftEngTxtList,
			Context mContext) {
		this.leftTxtList = leftTxtList;
		this.mContext = mContext;
		this.leftEngTxtList = leftEngTxtList;
	}

	public void setSelectPosition(int position) {
		selectPosition = position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return leftTxtList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return leftTxtList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(mContext)
				.inflate(R.layout.left_list, null);
		ChildHolder childHolder = new ChildHolder();
		childHolder.leftListTxt = (TextView) v.findViewById(R.id.left_list_Txt);
		childHolder.leftEngTxt = (TextView) v
				.findViewById(R.id.left_list_engTxt);
		childHolder.leftListImg = (ImageView) v
				.findViewById(R.id.left_list_image);
		childHolder.leftListTxt.setText(leftTxtList.get(position));
		childHolder.leftEngTxt.setText(leftEngTxtList.get(position));
		switch (position) {
		case 0:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.fragment_left_all_icon);
			break;
		case 1:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.route);
			break;
		case 2:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.fragment_left_love_icon);
			break;
		case 3:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.fragment_left_order_icon);
			break;
		case 4:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.fragment_left_surround_icon);
			break;
		case 5:
			childHolder.leftListImg
					.setBackgroundResource(R.drawable.fragment_left_setting_icon);
			break;
		default:
			break;
		}

		if (position == selectPosition) {
			v.setBackgroundResource(R.drawable.biz_navigation_tab_bg_pressed);
			childHolder.leftListTxt.setSelected(true);
		}
		return v;

	}

	final class ChildHolder {
		TextView leftListTxt;
		TextView leftEngTxt;
		ImageView leftListImg;
	}

}
