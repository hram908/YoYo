package com.asktun.mg.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.CreateGroup;
import com.asktun.mg.bean.DeleteGroupPhoto;
import com.asktun.mg.bean.ExitGroup;
import com.asktun.mg.bean.GroupInfo;
import com.asktun.mg.bean.GroupInfo.GroupInfoItem;
import com.asktun.mg.bean.GroupInfo.UserItem;
import com.asktun.mg.bean.RegisterBean;
import com.asktun.mg.bean.UserInfoBean.LifePhotoResult;
import com.asktun.mg.utils.UploadUtil;
import com.asktun.mg.utils.Util;
import com.asktun.mg.utils.UploadUtil.OnUploadProcessListener;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.StrUtil;
import com.google.gson.GsonBuilder;

/**
 * 附近群详情
 * 
 * @author Dean
 */
public class NearGroupActivity extends BaseActivity {

	private String protraitPath;

	@ViewInject(id = R.id.gridview)
	private GridView gridView;
	@ViewInject(id = R.id.gridview_ico)
	private GridView gridView_ico;
	@ViewInject(id = R.id.ll_grouphost, click = "click")
	private LinearLayout ll_grouphost;
	@ViewInject(id = R.id.btn_join, click = "click")
	private Button btn_join;
	@ViewInject(id = R.id.btn_talk, click = "click")
	private Button btn_talk;
	@ViewInject(id = R.id.btn_setting, click = "click")
	private Button btn_setting;
	@ViewInject(id = R.id.btn_exit, click = "click")
	private Button btn_exit;
	@ViewInject(id = R.id.ll_join)
	private LinearLayout ll_join;
	@ViewInject(id = R.id.ll_talk)
	private LinearLayout ll_talk;
	@ViewInject(id = R.id.ll_setting)
	private LinearLayout ll_setting;
	@ViewInject(id = R.id.ll_exit)
	private LinearLayout ll_exit;

	@ViewInject(id = R.id.tv_number)
	private TextView tv_number;
	@ViewInject(id = R.id.tv_spaceinfo)
	private TextView tv_spaceinfo;
	@ViewInject(id = R.id.tv_distance)
	private TextView tv_distance;
	@ViewInject(id = R.id.tv_name)
	private EditText tv_name;
	@ViewInject(id = R.id.tv_hostname)
	private TextView tv_hostname;
	@ViewInject(id = R.id.tv_description)
	private EditText tv_description;
	@ViewInject(id = R.id.tv_count)
	private TextView tv_count;
	@ViewInject(id = R.id.ll_name)
	private LinearLayout ll_name;
	@ViewInject(id = R.id.line)
	private View line;

	private GvAdapter adapter;
	private IcoAdapter adapter_ico;

	private boolean isMyGroup; // 是否我的群组
	private boolean isHost; // 是否我群主
	private boolean isAdmin; // 是否我管理员

	private String groupId;

	private GroupInfoItem groupItem;

	private List<UserItem> userItem = new ArrayList<GroupInfo.UserItem>();
	private List<LifePhotoResult> groupLifePhotos = new ArrayList<LifePhotoResult>();
	private boolean isEdit = false;

	private View rightView;

