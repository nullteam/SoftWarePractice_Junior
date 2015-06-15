package com.universer.HustWhereToEat.activity;

/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;

import com.universer.HustWhereToEat.R;
import com.universer.HustWhereToEat.fragment.AllFragment;
import com.universer.HustWhereToEat.fragment.LeftFragment;
import com.universer.HustWhereToEat.fragment.MyLoveFragment;
import com.universer.HustWhereToEat.fragment.MyOrdersFragment;
import com.universer.HustWhereToEat.fragment.RightFragment;
import com.universer.HustWhereToEat.fragment.RouteFragment;
import com.universer.HustWhereToEat.fragment.SettingFragment;
import com.universer.HustWhereToEat.fragment.SurroundFragment;
import com.universer.HustWhereToEat.fragment.AllFragment.MyPageChangeListener;
import com.universer.HustWhereToEat.util.IChangeFragment;
import com.universer.HustWhereToEat.view.SlidingMenu;

public class SlidingActivity extends FragmentActivity implements
		IChangeFragment {

	private static final String TAG = "SlidingActivity";

	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	AllFragment allFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		init();
		initListener(allFragment);

	}

	private void init() {

		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);

		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();

		leftFragment = new LeftFragment(this.getSupportFragmentManager());
		leftFragment.setChangeFragmentListener(this);

		t.replace(R.id.left_frame, leftFragment);
		rightFragment = new RightFragment();

//		t.replace(R.id.right_frame, rightFragment);
		allFragment = new AllFragment(this);

		t.replace(R.id.center_frame, allFragment);
		t.commit();

	}

	private void initListener(final AllFragment fragment) {
		fragment.setMyPageChangeListener(new MyPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Log.e(TAG, "onPageSelected : " + position);
				if (fragment.isFirst()) {
					mSlidingMenu.setCanSliding(true, false);
				} else if (fragment.isEnd()) {
					mSlidingMenu.setCanSliding(false, true);
				} else {
					mSlidingMenu.setCanSliding(false, false);
				}
			}
		});
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	@Override
	public void changeFragment(int position) {
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new AllFragment(this);
			initListener((AllFragment) fragment);
			break;
		case 1:
			fragment = new RouteFragment();
			break;
		case 2:
			fragment = new MyLoveFragment();
			break;

		case 3:
			fragment = new MyOrdersFragment();
			break;
		case 4:
			fragment = new SurroundFragment();
			break;
		case 5:
			fragment = new SettingFragment();

			break;
		default:
			break;

		}
		t.replace(R.id.center_frame, fragment);
		t.commit();
	}

}
