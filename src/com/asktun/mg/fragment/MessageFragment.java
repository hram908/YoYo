package com.asktun.mg.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.activity.ChatActivity;
import com.asktun.mg.activity.PageFrameActivity;
import com.asktun.mg.bean.AgreeFriend;
import com.asktun.mg.bean.FriendMessageInfo;
import com.asktun.mg.bean.UserMessage;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.XListView;
import com.asktun.mg.view.XListView.IXListViewListener;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.PreferenceOperateUtils;
import com.chen.jmvc.util.StrUtil;

/**
 * 消息
 * 
 * @Description
 * @author Dean
 * 
 */
public class MessageFragment extends BaseFragment implements IXListViewListener {

	private View view;

	@ViewInject(id = R.id.listview)
	private XListView listView;
	@ViewInject(id = R.id.empty)
	private TextView mEmpty;

	private MsgAdapter adapter;

	private List<FriendMessageInfo> msgList = new ArrayList<FriendMessageInfo>();

	private List<Integer> checkList = new ArrayList<Integer>();
	public boolean isDelete;

	private Button leftBtn;

	private Thread thread;

	private FinalDb db;

	private String lastTime;

	private PreferenceOperateUtils pou;

	private String nowTalkUser = ""; // 当前对话用户
	private String nowTalkGroup = "";// 当前对话群

