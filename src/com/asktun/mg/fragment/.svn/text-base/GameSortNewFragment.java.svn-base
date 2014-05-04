package com.asktun.mg.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.activity.GameDetailActivity;
import com.asktun.mg.activity.GameSortActivity;
import com.asktun.mg.bean.CategoryListBean;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.ApkItem;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadUtils;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.StarLayout;
import com.asktun.mg.view.XListView;
import com.asktun.mg.view.XListView.IXListViewListener;
import com.chen.jmvc.util.IResponseListener;

/**
 * 游戏分类（填充ViewPager）
 * 
 * @Description
 * @author Dean
 * 
 */
public class GameSortNewFragment extends BaseFragment implements
		IXListViewListener, AConstDefine {

	private View view;

	@ViewInject(id = R.id.listview)
	private XListView listView;

	private GameSortAdapter adapter;

	private List<GameInfo> list = new ArrayList<GameInfo>();
	private int totalDataCount = 1;
	private int page = 1;

	private String sortId = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		sortId = ((GameSortActivity) activity).getSortId();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = mInflater.inflate(R.layout.fragment_new_hot, null);
			FinalActivity.initInjectedView(this, view);
			init();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	private void init() {
		adapter = new GameSortAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.startPullRefresh();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				GameInfo gi = adapter.list.get(position - 1);
				Intent intent = new Intent(act, GameDetailActivity.class);
				intent.putExtra("gameId", gi.getGame_id());
				intent.putExtra("gameName", gi.getGame_name());
				intent.putExtra("gameInfo", gi);
				startActivity(intent);
			}
		});

	}

	private class GameSortAdapter extends BaseAdapter {

		private List<GameInfo> list = new ArrayList<GameInfo>();

		public void add(List<GameInfo> o) {
			list.addAll(o);
		}

		public void clear() {
			list.clear();
		}

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
				holder.tv_size = (TextView) convertView
						.findViewById(R.id.tv_size);
				holder.tv_gametype = (TextView) convertView
						.findViewById(R.id.tv_gametype);
				holder.starLayout = (StarLayout) convertView
						.findViewById(R.id.starLayout);
				holder.tv_playcount = (TextView) convertView
						.findViewById(R.id.tv_playcount);
				holder.btn_download = (Button) convertView
						.findViewById(R.id.btn_download);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GameInfo gi = list.get(position);
			if (gi != null) {
				imageLoader.displayImage(gi.getGame_ico(), holder.iv,
						options_round);
				holder.tv_name.setText(gi.getGame_name());
				holder.tv_size.setText(gi.getSize() + "M");
				holder.tv_gametype.setText(gi.getCate_name());
				holder.tv_playcount.setText(gi.getUser_num() + "人在玩");
				holder.starLayout.setCount(Integer.parseInt(gi.getScore()));
				final ApkItem apkItem = Util.GameInfoToAI(gi);
				displayApkStatus(holder.btn_download, apkItem.status);
				holder.btn_download.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (apkItem.status == STATUS_APK_UNINSTALL
								|| apkItem.status == STATUS_APK_UNUPDATE) {// 下载
							DownloadUtils.checkDownload(act, apkItem);
							list.get(position).setStatus(STATUS_APK_INSTALL);
						} else {// 取消下载
							Intent intent = new Intent(
									AConstDefine.BROADCAST_ACTION_CANCEL_DOWNLOAD);
							DownloadEntity entity = new DownloadEntity(apkItem);
							Bundle bundle = new Bundle();
							bundle.putParcelable(AConstDefine.DOWNLOAD_ENTITY,
									entity);
							intent.putExtras(bundle);
							act.sendBroadcast(intent);
							// if (entity.downloadType ==
							// AConstDefine.TYPE_OF_DOWNLOAD) {
							DownloadUtils.fillDownloadNotifycation(act, false);
							// } else if (entity.downloadType ==
							// AConstDefine.TYPE_OF_UPDATE) {
							// DownloadUtils.fillUpdateAndUpdatingNotifycation(
							// act, false);
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
			StarLayout starLayout;
			TextView tv_playcount;
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

	private void getGameSortNew(boolean isrefresh) {
		if (isrefresh) {
			page = 1;
			totalDataCount = 1;
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		if (adapter.getCount() >= totalDataCount) {
			listView.notifyFootViewTextChange();
			return;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", "1");
		params.put("page", page++);
		params.put("cate_id", sortId);
		UIDataProcess.reqData(CategoryListBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						CategoryListBean data = (CategoryListBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								totalDataCount = data.getCount();
								listView.setTotalDataCount(data.getCount());
								final List<GameInfo> item = data.getData();
								if (item != null && item.size() > 0) {
									new Thread() {

										@Override
										public void run() {
											List<GameInfo> item2 = setApkStatus(item);
											Message msg = notifyHandler
													.obtainMessage(0, item2);
											notifyHandler.sendMessage(msg);
										}

									}.start();
								}
							} else if (data.getFlg() == 0) {
								listView.setTotalDataCount(0);
								listView.notifyFootViewTextChange();
							}
						} else {
							showToast("无法获取信息");
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
						listView.setTotalDataCount(-1);
						listView.notifyFootViewTextChange();
					}
				});
	}

	Handler notifyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			List<GameInfo> item = (List<GameInfo>) msg.obj;
			adapter.add(item);
			adapter.notifyDataSetChanged();
			listView.notifyFootViewTextChange();
		}
	};

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
			act.sendBroadcast(new Intent().setAction("download"));
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

	@Override
	public void onRefresh() {
		getGameSortNew(true);
	}

	@Override
	public void onLoadMore() {
		getGameSortNew(false);
	}
}
