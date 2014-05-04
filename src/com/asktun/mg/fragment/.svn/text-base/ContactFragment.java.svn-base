package com.asktun.mg.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.activity.AddGorFActivity;
import com.asktun.mg.activity.GameDetailActivity;
import com.asktun.mg.activity.NearGroupActivity;
import com.asktun.mg.activity.NearPersonActivity;
import com.asktun.mg.bean.FriendsList;
import com.asktun.mg.bean.FriendsList.FriendsListItem;
import com.asktun.mg.bean.GroupsNear;
import com.asktun.mg.bean.GroupsNear.GroupsNearItem;
import com.asktun.mg.bean.MembersNear;
import com.asktun.mg.bean.MembersNear.MembersNearItem.GameInfo;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;
import com.asktun.mg.view.XListView.IXListViewListener;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 
 * @Description 联系人
 * @author Dean
 * 
 */
public class ContactFragment extends BaseFragment implements IXListViewListener {

	private View view;

	@ViewInject(id = R.id.rgroup)
	private RadioGroup rg;
	@ViewInject(id = R.id.lv_1)
	private XListView listView1;
	@ViewInject(id = R.id.lv_2)
	private XListView listView2;
	@ViewInject(id = R.id.empty_fridend, click = "btnClick")
	private TextView empty_fridend;
	@ViewInject(id = R.id.empty_group, click = "btnClick")
	private TextView empty_group;

	private PersonAdapter adapter1;
	private GroupAdapter adapter2;

	private int type;

	private static final int REQUEST_PERSON = 0;
	private static final int REQUEST_GROUP = 1;
	private static final int REQUEST_CREATE_GROUP = 2;

	private int hSpacing;
	private int cHeight;

