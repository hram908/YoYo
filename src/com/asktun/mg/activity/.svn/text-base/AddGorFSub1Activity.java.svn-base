package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.ApplyFriend;
import com.asktun.mg.bean.CheckExistBean;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 添加好友群组 或 创建群组
 * 
 * @author Dean
 */
public class AddGorFSub1Activity extends BaseActivity {

	@ViewInject(id = R.id.rl_1, click = "btnClick")
	private LinearLayout rl_1;
	@ViewInject(id = R.id.rl_2, click = "btnClick")
	private LinearLayout rl_2;
	@ViewInject(id = R.id.sub1_text)
	private EditText text;
	@ViewInject(id = R.id.sub1_button, click = "btnClick")
	private Button button;

	private int number = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_addfriendorgroup_sub1);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("添加");
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.rl_1:
			rl_1.setVisibility(View.GONE);
			rl_2.setVisibility(View.VISIBLE);
			number = 2;
			text.setHint("输入群号");
			break;
		case R.id.rl_2:
			rl_1.setVisibility(View.VISIBLE);
			rl_2.setVisibility(View.GONE);
			number = 1;
			text.setHint("输入账号");
			break;
		case R.id.sub1_button:
			String str = text.getText().toString();
			if (StrUtil.isEmpty(str)) {
				if (number == 1)
					showToast("请输入账号");
				else {
					showToast("请输入群号");
				}
				return;
			}
			if (number == 1) {
				// friend
				String friendId = text.getText().toString();
				if(friendId.equals(mApplication.getLoginbean().user_id)){
					showToast("这是你自己的号");
					return;
				}
				checkIsExist("user_id", friendId);
				
//				applyFriend(friendId);
				Util.hideKeyboard(this);
			} else {
				// group
				String groupId = text.getText().toString();
				checkIsExist("group_id", groupId);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 申请加好友
	 */
	private void applyFriend(String friendId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("friend_id", friendId);
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
							showToast("添加失败");
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
						showToast("添加失败");
					}
				});
	}
	
	/**
	 * @param userOrGroup  user_id   or   group_id
	 * @param str
	 */
	private void checkIsExist(String userOrGroup,final String str) {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		if("group_id".equals(userOrGroup)){
			params.put("my_id", mApplication.getLoginbean().user_id );
		}
		params.put(userOrGroup, str);
		UIDataProcess.reqData(CheckExistBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						CheckExistBean data = (CheckExistBean) arg0;
						if (data.getFlg() == 1) {
							if(number ==1){
								Intent intent = new Intent(AddGorFSub1Activity.this, NearPersonActivity.class);
								intent.putExtra("userId", str);
								intent.putExtra("ischeck", "1");
								startActivity(intent);
							}
						} else if(data.getFlg() == 3){
							if(number ==2){
								Intent intent_apply = new Intent(AddGorFSub1Activity.this,
										NearGroupActivity.class);
								intent_apply.putExtra("ischeck", "1");
								intent_apply.putExtra("groupId", str);
								startActivity(intent_apply);
							}
						} else if(data.getFlg() == 5){
							showToast("您已加入此群！");
						}
						else{
							showToast("您输入的账号不存在！");
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
						showToast("无法获取服务器信息");
					}
				});
	}

}