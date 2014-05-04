package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.FreeGroup;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.PreferenceOperateUtils;

/**
 * 群组设置
 * 
 * @author Dean
 */
public class GroupSettingActivity extends BaseActivity {

	@ViewInject(id = R.id.switchbutton)
	private CheckBox checkBox;
	@ViewInject(id = R.id.ll_manageMember, click = "onClick")
	private LinearLayout ll_manage;
	@ViewInject(id = R.id.btn_breakGroup, click = "onClick")
	private Button btn_break;
	
	private String groupId;
	private PreferenceOperateUtils pou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_groupsetting);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("群组设置");
		groupId = getIntent().getStringExtra("groupId");
		addRightButtonView(R.drawable.button_groupspace_selector).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GroupSettingActivity.this, GroupSpaceActivity.class);
				intent.putExtra("groupId", groupId);
				startActivity(intent);
			}
		});
		pou = new PreferenceOperateUtils(this);
		if(MyApp.isNoticeGroup){
			checkBox.setChecked(true);
		}
		init();
	}

	private void init() {
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					pou.setBoolean("noticeGroupSP", true);
					MyApp.isNoticeGroup = true;
				}else{
					pou.setBoolean("noticeGroupSP", false);
					MyApp.isNoticeGroup = false;
				}
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_breakGroup: //解散群組
			dialogOpen("提示", "确认解散群组吗？", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					breakGroup();
				}
			});
			
			break;
		case R.id.ll_manageMember:
			Intent intent = new Intent(GroupSettingActivity.this, GroupMemberActivity.class);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	private void breakGroup(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		UIDataProcess.reqData(FreeGroup.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						FreeGroup data = (FreeGroup) arg0;
						if (data.flg == 1) {
							showToast("解散群组成功");
							setResult(RESULT_OK);
							finish();
						} else if (data.flg == 2) {
							showToast("您已经发出过请求");
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