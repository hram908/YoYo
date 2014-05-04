package com.asktun.mg.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.bean.AppsItemInfo;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.download.FinalDBChen;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;

public class UserDownloadActivity extends BaseActivity {

	@ViewInject(id = R.id.rgroup)
	private RadioGroup rg;
	@ViewInject(id = R.id.my_download_list)
	private XListView listView;
	@ViewInject(id = R.id.my_native_download_list)
	private XListView listViewNative;

	private MyDownloadAdapter adapter;
	private MyAppAdapter appAdapter;

	private List<GameInfo> downloadList = new ArrayList<GameInfo>();
	private List<AppsItemInfo> appList = new ArrayList<AppsItemInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_download);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("我的下载");
		getAllGameApp();
		init();
	}

	private void init() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_1) {
					listView.setVisibility(View.VISIBLE);
					listViewNative.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_2) {
					listViewNative.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
			}
		});
		listViewNative.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);

		adapter = new MyDownloadAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		
		appAdapter = new MyAppAdapter();
		listViewNative.setAdapter(appAdapter);
		listViewNative.setPullLoadEnable(false);
		listViewNative.setPullRefreshEnable(false);
		
	}
	
	private void getAllGameApp(){
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
                String appName = pManager.getApplicationLabel(pak.applicationInfo).toString();
                if(MyApp.gnList.contains(appName)){
                	AppsItemInfo aii = new AppsItemInfo();
                	aii.setIcon(pManager.getApplicationIcon(pak.applicationInfo));
                	aii.setLabel(pManager.getApplicationLabel(pak.applicationInfo).toString());
                	aii.setPackageName(pak.applicationInfo.packageName);
                	String filePath = pak.applicationInfo.publicSourceDir;
                	aii.setSize(Util.round(new File(filePath).length() * 1.0 / (1024*1024), 2, BigDecimal.ROUND_UP) + "MB");
                	appList.add(aii);
                }
        	}
        }
	}

	private class MyDownloadAdapter extends BaseAdapter {

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
			return downloadList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return downloadList.get(arg0);
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
			final GameInfo gi = downloadList.get(position);
			if (gi != null) {
				imageLoader.displayImage(gi.getGame_ico(), holder.iv, options);
				holder.tv_name.setText(gi.getGame_name());
				holder.tv_size.setText(gi.getSize() + "M");
				final String filePath = gi.getFilePath();
				// final String filePath = "mnt/sdcard/test.apk";
				final String pName = Util.getPnameByApk(
						UserDownloadActivity.this, filePath);
				final boolean isInstalled = Util.isAppInstalled(
						UserDownloadActivity.this, pName);
				if (isInstalled) {
					holder.startBtn.setText("启动");
				} else {
					holder.startBtn.setText("安装");
				}
				holder.startBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isInstalled) {
							Util.startAppByPname(UserDownloadActivity.this,
									pName);
						} else {
							Util.installApk(UserDownloadActivity.this, filePath);
						}
					}
				});
			}
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					new AlertDialog.Builder(UserDownloadActivity.this)
					.setTitle("提示")
					.setMessage("确认删除"+"?") //二次提示
					.setNegativeButton("取消", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{

						}
					})
					.setPositiveButton("确定",  new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							//删除本地文件
							File df = new File(gi.getFilePath());
							System.out.println("file~~~~~~" + df.getPath() + " file exist() " + df.exists());
							if(df.exists()) {
								//如果文件存在
								df.delete();
							}
							downloadList.remove(gi);
							notifyDataSetChanged();
							//删除数据库中的内容
							new FinalDBChen(getParent(), DBNAME).deleteItem(TABNAME_DOWNLOADTASK, "game_name=?", new String[] {gi.getGame_name()});
							//发送一个删除文件的广播.让主页的下载按钮变为可下载
							Intent i = new Intent();
							i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_DELETE);
							i.setAction(DOWNLOAD_TYPE);
							getMyApplication().setDownloadSuccess(gi);
							sendBroadcast(i);
						}
					}).show();
					return false;
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_size;
			Button startBtn;
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
				holder.startBtn.setText("启动");
				final String pName = gi.getPackageName();
				holder.startBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
							Util.startAppByPname(UserDownloadActivity.this,
									pName);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

}
