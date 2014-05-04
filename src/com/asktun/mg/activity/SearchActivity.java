package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.SearchBean;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.ApkItem;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadUtils;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.StarLayout;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

public class SearchActivity extends BaseActivity implements AConstDefine {
	@ViewInject(id = R.id.search_et)
	private EditText text;
	@ViewInject(id = R.id.search_btn)
	private Button button;
	@ViewInject(id = R.id.listview)
	private XListView listView;

	private List<GameInfo> list = new ArrayList<GameInfo>();
	private GameAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_search);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("搜游戏");
		init();
	}

	private void init() {
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				GameInfo gi = list.get(position - 1);
				Intent intent = new Intent(SearchActivity.this,
						GameDetailActivity.class);
				intent.putExtra("gameId", gi.getGame_id());
				intent.putExtra("gameName", gi.getGame_name());
				intent.putExtra("gameInfo", gi);
				startActivity(intent);
			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = text.getText().toString();
				if (!StrUtil.isEmpty(str)) {
					search(str);
					Util.hideKeyboard(SearchActivity.this);
				} else {
					showToast("输入搜索内容");
					return;
				}
			}
		});
	}

	private class GameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_gamelist, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_gametype = (TextView) convertView
						.findViewById(R.id.tv_gametype);
				holder.tv_size = (TextView) convertView
						.findViewById(R.id.tv_size);
				holder.tv_playcount = (TextView) convertView
						.findViewById(R.id.tv_playcount);
				holder.starLayout = (StarLayout) convertView
						.findViewById(R.id.starLayout);
				holder.btn_download = (Button) convertView
						.findViewById(R.id.btn_download);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GameInfo gi = list.get(position);
			if (gi != null) {
				imageLoader.displayImage(gi.getGame_ico(), holder.iv, options_round);
				holder.tv_name.setText(gi.getGame_name());
				holder.tv_gametype.setText(gi.getCate_name());
				holder.tv_playcount.setText(gi.getUser_num() + "人在玩");
				holder.tv_size.setText(gi.getSize() + "M");
				holder.starLayout.setCount(Integer.parseInt(gi.getScore()));
				final ApkItem apkItem = Util.GameInfoToAI(gi);
				displayApkStatus(holder.btn_download, apkItem.status);
				holder.btn_download.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (apkItem.status == STATUS_APK_UNINSTALL
								|| apkItem.status == STATUS_APK_UNUPDATE) {// 下载
							DownloadUtils.checkDownload(SearchActivity.this,
									apkItem);
							list.get(position).setStatus(STATUS_APK_INSTALL);
						} else {// 取消下载
							Intent intent = new Intent(
									AConstDefine.BROADCAST_ACTION_CANCEL_DOWNLOAD);
							DownloadEntity entity = new DownloadEntity(apkItem);
							Bundle bundle = new Bundle();
							bundle.putParcelable(AConstDefine.DOWNLOAD_ENTITY,
									entity);
							intent.putExtras(bundle);
							sendBroadcast(intent);
							// if (entity.downloadType ==
							// AConstDefine.TYPE_OF_DOWNLOAD) {
							DownloadUtils.fillDownloadNotifycation(
									SearchActivity.this, false);
							// } else if (entity.downloadType ==
							// AConstDefine.TYPE_OF_UPDATE) {
							// DownloadUtils
							// .fillUpdateAndUpdatingNotifycation(
							// SearchActivity.this, false);
							// }
							list.get(position).setStatus(STATUS_APK_UNINSTALL);
						}
						notifyDataSetChanged();
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
			TextView tv_playcount;
			StarLayout starLayout;
			Button btn_download;
		}

	}

	/**
	 * 显示apk状态
	 * 
	 * @param mTextView
	 * @param status
	 */
	protected void displayApkStatus(TextView mTextView, int status) {
		switch (status) {
		case STATUS_APK_UNINSTALL:
			setvisibleInstallTextView(mTextView, true, R.string.install);
			break;
		case STATUS_APK_INSTALL:
		case STATUS_APK_UPDATE:
			setvisibleInstallTextView(mTextView, true, R.string.cancel);
			break;
		case STATUS_APK_INSTALL_DONE:
			setvisibleInstallTextView(mTextView, false, R.string.has_installed);
			break;
		case STATUS_APK_UNUPDATE:
			setvisibleInstallTextView(mTextView, true, R.string.update);
			break;
		}
	}

	private void setvisibleInstallTextView(TextView mTextView, boolean enable,
			int rId) {
		mTextView.setEnabled(enable);
		mTextView.setText(rId);
	}

	class DownloadClick implements OnClickListener {

		private GameInfo down;
		private Button bt;
		private int position;

		/**
		 * Title: Description:
		 */
		public DownloadClick(GameInfo down, Button bt, int position) {
			this.bt = bt;
			this.down = down;
			this.position = position;

		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {

			Log.v("test", "download````````````");
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 无sd卡
				showToast("没找到存储卡");
				return;
			}
			// test
			// down = new GameInfo();
			// down.setCate_name("1");
			// down.setGame_id("1");
			// down.setGame_name("P vs Z");
			// down.setUrl("http://117.135.129.181/wap/468816.mp3");

			System.out.println("点击了：" + down.getPosition());
			int state = down.getDownloadState();
			switch (state) {
			case DOWNLOAD_STATE_DELETE:
				// 可下载
				toDownload();
				break;
			case DOWNLOAD_STATE_SUCCESS:
				// 播放打开等等操作
				// Toast.makeText(getApplicationContext(),"打开了："+down.getMovieName(),
				// Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_STATE_WATTING:
				toDownload();
				break;
			case DOWNLOAD_STATE_NONE:
				toDownload();
				break;
			case DOWNLOAD_STATE_START:
				showToast("正在下载!");
				break;
			default:
				break;
			}
		}

		public void toDownload() {
			System.out.println(down.getGame_name() + ":DOWNLOAD_STATE_START"
					+ ":toDownload");
			down.setPosition(position);
			down.setDownloadState(DOWNLOAD_STATE_START);
			mApplication.setStartDownloadMovieItem(down);
			sendBroadcast(new Intent().setAction("download"));
			// bt.setClickable(false);
			// bt.setText("下载中");
			// 将最初的数据保存到数据库.主要是为了 让数据库中的数据第一反应与所操作的状态一致,因为在后面的下载任务中.在线程中更改数据库状态
			// 如果在线程未开始且程序退出的情况.那么这个状态会不正确
			List<GameInfo> ls = db.findItemsByWhereAndWhereValue("game_name",
					down.getGame_name(), GameInfo.class, TABNAME_DOWNLOADTASK,
					null);
			if (ls.size() == 0) {
				// 说明没有此条数据
				db.insertObject(down, TABNAME_DOWNLOADTASK);
			} else {
				// 更新这个状态
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "game_name=?",
						new String[] { down.getGame_name() }, down);
			}
		}

	}

	private void search(String name) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("game_name", name);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(SearchBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						SearchBean data = (SearchBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								list = data.getData();
								if (list != null && list.size() > 0) {
									new Thread() {
										@Override
										public void run() {
											list = setApkStatus(list);
											notifyHandler.sendEmptyMessage(0);
										}

									}.start();
									
								}
							} else if (data.getFlg() == 0) {
								showToast("没有数据");
							}
						} else {
							showToast("无法获取数据");
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
						showToast("无法获取数据");
					}
				});
	}
	
	Handler notifyHandler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter = new GameAdapter();
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	};
}
