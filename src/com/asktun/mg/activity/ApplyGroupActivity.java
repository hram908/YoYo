package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.GroupApply;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 申请入群
 * 
 * @author Dean
 */
public class ApplyGroupActivity extends BaseActivity {

	private String groupId;

	@ViewInject(id = R.id.et_content)
	private EditText et_content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_applygroup);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		addRightButtonView(R.drawable.button_gou_selector).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						groupApply();
					}
				});
		groupId = getIntent().getStringExtra("groupId");
		setTitleText("申请验证");
	}

	private void groupApply() {
		String content = et_content.getText().toString();
		if(StrUtil.isEmpty(content)){
			showToast("请输入申请理由");
			return;
		}
		Util.hideKeyboard(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		params.put("content", content);
		UIDataProcess.reqData(GroupApply.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						GroupApply data = (GroupApply) arg0;
						if (data.flg == 1) {
							showToast("申请成功");
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

}