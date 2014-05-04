package com.asktun.mg.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.LoginBean;
import com.asktun.mg.bean.RegisterBean;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.Md5;
import com.chen.jmvc.util.PreferenceOperateUtils;
import com.chen.jmvc.util.StrUtil;

/**
 * 注册
 * 
 * @author Dean
 */
public class RegisterActivity extends BaseActivity {

	@ViewInject(id = R.id.username)
	private EditText et_username;
	@ViewInject(id = R.id.password)
	private EditText et_password;
	@ViewInject(id = R.id.repit_password)
	private EditText et_repitpassword;
	@ViewInject(id = R.id.regist_nan, click = "btnClick")
	private ImageView regist_nan;
	@ViewInject(id = R.id.regist_nv, click = "btnClick")
	private ImageView regist_nv;

	private int sex = 1;
	
	
	private PreferenceOperateUtils pou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("注册");
		pou = new PreferenceOperateUtils(this);
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.regist_nan:
			sex = 1;
			register();
			break;
		case R.id.regist_nv:
			sex = 2;
			register();
			break;
		default:
			break;
		}
	}

	private void register() {
		final String pwd1 = et_password.getText().toString();
		String pwd2 = et_repitpassword.getText().toString();
		final String username = et_username.getText().toString();
		if (StrUtil.isEmpty(username)) {
			showToast("请输入用户名");
			return;
		}
		if (pwd1.length() < 6 || pwd1.length() > 18) {
			showToast("密码长度必须为6-18位");
			return;
		}
		if (StrUtil.isEmpty(pwd1)) {
			showToast("请输入密码");
			return;
		}
		if (StrUtil.isEmpty(pwd2)) {
			showToast("请再次输入密码");
			return;
		}
		if (!pwd1.equals(pwd2)) {
			showToast("两次输入密码不一致");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", username);
		params.put("user_passwd", Md5.MD5(pwd1));
		params.put("imei", Util.getIMEI(this));
		params.put("user_gender", sex + "");
		params.put("gt_id", pou.getString("gameIds", ""));
		params.put("group_id", pou.getString("GroupIds", ""));
		UIDataProcess.reqData(RegisterBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						RegisterBean data = (RegisterBean) arg0;
						if (data != null) {
							if (data.flg == 2) {
								showToast("已存在该用户");
							} else if (data.flg == 0) {
								showToast("注册失败");
							} else if (data.flg == -1) {
								showToast("缺少参数");
							} else {
								showToast("注册成功");
								pou.setString("username", username);
								pou.setString("password", pwd1);
								pou.setBoolean("isFirstLogin", true);
								LoginBean bean = new LoginBean();
								bean.user_id = data.user_id;
								bean.token = data.token; 
								bean.user_name = data.user_name;
								bean.is_vip = data.is_vip;
								bean.now_time = data.now_time;
								mApplication.setLoginbean(bean);
								mApplication.startLocation();
								startActivity(PageFrameActivity.class);
								mApplication.clearActivity();
								finish();
							}
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
						showToast("无法连接服务器");
					}
				});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}