	@Override
	public void addChildView(ViewGroup contentLayout) {
		// TODO Auto-generated method stub
		super.addChildView(contentLayout);
		view = mInflater.inflate(R.layout.fragment_message, null);
		FinalActivity.initInjectedView(this, view);
		contentLayout.addView(view, layoutParamsFF);
		setTitleText("消息");
		addRightButtonView(R.drawable.button_delete_selector)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!isDelete) {
							isDelete = true;
						} else {
							if (checkList.size() == 0) {
								isDelete = false;
							} else {
								deleteMessage();
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
		leftBtn = addLeftButtonView(R.drawable.button_ignore_selector);
		leftBtn.setText("忽略未读");
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ignoreMessage();
			}
		});
		db = FinalDb.create(act);
		init();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setTitleLayoutBackground(R.drawable.top_bg_1);
		nowTalkGroup = "";
		nowTalkUser = "";
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		thread.interrupt();
	}

	private void init() {
		listView.setEmptyView(mEmpty);
		adapter = new MsgAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);

		pou = new PreferenceOperateUtils(act);
		lastTime = pou.getString(mApplication.getLoginbean().user_id, "");
		// if (StrUtil.isEmpty(lastTime)) {
		// lastTime = mApplication.getLoginbean().now_time;
		// }

		List<FriendMessageInfo> list = db.findAllByWhere(
				FriendMessageInfo.class,
				"myUserId = '" + mApplication.getLoginbean().user_id + "'");

		if (list != null && list.size() > 0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				msgList.add(list.get(i));
			}
			adapter.notifyDataSetChanged();
		}

		thread = new Thread() {
			@Override
			public void run() {
				Thread.currentThread();
				while (!Thread.interrupted()) {
					if (!isDelete) {
						loadMessage();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						Log.d("thread", "thread running");
					}
				}
			};
		};
		thread.start();
	}

	private class MsgAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return msgList.size();
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
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_messagelist,
						null, false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.iv_midLine = (ImageView) convertView
						.findViewById(R.id.midLine);
				holder.newNotice = (ImageView) convertView
						.findViewById(R.id.newNotice);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.rightLine = convertView.findViewById(R.id.rightline);
				holder.ll_agree = (LinearLayout) convertView
						.findViewById(R.id.ll_agree);
				holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
				holder.tv_agree = (Button) convertView
						.findViewById(R.id.tv_agree);
				holder.tv_disagree = (Button) convertView
						.findViewById(R.id.tv_disagree);
				holder.cb_delete = (CheckBox) convertView
						.findViewById(R.id.cb_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final FriendMessageInfo item = msgList.get(arg0);
			final String friendId; // 发送方id
			final String groupId; // 发送方id

			// 0 好友消息 ；1 群组消息 ；2 好友申请 ；3群组申请
			if (item.getType() == 2 || item.getType() == 3) {
				friendId = item.getUser_id();
				groupId = item.getGroup_id();
				holder.ll_agree.setVisibility(View.VISIBLE);
				holder.tv_distance.setText("申请消息");
				holder.iv_midLine.setVisibility(View.GONE);
				holder.tv_distance.setTextColor(getResources().getColor(
						R.color.red));
				holder.newNotice.setVisibility(View.INVISIBLE);
			} else {
				groupId = null;
				if (item.getType() == 0) {
					if (item.getUser_id().equals(
							mApplication.getLoginbean().user_id)) {
						friendId = item.getFriend_id();
					} else {
						friendId = item.getUser_id();
					}
				} else {
					friendId = item.getGroup_id();
				}
				holder.iv_midLine.setVisibility(View.VISIBLE);
				holder.ll_agree.setVisibility(View.GONE);
				holder.tv_distance.setText(item.getDistance() + "m");
				holder.tv_distance.setTextColor(getResources().getColor(
						R.color.gray));
				if ("1".equals(item.getStatus())) {
					holder.newNotice.setVisibility(View.INVISIBLE);
				} else {
					holder.newNotice.setVisibility(View.VISIBLE);
				}
			}
			holder.tv_name.setText(item.getUser_name());
			holder.tv_time.setText(item.getAdd_time());
			holder.tv_content.setText(item.getDescription());
			String image = item.getUser_ico();
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}
			if (isDelete) {
				holder.rightLine.setVisibility(View.VISIBLE);
				holder.cb_delete.setVisibility(View.VISIBLE);
			} else {
				holder.cb_delete.setVisibility(View.GONE);
				holder.rightLine.setVisibility(View.GONE);
			}

			if (checkList.contains(arg0)) {
				holder.cb_delete.setChecked(true);
			} else {
				holder.cb_delete.setChecked(false);
			}

			holder.cb_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (checkList.contains(arg0)) {
						checkList.remove((Object) arg0);
					} else {
						checkList.add(arg0);
					}
					notifyDataSetChanged();
				}
			});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(act, ChatActivity.class);
					intent.putExtra("name", item.getUser_name());
					if (item.getType() == 1) {
						intent.putExtra("isGroup", true);
						intent.putExtra("groupId", friendId);
						nowTalkGroup = friendId;
					} else {
						intent.putExtra("friendId", friendId);
						nowTalkUser = friendId;
					}
					if (msgList.get(arg0).getStatus().equals("0")) {
						msgList.get(arg0).setStatus("1");
						db.update(msgList.get(arg0));
						notifyDataSetChanged();
						setNoReadCount();
					}
					startActivity(intent);
				}
			});
			holder.tv_agree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (item.getType() == 2) {
						agreeFriend(friendId, null, 1);
					} else {
						agreeFriend(friendId, groupId, 1);
					}
					isDelete = false;
					checkList.clear();
					adapter.notifyDataSetChanged();
					checkList.add(arg0);
				}
			});
			holder.tv_disagree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (item.getType() == 2) {
						agreeFriend(friendId, null, 2);
					} else {
						agreeFriend(friendId, groupId, 2);
					}
					isDelete = false;
					checkList.clear();
					adapter.notifyDataSetChanged();
					checkList.add(arg0);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			ImageView iv_midLine;
			TextView tv_name;
			TextView tv_time;
			TextView tv_distance;
			TextView tv_content;
			RelativeLayout rl;
			LinearLayout ll_agree;
			Button tv_agree;
			Button tv_disagree;
			CheckBox cb_delete;
			View rightLine;
			ImageView newNotice;
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadMessage();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	private void loadMessage() {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		if (mApplication.mLocation != null) {
			params.put("long", mApplication.mLocation.getLongitude() + "");
			params.put("lati", mApplication.mLocation.getLatitude() + "");
		}
		params.put("lastTime", lastTime);

		UIDataProcess.reqData(UserMessage.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						UserMessage data = (UserMessage) arg0;
						if (data.flg == 1) {
							if (StrUtil.isEmpty(lastTime)) {
								if (data.data != null) {
									for (FriendMessageInfo item : data.data) {// 好友申请
										item.setType(2);
										item.setStatus("1");
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 2 and user_id = '"
														+ item.getUser_id()
														+ "'");
										db.save(item);
										lastTime = item.getAdd_time();
									}
								}
								if (data.groupsApplyInfo != null) {// 群主申请
									for (FriendMessageInfo item : data.groupsApplyInfo) {
										item.setType(3);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										item.setStatus("1");
										// msgList.add(item);
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 3 and user_id = '"
														+ item.getUser_id()
														+ "' and group_id = '"
														+ item.getGroup_id()
														+ "'");
										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								if (data.friendMessageInfo != null) {
									for (FriendMessageInfo item : data.friendMessageInfo) {
										item.setType(0);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										item.setStatus("1");
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 0 and user_name = '"
														+ item.getUser_name()
														+ "'");
										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								if (data.groupMessageInfo != null) {
									for (FriendMessageInfo item : data.groupMessageInfo) {
										item.setType(1);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										item.setStatus("1");
										// msgList.add(item);
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 1 and group_id = '"
														+ item.getGroup_id()
														+ "'");
										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								List<FriendMessageInfo> temp = db
										.findAllByWhere(
												FriendMessageInfo.class,
												"myUserId = '"
														+ mApplication
																.getLoginbean().user_id
														+ "'");
								if (temp != null && temp.size() > 0) {
									for (int i = temp.size() - 1; i >= 0; i--) {
										msgList.add(temp.get(i));
									}
								}
								// msgList.addAll(db.findAllByWhere(
								// FriendMessageInfo.class,
								// "myUserId = '"
								// + mApplication.getLoginbean().user_id
								// + "'"));
								adapter.notifyDataSetChanged();
								setNoReadCount();
							} else { // 0 好友消息; 1 群组消息; 2好友申请; 3 群组申请
								if (data.data != null) {
									for (FriendMessageInfo item : data.data) { // 好友申请
										item.setType(2);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										item.setStatus("0");
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 2 and user_id = '"
														+ item.getUser_id()
														+ "'");
										db.save(item);
										lastTime = item.getAdd_time();
									}
								}
								if (data.groupsApplyInfo != null) {
									for (FriendMessageInfo item : data.groupsApplyInfo) {
										item.setType(3);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										item.setStatus("0");
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 3 and user_id = '"
														+ item.getUser_id()
														+ "' and group_id = '"
														+ item.getGroup_id()
														+ "'");
										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								if (data.friendMessageInfo != null) {
									for (FriendMessageInfo item : data.friendMessageInfo) {
										item.setType(0);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										if (item.getUser_id()
												.equals(mApplication
														.getLoginbean().user_id)) {
											item.setStatus("1");
										} else {
											if (nowTalkUser.equals(item
													.getUser_id())) {
												item.setStatus("1");
											} else {
												item.setStatus("0");
											}
										}

										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 0 and user_name = '"
														+ item.getUser_name()
														+ "'");

										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								if (data.groupMessageInfo != null) {
									for (FriendMessageInfo item : data.groupMessageInfo) {
										item.setType(1);
										item.setMyUserId(mApplication
												.getLoginbean().user_id);
										if (MyApp.isNoticeGroup) {
											if (item.getUser_i()
													.equals(mApplication
															.getLoginbean().user_name)) {
												item.setStatus("1");
											} else {
												if (nowTalkGroup.equals(item
														.getGroup_id())) {
													item.setStatus("1");
												} else {
													item.setStatus("0");
												}
											}
										} else {
											item.setStatus("1");
										}
										db.deleteByWhere(
												FriendMessageInfo.class,
												"type = 1 and group_id = '"
														+ item.getGroup_id()
														+ "'");
										db.save(item);
										if (Util.compare2Date(
												item.getAdd_time(), lastTime)) {
											lastTime = item.getAdd_time();
										}
									}
								}
								List<FriendMessageInfo> temp = db
										.findAllByWhere(
												FriendMessageInfo.class,
												"myUserId = '"
														+ mApplication
																.getLoginbean().user_id
														+ "'");
								if (temp != null && temp.size() > 0) {
									msgList.clear();
									for (int i = temp.size() - 1; i >= 0; i--) {
										msgList.add(temp.get(i));
									}
								}
								// msgList.clear();
								// msgList.addAll(db.findAllByWhere(
								// FriendMessageInfo.class,
								// "myUserId = '"
								// + mApplication.getLoginbean().user_id
								// + "'"));
								adapter.notifyDataSetChanged();
								setNoReadCount();
							}
							pou.setString(mApplication.getLoginbean().user_id,
									lastTime);
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
					}
				});
	}

	/**
	 * 从朋友消息里查找是否已有用户消息
	 * 
	 * @param userId
	 * @return
	 */
	private int searchFromFriendMsg(String userId) {

		for (int i = 0; i < msgList.size(); i++) {
			FriendMessageInfo item = msgList.get(i);
			if (item.getType() == 0) {
				if (item.getUser_name().equals(userId)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从群组消息里查找是否已有用户消息
	 * 
	 * @param userId
	 * @return
	 */
	private int searchFromGroupMsg(String userId) {

		for (int i = 0; i < msgList.size(); i++) {
			FriendMessageInfo item = msgList.get(i);
			if (item.getType() == 1) {
				if (item.getGroup_id().equals(userId)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param friendId
	 * @param type
	 *            1 同意 2拒绝
	 */
	private void agreeFriend(String friendId, String gourpId, int type) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		if (StrUtil.isEmpty(gourpId)) {
			params.put("user_id", mApplication.getLoginbean().user_id);
			params.put("friend_id", friendId);
		} else {
			params.put("user_id", friendId);
			params.put("group_id", gourpId);
		}
		params.put("type", type);
		UIDataProcess.reqData(AgreeFriend.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						AgreeFriend data = (AgreeFriend) arg0;
						if (data.flg == 1) {
							// showToast("请求成功");
							db.delete(msgList.get(checkList.get(0)));
							msgList.remove((int) checkList.get(0));
							checkList.clear();
							setNoReadCount();
							adapter.notifyDataSetChanged();
						} else if (data.flg == 2) {
							showToast("已经发送过，处理中...");
							checkList.clear();
						} else {
							showToast("数据请求失败");
							checkList.clear();
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
						showToast("数据请求失败");
						checkList.clear();
					}
				});
	}

	/**
	 * 更新消息框上未读提示
	 */
	private void setNoReadCount() {

		int count = 0;
		for (FriendMessageInfo info : msgList) {
			if ("0".equals(info.getStatus())) {
				count++;
			}
		}
		try {
			((PageFrameActivity) getActivity()).setBadge(count);
		} catch (Exception e) {
			return;
		}

	}

	/**
	 * 忽略未读
	 */
	private void ignoreMessage() {

		for (int i = 0; i < msgList.size(); i++) {
			msgList.get(i).setStatus("1");
			FriendMessageInfo info = msgList.get(i);
			db.update(info);
		}
		setNoReadCount();
		adapter.notifyDataSetChanged();
	}

	/**
	 * 删除消息
	 */
	private void deleteMessage() {
		Integer[] inte = checkList.toArray(new Integer[0]);
		Arrays.sort(inte);

		for (int i = inte.length - 1; i >= 0; i--) {
			int position = inte[i];
			db.delete(msgList.get(position));
			msgList.remove(position);
		}
		isDelete = false;
		checkList.clear();
		setNoReadCount();
		adapter.notifyDataSetChanged();
	}

	/**
	 * 返回键处理 隐藏删除
	 */
	public void doInBackPress() {
		isDelete = false;
		checkList.clear();
		adapter.notifyDataSetChanged();
	}

	// private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// };

}
