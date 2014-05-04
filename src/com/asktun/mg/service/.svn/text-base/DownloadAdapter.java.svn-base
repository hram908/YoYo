package com.asktun.mg.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.asktun.mg.R;
import com.asktun.mg.activity.DownloadManageActivity;
import com.asktun.mg.activity.DownloadManageActivity.MyHandler;

/**
 * 更新、安装页expandable适配�?
 * 
 * @author yvon
 * 
 */
public class DownloadAdapter extends BaseExpandableListAdapter implements AConstDefine, DownloadService.DownloadStatusListener {
	private Context context;
	private List<List<DownloadEntity>> childList;
	private List<String> groupList;
	private String updatingString;
	private Bitmap mDefaultBitmap;
	private String continueString, cancelString, pauseString, installString, ignoreString, deleteString, updateString;

	private static final int EVENT_REFRESH_DATA = 2;

	private MyHandler mHandler;

	private DecimalFormat df = null;

	public static List<String> rootApkList = new ArrayList<String>();

	public DownloadAdapter(Context context, List<List<DownloadEntity>> list, List<String> groupList, DownloadManageActivity.MyHandler mHandler) {
		this.context = context;
		this.childList = list;
		this.groupList = groupList;
		this.mHandler = mHandler;
		DownloadService.setDownloadStatusListener(this);
		initString();
		df = new DecimalFormat("##0.0");
		try {
			mDefaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_ico_no);
		} catch (OutOfMemoryError e) {
			if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
				mDefaultBitmap.recycle();
			}
		}
		registerAllReceiver();
	}

	private void initString() {
		updatingString = context.getString(R.string.updateapk);
		continueString = context.getString(R.string.button_status_continue);
		cancelString = context.getString(R.string.button_status_cancel);
		pauseString = context.getString(R.string.button_status_pause);
		installString = context.getString(R.string.button_status_install);
		ignoreString = context.getString(R.string.ignore);
		deleteString = context.getString(R.string.app_delete);
		updateString = context.getString(R.string.update);
	}

	private void registerAllReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_ACTION_COMPLETE_DOWNLOAD);
		intentFilter.addAction(BROADCAST_ACTION_ADD_UPDATE);
		intentFilter.addAction(BROADCAST_ACTION_INSTALL_COMPLETE);
		intentFilter.addAction(BROADCAST_ACTION_REMOVE_COMPLETE);
		intentFilter.addAction(BROADCAST_ACTION_ADD_DOWNLOAD_LIST);
		intentFilter.addAction(BROADCAST_ACTION_UPDATE_ROOTSTATUS);
		context.registerReceiver(mDownloadReceiver, intentFilter);
	}

	private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_ACTION_COMPLETE_DOWNLOAD.equals(intent.getAction())) {// 下载完成
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					DownloadEntity entity = bundle.getParcelable(DOWNLOAD_ENTITY);
					removeCompleteToInstall(entity);// 移除下载完成的应用至待安�?
				}
			} else if (BROADCAST_ACTION_ADD_UPDATE.equals(intent.getAction())) {// 添加至更�?
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					DownloadEntity entity = bundle.getParcelable(DOWNLOAD_ENTITY);
					addUpdate(entity);// 将新更新添加至列表中
				}
			} else if (BROADCAST_ACTION_INSTALL_COMPLETE.equals(intent.getAction())) {// 安装完成
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					DownloadEntity entity = bundle.getParcelable(DOWNLOAD_ENTITY);
					deleteWaitInstallApk(entity);// 从待安装的列表中删除应用
					for (int i = 0; i < rootApkList.size(); i++) {
						if (rootApkList.get(i).equals(entity.packageName)) {
							rootApkList.remove(entity.packageName);
						}
					}
				}
			} else if (BROADCAST_ACTION_REMOVE_COMPLETE.equals(intent.getAction())) {// 应用卸载完成
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					DownloadEntity entity = bundle.getParcelable(DOWNLOAD_ENTITY);
					deleteUpdateDownloadEntity(entity);
				}
			} else if (BROADCAST_ACTION_ADD_DOWNLOAD_LIST.equals(intent.getAction())) {// 加入下载列表
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					DownloadEntity entity = bundle.getParcelable(DOWNLOAD_ENTITY);
					addDownloadList(entity);// 添加至正在下载列�?
				}
			} else if (BROADCAST_ACTION_UPDATE_ROOTSTATUS.equals(intent.getAction())) {// Root安装失败
				updateInstallFailStatus(intent.getStringExtra(DOWNLOAD_APKPACKAGENAME));// 更新安装失败状�?
			}
		}
	};

	public void unregisterAllReceiver() {
		context.unregisterReceiver(mDownloadReceiver);
	}

	/**
	 * 移除下载完成的应用至待安�?
	 * 
	 * @param entity
	 */
	private void removeCompleteToInstall(DownloadEntity entity) {
		boolean removed = false;
		removeMessage();
		for (int i = 0; i < childList.size(); i++) {
			int j = 0;
			for (; j < childList.get(i).size(); j++) {
				DownloadEntity d = childList.get(i).get(j);
				if (d.appId == entity.appId && d.category == entity.category) {
					childList.get(i).remove(j);// 移除
					if (childList.size() > 1 && i == 0 && childList.get(i).size() == 0) {
						groupList.remove(i);
						childList.remove(i);
					}
					entity.downloadType = TYPE_OF_COMPLETE;
					childList.get(childList.size() - 2).add(entity);// 添加到待安装childList.size() - 2待安装的那组 在倒数第二个位置

					removed = true;
					break;
				}
			}
			if (removed) {
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 将新更新添加至列表中
	 * 
	 * @param entity
	 */
	private void addUpdate(DownloadEntity entity) {
		removeMessage();

		List<DownloadEntity> downloadList = DownloadService.mDownloadService.getAllDownloadList();
		for (int i = 0; i < downloadList.size(); i++) {
			DownloadEntity d = downloadList.get(i);
			if (d.appName.equals(entity.appName) && d.versionCode == entity.versionCode) {
				int position = 1;
				if (childList.size() > 2) {
					position = 2;
				}
				childList.get(position).add(d);// 添加到更新位�?

				DownloadUtils.fillUpdateNotifycation(context);// 通知栏更�?
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 从待安装的列表中删除应用
	 * 
	 * @param entity
	 */
	private void deleteWaitInstallApk(DownloadEntity entity) {
		removeMessage();
		List<DownloadEntity> list = childList.get(childList.size() - 2);// childList.size() - 2待安装的那组 在倒数第二个位置
		for (int i = 0; i < list.size(); i++) {
			DownloadEntity mDownloadEntity = list.get(i);
			if (mDownloadEntity.appName.equals(entity.appName) && mDownloadEntity.versionCode == entity.versionCode) {
				list.remove(i);// 移除
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 卸载后需将可更新应用移除
	 */
	private void deleteUpdateDownloadEntity(DownloadEntity entity) {
		removeMessage();
		int position = 0;
		if (childList.size() == 4) {
			position = 1;
		}
		for (int i = 0; i < childList.get(position).size(); i++) {
			DownloadEntity d = childList.get(position).get(i);
			if (d.packageName.equals(entity.packageName) && d.versionCode == entity.versionCode) {
				childList.get(position).remove(i);// 从更新位置移�?
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 添加至正在下载列�?
	 * 
	 * @param entity
	 */
	private void addDownloadList(DownloadEntity entity) {
		removeMessage();
		List<DownloadEntity> downloadList = DownloadService.mDownloadService.getAllDownloadList();
		for (int i = 0; i < downloadList.size(); i++) {
			DownloadEntity d = downloadList.get(i);
			if (d.packageName.equals(entity.packageName) && d.versionCode == entity.versionCode) {
				if (groupList.size() > 3) {
					childList.get(0).add(d);// 添加至正在下�?
				} else {
					groupList.add(0, context.getString(R.string.transferapk));// 添加�?��
					List<DownloadEntity> downloadingList = new ArrayList<DownloadEntity>();
					downloadingList.add(d);
					childList.add(0, downloadingList);
				}
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 更新root安装失败的状�?
	 * 
	 * @param apkPackageName
	 */
	private void updateInstallFailStatus(String apkPackageName) {
		removeMessage();
		if (childList.size() == 3) {
			for (int i = 0; i < childList.get(1).size(); i++) {
				DownloadEntity downloadEntity = childList.get(1).get(i);
				if (apkPackageName.equals(downloadEntity.packageName)) {
					downloadEntity.downloadType = TYPE_OF_COMPLETE;// 更新应用下载类型
					break;
				}
			}
		} else {
			for (int i = 0; i < childList.get(2).size(); i++) {
				DownloadEntity downloadEntity = childList.get(2).get(i);
				if (apkPackageName.equals(downloadEntity.packageName)) {
					downloadEntity.downloadType = TYPE_OF_COMPLETE;// 更新应用下载类型
					break;
				}
			}
		}
		sendMessage();
	}

	/**
	 * 删除刷新消息
	 */
	private void removeMessage() {
		if (mHandler != null && mHandler.hasMessages(EVENT_REFRESH_DATA)) {
			mHandler.removeMessages(EVENT_REFRESH_DATA);
		}
	}

	/**
	 * 发�?刷新消息
	 */
	private void sendMessage() {
		if (mHandler != null && !mHandler.hasMessages(EVENT_REFRESH_DATA)) {
			mHandler.sendEmptyMessage(EVENT_REFRESH_DATA);
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupList == null ? 0 : groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adownloadexpandgroup, null);
			holder = new GroupViewHolder();
			holder.mTextView = (TextView) convertView.findViewById(R.id.tvExpandGroupTitle);
			holder.mButton = (Button) convertView.findViewById(R.id.btnOneKeyUpdate);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.ivExpandGroupPic);

			holder.mButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DownloadUtils.checkOneKeyDownload(context, null);
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		String str = null;
		if (groupList.size() > groupPosition) {
			str = groupList.get(groupPosition);
			holder.mTextView.setText(str);// 设置组title
		}
		if (isExpanded) {// 是否扩展
			holder.mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pic_down));
		} else {
			holder.mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pic_up));
		}
		holder.mImageView.setVisibility(View.VISIBLE);
		if (updatingString.equals(str)) {// title为可更新应用
//			holder.mButton.setVisibility(View.VISIBLE);
//			holder.mImageView.setVisibility(View.GONE);
			holder.mButton.setVisibility(View.GONE);
			holder.mImageView.setVisibility(View.VISIBLE);
		} else {// title为其�?
			holder.mButton.setVisibility(View.GONE);
			holder.mImageView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childList == null ? 0 : childList.get(groupPosition).size() == 0 ? 1 : childList.get(groupPosition).size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list_download, null);
			holder = new ChildViewHolder();
			holder.mImageView = (ImageView) convertView.findViewById(R.id.iconImageview);
			holder.mAppNameTextView = (TextView) convertView.findViewById(R.id.appnametextview);
			holder.mAppVersionTextView = (TextView) convertView.findViewById(R.id.appversiontextview);
			holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_horizontal);
			holder.mCenterTextView = (TextView) convertView.findViewById(R.id.centertextview);
			holder.mBottomTextView = (TextView) convertView.findViewById(R.id.bottomtextview);
			holder.mFirstButton = (Button) convertView.findViewById(R.id.firstButton);
			holder.mSecondButton = (Button) convertView.findViewById(R.id.secondButton);
			holder.mLongButton = (Button) convertView.findViewById(R.id.longButton);
			holder.mEmptyTextView = (TextView) convertView.findViewById(R.id.emptytextview);
			holder.mContentLayout = convertView.findViewById(R.id.contentlayout);
			holder.mAuthorityImageview = (ImageView) convertView.findViewById(R.id.authorityimageview);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
			holder.mProgressBar.setProgress(0);
		}
		
		DownloadEntity entity = null;
		if (childList.size() > groupPosition && childList.get(groupPosition).size() > childPosition) {// childPosition可能�?，因为getChildrenCount至少大于1
			entity = childList.get(groupPosition).get(childPosition);
			switch (entity.downloadType) {
			case AConstDefine.TYPE_OF_DOWNLOAD:// 正在下载
				fillDownloadChildView(entity, holder);
				if (!TextUtils.isEmpty(entity.iconUrl)) {
					try {
						FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
					} catch (OutOfMemoryError e) {
						if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
							mDefaultBitmap.recycle();
						}
					}
				}
				break;
			case AConstDefine.TYPE_OF_UPDATE:// 正在更新
				fillUpdateChildView(entity, holder);
//				if (entity != null && entity.installedIcon != null) {
//					holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
//				} else {
//					holder.mImageView.setImageBitmap(mDefaultBitmap);
//				}
				if (!TextUtils.isEmpty(entity.iconUrl)) {
					try {
						FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
					} catch (OutOfMemoryError e) {
						if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
							mDefaultBitmap.recycle();
						}
					}
				}
				break;
			case AConstDefine.TYPE_OF_COMPLETE:// 下载完成
				fillWaitInstallChildView(entity, holder);
				if (!TextUtils.isEmpty(entity.iconUrl)) {
					try {
						FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
					} catch (OutOfMemoryError e) {
						if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
							mDefaultBitmap.recycle();
						}
					}
				}
				break;
			case TYPE_OF_IGNORE:// 忽略类型
				fillIgnoreChildView(entity, holder);
				if (entity != null && entity.installedIcon != null) {
					holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
				} else {
					holder.mImageView.setImageBitmap(mDefaultBitmap);
				}
				break;
			}
		} else {// childList.get(groupPosition).size()=0 的情�?
			if (childList.size() == 3) {// 有正在下�?   3 ->222222222222222222222222222222

				if (groupPosition == 0) {// 正在传输
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);
						fillDownloadChildView(entity, holder);// 填充下载childView

						if (!TextUtils.isEmpty(entity.iconUrl)) {
							try {
								FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
							} catch (OutOfMemoryError e) {
								if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
									mDefaultBitmap.recycle();
								}
							}
						}
					}
				} else if (groupPosition == 2) {// 正在更新
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);

						if (entity != null && entity.installedIcon != null) {
							holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
						} else {
							holder.mImageView.setImageBitmap(mDefaultBitmap);
						}
					}

					fillUpdateChildView(entity, holder);// 填充更新childView

				} else if (groupPosition == 1) {//等待安装
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);
						if (entity != null && !TextUtils.isEmpty(entity.iconUrl)) {
							try {
								FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
							} catch (OutOfMemoryError e) {
								if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
									mDefaultBitmap.recycle();
								}
							}
						}
					}
					
					fillWaitInstallChildView(entity, holder);// 填充等待安装childView
					
				} else if (groupPosition == 3) {
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);
						if (entity != null && entity.installedIcon != null) {
							holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
						} else {
							holder.mImageView.setImageBitmap(mDefaultBitmap);
						}
					}
					fillIgnoreChildView(entity, holder);//填充忽略childView
				}

			} else {

				if (groupPosition == 1) {
					if (childList.get(groupPosition).size() > childPosition) { // groupPosition ->0
						entity = childList.get(groupPosition).get(childPosition);
						if (entity != null && entity.installedIcon != null) {
							holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
						} else {
							holder.mImageView.setImageBitmap(mDefaultBitmap);
						}
					}
					fillUpdateChildView(entity, holder);
				} else if (groupPosition == 0) {
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);
						if (entity != null && !TextUtils.isEmpty(entity.iconUrl)) {
							try {
								FileService.getBitmap(entity.iconUrl, holder.mImageView, mDefaultBitmap, 0);
							} catch (OutOfMemoryError e) {
								if (mDefaultBitmap != null && !mDefaultBitmap.isRecycled()) {
									mDefaultBitmap.recycle();
								}
							}
						}
					}
					fillWaitInstallChildView(entity, holder);
				} else if (groupPosition == 2) {
					if (childList.get(groupPosition).size() > childPosition) {
						entity = childList.get(groupPosition).get(childPosition);
						if (entity != null && entity.installedIcon != null) {
							holder.mImageView.setImageBitmap(((BitmapDrawable) entity.installedIcon).getBitmap());
						} else {
							holder.mImageView.setImageBitmap(mDefaultBitmap);
						}
					}
					fillIgnoreChildView(entity, holder);
				}

			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 下载状�?的条目显�?
	 * 
	 * @param entity
	 * @param holder
	 */
	private void fillDownloadChildView(DownloadEntity entity, ChildViewHolder holder) {
		holder.mEmptyTextView.setVisibility(View.GONE);
		holder.mContentLayout.setVisibility(View.VISIBLE);
		holder.mAppNameTextView.setText(entity.appName);
		holder.mAppVersionTextView.setVisibility(View.VISIBLE);
		holder.mAppVersionTextView.setText(entity.versionName);
		String currentDownloadSize = df.format(entity.currentPosition / 1048576.0);
		String currentTotalSize = df.format(entity.fileLength / 1048576.0);
		holder.mBottomTextView.setText(currentDownloadSize + "M/" + currentTotalSize + "M");

		setButtonStatus(entity, holder);

		holder.mContentLayout.setOnClickListener(new DetailOnClickListener(entity));

		if (entity.heavy > 0) {
			holder.mAuthorityImageview.setVisibility(View.VISIBLE);
		} else {
			holder.mAuthorityImageview.setVisibility(View.GONE);
		}

		switch (entity.getStatus()) {
		case STATUS_OF_DOWNLOADING:
		case STATUS_OF_COMPLETE:
		case STATUS_OF_PREPARE:
			holder.mCenterTextView.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.VISIBLE);
			if (entity.fileLength > 0 && entity.currentPosition > 0) {
				double progress = entity.currentPosition * 100.0 / entity.fileLength;
				holder.mProgressBar.setProgress((int) progress);
			}
			break;
		case STATUS_OF_EXCEPTION:
			holder.mCenterTextView.setVisibility(View.VISIBLE);
			holder.mProgressBar.setVisibility(View.GONE);
			holder.mCenterTextView.setText(R.string.download_error_msg1);
			break;
		case STATUS_OF_PAUSE:
		case STATUS_OF_PAUSE_ON_TRAFFIC_LIMIT:
		case STATUS_OF_PAUSE_ON_EXIT_SYSTEM:
			holder.mCenterTextView.setVisibility(View.VISIBLE);
			holder.mProgressBar.setVisibility(View.GONE);
			holder.mCenterTextView.setText(R.string.paused);
			break;
		}
	}

	/**
	 * 更新状�?的条目显�?
	 * 
	 * @param entity
	 * @param holder
	 */
	private void fillUpdateChildView(DownloadEntity entity, ChildViewHolder holder) {
		if (entity == null) {
			holder.mEmptyTextView.setVisibility(View.VISIBLE);
			holder.mContentLayout.setVisibility(View.GONE);
			holder.mEmptyTextView.setText(R.string.current_not_update_app);
			return;
		}

		holder.mEmptyTextView.setVisibility(View.GONE);
		holder.mContentLayout.setVisibility(View.VISIBLE);
		holder.mAppNameTextView.setText(entity.appName);
		holder.mAppVersionTextView.setVisibility(View.INVISIBLE);
		holder.mAppVersionTextView.setText("V" + entity.installedVersionName + "/" + DJMarketUtils.sizeFormat((int) entity.installedFileLength));
		holder.mBottomTextView.setVisibility(View.VISIBLE);

		holder.mBottomTextView.setText(DJMarketUtils.sizeFormat((int) entity.fileLength));

		setButtonStatus(entity, holder);

		if (entity.heavy > 0) {
			holder.mAuthorityImageview.setVisibility(View.VISIBLE);
		} else {
			holder.mAuthorityImageview.setVisibility(View.GONE);
		}

		holder.mContentLayout.setOnClickListener(new DetailOnClickListener(entity));
		switch (entity.getStatus()) {
		case STATUS_OF_DOWNLOADING:
		case STATUS_OF_COMPLETE:
		case STATUS_OF_PREPARE:
			holder.mCenterTextView.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.VISIBLE);
			if (entity.fileLength > 0 && entity.currentPosition >= 0) {
				double progress = entity.currentPosition * 100.0 / entity.fileLength;
				holder.mProgressBar.setProgress((int) progress);
			}
			break;
		case STATUS_OF_INITIAL:
			holder.mCenterTextView.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.GONE);

			break;
		case STATUS_OF_EXCEPTION:
			holder.mCenterTextView.setVisibility(View.VISIBLE);
			holder.mProgressBar.setVisibility(View.GONE);
			holder.mCenterTextView.setText(R.string.update_error_msg1);
			break;
		default:
			holder.mCenterTextView.setVisibility(View.VISIBLE);
			holder.mProgressBar.setVisibility(View.GONE);
			holder.mCenterTextView.setText(R.string.paused);
			break;
		}
	}

	/**
	 * 待安装条目显�?
	 * 
	 * @param entity
	 * @param holder
	 */
	private void fillWaitInstallChildView(DownloadEntity entity, ChildViewHolder holder) {
		if (entity == null) {
			holder.mEmptyTextView.setVisibility(View.VISIBLE);
			holder.mContentLayout.setVisibility(View.GONE);
			holder.mEmptyTextView.setText(R.string.current_not_install_app);
			return;
		}
		holder.mContentLayout.setOnClickListener(new DetailOnClickListener(entity));
		holder.mEmptyTextView.setVisibility(View.GONE);
		holder.mContentLayout.setVisibility(View.VISIBLE);
		holder.mAppNameTextView.setText(entity.appName);
		holder.mAppVersionTextView.setVisibility(View.GONE);
		holder.mCenterTextView.setVisibility(View.GONE);
		holder.mProgressBar.setVisibility(View.GONE);
		holder.mBottomTextView.setVisibility(View.VISIBLE);
//		holder.mBottomTextView.setText("V" + entity.versionName + "/" + DJMarketUtils.sizeFormat((int) entity.fileLength));
		holder.mBottomTextView.setText( DJMarketUtils.sizeFormat((int) entity.fileLength));

		setButtonStatus(entity, holder);

		if (entity.heavy > 0) {
			holder.mAuthorityImageview.setVisibility(View.VISIBLE);
		} else {
			holder.mAuthorityImageview.setVisibility(View.GONE);
		}
	}

	/**
	 * 忽略条目显示
	 * 
	 * @param entity
	 * @param holder
	 */
	private void fillIgnoreChildView(DownloadEntity entity, ChildViewHolder holder) {
		if (entity == null) {
			holder.mEmptyTextView.setVisibility(View.VISIBLE);
			holder.mContentLayout.setVisibility(View.GONE);
//			holder.mEmptyTextView.setText(R.string.current_not_ignore_app);
			return;
		}

		holder.mAuthorityImageview.setVisibility(View.GONE);
		holder.mAppNameTextView.setText(entity.appName);
		holder.mEmptyTextView.setVisibility(View.GONE);
		holder.mContentLayout.setVisibility(View.VISIBLE);
		holder.mCenterTextView.setVisibility(View.GONE);

		holder.mAppVersionTextView.setVisibility(View.GONE);

		holder.mBottomTextView.setText("V" + entity.installedVersionName + "/" + DJMarketUtils.formatString(entity.installedFileLength / 1048576.0) + "M");
		holder.mProgressBar.setVisibility(View.GONE);

		setButtonStatus(entity, holder);

		holder.mContentLayout.setOnClickListener(new DetailOnClickListener(entity));

	}

	/**
	 * 设置 button 状�?
	 * 
	 * @param entity
	 * @param holder
	 */
	private void setButtonStatus(DownloadEntity entity, ChildViewHolder holder) {
		if (entity.downloadType == TYPE_OF_DOWNLOAD) {
			holder.mLongButton.setVisibility(View.GONE);
			switch (entity.getStatus()) {
			case STATUS_OF_PREPARE:
			case STATUS_OF_DOWNLOADING:
				holder.mFirstButton.setVisibility(View.GONE);
				setFirstButtonStyle(holder.mSecondButton, pauseString);
				holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_EXCEPTION:
			case STATUS_OF_PAUSE:
			case STATUS_OF_PAUSE_ON_EXIT_SYSTEM:
				setSecondButtonStyle(holder.mFirstButton, continueString);
				setFirstButtonStyle(holder.mSecondButton, cancelString);
				holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_COMPLETE:
				holder.mFirstButton.setVisibility(View.GONE);
				setFirstButtonStyle(holder.mSecondButton, pauseString);
				holder.mSecondButton.setEnabled(false);
				break;
			default:
				setFirstButtonStyle(holder.mFirstButton, continueString);
				setSecondButtonStyle(holder.mSecondButton, cancelString);
				break;
			}
		} else if (entity.downloadType == TYPE_OF_UPDATE) {
			switch (entity.getStatus()) {
			case STATUS_OF_IGNORE:
				holder.mLongButton.setVisibility(View.VISIBLE);
				holder.mLongButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_INITIAL:
				holder.mLongButton.setVisibility(View.GONE);
				setFirstButtonStyle(holder.mSecondButton, updateString);
				setSecondButtonStyle(holder.mFirstButton, ignoreString);
				holder.mFirstButton.setVisibility(View.GONE);// 屏蔽  忽略按钮
				holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_DOWNLOADING:
			case STATUS_OF_PREPARE:
				holder.mLongButton.setVisibility(View.GONE);
				setSecondButtonStyle(holder.mFirstButton, pauseString);
				setFirstButtonStyle(holder.mSecondButton, cancelString);
				holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_EXCEPTION:
			case STATUS_OF_PAUSE:
				holder.mLongButton.setVisibility(View.GONE);
				setSecondButtonStyle(holder.mFirstButton, continueString);
				setFirstButtonStyle(holder.mSecondButton, cancelString);
				holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
				break;
			case STATUS_OF_COMPLETE:
				holder.mLongButton.setVisibility(View.GONE);
				setSecondButtonStyle(holder.mFirstButton, pauseString);
				setFirstButtonStyle(holder.mSecondButton, cancelString);
				holder.mFirstButton.setEnabled(false);
				holder.mSecondButton.setEnabled(false);
				break;
			}
		} else if (entity.downloadType == TYPE_OF_COMPLETE) {
			holder.mLongButton.setVisibility(View.GONE);
			holder.mSecondButton.setVisibility(View.VISIBLE);
			holder.mFirstButton.setVisibility(View.VISIBLE);
			setCommonButtonStyle(holder.mFirstButton, installString);
			setSecondButtonStyle(holder.mSecondButton, deleteString);
			holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
			holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
			for (int i = 0; i < rootApkList.size(); i++) {
				if (null != entity.packageName && rootApkList.get(i).equals(entity.packageName)) {
					holder.mFirstButton.setFocusable(false);
					holder.mFirstButton.setFocusableInTouchMode(false);
					holder.mFirstButton.setEnabled(false);
					holder.mFirstButton.setClickable(false);
					holder.mSecondButton.setFocusable(false);
					holder.mSecondButton.setFocusableInTouchMode(false);
					holder.mSecondButton.setEnabled(false);
					holder.mSecondButton.setClickable(false);
					holder.mFirstButton.setText(R.string.installing);
				}
			}
		} else if (entity.downloadType == TYPE_OF_IGNORE) {
//			setSecondButtonStyle(holder.mLongButton, context.getString(R.string.cancle_ignore));
			holder.mFirstButton.setVisibility(View.GONE);
			holder.mSecondButton.setVisibility(View.GONE);
			holder.mLongButton.setVisibility(View.VISIBLE);
			holder.mLongButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
		}
	}

	/**
	 * 设置 button 样式(黄色)
	 * 
	 * @param mButton
	 * @param text
	 */
	private void setFirstButtonStyle(Button mButton, String text) {
		mButton.setEnabled(true);
		mButton.setVisibility(View.VISIBLE);
//		mButton.setBackgroundResource(R.drawable.update_selector);
		mButton.setTextColor(Color.parseColor("#0e567d"));
		mButton.setText(text);
	}

	/**
	 * 设置 button 样式(蓝色)
	 * 
	 * @param mButton
	 * @param text
	 */
	private void setSecondButtonStyle(Button mButton, String text) {
		mButton.setEnabled(true);
		mButton.setVisibility(View.VISIBLE);
//		mButton.setBackgroundResource(R.drawable.cancel_selector);
		mButton.setTextColor(Color.parseColor("#7f5100"));
		mButton.setText(text);
	}

	/**
	 * 设置 button 样式(白色)
	 * 
	 * @param mButton
	 * @param text
	 */
	private void setCommonButtonStyle(Button mButton, String text) {
		mButton.setEnabled(true);
		mButton.setVisibility(View.VISIBLE);
		mButton.setText(text);
		mButton.setBackgroundResource(R.drawable.install_button_selector);
		mButton.setTextColor(Color.BLACK);
	}

	private class OnDownloadClickListener implements View.OnClickListener {
		private DownloadEntity entity;
		private ChildViewHolder holder;

		OnDownloadClickListener(DownloadEntity entity, ChildViewHolder holder) {
			this.entity = entity;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
			String text = ((TextView) v).getText().toString();
			if (entity.downloadType == TYPE_OF_DOWNLOAD) {
				if (pauseString.equals(text)) {
					setFirstButtonStyle(holder.mSecondButton, cancelString);

					setSecondButtonStyle(holder.mFirstButton, continueString);

					holder.mFirstButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
					holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));

					holder.mProgressBar.setVisibility(View.GONE);
					holder.mCenterTextView.setText(R.string.paused);

					entity.setStatus(STATUS_OF_PAUSE);

				} else if (continueString.equals(text)) {
					if (entity.canDownload()) {
						holder.mFirstButton.setVisibility(View.GONE);

						setFirstButtonStyle(holder.mSecondButton, pauseString);
						holder.mSecondButton.setOnClickListener(new OnDownloadClickListener(entity, holder));
						holder.mCenterTextView.setVisibility(View.GONE);
						holder.mProgressBar.setVisibility(View.VISIBLE);
						if (entity.fileLength > 0 && entity.currentPosition > 0) {
							double progress = entity.currentPosition * 100.0 / entity.fileLength;
							holder.mProgressBar.setProgress((int) progress);
						} else {
							holder.mProgressBar.setProgress(0);
						}
						String currentDownloadSize = df.format(entity.currentPosition / 1048576.0);
						String currentTotalSize = df.format(entity.fileLength / 1048576.0);
						holder.mBottomTextView.setText(currentDownloadSize + "M/" + currentTotalSize + "M");

						DownloadUtils.checkDownload(context, entity);
					}
				} else if (cancelString.equals(text)) {
					cancelDownloadEntity(entity);
//					DownloadUtils.fillDownloadNotifycation(context, false);

					Bundle bundle = new Bundle();
					bundle.putParcelable(DOWNLOAD_ENTITY, entity);
					sendServiceBroadcastByAction(BROADCAST_ACTION_CANCEL_DOWNLOAD, bundle);
				}
			} else if (entity.downloadType == TYPE_OF_UPDATE) {
				// 当按钮为更新时，点击�?��下载此应用更�?
				if (updateString.equals(text)) {
					setSecondButtonStyle(holder.mFirstButton, pauseString);
					setFirstButtonStyle(holder.mSecondButton, cancelString);
					holder.mProgressBar.setVisibility(View.VISIBLE);

					if (entity.fileLength > 0 && entity.currentPosition > 0) {
						double progress = entity.currentPosition * 100.0 / entity.fileLength;
						holder.mProgressBar.setProgress((int) progress);
					} else {
						holder.mProgressBar.setProgress(0);
					}

					DownloadUtils.checkDownload(context, entity);
				} else if (ignoreString.equals(text)) { // 当按钮为忽略�?
					
					updateToIgnore(entity);
				} else if (pauseString.equals(text)) { // 当按钮为暂停状�?时，点击�?��停此下载
					setSecondButtonStyle(holder.mFirstButton, continueString);
					holder.mProgressBar.setVisibility(View.GONE);
					holder.mCenterTextView.setText(R.string.paused);

					entity.setStatus(STATUS_OF_PAUSE);

				} else if (cancelString.equals(text)) { // 当按钮为取消状�?时，点击�?��取消此次更新操作，其它相关状态还�?
					setFirstButtonStyle(holder.mSecondButton, updateString);
					setSecondButtonStyle(holder.mFirstButton, ignoreString);
					holder.mProgressBar.setVisibility(View.GONE);

//					DownloadUtils.fillUpdateAndUpdatingNotifycation(context, false);
					System.out.println("cancelString entity status" + entity.downloadType);
					Bundle bundle = new Bundle();
					bundle.putParcelable(DOWNLOAD_ENTITY, entity);
					sendServiceBroadcastByAction(BROADCAST_ACTION_CANCEL_DOWNLOAD, bundle);
				} else if (continueString.equals(text)) { // 当按钮为继续状�?�?
					setFirstButtonStyle(holder.mSecondButton, cancelString);
					setSecondButtonStyle(holder.mFirstButton, pauseString);
					holder.mCenterTextView.setVisibility(View.GONE);
					holder.mProgressBar.setVisibility(View.VISIBLE);
					if (entity.fileLength > 0 && entity.currentPosition > 0) {
						double progress = entity.currentPosition * 100.0 / entity.fileLength;
						holder.mProgressBar.setProgress((int) progress);
					}

					DownloadUtils.checkDownload(context, entity);
				}
			} else if (entity.downloadType == TYPE_OF_COMPLETE) {
				if (installString.equals(text)) {
					String path = DOWNLOAD_ROOT_PATH + entity.hashCode() + DOWNLOAD_FILE_POST_SUFFIX;
					System.out.println("installApk path" + path);
					File file = new File(path);
					if (file.exists()) {
						DownloadUtils.installApk(context, path);
					} else {
//						Toast.makeText(context, R.string.download_file_delete_prompt, Toast.LENGTH_SHORT).show();
						deleteWaitInstallApk(entity);
						Bundle bundle = new Bundle();
						bundle.putParcelable(DOWNLOAD_ENTITY, entity);
						sendServiceBroadcastByAction(BROADCAST_ACTION_REMOVE_DOWNLOAD, bundle);
					}
				} else if (deleteString.equals(text)) {
					String path = DOWNLOAD_ROOT_PATH + entity.hashCode() + DOWNLOAD_FILE_POST_SUFFIX;
					boolean flag = DownloadUtils.deleteDownloadFile(path);
					if (flag) {
						deleteWaitInstallApk(entity);
						Bundle bundle = new Bundle();
						bundle.putParcelable(DOWNLOAD_ENTITY, entity);
						sendServiceBroadcastByAction(BROADCAST_ACTION_REMOVE_DOWNLOAD, bundle);
					}
				}
			} else if (entity.downloadType == TYPE_OF_IGNORE) {
				deleteIgnore(entity);
				Bundle bundle = new Bundle();
				bundle.putParcelable(DOWNLOAD_ENTITY, entity);
				sendServiceBroadcastByAction(BROADCAST_ACTION_CANCEL_IGNORE, bundle);
			}
		}
	}

	private class DetailOnClickListener implements OnClickListener {
		private DownloadEntity entity;

		DetailOnClickListener(DownloadEntity entity) {
			this.entity = entity;
		}

		@Override
		public void onClick(View v) {
			if (entity != null) {
				if (entity.downloadType != TYPE_OF_IGNORE) {
//					Intent intent = new Intent(context, ApkDetailActivity.class);
//					Bundle bundle = new Bundle();
//					ApkItem item = new ApkItem();
//					item.appId = entity.appId;
//					item.category = entity.category;
//					item.appName = entity.appName;
//					item.apkUrl = entity.url;
//					item.appIconUrl = entity.iconUrl;
//					bundle.putParcelable("apkItem", item);
//					intent.putExtras(bundle);
//					context.startActivity(intent);
				}
			} else {
//				DJMarketUtils.showToast(context, R.string.not_found_soft_detail);
			}
		}
	}

	/**
	 * 将更新列表应用移至忽�?
	 */
	private void updateToIgnore(DownloadEntity entity) {
		int position = 0;

		removeMessage();

		if (childList.size() > 3) {
			position = 1;
		}
		for (int i = 0; i < childList.get(position).size(); i++) {
			DownloadEntity d = childList.get(position).get(i);
			if (d.packageName.equals(entity.packageName) && d.versionCode == entity.versionCode) {
				if (d.downloadType == TYPE_OF_UPDATE) {
					childList.get(position).remove(i);
					d.downloadType = TYPE_OF_IGNORE;
					childList.get(childList.size() - 1).add(d);
					DownloadUtils.fillUpdateNotifycation(context);
				}
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 移除掉已忽略应用
	 * 
	 * @param entity
	 */
	private void deleteIgnore(DownloadEntity entity) {
		removeMessage();
		int position = childList.size() - 1;
		for (int i = 0; i < childList.get(position).size(); i++) {
			DownloadEntity d = childList.get(position).get(i);
			if (d.packageName.equals(entity.packageName) && d.versionCode == entity.versionCode) {
				childList.get(position).remove(i);
				break;
			}
		}
		sendMessage();
	}

	/**
	 * 取消下载
	 * 
	 * @param entity
	 * @return
	 */
	private boolean cancelDownloadEntity(DownloadEntity entity) {
		System.out.println("cancelDownloadEntity----entity="+entity.appId+entity.appName+entity.downloadType);
		removeMessage();
		if(childList.size() > 0){
			for (int i = 0; i < childList.get(0).size(); i++) {
				DownloadEntity d = childList.get(0).get(i);
	//			if (d.appId == entity.appId && d.category == entity.category) {
				if (d.appId == entity.appId) {
					childList.get(0).remove(i);
					if (childList.get(0).size() == 0) {
						childList.remove(0);
						groupList.remove(0);
					}
					break;
				}
			}
		}
		sendMessage();
		return false;
	}

	/**
	 * 发�?广播
	 * 
	 * @param action
	 * @param bundle
	 */
	private void sendServiceBroadcastByAction(String action, Bundle bundle) {
		Intent intent = new Intent(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.sendBroadcast(intent);
	}

	private static final class GroupViewHolder {
		TextView mTextView;
		Button mButton;
		ImageView mImageView;
	}

	private static final class ChildViewHolder {
		ImageView mImageView; // 应用icon
		TextView mAppNameTextView; // 应用名称
		TextView mAppVersionTextView; // 应用版本名称
		TextView mCenterTextView; // 下载指示(暂停�?
		TextView mBottomTextView; // 底部文字
		TextView mEmptyTextView; // 当没有可更新应用和安装应用时显示�?
		ProgressBar mProgressBar; // 下载进度�?
		Button mFirstButton, mSecondButton, mLongButton;
		View mContentLayout;
		ImageView mAuthorityImageview;
	}

	@Override
	public void onUpdateListDone(List<DownloadEntity> list) {
		removeMessage();
		int position = 0;
		if (childList.size() > 0) {
			position = 1;
		}
		childList.get(position).addAll(list);
		sendMessage();
	}

	@Override
	public void onRemoveDownload(DownloadEntity entity) {
		removeMessage();
		cancelDownloadEntity(entity);
		sendMessage();
	}

	public void refreshAdapter() {
		notifyDataSetChanged();
	}
}