	private int cHeight;
	private int space10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_neargroup);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		isMyGroup = getIntent().getBooleanExtra("isMyGroup", false);
		isHost = getIntent().getBooleanExtra("isHost", false);
		isAdmin = getIntent().getBooleanExtra("isAdmin", false);
		groupId = getIntent().getStringExtra("groupId");
		ischeck = getIntent().getStringExtra("ischeck");
		if (isMyGroup) {
			ll_join.setVisibility(View.GONE);
			ll_talk.setVisibility(View.VISIBLE);
			ll_exit.setVisibility(View.VISIBLE);

			if (isHost) {
				ll_name.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
				ll_exit.setVisibility(View.GONE);
				ll_setting.setVisibility(View.VISIBLE);
				addRightEdit();
			}
			if (isAdmin) {
				// btn_exit.setVisibility(View.GONE);
				// btn_setting.setVisibility(View.VISIBLE);
				addRightEdit();
			}
		}
		init();
		showLoadingView();
		if (!StrUtil.isEmpty(groupId)) {
			loadData();
		} else {
			setErrorMessage();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void init() {
		cHeight = Util.dpToPx(getResources(), 46);
		space10 = Util.dpToPx(getResources(), 10);

		tv_description.setEnabled(false);
		tv_name.setEnabled(false);
		adapter = new GvAdapter();
		adapter_ico = new IcoAdapter();
		gridView_ico.setAdapter(adapter_ico);
		gridView_ico.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isEdit) {
					if (arg2 == groupLifePhotos.size()) {
						// 添加新图片
						if (groupLifePhotos.size() == 8) {
							showToast("最多只能上传8张");
							return;
						}
						defaultstartActivityForResult(
								new Intent(NearGroupActivity.this,
										SelectPicActivity.class), 2);
					} else {
						// 删除已选图片
						dialogOpen("提示", "确定删除?",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (groupLifePhotos.size() == 1) {
											showToast("请至少保留一张");
											return;
										}
										deletePhoto(groupLifePhotos.get(arg2).photo_id);
									}
								});
					}

				} else {

					ArrayList<String> list = new ArrayList<String>();
					if (groupLifePhotos.size() > 0) {
						for (LifePhotoResult item : groupLifePhotos) {
							list.add(item.photo);
						}
					}
					Intent intent = new Intent(NearGroupActivity.this,
							ImageBrowser2Activity.class);
					intent.putStringArrayListExtra("list", list);
					intent.putExtra("position", arg2);
					startActivity(intent);
				}
			}
		});
	}

	private void addRightEdit() {
		rightView = addRightButtonView(R.drawable.button_edit_selector);
		rightView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isEdit) {
					rightView
							.setBackgroundResource(R.drawable.button_gou_selector);
					tv_description.setEnabled(true);
					tv_name.setEnabled(true);
					isEdit = true;
				} else {
					editGroup();
				}
				adapter_ico.setCount();
				adapter_ico.notifyDataSetChanged();
			}
		});
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.ll_grouphost: // 群主
			if ("80000".equals(groupItem.host_id)) {
				break;
			}
			String hostId = (Integer.parseInt(groupItem.host_id) - 80000) + "";
			if (hostId.equals(mApplication.getLoginbean().user_id)) {
				startActivity(UserInfoActivity.class);
			} else {
				Intent intent_host = new Intent(NearGroupActivity.this,
						NearPersonActivity.class);
				// intent_host.putExtra("isHost", true);
				intent_host.putExtra("groupId", groupId);
				intent_host.putExtra("userId", hostId);
				startActivity(intent_host);
			}
			break;
		case R.id.btn_join:
			Intent intent_apply = new Intent(NearGroupActivity.this,
					ApplyGroupActivity.class);
			intent_apply.putExtra("groupId", groupId);
			startActivity(intent_apply);
			break;
		case R.id.btn_talk:
			Intent intent = new Intent(NearGroupActivity.this,
					ChatActivity.class);
			intent.putExtra("name", groupItem.group_name);
			intent.putExtra("groupId", groupId);
			intent.putExtra("isGroup", true);
			startActivity(intent);
			break;
		case R.id.btn_setting:
			Intent intent_setting = new Intent(NearGroupActivity.this,
					GroupSettingActivity.class);
			intent_setting.putExtra("groupId", groupId);
			startActivityForResult(intent_setting, 0);
			break;
		case R.id.btn_exit: // exit gourp
			dialogOpen("提示", "确认退出群组吗？", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					exitGroup();
				}
			});

			break;
		default:
			break;
		}
	}

	private class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userItem.size();
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
				convertView = mInflater.inflate(R.layout.item_groupmember,
						null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final UserItem item = userItem.get(arg0);
			holder.tv_name.setText(item.user_name + "");
			String image = item.user_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageDrawable(null);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// String userId = (Integer.parseInt(item.user_id) - 80000)
					// + "";
					if (item.user_id.equals(mApplication.getLoginbean().user_id)) {
						startActivity(UserInfoActivity.class);
					} else {
						Intent intent = new Intent(NearGroupActivity.this,
								NearPersonActivity.class);
						intent.putExtra("userId", item.user_id);
						startActivity(intent);
					}
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
		}

	}

	/**
	 * 相册
	 * 
	 * @Description
	 * @author Dean
	 * 
	 */
	private class IcoAdapter extends BaseAdapter {

		private int count;

		public IcoAdapter() {
			// TODO Auto-generated constructor stub
			setCount();
		}

		public void setCount() {
			if (isEdit) {
				count = groupLifePhotos.size() + 1;
			} else {
				count = groupLifePhotos.size();
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return count;
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
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.userinfo_icon_gv_item,
						null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.iv_del = (ImageView) convertView
						.findViewById(R.id.iv_del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (isEdit) {
				holder.iv_del.setVisibility(View.VISIBLE);
			} else {
				holder.iv_del.setVisibility(View.GONE);
			}
			if (isEdit && arg0 == count - 1) {
				holder.iv.setImageResource(R.drawable.icon_add);
				holder.iv_del.setVisibility(View.GONE);
			} else {
				LifePhotoResult item = groupLifePhotos.get(arg0);
				String image = item.photo;
				if (!StrUtil.isEmpty(image)) {
					imageLoader.displayImage(image, holder.iv, options);
				} else {
					holder.iv.setImageResource(R.drawable.moren_img);
				}
			}
			holder.iv_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialogOpen("提示", "确定删除?",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (groupLifePhotos.size() == 1) {
										showToast("请至少保留一张");
										return;
									}
									deletePhoto(groupLifePhotos.get(arg0).photo_id);
								}
							});
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			ImageView iv_del;
		}

	}

	private String ischeck;

	/**
	 * 加载详情
	 */
	private void loadData() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("group_id", groupId);
		if (!StrUtil.isEmpty(ischeck))
			params.put("is_check", ischeck);
		params.put("long", mApplication.mLocation.getLongitude());
		params.put("lati", mApplication.mLocation.getLatitude());
		UIDataProcess.reqData(GroupInfo.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GroupInfo data = (GroupInfo) arg0;
						if (data.flg == 1) {
							groupItem = data.data;
							if (data.userIco != null && data.userIco.size() > 0) {
								userItem.clear();
								userItem.addAll(data.userIco);
								int ii = userItem.size();
								LayoutParams params = new LayoutParams(ii
										* (cHeight + space10),
										(int) (cHeight + 2.4 * space10));
								gridView.setLayoutParams(params);
								gridView.setColumnWidth(cHeight);
								gridView.setHorizontalSpacing(space10);
								gridView.setStretchMode(GridView.NO_STRETCH);
								gridView.setNumColumns(ii);
								gridView.setAdapter(adapter);
							}
							if (data.groupLifePhotos != null) {
								groupLifePhotos.clear();
								groupLifePhotos.addAll(data.groupLifePhotos);
								adapter_ico.setCount();
								adapter_ico.notifyDataSetChanged();
							}

							setTitleText(groupItem.group_name);
							tv_name.setText(groupItem.group_name);
							tv_number.setText(groupItem.group_id);
							tv_hostname.setText(groupItem.host_id);
							tv_spaceinfo.setText(groupItem.info);
							tv_description.setText(groupItem.description);
							tv_distance.setText(groupItem.distance + "m");
							tv_count.setText("群成员(" + userItem.size() + "/"
									+ groupItem.mem_ids + ")");
							// String imageurl = groupItem.group_ico;
							// if (!StrUtil.isEmpty(imageurl)) {
							// imageLoader.displayImage(imageurl, image,
							// options_person);
							// }
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
						// showToast("获取数据失败");
						setErrorMessage();
					}
				});
	}

	private void exitGroup() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		UIDataProcess.reqData(ExitGroup.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						ExitGroup data = (ExitGroup) arg0;
						if (data.flg == 1) {
							showToast("成功退出群组");
							setResult(RESULT_OK);
							finish();
						} else if (data.flg == 2) {
							showToast("您已经发出过申请");
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

	private void editGroup() {
		String name = tv_name.getText().toString();
		String description = tv_description.getText().toString();

		if (isHost) {
			if (StrUtil.isEmpty(name)) {
				showToast("请输入群组名称");
				return;
			}
		}
		if (StrUtil.isEmpty(description)) {
			showToast("请输入群组介绍");
			return;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("group_id", groupId);
		if (isHost)
			params.put("group_name", name);
		params.put("description", description);
		UIDataProcess.reqData(CreateGroup.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						CreateGroup data = (CreateGroup) arg0;
						if (data.flg == 1) {
							showToast("修改成功");
							rightView
									.setBackgroundResource(R.drawable.button_edit_selector);
							isEdit = false;
							tv_description.setEnabled(false);
							tv_name.setEnabled(false);
							tv_spaceinfo.setEnabled(false);
							adapter_ico.setCount();
							adapter_ico.notifyDataSetChanged();
						} else {
							rightView
									.setBackgroundResource(R.drawable.button_edit_selector);
							isEdit = false;
							tv_description.setEnabled(false);
							tv_name.setEnabled(false);
							tv_spaceinfo.setEnabled(false);
							adapter_ico.setCount();
							adapter_ico.notifyDataSetChanged();
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
						rightView
								.setBackgroundResource(R.drawable.button_edit_selector);
						isEdit = false;
						tv_description.setEnabled(false);
						tv_name.setEnabled(false);
						tv_spaceinfo.setEnabled(false);
						adapter_ico.setCount();
						adapter_ico.notifyDataSetChanged();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 2: // 选择图片
				if (data != null) {
					Bundle extras = data.getExtras();
					protraitPath = extras.getString("path");
					File protraitFile = new File(protraitPath);
					if (StrUtil.isEmpty(protraitPath) || !protraitFile.exists()) {
						showToast("图像不存在，上传失败");
						return;
					}
					uploadIcon(protraitPath);
				}
				break;
			case 0: // 解散群组
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void uploadIcon(String iconPath) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("group_id", groupId);
		String fileKey = "photo";
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(new OnUploadProcessListener() {

			@Override
			public void onUploadProcess(int uploadSize) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUploadDone(int responseCode, final String obj) {
				// TODO Auto-generated method stub
				removeProgressDialog();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						RegisterBean o = null;
						JsonReqUtil.GsonObj gsonObj = null;
						try {
							gsonObj = RegisterBean.class.newInstance();
						} catch (InstantiationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						GsonBuilder builder = new GsonBuilder();
						builder.excludeFieldsWithoutExposeAnnotation();
						Type type = gsonObj.getTypeToken();
						try {
							o = builder.create().fromJson(obj, type);
						} catch (Exception e) {
							Log.e("JsonReqUtil", "得到数据异常");
						}
						if (o != null) {
							if (o.flg == 1) {
								loadData();
							}
						} else {
							showToast("上传图片失败");
						}
					}
				});
			}

			@Override
			public void initUpload(int fileSize) {
				// TODO Auto-generated method stub
			}
		}); // 设置监听器监听上传状态

		uploadUtil.uploadFile(iconPath, fileKey, JsonReqUtil.getURL()
				+ "userGroups/addGroupPhoto", params);
	}

	private void deletePhoto(String photoId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("photo_id", photoId);
		params.put("group_id", groupId);
		UIDataProcess.reqData(DeleteGroupPhoto.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						DeleteGroupPhoto data = (DeleteGroupPhoto) arg0;
						if (data.flg == 1) {
							loadData();
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
					}
				});
	}

	@Override
	public void RequestData() {
		// TODO Auto-generated method stub
		super.RequestData();
		loadData();
	}

}