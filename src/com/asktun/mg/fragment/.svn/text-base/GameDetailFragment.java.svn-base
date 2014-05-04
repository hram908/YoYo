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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.activity.GameDetailActivity;
import com.asktun.mg.activity.ImageBrowser2Activity;
import com.asktun.mg.activity.NearPersonActivity;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.GamesDetailBean;
import com.asktun.mg.bean.GamesDetailBean.FriendData;
import com.asktun.mg.bean.GamesDetailBean.GameDetail;
import com.asktun.mg.bean.GamesDetailBean.OtherData;
import com.asktun.mg.ecogallery.EcoGallery;
import com.asktun.mg.ecogallery.EcoGalleryAdapterView;
import com.asktun.mg.ecogallery.EcoGalleryAdapterView.OnItemClickListener;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.ApkItem;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadUtils;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.StarLayout;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 游戏详情
 * 
 * @Description
 * @author Dean
 * 
 */
public class GameDetailFragment extends BaseFragment implements AConstDefine {

	private View view;
	@ViewInject(id = R.id.frag_game_detail_gridview)
	private GridView gridView;
	@ViewInject(id = R.id.frag_game_detail_gridview2)
	private GridView gridView2;
	@ViewInject(id = R.id.frag_game_detail_gallery)
	private EcoGallery gallery;
	@ViewInject(id = R.id.frag_game_detail_image)
	private ImageView img;
	@ViewInject(id = R.id.frag_game_detail_starLayout)
	private StarLayout starLayout;
	@ViewInject(id = R.id.frag_game_detail_gametype)
	private TextView typeTV;
	@ViewInject(id = R.id.frag_game_detail_size)
	private TextView sizeTV;
	@ViewInject(id = R.id.frag_game_detail_time)
	private TextView tiemTV;
	@ViewInject(id = R.id.frag_game_detail_downloadCount)
	private TextView downCountTV;
	@ViewInject(id = R.id.frag_game_detail_award)
	private TextView bonusTV;
	@ViewInject(id = R.id.frag_game_detail_describe)
	private TextView desTV;
	@ViewInject(id = R.id.scrollView)
	private ScrollView scrollView;
	@ViewInject(id = R.id.btn_download)
	private Button downloadBtn;

	private FriendAdapter friendAdapter;
	private OtherAdapter otherAdapter;
	private ImageAdapter adapter_gallery;

	private String gameId = null;

	private GameDetail gameDetail;
	private List<FriendData> fList = new ArrayList<FriendData>();
	private List<OtherData> oList = new ArrayList<OtherData>();

