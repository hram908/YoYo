package com.asktun.mg.activity;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.adapter.CustomFragmentPagerAdapter;
import com.asktun.mg.fragment.ContactFragment;
import com.asktun.mg.fragment.GameFragment;
import com.asktun.mg.fragment.MessageFragment;
import com.asktun.mg.fragment.NearFragment;
import com.asktun.mg.fragment.UserFragment;
import com.asktun.mg.service.DownloadService;
import com.asktun.mg.utils.ExitApplication;
import com.asktun.mg.utils.SoundPlayer;
import com.asktun.mg.view.BadgeView;
import com.asktun.mg.view.MyViewPager;

public class PageFrameActivity extends BaseActivity {
	private MyViewPager mTabPager;
	private ArrayList<Fragment> pagerItemList = null;
	private int currIndex = 0;
	private int pagerCount;
	private FrameLayout main_bottom;
	private Fragment page1;
	private Fragment page2;
	private Fragment page3;
	private Fragment page4;
	private Fragment page5;

	private View iv_tab2;
	private BadgeView badge;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_frame);
		ExitApplication.getInstance().addActivity(this);

		Intent downloadIntent = new Intent(this, DownloadService.class);// 开启下载服务
		startService(downloadIntent);
		
		mTabPager = (MyViewPager) findViewById(R.id.vPager);
		mTabPager.setOffscreenPageLimit(1);
		main_bottom = (FrameLayout) findViewById(R.id.main_bottom);

		mTabPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}

		});

		iv_tab2 = findViewById(R.id.img_2);
		badge = new BadgeView(this, iv_tab2);
		badge.setText("1");

		page1 = new NearFragment();
		page2 = new MessageFragment();
		page3 = new ContactFragment();
		page4 = new GameFragment();
		page5 = new UserFragment();

		pagerItemList = new ArrayList<Fragment>();
		pagerItemList.add(page1);
		pagerItemList.add(page2);
		pagerItemList.add(page3);
		pagerItemList.add(page4);
		pagerItemList.add(page5);
		pagerCount = pagerItemList.size();
		FragmentManager mFragmentManager = this.getSupportFragmentManager();
		CustomFragmentPagerAdapter mFragmentPagerAdapter = new CustomFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mTabPager.setAdapter(mFragmentPagerAdapter);
		mTabPager.setOffscreenPageLimit(2);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	public void setBadge(int count) {
		if (count == 0) {
			badge.hide();
		} else {
			if (MyApp.isSoundEnable)
				SoundPlayer.dingdong();
			if (MyApp.isVibrateEnable)
				SoundPlayer.vibrator();
			badge.setText(count + "");
			badge.show();
		}
	}

	@Override
	public void onBackPressed() {
		imageLoader.stop();
		super.onBackPressed();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			changeBg(arg0);
			currIndex = arg0;
		}

	}

	public class MyOnClickListener implements OnClickListener {
		int index = 0;

		public MyOnClickListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index, false);
		}
	}

	/**
	 * @param arg0
	 */
	public void changeBg(int arg0) {
		for (int i = 0; i < pagerCount; i++) {
			int imgId = getResources().getIdentifier("img_" + (i + 1), "id",
					PageFrameActivity.this.getPackageName());
			ImageView iv = (ImageView) findViewById(imgId);
			iv.setOnClickListener(new MyOnClickListener(i));
			int resId = getResources().getIdentifier("index_" + (i + 1),
					"drawable", PageFrameActivity.this.getPackageName());
			Drawable drawable = getResources().getDrawable(resId);
			int resId2 = getResources().getIdentifier("index_0" + (i + 1),
					"drawable", PageFrameActivity.this.getPackageName());
			Drawable drawable2 = getResources().getDrawable(resId2);
			if (arg0 == i) {
				iv.setBackgroundDrawable(drawable);
			} else {
				iv.setBackgroundDrawable(drawable2);
			}
		}
	}

	public int getBottomHeight() {
		main_bottom.measure(0, 0);
		return main_bottom.getMeasuredHeight();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (currIndex == 1) { // 消息页面上

				if (((MessageFragment) page2).isDelete) {
					((MessageFragment) page2).doInBackPress();
				} else {
//					ExitDoubleClick.getInstance(this).doDoubleClick(3000, null);
					 moveTaskToBack(false);//
				}
			} else {
//				ExitDoubleClick.getInstance(this).doDoubleClick(3000, null);
				 moveTaskToBack(false);//
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationManager messageNotificatioManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		messageNotificatioManager.cancelAll();
		mApplication.stopLocation();
		
		stopService(new Intent(this, DownloadService.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				changeItem(currIndex);
			}
		}, 200);
	}

	public void changeItem(int index) {
		mTabPager.setCurrentItem(index, false);
		changeBg(index);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
}