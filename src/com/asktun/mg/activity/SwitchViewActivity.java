package com.asktun.mg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.view.MyScrollLayout;
import com.asktun.mg.view.OnViewChangeListener;

public class SwitchViewActivity extends BaseActivity implements
		OnViewChangeListener, OnClickListener {
	/** Called when the activity is first created. */

	private MyScrollLayout mScrollLayout;
	private int mViewCount;
	private int mCurSel;
	private LinearLayout clickBtn;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrollviewswitch);

		sp = getSharedPreferences("my.switchview", Context.MODE_WORLD_WRITEABLE
				| Context.MODE_WORLD_READABLE);
		int flag = sp.getInt("my.first", 0);
		final int version = getAppVersionName(this);
		if (flag == version) {
			gotoSplash();
		}

		clickBtn = (LinearLayout) findViewById(R.id.llayout);

		clickBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurSel < mViewCount - 1) {
					setCurPoint(++mCurSel);
					mScrollLayout.snapToScreen(mCurSel);
				} else {
					gotoSplash();
					sp.edit().putInt("my.first", version).commit();
				}

			}
		});

		init();
		Log.v("@@@@@@", "this is in  SwitchViewDemoActivity onClick()");
	}

	private void gotoSplash() {
		Intent it = new Intent();
		it.setClass(SwitchViewActivity.this, WelcomeActivity.class);
		SwitchViewActivity.this.startActivity(it);
		SwitchViewActivity.this.finish();
	}

	private void init() {
		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		mViewCount = mScrollLayout.getChildCount();
		mCurSel = 0;
		mScrollLayout.SetOnViewChangeListener(this);
		Log.v("@@@@@@", "this is in  SwitchViewDemoActivity init()");
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mCurSel = index;
	}

	@Override
	public void OnViewChange(int view) {
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public static int getAppVersionName(Context context) {
		int versionName = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
}