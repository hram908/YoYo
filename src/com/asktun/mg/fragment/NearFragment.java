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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.activity.FilterNearActivity;
import com.asktun.mg.activity.GameDetailActivity;
import com.asktun.mg.activity.NearGroupActivity;
import com.asktun.mg.activity.NearPersonActivity;
import com.asktun.mg.bean.GroupsNear;
import com.asktun.mg.bean.SelectMembers;
import com.asktun.mg.bean.GroupsNear.GroupsNearItem;
import com.asktun.mg.bean.MembersNear;
import com.asktun.mg.bean.MembersNear.MembersNearItem;
import com.asktun.mg.bean.MembersNear.MembersNearItem.GameInfo;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;
import com.asktun.mg.view.XListView.IXListViewListener;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.PreferenceOperateUtils;
import com.chen.jmvc.util.StrUtil;

/**
 * 附近
 * 
 * @Description
 * @author Dean
 * 
 */
public class NearFragment extends BaseFragment implements IXListViewListener {

	private View view;

	@ViewInject(id = R.id.rgroup)
	private RadioGroup rg;
	@ViewInject(id = R.id.lv_1)
	private XListView listView1;
	@ViewInject(id = R.id.lv_2)
	private XListView listView2;

	private PersonAdapter adapter1;
	private GroupAdapter adapter2;

	private int[] Index = { 1, 1 };
	private int[] totalDataCount = { 1, 1 };
	private int type;

	private int hSpacing;
	private int cHeight;

	private HashMap<Integer, Boolean> openMap = new HashMap<Integer, Boolean>();

	/******** 筛选 ********/
	private View dialogView;
	private Button btn_dialog1;
	private Button btn_dialog2;
	private Button btn_dialog3;
	private Button btn_dialog4;
	private Button btn_dialog_cancel;

	/******* 筛选用 *******/
	private String sex; // 筛选用
	private String age;
	private String job;
	private String game_name;
	private String constellate; // 星座
	private String last_active_time; // 最后活动时间

	/******** 选择对象 ********/
	private View chooseView;
	private Button btn_config;
	private Button btn_cancel;
	private ListView dialoglistView;

	private View rightView;

	private PreferenceOperateUtils pou;

