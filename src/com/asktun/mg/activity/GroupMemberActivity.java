package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.AddAdmin;
import com.asktun.mg.bean.MemberList;
import com.asktun.mg.bean.MemberList.MemberListItem;
import com.asktun.mg.bean.RemoveGroup;
import com.asktun.mg.bean.TransferGroup;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 群成员列表
 * 
 * @author Dean
 */
public class GroupMemberActivity extends BaseActivity {

	@ViewInject(id = R.id.listview)
	private XListView listView;

	private MyAdapter adapter;

	private View dialogView;
	private Button btn_dialog1;
	private Button btn_dialog2;
	private Button btn_dialog3;
	private Button btn_dialog_cancel;

	private String groupId;

	private int clickPosition;

	private List<MemberListItem> list = new ArrayList<MemberListItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_groupmember_manage);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		groupId = getIntent().getStringExtra("groupId");
		setTitleText("群成员列表");
		// addRightButtonView(R.drawable.button_gou_selector);
		initDialog();
		init();
	}

	private void initDialog() {
		dialogView = mInflater.inflate(R.layout.dialog_groupspace_edit, null);
		btn_dialog1 = (Button) dialogView.findViewById(R.id.btn_dialog1);
		btn_dialog2 = (Button) dialogView.findViewById(R.id.btn_dialog2);
		btn_dialog3 = (Button) dialogView.findViewById(R.id.btn_dialog3);
		btn_dialog_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btn_dialog1.setText("设为管理员");
		btn_dialog2.setText("转让群组");
		btn_dialog3.setText("移除");
		btn_dialog_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeDialog(Constant.DIALOGCENTER);
			}
		});
		btn_dialog1.setOnClickListener(new OnClickListener() { // 设为管理员

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						addAdmin();
						removeDialog(Constant.DIALOGCENTER);
					}
				});
		btn_dialog2.setOnClickListener(new OnClickListener() { // 转让群组

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						transferGroup();
						removeDialog(Constant.DIALOGCENTER);
					}
				});
		btn_dialog3.setOnClickListener(new OnClickListener() { // 转让群组

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						removeGroup();
						removeDialog(Constant.DIALOGCENTER);
					}
				});
	}

	private void init() {
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);

		loadData();
	}

	private class MyAdapter extends BaseAdapter {

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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.item_groupmember_manage, null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_age = (TextView) convertView
						.findViewById(R.id.tv_age);
				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_role = (TextView) convertView
						.findViewById(R.id.tv_role);
				holder.btn_manage = (Button) convertView
						.findViewById(R.id.btn_manage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MemberListItem item = list.get(position);
			holder.tv_name.setText(item.user_name);
			holder.tv_age.setText(item.user_age);
			holder.tv_distance.setText(item.distance + "m");
			holder.tv_time.setText(Util.friendly_time(item.last_active_time));
			if (item.user_gender == 1) {
				holder.tv_age.setBackgroundResource(R.drawable.sex_nan);
				// holder.tv_age.setTextColor(getResources().getColor(R.color.color_nan));
			} else {
				holder.tv_age.setBackgroundResource(R.drawable.sex_nv);
				// holder.tv_age.setTextColor(getResources().getColor(R.color.color_nv));
			}
			if (item.type == 1) {
				holder.tv_role.setText("群管理员");
				// holder.tv_role.setTextColor(getResources().getColor(
				// R.color.orange));
				holder.tv_role
						.setBackgroundResource(R.drawable.icon_qunzhuguanli);
				holder.btn_manage.setVisibility(View.GONE);
			} else {
				holder.tv_role.setText("群成员");
				holder.tv_role.setBackgroundResource(R.drawable.icon_chengyuan);
				// holder.tv_role.setTextColor(getResources().getColor(
				// R.color.gray));
				holder.btn_manage.setVisibility(View.VISIBLE);
			}
			String image = item.user_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}

			holder.btn_manage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clickPosition = position;
					showDialog(Constant.DIALOGCENTER, dialogView,
							diaplayWidth / 5);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_distance;
			TextView tv_time;
			TextView tv_age;
			TextView tv_role;
			Button btn_manage;
		}

	}

	private void loadData() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("long", mApplication.mLocation.getLongitude());
		params.put("lati", mApplication.mLocation.getLatitude());
		params.put("group_id", groupId);
		UIDataProcess.reqData(MemberList.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						MemberList data = (MemberList) arg0;
						if (data.flg == 0) {
							list.clear();
							adapter.notifyDataSetChanged();
							if (data.admin_info != null) {
								for (MemberListItem item : data.admin_info) {
									item.type = 1;
									list.add(item);
								}
							}
							if (data.member_info != null) {
								list.addAll(data.member_info);
							}
							adapter.notifyDataSetChanged();

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
	 * 设为管理员
	 */
	private void addAdmin() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("host_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		params.put("user_id", list.get(clickPosition).user_id);
		UIDataProcess.reqData(AddAdmin.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						AddAdmin data = (AddAdmin) arg0;
						if (data.flg == 1) {
							loadData();
							showToast("设置管理员成功");
						} else if (data.flg == -3) {
							showToast("没有权限");
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
	 * 转让群组
	 */
	private void transferGroup() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("host_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		params.put("user_id", list.get(clickPosition).user_id);
		UIDataProcess.reqData(TransferGroup.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						TransferGroup data = (TransferGroup) arg0;
						if (data.flg == 1) {
							loadData();
							showToast("转让群组成功");
						} else if (data.flg == -3) {
							showToast("没有权限");
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
	 * 移出群组
	 */
	private void removeGroup() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("host_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		params.put("user_id", list.get(clickPosition).user_id);
		UIDataProcess.reqData(RemoveGroup.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						RemoveGroup data = (RemoveGroup) arg0;
						if (data.flg == 1) {
							loadData();
							showToast("移除成功");
						} else if (data.flg == -3) {
							showToast("没有权限");
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

}