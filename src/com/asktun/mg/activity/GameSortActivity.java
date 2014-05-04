package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.fragment.GameSortGoodFragment;
import com.asktun.mg.fragment.GameSortHotFragment;
import com.asktun.mg.fragment.GameSortNewFragment;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadService;
import com.asktun.mg.view.BadgeView;

/**
 * 游戏分类（角色扮演。。。）
 * 
 * @author Dean
 */
public class GameSortActivity extends BaseActivity implements AConstDefine{

	@ViewInject(id = R.id.viewpager)
	ViewPager vPager;
	@ViewInject(id = R.id.pagertab)
	PagerTabStrip pagerTabStrip;

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	List<String> titleList = new ArrayList<String>();
	
	private String sortId = null;
	
	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	
	private Button btn;
	private BadgeView badge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_game_sort_detail);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		Intent intent = getIntent();
		if(intent != null){
			String title = intent.getStringExtra("sortTitle");
			sortId = intent.getStringExtra("sortId");
			setTitleText(title);
		}
		btn = new Button(this);
		btn.setBackgroundResource(R.drawable.downloadmanage_btn_selector);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GameSortActivity.this, DownloadManageActivity.class));
			}
		});
		addRightView(btn);
		
		addRightButtonView(R.drawable.search_btn_selector).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(GameSortActivity.this, SearchActivity.class));
					}
				});
		badge = new BadgeView(this, btn);
		initBroadcast();
		init();
	}
	
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBadgeReceiver);
	}

	private void init() {
		pagerTabStrip.setTabIndicatorColorResource(R.color.orange);
		titleList.add("最新");
		titleList.add("最热");
		titleList.add("好评");
		
		fragmentList.add(new GameSortNewFragment());
		fragmentList.add(new GameSortHotFragment());
		fragmentList.add(new GameSortGoodFragment());

		MyPagerAdapter adapter = new MyPagerAdapter(
				getSupportFragmentManager(), fragmentList, titleList);
		vPager.setAdapter(adapter);

	}

	/**
	 * 定义适配器
	 * 
	 * @author Dean
	 */
	class MyPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragmentList;
		private List<String> titleList;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
				List<String> titleList) {
			super(fm);
			this.fragmentList = fragmentList;
			this.titleList = titleList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return (titleList.size() > position) ? titleList.get(position) : "";
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

	private BroadcastReceiver mBadgeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int count = 0;
			List<DownloadEntity> downloadList = DownloadService.mDownloadService.getAllDownloadList();
			for (int i = 0; i < downloadList.size(); i++) {
				DownloadEntity entity = downloadList.get(i);
				if (entity.downloadType == TYPE_OF_DOWNLOAD) {// 正在下载应用
					count++;
				}
			}
			System.out.println("``count= "+ count);
			if(count > 0){
				badge.setText(count +"");
				badge.show();
			}else{
				badge.hide();
			}
		}
	};
	
	private void initBroadcast() {
		
		int count = 0;
		List<DownloadEntity> downloadList = DownloadService.mDownloadService.getAllDownloadList();
		for (int i = 0; i < downloadList.size(); i++) {
			DownloadEntity entity = downloadList.get(i);
			if (entity.downloadType == TYPE_OF_DOWNLOAD) {// 正在下载应用
				count++;
			}
		}
		System.out.println("``count= "+ count);
		if(count > 0){
			badge.setText(count +"");
			badge.show();
		}else{
			badge.hide();
		}
		
		
		// 注册广播
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("mBadgeReceiver");
		registerReceiver(mBadgeReceiver, myIntentFilter);

		System.out.println("register download broadcast~~~~~~~");
		// 注册广播
//		IntentFilter mydownloadIntentFilter = new IntentFilter();
//		mydownloadIntentFilter.addAction("download");
//		act.registerReceiver(mDownloadBroadcastReceiver, mydownloadIntentFilter);
	}
	
}