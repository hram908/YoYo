package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.ApplyFriend;
import com.asktun.mg.bean.DeleteFriend;
import com.asktun.mg.bean.HostInfo;
import com.asktun.mg.bean.HostInfo.GameItem;
import com.asktun.mg.bean.HostInfo.HostInfoItem;
import com.asktun.mg.bean.UserInfoBean;
import com.asktun.mg.bean.UserInfoBean.LifePhotoResult;
import com.asktun.mg.bean.UserInfoBean.UserInfo;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 附近人详情
 * 
 * @author Dean
 */
public class NearPersonActivity extends BaseActivity {

	@ViewInject(id = R.id.gridview)
	private GridView gridView_play;
	@ViewInject(id = R.id.gridview_love)
	private GridView gridView_love;
	@ViewInject(id = R.id.gridview_ico)
	private GridView gridView_ico;
	@ViewInject(id = R.id.gridview_game)
	private GridView gridView_game;
	@ViewInject(id = R.id.ll_more)
	private LinearLayout ll_more;
	@ViewInject(id = R.id.btn_add, click = "onClick")
	private Button btn_add;
	@ViewInject(id = R.id.btn_delete, click = "onClick")
	private Button btn_delete;
	@ViewInject(id = R.id.btn_send, click = "onClick")
	private Button btn_send;
	@ViewInject(id = R.id.btn_apply, click = "onClick")
	private Button btn_apply;

	@ViewInject(id = R.id.ll_add)
	private LinearLayout ll_add;
	@ViewInject(id = R.id.ll_delete)
	private LinearLayout ll_delete;
	@ViewInject(id = R.id.ll_send)
	private LinearLayout ll_send;
	@ViewInject(id = R.id.ll_apply)
	private LinearLayout ll_apply;

	@ViewInject(id = R.id.tv_relationship)
	private TextView tv_relationship;
	@ViewInject(id = R.id.tv_number)
	private TextView tv_number;
	@ViewInject(id = R.id.tv_age)
	private TextView tv_age;
	@ViewInject(id = R.id.tv_sign)
	private TextView tv_sign;
	@ViewInject(id = R.id.tv_address)
	private TextView tv_address;
	@ViewInject(id = R.id.tv_job)
	private TextView tv_job;
	@ViewInject(id = R.id.tv_school)
	private TextView tv_school;
	@ViewInject(id = R.id.tv_interesting)
	private TextView tv_interesting;
	@ViewInject(id = R.id.tv_distance)
	private TextView tv_distance;
	@ViewInject(id = R.id.tv_time)
	private TextView tv_time;
	@ViewInject(id = R.id.tv_xingzuo)
	private TextView tv_xingzuo;
	@ViewInject(id = R.id.tv_purpose)
	private TextView tv_purpose;
	@ViewInject(id = R.id.iv_purpose)
	private TextView iv_purpose;

	private GvAdapter adapter_play;
	private GvAdapter adapter_love;
	private GameAdapter adapter_game;
	private IcoAdapter adapter_ico;

	private boolean isHost; // 是否群主
	private String hostId;
	private String userId; // 查看别人信息用
	// private String groupId;
	private boolean isFriend;

	private String name;

	private List<LifePhotoResult> lifePhotoResult = new ArrayList<LifePhotoResult>();
	private List<LifePhotoResult> gamePhotoResult = new ArrayList<LifePhotoResult>();