	@Override
	public void addChildView(ViewGroup contentLayout) {
		// TODO Auto-generated method stub
		super.addChildView(contentLayout);
		view = mInflater.inflate(R.layout.fragment_contact, null);
		FinalActivity.initInjectedView(this, view);
		contentLayout.addView(view, layoutParamsFF);
		setTitleText("联系人");
		addRightButtonView(R.drawable.button_add_selector).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(act, AddGorFActivity.class);
						startActivityForResult(intent, REQUEST_CREATE_GROUP);
					}
				});
		init();

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.empty_fridend:
			listView1.startPullRefresh();
			break;
		case R.id.empty_group:
			listView2.startPullRefresh();
			break;

		default:
			break;
		}
	}

	private void init() {

		cHeight = Util.dpToPx(getResources(), 24);
		hSpacing = Util.dpToPx(getResources(), 8);

		adapter1 = new PersonAdapter();
		adapter2 = new GroupAdapter();
		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView1.setEmptyView(empty_fridend);
		listView2.setEmptyView(empty_group);
		empty_group.setVisibility(View.GONE);
		listView1.setPullLoadEnable(false);
		listView1.setPullRefreshEnable(true);
		listView1.setXListViewListener(this);
		listView1.startPullRefresh();
		listView2.setPullLoadEnable(false);
		listView2.setPullRefreshEnable(true);
		listView2.setXListViewListener(this);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_1) {
					type = 0;
					listView1.setVisibility(View.VISIBLE);
					listView2.setVisibility(View.GONE);
					empty_group.setVisibility(View.GONE);
					if (adapter1.getCount() == 0) {
						listView1.startPullRefresh();
					}
				} else {
					type = 1;
					listView2.setVisibility(View.VISIBLE);
					listView1.setVisibility(View.GONE);
					empty_fridend.setVisibility(View.GONE);
					if (adapter2.getCount() == 0) {
						listView2.startPullRefresh();
					}
				}
			}
		});

		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act, NearGroupActivity.class);
				intent.putExtra("isMyGroup", true);
				intent.putExtra("groupId", adapter2.list.get(arg2 - 1).group_id);
				int isHost = adapter2.list.get(arg2 - 1).is_host;
				int isAdmin = adapter2.list.get(arg2 - 1).is_admin;
				if (isHost == 1) {
					intent.putExtra("isHost", true);
				} else if (isAdmin == 1) {
					intent.putExtra("isAdmin", true);
				}
				startActivityForResult(intent, REQUEST_GROUP);
			}
		});

	}

	private class PersonAdapter extends BaseAdapter {

		private List<FriendsListItem> list = new ArrayList<FriendsListItem>();

		public void add(FriendsListItem o) {
			list.add(o);
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
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		private NearPlayAdapter npAdapter;

		@Override
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_nearperson, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_age = (TextView) convertView
						.findViewById(R.id.tv_age);
				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				holder.tv_purpose = (TextView) convertView
						.findViewById(R.id.tv_purpose);
				holder.tv_sign = (TextView) convertView
						.findViewById(R.id.tv_sign);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.hsv = (LinearLayout) convertView.findViewById(R.id.hsv);
				holder.line = convertView.findViewById(R.id.line);
				holder.gv = (GridView) convertView.findViewById(R.id.gridview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final FriendsListItem item = list.get(arg0);
			holder.hsv.setVisibility(View.VISIBLE);
			holder.tv_name.setText(item.user_name);
			holder.tv_age.setText(item.user_age);
			holder.tv_distance.setText(item.distance + "m");
			holder.tv_time.setText(Util.friendly_time(item.last_active_time));
			Util.getPurposeIcon(item.purpose, holder.tv_purpose);
			if (item.user_gender == 1) {
				holder.tv_age.setBackgroundResource(R.drawable.sex_nan);
				// holder.tv_age.setTextColor(getResources().getColor(
				// R.color.color_nan));
				// holder.tv_sign.setTextColor(getResources().getColor(
				// R.color.color_nan));
			} else {
				holder.tv_age.setBackgroundResource(R.drawable.sex_nv);
				// holder.tv_age.setTextColor(getResources().getColor(
				// R.color.color_nv));
				// holder.tv_sign.setTextColor(getResources().getColor(
				// R.color.color_nv));
			}
			holder.tv_sign.setText(item.signature);
			String image = item.user_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageDrawable(null);
			}

			if (item.game_info != null && item.game_info.size() > 0) {
				npAdapter = new NearPlayAdapter(item.game_info);
				int ii = npAdapter.getCount();
				LayoutParams params = new LayoutParams(ii
						* (cHeight + hSpacing), cHeight + hSpacing);
				holder.gv.setLayoutParams(params);
				holder.gv.setColumnWidth(cHeight);
				holder.gv.setHorizontalSpacing(hSpacing);
				holder.gv.setStretchMode(GridView.NO_STRETCH);
				holder.gv.setNumColumns(ii);
				holder.gv.setAdapter(npAdapter);
			} else {
				List<GameInfo> gameList = new ArrayList<MembersNear.MembersNearItem.GameInfo>();
				npAdapter = new NearPlayAdapter(gameList);
				int ii = npAdapter.getCount();
				LayoutParams params = new LayoutParams(ii
						* (cHeight + hSpacing), 0);
				holder.gv.setLayoutParams(params);
				holder.gv.setColumnWidth(cHeight);
				holder.gv.setHorizontalSpacing(hSpacing);
				holder.gv.setStretchMode(GridView.NO_STRETCH);
				holder.gv.setNumColumns(ii);
				holder.gv.setAdapter(npAdapter);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(act, NearPersonActivity.class);
					intent.putExtra("userId", item.friend_id);
					startActivity(intent);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_purpose;
			TextView tv_name;
			TextView tv_age;
			TextView tv_distance;
			TextView tv_sign;
			TextView tv_time;
			LinearLayout hsv;
			View line;
			GridView gv;
		}

	}

	private class NearPlayAdapter extends BaseAdapter {

		private List<GameInfo> list;

		public NearPlayAdapter(List<GameInfo> list) {
			this.list = list;
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
			final GameInfo item = list.get(arg0);
			holder.tv_name.setVisibility(View.GONE);
			// holder.tv_name.setText(item.game_name);
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
					Intent intent = new Intent(act, GameDetailActivity.class);
					intent.putExtra("gameId", item.game_id);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_PERSON:
				listView1.startPullRefresh();
				break;
			case REQUEST_GROUP:
				listView2.startPullRefresh();
				break;
			case REQUEST_CREATE_GROUP:
				if (type == 0) {
					rg.check(R.id.rb_2);
				}
				listView2.startPullRefresh();
				break;
			default:
				break;
			}

		}
	}

	private class GroupAdapter extends BaseAdapter {

		private List<GroupsNearItem> list = new ArrayList<GroupsNearItem>();

		public void add(GroupsNearItem o) {
			list.add(o);
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
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_neargroup, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_count = (TextView) convertView
						.findViewById(R.id.tv_count);
				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				holder.tv_descrip = (TextView) convertView
						.findViewById(R.id.tv_descrip);
				holder.tv_role = (TextView) convertView
						.findViewById(R.id.tv_role);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GroupsNearItem item = list.get(position);
			holder.tv_name.setText(item.group_name);
			holder.tv_descrip.setText(item.description);
			holder.tv_count.setText(item.mem_ids + "/" + item.user_num);
			String image = item.group_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}
			holder.tv_distance.setText(item.distance + "m");
			holder.tv_time.setText(Util.friendly_time(item.last_active_time));

			if (item.is_host == 1) {
				holder.tv_role.setText("群主");
				holder.tv_role.setBackgroundResource(R.drawable.icon_qunzhu);

				// holder.tv_role.setTextColor(getResources().getColor(
				// R.color.gray));
			} else if (item.is_admin == 1) {
				holder.tv_role
						.setBackgroundResource(R.drawable.icon_qunzhuguanli);
				holder.tv_role.setText("群管理员");
				// holder.tv_role.setTextColor(getResources().getColor(
				// R.color.orange));
			} else {
				holder.tv_role.setText("群成员");
				holder.tv_role.setBackgroundResource(R.drawable.icon_chengyuan);

				// holder.tv_role.setTextColor(getResources().getColor(
				// R.color.orange));
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_count;
			TextView tv_distance;
			TextView tv_descrip;
			TextView tv_time;
			TextView tv_role;
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		switch (type) {
		case 0:
			loadFriendsData();
			break;
		case 1:
			loadGroupData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

	private void loadFriendsData() {
		adapter1.clear();
		adapter1.notifyDataSetChanged();
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (mApplication.getLoginbean() != null) {
			params.put("user_id", mApplication.getLoginbean().user_id);
			params.put("token", mApplication.getLoginbean().token);
		}
		if (mApplication.mLocation != null) {
			params.put("long", mApplication.mLocation.getLongitude() + "");
			params.put("lati", mApplication.mLocation.getLatitude() + "");
		}
		UIDataProcess.reqData(FriendsList.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						FriendsList data = (FriendsList) arg0;
						if (data.flg == 1) {
							listView1.setTotalDataCount(1);
							List<FriendsListItem> item = data.data;
							if (item != null) {
								int size = item.size();
								for (int i = 0; i < size; i++) {
									adapter1.add(item.get(i));
								}
								adapter1.notifyDataSetChanged();
								listView1.notifyFootViewTextChange();
							}
						} else {
							listView1.setTotalDataCount(0);
							listView1.notifyFootViewTextChange();
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Object arg0) {
						listView1.setTotalDataCount(-1);
						listView1.notifyFootViewTextChange();
					}
				});

	}

	private void loadGroupData() {
		adapter2.clear();
		adapter2.notifyDataSetChanged();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("long", mApplication.mLocation.getLongitude() + "");
		params.put("lati", mApplication.mLocation.getLatitude() + "");
		UIDataProcess.reqData(GroupsNear.class, params, 1,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GroupsNear data = (GroupsNear) arg0;
						if (data.flg == 1) {
							listView2.setTotalDataCount(1);
							List<GroupsNearItem> item = data.data;
							if (item != null) {
								int size = item.size();
								for (int i = 0; i < size; i++) {
									adapter2.add(item.get(i));
								}
							}
							adapter2.notifyDataSetChanged();
							listView2.notifyFootViewTextChange();
						} else {
							listView2.setTotalDataCount(-1);
							listView2.notifyFootViewTextChange();
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Object arg0) {
						listView2.setTotalDataCount(-1);
						listView2.notifyFootViewTextChange();
					}
				});

	}

}