	@Override
	public void addChildView(ViewGroup contentLayout) {
		// TODO Auto-generated method stub
		super.addChildView(contentLayout);
		view = mInflater.inflate(R.layout.fragment_friends, null);
		FinalActivity.initInjectedView(this, view);
		contentLayout.addView(view, layoutParamsFF);
		setTitleText("附近");
		rightView = addRightButtonView(R.drawable.xiala_top_selector);
		rightView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act.showDialog(Constant.DIALOGCENTER, dialogView, diaplayWidth / 5);
			}
		});
		pou = new PreferenceOperateUtils(act);
		initDialog();
		init();
	}

	/**
	 * 清楚筛选条件
	 */
	private void resetAllFilter() {
		age = null;
		job = null;
		game_name = null;
		constellate = null;
		last_active_time = null;
	}

	private static final String[] dxList = { "队友", "基友", "闺蜜", "师傅", "PK对手",
			"情侣" };
	private int chooseposition = 0;
	private ChooseAdapter chooseAdapter;

	private void initChoose() {
		pou.setBoolean("isFirstLogin", false);
		chooseView = mInflater.inflate(R.layout.dialog_choosedx, null);
		btn_config = (Button) chooseView.findViewById(R.id.btn_config);
		btn_cancel = (Button) chooseView.findViewById(R.id.btn_cancel);
		dialoglistView = (ListView) chooseView.findViewById(R.id.listview);
		chooseAdapter = new ChooseAdapter();
		dialoglistView.setAdapter(chooseAdapter);
		dialoglistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				chooseposition = arg2;
				chooseAdapter.notifyDataSetChanged();
			}
		});
		btn_config.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendChoose();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act.removeDialog(Constant.DIALOGCENTER);
			}
		});
		act.showDialog(Constant.DIALOGCENTER, chooseView, diaplayWidth / 5);
	}

	private void sendChoose() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("purpose", dxList[chooseposition]);
		UIDataProcess.reqData(SelectMembers.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						SelectMembers data = (SelectMembers) arg0;
						if (data.flg == 1) {
							act.removeDialog(Constant.DIALOGCENTER);
						} else {
							act.removeDialog(Constant.DIALOGCENTER);
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
						act.removeDialog(Constant.DIALOGCENTER);
					}
				});
	}

	private class ChooseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dxList.length;
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
				convertView = mInflater.inflate(R.layout.item_choosedx, null,
						false);
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb_choose);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name.setText(dxList[arg0]);
			if (chooseposition == arg0) {
				holder.cb.setChecked(true);
			} else {
				holder.cb.setChecked(false);
			}
			return convertView;
		}

		private class ViewHolder {
			CheckBox cb;
			TextView tv_name;
		}

	}

	private void initDialog() {
		dialogView = mInflater.inflate(R.layout.dialog_filter, null);
		btn_dialog1 = (Button) dialogView.findViewById(R.id.btn_dialog1);
		btn_dialog2 = (Button) dialogView.findViewById(R.id.btn_dialog2);
		btn_dialog3 = (Button) dialogView.findViewById(R.id.btn_dialog3);
		btn_dialog4 = (Button) dialogView.findViewById(R.id.btn_dialog4);
		btn_dialog_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btn_dialog_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act.removeDialog(Constant.DIALOGCENTER);
			}
		});
		btn_dialog4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// act.dialogOpen("提醒", "请先完善自己的个人信息", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// startActivity(new Intent(act, UserInfoActivity.class));
				// }
				// });
				startActivityForResult(
						new Intent(act, FilterNearActivity.class), 0000);
				act.removeDialog(Constant.DIALOGCENTER);
			}
		});
		btn_dialog1.setOnClickListener(new OnClickListener() { // 全部

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						resetAllFilter();
						sex = null;
						listView1.startPullRefresh();
						act.removeDialog(Constant.DIALOGCENTER);
					}
				});
		btn_dialog2.setOnClickListener(new OnClickListener() { // 男

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						resetAllFilter();
						sex = "1";
						listView1.startPullRefresh();
						act.removeDialog(Constant.DIALOGCENTER);
					}
				});
		btn_dialog3.setOnClickListener(new OnClickListener() { // 女

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						resetAllFilter();
						sex = "2";
						listView1.startPullRefresh();
						act.removeDialog(Constant.DIALOGCENTER);
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && null != data) {
			sex = data.getStringExtra("sex");
			age = data.getStringExtra("age");
			job = data.getStringExtra("job");
			game_name = data.getStringExtra("game_name");
			constellate = data.getStringExtra("constellate");
			last_active_time = data.getStringExtra("last_active_time");
			listView1.startPullRefresh();
		}
	}

	private void init() {

		cHeight = Util.dpToPx(getResources(), 24);
		hSpacing = Util.dpToPx(getResources(), 8);

		adapter1 = new PersonAdapter();
		adapter2 = new GroupAdapter();
		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView1.setPullLoadEnable(true);
		listView1.setPullRefreshEnable(true);
		listView1.setXListViewListener(this);
		listView1.startPullRefresh();
		listView2.setPullLoadEnable(true);
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
					rightView.setVisibility(View.VISIBLE);
					if (adapter1.getCount() == 0) {
						listView1.startPullRefresh();
					}
				} else {
					type = 1;
					listView2.setVisibility(View.VISIBLE);
					listView1.setVisibility(View.GONE);
					if (adapter2.getCount() == 0) {
						listView2.startPullRefresh();
					}
					rightView.setVisibility(View.INVISIBLE);
				}
			}
		});

		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act, NearGroupActivity.class);
				intent.putExtra("groupId", adapter2.list.get(arg2 - 1).group_id);
				String is_members = adapter2.list.get(arg2 - 1).is_members;
				if (!StrUtil.isEmpty(is_members)) {

					if (is_members.equals("群成员")) {
						intent.putExtra("isMyGroup", true);
					}
					if (is_members.equals("管理员")) {
						intent.putExtra("isMyGroup", true);
						intent.putExtra("isAdmin", true);
					}
					if (is_members.equals("群主")) {
						intent.putExtra("isMyGroup", true);
						intent.putExtra("isHost", true);
					}
				}
				startActivity(intent);
			}
		});

		if (pou.getBoolean("isFirstLogin", false)) {
			initChoose();
		}
	}

	private class PersonAdapter extends BaseAdapter {

		private List<MembersNearItem> list = new ArrayList<MembersNearItem>();

		public void add(MembersNearItem o) {
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
			holder.hsv.setVisibility(View.VISIBLE);
			final MembersNearItem item = list.get(arg0);
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
					intent.putExtra("userId", item.user_id);
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
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
			GroupsNearItem item = list.get(arg0);
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

			if (StrUtil.isEmpty(item.is_members)) {
				holder.tv_role.setVisibility(View.GONE);
			} else {
				holder.tv_role.setVisibility(View.VISIBLE);
				holder.tv_role.setText(item.is_members);
				if (item.is_members.equals("群成员")) {
					// holder.tv_role.setTextColor(getResources().getColor(
					// R.color.gray));
					holder.tv_role
							.setBackgroundResource(R.drawable.icon_chengyuan);
				} else if (item.is_members.equals("群主")) {
					holder.tv_role
							.setBackgroundResource(R.drawable.icon_qunzhu);
					// holder.tv_role.setTextColor(getResources().getColor(
					// R.color.orange));
				} else {
					holder.tv_role
							.setBackgroundResource(R.drawable.icon_qunzhuguanli);
				}
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
	public void onRefresh() {
		// TODO Auto-generated method stub
		switch (type) {
		case 0:
			loadMemberData(true);
			break;
		case 1:
			loadGroupData(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		switch (type) {
		case 0:
			loadMemberData(false);
			break;
		case 1:
			loadGroupData(false);
			break;
		default:
			break;
		}
	}

	private void loadGroupData(boolean isrefresh) {

		if (isrefresh) {
			Index[type] = 1;
			totalDataCount[type] = 1;
			adapter2.clear();
			adapter2.notifyDataSetChanged();
		}

		if (adapter2.getCount() >= totalDataCount[type]) {
			listView2.notifyFootViewTextChange();
			return;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("page", Index[type]++);
		params.put("pagesize", "12");
		if (mApplication.mLocation != null) {
			params.put("long", mApplication.mLocation.getLongitude() + "");
			params.put("lati", mApplication.mLocation.getLatitude() + "");
		}
		params.put("distance", 10000);
		UIDataProcess.reqData(GroupsNear.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GroupsNear data = (GroupsNear) arg0;
						if (data.flg == 1) {
							totalDataCount[type] = data.count;
							listView2.setTotalDataCount(data.count);
							List<GroupsNearItem> item = data.data;
							if (item != null && item.size() > 0) {
								for (int i = 0; i < item.size(); i++) {
									adapter2.add(item.get(i));
								}
								adapter2.notifyDataSetChanged();
								listView2.notifyFootViewTextChange();
							} else {
								listView2.setTotalDataCount(0);
								listView2.notifyFootViewTextChange();
							}
						} else {
							listView2.setTotalDataCount(0);
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

	private void loadMemberData(boolean isrefresh) {

		if (isrefresh) {
			openMap.clear();
			Index[type] = 1;
			totalDataCount[type] = 1;
			adapter1.clear();
			adapter1.notifyDataSetChanged();
		}

		if (adapter1.getCount() >= totalDataCount[type]) {
			listView1.notifyFootViewTextChange();
			return;
		}

		// &sex=1性别0保密，1男，2女&age=23（筛选）选填

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("page", Index[type]++);
		params.put("pagesize", "12");
		if (mApplication.mLocation != null) {
			params.put("long", mApplication.mLocation.getLongitude() + "");
			params.put("lati", mApplication.mLocation.getLatitude() + "");
		}
		params.put("distance", 10000);
		if (!StrUtil.isEmpty(sex))
			params.put("sex", sex);
		if (!StrUtil.isEmpty(age))
			params.put("age", age);
		if (!StrUtil.isEmpty(job))
			params.put("job", job);
		if (!StrUtil.isEmpty(game_name))
			params.put("game_name", game_name);
		if (!StrUtil.isEmpty(constellate))
			params.put("constellate", constellate);
		if (!StrUtil.isEmpty(last_active_time))
			params.put("last_active_time", last_active_time);
		UIDataProcess.reqData(MembersNear.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						MembersNear data = (MembersNear) arg0;
						if (data.flg == 1) {
							totalDataCount[type] = data.count;
							listView1.setTotalDataCount(data.count);
							List<MembersNearItem> item = data.data;
							if (item != null && item.size() > 0) {
								int size = item.size();
								for (int i = 0; i < size; i++) {
									adapter1.add(item.get(i));
								}
							}
							adapter1.notifyDataSetChanged();
							listView1.notifyFootViewTextChange();
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

}