	private int cHeight;
	private int height_game;
	private int space10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_nearperson);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		showLoadingView();
		init();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_send:
			Intent intent = new Intent(NearPersonActivity.this,
					ChatActivity.class);
			intent.putExtra("name", name);
			if (!StrUtil.isEmpty(hostId))
				intent.putExtra("friendId", hostId);
			else
				intent.putExtra("friendId", userId);
			startActivity(intent);
			break;
		case R.id.btn_add:
			applyFriend();
			break;
		case R.id.btn_apply:

			break;
		case R.id.btn_delete:
			dialogOpen("提醒", "确认删除好友吗", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					deleteFriend();
				}
			});
			break;
		default:
			break;
		}
	}

	private void init() {

		adapter_play = new GvAdapter();
		adapter_love = new GvAdapter();
		adapter_game = new GameAdapter();
		adapter_ico = new IcoAdapter();
		gridView_ico.setAdapter(adapter_ico);
		gridView_ico.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ArrayList<String> list = new ArrayList<String>();
				if (lifePhotoResult.size() > 0) {
					for (LifePhotoResult item : lifePhotoResult) {
						list.add(item.photo);
					}
				}
				Intent intent = new Intent(NearPersonActivity.this,
						ImageBrowser2Activity.class);
				intent.putStringArrayListExtra("list", list);
				intent.putExtra("position", arg2);
				startActivity(intent);
			}
		});

		isFriend = getIntent().getBooleanExtra("isFriend", false);
		isHost = getIntent().getBooleanExtra("isHost", false);
		hostId = getIntent().getStringExtra("hostId");
		userId = getIntent().getStringExtra("userId");
		ischeck = getIntent().getStringExtra("ischeck");
		ll_add.setVisibility(View.VISIBLE);

		if (isHost) {
			loadHostInfo();
			return;
		}
		loadUserInfo();
	}

	private class GvAdapter extends BaseAdapter {

		private List<GameItem> list = new ArrayList<HostInfo.GameItem>();

		public void addAll(List<GameItem> data) {
			list.addAll(data);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
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
			final GameItem item = list.get(arg0);
			holder.tv_name.setText(item.game_name);
			String image = item.game_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options_round10);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(NearPersonActivity.this,
							GameDetailActivity.class);
					intent.putExtra("gameId", item.gt_id);
					if (StrUtil.isEmpty(item.game_id)) {
						intent.putExtra("gameId", item.gt_id);
						return;
					} else {
						intent.putExtra("gameId", item.game_id);
					}
					intent.putExtra("gameName", item.game_name);
					startActivity(intent);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
		}

	}

	private class IcoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lifePhotoResult.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_image, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LifePhotoResult item = lifePhotoResult.get(arg0);
			String image = item.photo;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
		}

	}

	private class GameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gamePhotoResult.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_gameimage, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LifePhotoResult item = gamePhotoResult.get(arg0);
			String image = item.photo;
			if (!StrUtil.isEmpty(image)) {

				imageLoader.displayImage(image, holder.iv, options_round);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
		}

	}

	/**
	 * 加载群主信息
	 */
	private void loadHostInfo() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("host_id", hostId);
		UIDataProcess.reqData(HostInfo.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						HostInfo data = (HostInfo) arg0;
						if (data.flg == 1) {
							HostInfoItem item = data.hostinfo;
							if (null != data.data) {
								adapter_play.addAll(data.data);
								adapter_play.notifyDataSetChanged();
							}
							if (data.userLifePhotos != null) {
								lifePhotoResult.addAll(data.userLifePhotos);
								adapter_ico.notifyDataSetChanged();
							}
							if (data.userGamePhotos != null) {
								gamePhotoResult.addAll(data.userGamePhotos);
								adapter_game.notifyDataSetChanged();
							}
							setGvData();
							name = item.user_name;
							setTitleText(item.user_name);
							tv_number.setText(item.user_id);
							tv_age.setText(item.user_age);
							if (item.user_gender.equals("2")) {
								tv_age.setBackgroundResource(R.drawable.sex_nv);
								// tv_age.setTextColor(getResources().getColor(
								// R.color.color_nv));
							}
							tv_sign.setText(item.signature);
							tv_address.setText(item.user_address);
							tv_job.setText(item.job);
							tv_interesting.setText(item.interest);
							tv_distance.setText(item.distance + "m");
							tv_xingzuo.setText(item.user_constellate);
							tv_time.setText(Util
									.friendly_time(item.last_active_time));
							tv_purpose.setText("寻找" + item.purpose + "中");
							Util.getPurposeIcon(item.purpose, iv_purpose);
							if ("0".equals(data.isfriend)) {
								tv_relationship.setText("陌生人");
							} else if ("1".equals(data.isfriend)) {
								tv_relationship.setText("好友");
								isFriend = true;
							} else {
								tv_relationship.setText("陌生人");
							}
							// String imageurl = item.user_ico;
							// if (!StrUtil.isEmpty(imageurl)) {
							// imageLoader.displayImage(imageurl, image);
							// }
							if (isFriend) {
								ll_add.setVisibility(View.GONE);
								ll_delete.setVisibility(View.VISIBLE);
								ll_more.setVisibility(View.VISIBLE);
							}

							IcoAdapter icoAdapter = new IcoAdapter();
							gridView_ico.setAdapter(icoAdapter);

						} else {
							showToast("获取数据失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("获取数据失败");
					}
				});
	}

	private void setGvData() {
		DisplayMetrics dm = Util.getScreenSize(this);
		int num = Util.dpToPx(getResources(), 10);
		cHeight = Util.dpToPx(getResources(), 46);
		height_game = (int) ((dm.widthPixels - num * 5) / 4.0);
		space10 = Util.dpToPx(getResources(), 10);

		int ii = adapter_play.getCount();
		LayoutParams params = new LayoutParams(ii * (cHeight + space10),
				(int) (cHeight + 2.4 * space10));
		gridView_play.setLayoutParams(params);
		gridView_play.setColumnWidth(cHeight);
		gridView_play.setHorizontalSpacing(space10);
		gridView_play.setStretchMode(GridView.NO_STRETCH);
		gridView_play.setNumColumns(ii);
		gridView_play.setAdapter(adapter_play);

		int ii_love = adapter_love.getCount();
		LayoutParams params_love = new LayoutParams(ii_love
				* (cHeight + space10), (int) (cHeight + 2.4 * space10));
		gridView_love.setLayoutParams(params_love);
		gridView_love.setColumnWidth(cHeight);
		gridView_love.setHorizontalSpacing(space10);
		gridView_love.setStretchMode(GridView.NO_STRETCH);
		gridView_love.setNumColumns(ii_love);
		gridView_love.setAdapter(adapter_love);

		int ii2 = adapter_game.getCount();
		LayoutParams params2 = new LayoutParams(ii2 * (height_game + space10),
				height_game + space10);
		gridView_game.setLayoutParams(params2);
		gridView_game.setColumnWidth(height_game);
		gridView_game.setHorizontalSpacing(space10);
		gridView_game.setStretchMode(GridView.NO_STRETCH);
		gridView_game.setNumColumns(ii2);
		gridView_game.setAdapter(adapter_game);

		gridView_game.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ArrayList<String> list = new ArrayList<String>();
				if (gamePhotoResult.size() > 0) {
					for (LifePhotoResult item : gamePhotoResult) {
						list.add(item.photo);
					}
				}
				Intent intent = new Intent(NearPersonActivity.this,
						ImageBrowser2Activity.class);
				intent.putStringArrayListExtra("list", list);
				intent.putExtra("position", arg2);
				startActivity(intent);
			}
		});

	}

	@Override
	public void RequestData() {
		// TODO Auto-generated method stub
		super.RequestData();
		loadUserInfo();
	}

	private String ischeck;

	private void loadUserInfo() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("other_id", userId);
		if (!StrUtil.isEmpty(ischeck))
			params.put("is_check", ischeck);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("lati", mApplication.mLocation.getLatitude());
		params.put("long", mApplication.mLocation.getLongitude());
		params.put("distance", 2000);
		UIDataProcess.reqData(UserInfoBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						UserInfoBean data = (UserInfoBean) arg0;
						if (data.getFlg() == 1) {
							UserInfo item = data.getData();
							if (data.recentPlay != null) {
								adapter_play.addAll(data.recentPlay);
							}
							if (data.loveGame != null) {
								adapter_love.addAll(data.loveGame);
							}
							if (data.lifePhotoResult != null) {
								lifePhotoResult.addAll(data.lifePhotoResult);
								adapter_ico.notifyDataSetChanged();
							}
							if (data.gamePhotoResult != null) {
								gamePhotoResult.addAll(data.gamePhotoResult);
								adapter_game.notifyDataSetChanged();
							}
							setGvData();
							name = item.getUser_name();
							setTitleText(item.getUser_name());
							tv_number.setText(item.getUser_id());
							tv_school.setText(item.getSchool());
							tv_age.setText(item.getUser_age());
							if ("2".equals(item.getUser_gender())) {
								tv_age.setBackgroundResource(R.drawable.sex_nv);
								// tv_age.setTextColor(getResources().getColor(
								// R.color.color_nv));
							}
							tv_sign.setText(item.getSignature());
							tv_address.setText(item.getUser_address());
							tv_job.setText(item.getJob());
							tv_interesting.setText(item.getInterest());
							tv_distance.setText(item.getDistance() + "m");
							tv_xingzuo.setText(item.getUser_constellate());
							tv_time.setText(Util.friendly_time(item
									.getLast_active_time()));
							tv_purpose.setText("寻找" + item.getPurpose() + "中");
							Util.getPurposeIcon(item.getPurpose(), iv_purpose);
							if ("0".equals(item.getIs_friend())) {
								tv_relationship.setText("陌生人");
							} else if ("1".equals(item.getIs_friend())) {
								tv_relationship.setText("好友");
								isFriend = true;
							} else {
								tv_relationship.setText("陌生人");
							}
							// String imageurl = item.getUser_ico();
							// if (!StrUtil.isEmpty(imageurl)) {
							// imageLoader.displayImage(imageurl, image,
							// options_person);
							// }
							if (isFriend) {
								ll_add.setVisibility(View.GONE);
								ll_delete.setVisibility(View.VISIBLE);
								ll_more.setVisibility(View.VISIBLE);
							}
							hideLoadingView();

						} else {
							// showToast("获取数据失败");
							setErrorMessage();
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
						setErrorMessage();
						// showToast("获取数据失败");
					}
				});
	}

	/**
	 * 申请加好友
	 */
	private void applyFriend() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		if (!StrUtil.isEmpty(userId))
			params.put("friend_id", userId);
		else
			params.put("friend_id", hostId);
		UIDataProcess.reqData(ApplyFriend.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						ApplyFriend data = (ApplyFriend) arg0;
						if (data.flg == 1) {
							showToast("申请发送成功");
						} else if (data.flg == 2) {
							showToast("您已发送过申请");
						} else {
							showToast("获取数据失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("获取数据失败");
					}
				});
	}

	/**
	 * 删除好友
	 */
	private void deleteFriend() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		if (!StrUtil.isEmpty(userId))
			params.put("friend_id", userId);
		else
			params.put("friend_id", hostId);
		UIDataProcess.reqData(DeleteFriend.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						DeleteFriend data = (DeleteFriend) arg0;
						if (data.flg == 1) {
							showToast("删除好友成功");
							setResult(RESULT_OK);
							finish();
						} else {
							showToast("删除好友失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("获取数据失败");
					}
				});
	}

}