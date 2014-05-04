package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.DeleteActive;
import com.asktun.mg.bean.GroupZone;
import com.asktun.mg.bean.GroupZone.ZoneInfo;
import com.asktun.mg.view.XListView;
import com.asktun.mg.view.XListView.IXListViewListener;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 群空间
 * 
 * @author Dean
 */
public class GroupSpaceActivity extends BaseActivity implements
		IXListViewListener {

	@ViewInject(id = R.id.listview)
	private XListView listView;
	@ViewInject(id = R.id.image)
	private ImageView image;
	@ViewInject(id = R.id.tv_number)
	private TextView tv_number;
	@ViewInject(id = R.id.tv_distance)
	private TextView tv_distance;

	private MyAdapter adapter;

	private View dialogView;

	private String groupId;

	private List<ZoneInfo> zoneItems = new ArrayList<ZoneInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_groupspace);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		groupId = getIntent().getStringExtra("groupId");
		setTitleText("群空间");
		addRightButtonView(R.drawable.button_fabu_selector).setOnClickListener(
				new OnClickListener() { 

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(GroupSpaceActivity.this,
								CreatActiveActivity.class);
						intent.putExtra("groupId", groupId);
						startActivityForResult(intent, 0);
					}
				});
		init();
	}

	private void init() {

		// dialogView = mInflater.inflate(R.layout.dialog_groupspace_edit,
		// null);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GroupSpaceActivity.this,GroupActiveDetailActivity.class);
				intent.putExtra("zoneInfo", zoneItems.get(arg2-1));
				startActivityForResult(intent, 0);
			}
		});

		loadData();
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if(RESULT_OK == resultCode){
			loadData();
		}
		
	}



	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return zoneItems.size();
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
				convertView = mInflater.inflate(R.layout.item_groupspace, null,
						false);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.btn_delete = (Button) convertView
						.findViewById(R.id.btn_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final ZoneInfo item = zoneItems.get(arg0);
			holder.tv_content.setText(item.info);
			holder.tv_time.setText(item.add_time);
			holder.btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deleteActive(item.zone_id);
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

		private class ViewHolder {
			TextView tv_time;
			TextView tv_content;
			Button btn_delete;
		}

	}

	private void loadData() {
		zoneItems.clear();
		adapter.notifyDataSetChanged();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("group_id", groupId);
		params.put("Long", mApplication.mLocation.getLongitude()+"");
		params.put("Lati", mApplication.mLocation.getLatitude()+"");
		UIDataProcess.reqData(GroupZone.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GroupZone data = (GroupZone) arg0;
						if (data.flg == 1) {
							if (null != data.zoneInfo) {
								zoneItems.addAll(data.zoneInfo);
								adapter.notifyDataSetChanged();
							}
							tv_number.setText(data.data.group_id);
							tv_distance.setText(data.data.distance + "m");
							String imageurl = data.data.group_ico;
							if (!StrUtil.isEmpty(imageurl)) {
								imageLoader.displayImage(imageurl, image,
										options);
							}
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	private void deleteActive(String zoneId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("zone_id", zoneId);
		UIDataProcess.reqData(DeleteActive.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						DeleteActive data = (DeleteActive) arg0;
						if (data.flg == 1) {
							showToast("成功取消活动");
							loadData();
						} else if (data.flg == -3) {
							showToast("没有权限");
						}else {
							showToast("请求失败");
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