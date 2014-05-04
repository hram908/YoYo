package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.PasswordModifyBean;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.Md5;

public class UserSetPasswdActivity extends BaseActivity {
	@ViewInject (id = R.id.member_info_oldpsw)
	private EditText oldpsd;
	@ViewInject (id = R.id.member_info_newpsw)
	private EditText newpsd;
	@ViewInject (id = R.id.member_info_newpsw_confirm)
	private EditText newpsdConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_set_passwd);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		Button btn = new Button(this);
//		btn.setText("保存");
		btn.setBackgroundResource(R.drawable.ok_hook_btn_selector);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				modifyPsd();
			}
		});
		addRightView(btn);
		setTitleText("密码设置");
		init();
	}

	private void init(){
		
	}
	
	private void modifyPsd(){
		String oldStr = oldpsd.getText().toString();
		String newStr = newpsd.getText().toString();
		String newStrCon = newpsdConfirm.getText().toString();
		
		if(oldStr.length() > 0){
			if(newStr.length() < 6 || newStr.length() > 16) {
				showToast("新密码必须为6-16位字符");
				return;
			}
			if(!newStrCon.equals(newStr)) {
				showToast("密码输入不一致");
				return;
			}
		}else {
			showToast("密码不能为空");
			return;
		}
		Util.hideKeyboard(this);
		setPsd(oldStr, newStr);
	}
	
	private void setPsd(String old,String newpsd) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("oldpsd", Md5.MD5(old));
		params.put("newpsd", Md5.MD5(newpsd));
		UIDataProcess.reqData(PasswordModifyBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						PasswordModifyBean data = (PasswordModifyBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								showToast("修改成功");
							} else if (data.getFlg() == 0) {
								showToast("修改失败");
							} else if (data.getFlg() == 2) {
								showToast("密码错误");
							}
						} else {
							showToast("操作失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
						showToast("操作失败");
					}
				});
	}
}