	private String[] introImgUrl = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		gameId = ((GameDetailActivity) activity).getGameId();
		// gi = ((GameDetailActivity) activity).getGameInfo();
		System.out.println("game detail fragment````game Id" + gameId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = mInflater.inflate(R.layout.fragment_game_detail, null);
			FinalActivity.initInjectedView(this, view);
			act.showLoadingView();
			init();
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	private void init() {
		getGameInfo();
		friendAdapter = new FriendAdapter();
		otherAdapter = new OtherAdapter();
		adapter_gallery = new ImageAdapter();
		scrollView.post(new Runnable() {

			@Override
			public void run() {
				scrollView.scrollTo(0, 0);
			}
		});

	}

	private void setData() {
		if (gameDetail.getGame_ico() != null) {
			imageLoader.displayImage(gameDetail.getGame_ico(), img,
					options_round);
		}
		if (!"null".equals(gameDetail.getScore())
				&& !StrUtil.isEmpty(gameDetail.getScore()))
			starLayout.setCount(Integer.parseInt(gameDetail.getScore()));
		if (("1").equals(gameDetail.getIsonline())) {
			typeTV.setText("网络游戏");
		} else if (("2").equals(gameDetail.getIsonline())) {
			typeTV.setText("单机游戏");
		}
		if (!StrUtil.isEmpty(gameDetail.getSize()))
			sizeTV.setText(gameDetail.getSize() + "M");
		tiemTV.setText(gameDetail.getAdd_time());
		downCountTV.setText("下载次数:" + gameDetail.getDown_times());
		bonusTV.setText("下载奖励：" + gameDetail.getPoint() + "积分");
		desTV.setText(gameDetail.getDescription());

		String imgDir = gameDetail.getImg_dir();
		if (!StrUtil.isEmpty(imgDir))
			introImgUrl = imgDir.split(",");

		GameInfo gi;
		gi = new GameInfo();
		gi.setGame_id(gameId);
		gi.setGame_name(gameDetail.getGame_name());
		gi.setUrl(gameDetail.getUrl());
		gi.setSize(gameDetail.getSize());
		gi = setApkStatus(gi);
		// downloadBtn.setOnClickListener(new DownloadClick(gi,
		// downloadBtn, -1));
		final ApkItem apkItem = new ApkItem();
		apkItem.appId = Integer.parseInt(gameId);
		apkItem.apkSize = gameDetail.getSize();
		apkItem.apkUrl = gameDetail.getUrl();
		apkItem.appIconUrl = gameDetail.getGame_ico();
		apkItem.appName = gameDetail.getGame_name();
		apkItem.status = gi.getStatus();
		displayApkStatus(downloadBtn, apkItem.status);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (apkItem.status == STATUS_APK_UNINSTALL
						|| apkItem.status == STATUS_APK_UNUPDATE) {// 下载
					DownloadUtils.checkDownload(act, apkItem);
					apkItem.status = STATUS_APK_INSTALL;
				} else {// 取消下载
					Intent intent = new Intent(
							AConstDefine.BROADCAST_ACTION_CANCEL_DOWNLOAD);
					DownloadEntity entity = new DownloadEntity(apkItem);
					Bundle bundle = new Bundle();
					bundle.putParcelable(AConstDefine.DOWNLOAD_ENTITY, entity);
					intent.putExtras(bundle);
					act.sendBroadcast(intent);
					// if (entity.downloadType == AConstDefine.TYPE_OF_DOWNLOAD)
					// {
					DownloadUtils.fillDownloadNotifycation(act, false);
					// } else if (entity.downloadType ==
					// AConstDefine.TYPE_OF_UPDATE) {
					// DownloadUtils.fillUpdateAndUpdatingNotifycation(act,
					// false);
					// }
					apkItem.status = STATUS_APK_UNINSTALL;
				}
				displayApkStatus(downloadBtn, apkItem.status);
			}
		});

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

	private int cHeight;
	private int space10;

	private void setAdapterData() {
		cHeight = Util.dpToPx(getResources(), 46);
		space10 = Util.dpToPx(getResources(), 10);
		if (fList != null) {
			int ii = fList.size();
			LayoutParams params = new LayoutParams(ii * (cHeight + space10)
					+ space10, (int) (cHeight + 3 * space10));
			gridView.setLayoutParams(params);
			gridView.setColumnWidth(cHeight);
			gridView.setHorizontalSpacing(space10);
			gridView.setStretchMode(GridView.NO_STRETCH);
			gridView.setNumColumns(ii);
			gridView.setAdapter(friendAdapter);
		}
		if (oList != null) {
			int ii2 = oList.size();
			LayoutParams params2 = new LayoutParams(ii2 * (cHeight + space10)
					+ space10, (int) (cHeight + 3 * space10));
			gridView2.setLayoutParams(params2);
			gridView2.setColumnWidth(cHeight);
			gridView2.setHorizontalSpacing(space10);
			gridView2.setStretchMode(GridView.NO_STRETCH);
			gridView2.setNumColumns(ii2);
			gridView2.setAdapter(otherAdapter);
		}

		gallery.setAdapter(adapter_gallery);
		if (adapter_gallery.getCount() > 1)
			gallery.setSelection(1);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(EcoGalleryAdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < introImgUrl.length; i++) {
					list.add(introImgUrl[i]);
				}
				Intent intent = new Intent(act, ImageBrowser2Activity.class);
				intent.putStringArrayListExtra("list", list);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});

	}

	private class FriendAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return fList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return fList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_groupmember,
						null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final FriendData fd = fList.get(position);
			if (fd != null) {
				imageLoader.displayImage(fd.getUser_ico(), holder.iv, options);
				holder.tv_name.setText(fd.getUser_name());
				holder.iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(act,
								NearPersonActivity.class);
						intent.putExtra("isFriend", true);
						intent.putExtra("userId", fd.getUser_id());
						startActivity(intent);
					}
				});
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
		}
	}

	private class OtherAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return oList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return oList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_nearplay, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				LayoutParams param = new LayoutParams(cHeight, cHeight);
				holder.iv.setScaleType(ScaleType.FIT_XY);
				holder.iv.setLayoutParams(param);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final OtherData od = oList.get(position);
			if (od != null) {
				imageLoader.displayImage(od.getGame_ico(), holder.iv,
						options_round10);
				holder.tv_name.setText(od.getGame_name());
				// holder.tv_score.setVisibility(View.VISIBLE);
				holder.iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(act,
								GameDetailActivity.class);
						intent.putExtra("gameId", od.getGame_id());
						intent.putExtra("gameName", od.getGame_name());
						startActivity(intent);
					}
				});
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
		}
	}

	private class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (introImgUrl == null)
				return 0;
			else
				return introImgUrl.length;
		}

		private int width;
		private int height;

		public ImageAdapter() {
			width = (diaplayWidth - 20) / 3;
			height = width * 4 / 3;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ImageView imageView = new ImageView(act);
			EcoGallery.LayoutParams param = new EcoGallery.LayoutParams(width,
					height);
			imageView.setLayoutParams(param);
			imageView.setScaleType(ScaleType.FIT_XY);
			if (introImgUrl != null && position < introImgUrl.length) {
				imageLoader.displayImage(introImgUrl[position], imageView,
						options);
				// imageView.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// ArrayList<String> list = new ArrayList<String>();
				// for (int i = 0; i < introImgUrl.length; i++) {
				// list.add(introImgUrl[i]);
				// }
				// Intent intent = new Intent(act,
				// ImageBrowser2Activity.class);
				// intent.putStringArrayListExtra("list", list);
				// intent.putExtra("position", position);
				// startActivity(intent);
				// }
				// });
			}
			return imageView;
		}
	}

	public void getGameInfo() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("game_id", gameId);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(GamesDetailBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GamesDetailBean data = (GamesDetailBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								gameDetail = data.getData();
								fList = data.getFriendData();
								oList = data.getOtherData();
								setData();
								friendAdapter.notifyDataSetChanged();
								otherAdapter.notifyDataSetChanged();
								adapter_gallery.notifyDataSetChanged();
								setAdapterData();
								act.hideLoadingView();
							} else {
								// showToast("无法获取信息");
								act.setErrorMessage();
							}
						} else {
							// showToast("无法获取信息");
							act.setErrorMessage();
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						// showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// showToast("无法获取信息");
						act.setErrorMessage();
					}
				});
	}

}
