package com.asktun.mg.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.AppsItemInfo;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.GetUpdateApk;
import com.asktun.mg.fragment.GameFragment;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.ApkItem;
import com.asktun.mg.service.DataManager;
import com.asktun.mg.service.DownloadAdapter;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadService;
import com.asktun.mg.service.DownloadUtils;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.IResponseListener;

public class DownloadManageActivity extends BaseActivity implements
		AConstDefine {

	private static final int EVENT_REQUEST_DATA = 1; // 请求与下载有关的数据
	private static final int EVENT_REFRESH_DATA = 2; // 刷新显示数据

	public static final int FLAG_RESTORE = 1;
	public static final int FLAG_BACKUP = 2;

	private static final int EVENT_REQUEST_SOFTWARE_LIST = 3;
	private static final int EVENT_LOADED = 4;

	@ViewInject(id = R.id.rgroup)
	private RadioGroup rg;
	@ViewInject(id = R.id.lv_1)
	private ExpandableListView mExpandableListView;
	// @ViewInject(id = R.id.lv_1)
	// private ScrollView listView1;
	// @ViewInject(id = R.id.download_listview_lin)
	// private LinearLayout layout;
	@ViewInject(id = R.id.lv_2)
	private XListView listView2;
	@ViewInject(id = R.id.lv_3)
	private XListView listView3;

	private UpdateAdapter updateAdapter;

	private List<GameInfo> downloadList = new ArrayList<GameInfo>();
	public static List<View> vList = new ArrayList<View>();

	private List<AppsItemInfo> appList = new ArrayList<AppsItemInfo>();
	private MyAppAdapter appAdapter;

	private MyHandler mHandler;
	private DownloadAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_download_manage);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("下载管理");
		init();
	}

	private void init() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_1) {
					// type = 0;
					mExpandableListView.setVisibility(View.VISIBLE);
					listView2.setVisibility(View.GONE);
					listView3.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_2) {
					listView2.setVisibility(View.VISIBLE);
					listView3.setVisibility(View.GONE);
					mExpandableListView.setVisibility(View.GONE);
				} else {
					// type = 1;
					listView3.setVisibility(View.VISIBLE);
					listView2.setVisibility(View.GONE);
					mExpandableListView.setVisibility(View.GONE);
				}
			}
		});
		updateAdapter = new UpdateAdapter();

		appAdapter = new MyAppAdapter();
		listView2.setAdapter(updateAdapter);
		listView3.setAdapter(appAdapter);

		listView2.setPullLoadEnable(false);
		listView2.setPullRefreshEnable(false);
		listView3.setPullLoadEnable(false);
		listView3.setPullRefreshEnable(false);

		initHandler();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		appList.clear();
		getAllGameApp();
		appAdapter.notifyDataSetChanged();
	}

	private void getAllGameApp() {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = getPackageManager();
		// 获取手机内所有应用
		List<PackageInfo> packlist = pManager.getInstalledPackages(0);
		for (int i = 0; i < packlist.size(); i++) {
			PackageInfo pak = packlist.get(i);
			// if()里的值如果==0则为自己装的程序，否则为系统工程自带
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 添加自己已经安装的应用程序
				apps.add(pak);
				String appName = pManager.getApplicationLabel(
						pak.applicationInfo).toString();
				if (MyApp.gnList.contains(appName)) {
					AppsItemInfo aii = new AppsItemInfo();
					aii.setIcon(pManager
							.getApplicationIcon(pak.applicationInfo));
					aii.setLabel(pManager.getApplicationLabel(
							pak.applicationInfo).toString());
					aii.setPackageName(pak.applicationInfo.packageName);
					String filePath = pak.applicationInfo.publicSourceDir;
					aii.setSize(Util.round(new File(filePath).length() * 1.0
							/ (1024 * 1024), 2, BigDecimal.ROUND_UP)
							+ "MB");
					appList.add(aii);
				}
			}
		}
	}

	/**
	 * 请求数据（可更新，待安装，已忽略）
	 */
	private void initHandler() {
		HandlerThread mHandlerThread = new HandlerThread("");
		mHandlerThread.start();
		mHandler = new MyHandler(mHandlerThread.getLooper());
		mHandler.sendEmptyMessage(EVENT_REQUEST_DATA);
	}

	public class MyHandler extends Handler {
		MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case EVENT_REQUEST_DATA:// 请求数据
				final List<List<DownloadEntity>> childList = initData();// 获取子数据
				boolean hasDownloadData = childList != null ? childList.size() > 2 : false; // 2->111111111111111111111111
				final List<String> groupList = initGroupData(hasDownloadData);// 获取父数据
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initViews(groupList, childList);
					}
				});
				break;
			case EVENT_REFRESH_DATA:// 刷新listview
				runOnUiThread(new Runnable() {

					@Override
					public void run() {// 每隔1秒刷新数据
						mAdapter.refreshAdapter();
						mHandler.sendEmptyMessageDelayed(EVENT_REFRESH_DATA,
								1000L);
					}
				});
				break;
			}
		}
	}

	/**
	 * 初始化父数据，3组或4组
	 * 
	 * @param hasDownloadData
	 * @return
	 */
	private List<String> initGroupData(boolean hasDownloadData) {
		List<String> list = new ArrayList<String>();
		if (hasDownloadData) {
			list.add(getString(R.string.transferapk));// 正在传输
		}
		list.add(getString(R.string.waitinstallapk));// 待安装
		list.add(getString(R.string.updateapk));// 可更新
//		list.add(getString(R.string.ignoreapk));// 忽略
		// list.add(getString(R.string.updateapk));// 可更新
		// list.add(getString(R.string.ignoreapk));// 忽略
		return list;
	}

	/**
	 * 初始化expandablelistview
	 * 
	 * @param groupList
	 * @param childList
	 */
	private void initViews(List<String> groupList,
			List<List<DownloadEntity>> childList) {
		mExpandableListView = (ExpandableListView) findViewById(R.id.lv_1);
		// mExpandableListView.setChildDivider(getResources().getDrawable(R.drawable.list_divider));
		mExpandableListView.setGroupIndicator(null);
		mAdapter = new DownloadAdapter(this, childList, groupList, mHandler);
		mExpandableListView.setAdapter(mAdapter);
		int count = groupList.size();
		for (int i = 0; i < count; i++) {
			mExpandableListView.expandGroup(i);
		}
		// mExpandableListView.collapseGroup(count);
		// mLoadingView.setVisibility(View.GONE);
		mExpandableListView.setVisibility(View.VISIBLE);
		mHandler.sendEmptyMessageDelayed(EVENT_REFRESH_DATA, 1000L);
	}

	/**
	 * 初始化组数据
	 * 
	 * @return
	 */
	private List<List<DownloadEntity>> initData() {
		List<List<DownloadEntity>> adapterData = new ArrayList<List<DownloadEntity>>();
		List<DownloadEntity> installList = new ArrayList<DownloadEntity>();
		List<DownloadEntity> downloadingList = new ArrayList<DownloadEntity>();
		List<DownloadEntity> updatingList = new ArrayList<DownloadEntity>();
		List<DownloadEntity> ignoreList = new ArrayList<DownloadEntity>();
		if (DownloadService.mDownloadService != null) {
			List<DownloadEntity> downloadList = DownloadService.mDownloadService
					.getAllDownloadList();
			for (int i = 0; i < downloadList.size(); i++) {
				DownloadEntity entity = downloadList.get(i);
				if (entity.downloadType == TYPE_OF_COMPLETE) {// 待安装应用
					installList.add(entity);
				} else if (entity.downloadType == TYPE_OF_DOWNLOAD) {// 正在下载应用
					downloadingList.add(entity);
				} else if (entity.downloadType == TYPE_OF_UPDATE) {// 可更新应用
//					DownloadUtils.setInstallDownloadEntity(this, entity);
					updatingList.add(entity);
				} else if (entity.downloadType == TYPE_OF_IGNORE) {// 已忽略应用
				// DownloadUtils.setInstallDownloadEntity(this, entity);
				// ignoreList.add(entity);
				}
			}
			if (downloadingList.size() > 0) {
				adapterData.add(downloadingList);
			}
			System.out.println("download size:" + downloadingList.size() + ", "
					+ updatingList.size() + ", " + installList.size());
		}
		adapterData.add(installList);
//		adapterData.add(giListToDownEntityList());
//		adapterData.add(ignoreList);
		adapterData.add(updatingList);
		// adapterData.add(ignoreList);
		return adapterData;
	}

	private List<DownloadEntity> giListToDownEntityList() {
		List<DownloadEntity> updatingList = new ArrayList<DownloadEntity>();
		if(null != GameFragment.updateList && GameFragment.updateList.size() > 0){
			for(GameInfo gi : GameFragment.updateList){
				DownloadEntity de = new DownloadEntity(Util.GameInfoToAI(gi));
				de.downloadType = TYPE_OF_UPDATE;
				de.fileLength = (long) ((Float.parseFloat(gi.getSize()))*1024*1024);
				System.out.println("gilde---gi=" + gi.getGame_name() + " de=" + de.appName+" de.fileLength="+de.fileLength);
				updatingList.add(de);
			}
		}
		return updatingList;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private class UpdateAdapter extends BaseAdapter {

		private List<GameInfo> list = new ArrayList<GameInfo>();
		private GameInfo gi = null;

		public void add(GameInfo o) {
			list.add(o);
		}

		public void clear() {
			list.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// return list.size();
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_download_manage,
						null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_gametype = (TextView) convertView
						.findViewById(R.id.tv_gametype);
				holder.tv_size = (TextView) convertView
						.findViewById(R.id.tv_size);
				holder.tv_process = (TextView) convertView
						.findViewById(R.id.tv_process);
				holder.tv_process.setVisibility(View.GONE);
				holder.btn_download = (Button) convertView
						.findViewById(R.id.btn_download);
				holder.btn_download.setText("更新");
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (list.size() > 0)
				gi = list.get(position);
			if (gi != null) {
				imageLoader.displayImage(gi.getGame_ico(), holder.iv);
				holder.tv_name.setText(gi.getGame_name());
				holder.tv_gametype.setText(gi.getCate_name());
				holder.tv_size.setText(gi.getSize() + "M");
				final ApkItem apkItem = Util.GameInfoToAI(gi);
				holder.btn_download.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (apkItem.status == STATUS_APK_UNINSTALL
								|| apkItem.status == STATUS_APK_UNUPDATE) {// 下载
							DownloadUtils.checkDownload(
									DownloadManageActivity.this, apkItem);
							showToast(apkItem.appName + " 开始下载！");
							list.remove(gi);
							notifyDataSetChanged();
						} else {// 取消下载
							Intent intent = new Intent(
									AConstDefine.BROADCAST_ACTION_CANCEL_DOWNLOAD);
							DownloadEntity entity = new DownloadEntity(apkItem);
							Bundle bundle = new Bundle();
							bundle.putParcelable(AConstDefine.DOWNLOAD_ENTITY,
									entity);
							intent.putExtras(bundle);
							sendBroadcast(intent);
							DownloadUtils.fillDownloadNotifycation(
									DownloadManageActivity.this, false);
						}
					}
				});
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_size;
			TextView tv_gametype;
			TextView tv_process;
			Button btn_download;
		}

	}

	private class MyAppAdapter extends BaseAdapter {

		// private List<ActiveListItem> list = new ArrayList<ActiveListItem>();
		//
		// public void add(ActiveListItem o) {
		// list.add(o);
		// }
		//
		// public void clear() {
		// list.clear();
		// }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return appList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_download, null,
						false);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.item_download_icon);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.item_download_name);
				holder.tv_size = (TextView) convertView
						.findViewById(R.id.item_download_size);
				holder.startBtn = (Button) convertView
						.findViewById(R.id.item_download_start);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AppsItemInfo gi = appList.get(position);
			if (gi != null) {
				holder.iv.setImageDrawable(gi.getIcon());
				holder.tv_name.setText(gi.getLabel());
				holder.tv_size.setText(gi.getSize());
				holder.startBtn.setText("卸载");
				final String pName = gi.getPackageName();
				holder.startBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Util.uninstallApk(DownloadManageActivity.this, pName);
					}
				});
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_size;
			Button startBtn;
		}

	}

	private void getUpdateList() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("game_info", DataManager.newInstance().initUpdateJson(this));
		UIDataProcess.reqData(GetUpdateApk.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						GetUpdateApk data = (GetUpdateApk) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								
							}
						} else {
							// showToast("无法获取数据");
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
						// showProgressDialog();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						// removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
						// showToast("无法获取数据");
					}
				});
	}
}
