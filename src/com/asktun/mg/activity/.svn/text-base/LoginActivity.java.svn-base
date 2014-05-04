package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.LoginBean;
import com.asktun.mg.utils.UpdateManager;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.Md5;
import com.chen.jmvc.util.PreferenceOperateUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * 登录
 * 
 * @author Dean
 */
public class LoginActivity extends BaseActivity {

	@ViewInject(id = R.id.username)
	private EditText userName;
	@ViewInject(id = R.id.password)
	private EditText password;
	@ViewInject(id = R.id.btn_login, click = "btnClick")
	private Button loginbtn;
	@ViewInject(id = R.id.btn_register, click = "btnClick")
	private Button registerbtn;
	private PreferenceOperateUtils pou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
		mApplication.addActivity(this);
		pou = new PreferenceOperateUtils(LoginActivity.this);
		userName.setText(pou.getString("username", ""));
		password.setText(pou.getString("password", ""));

		MyApp.isSoundEnable = pou.getBoolean("soundSP", true);
		MyApp.isVibrateEnable = pou.getBoolean("vibrateSP", false);
		MyApp.isVibrateEnable = pou.getBoolean("noticeGroupSP", false);
		int size = userName.getText().length();
		userName.setSelection(size);
		// update();
		UpdateManager.getUpdateManager().checkAppUpdate(this, null); // 检查更新
	}

	private void reqLogin() {

		final String uname = userName.getText().toString();
		final String pwd = password.getText().toString();
		
		int length = uname.length();
		if(length>16){
			showToastInThread("用户名长度不能大于16");
			return;
		}
		if (uname.equals("")) {
			showToastInThread("用户名不能为空");
			return;
		}
		if (pwd.equals("")) {
			showToastInThread("密码不能为空");
			return;
		}

		if (!Util.isNetConnected(this)) {
			showToast("当前网络不可用，请检查你的网络设置");
			return;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", uname);
		params.put("user_pass", Md5.MD5(pwd));

		UIDataProcess.reqData(LoginBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						LoginBean data = (LoginBean) arg0;
						if (data != null) {
							if (data.flg == 2) {
								showToast("不存在该用户");
							} else if (data.flg == 3) {
								showToast("账号被禁用");
							} else if (data.flg == 4) {
								showToast("密码错误");
							} else if (data.flg == -1) {
								showToast("缺少参数");
							} else {
								showToast("登录成功");
								pou.setString("username", uname);
								pou.setString("password", pwd);
								mApplication.setLoginbean(data);
								mApplication.startLocation();
								startActivity(PageFrameActivity.class);
								finish();
							}
						} else {
							showToast("无法连接到服务器");
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
						showToast("无法连接到服务器");
					}
				});

	}

	public void btnClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_login:
			reqLogin();
			break;
		case R.id.btn_register:
			startActivity(RegisterActivity.class);
			break;
		}
	}

	private void update() {
		// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
		com.umeng.common.Log.LOG = true;

		UmengUpdateAgent.setUpdateOnlyWifi(false); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				// TODO Auto-generated method stub
				switch (updateStatus) {
				case 0: // has update
					Log.i("--->", "callback result");
					UmengUpdateAgent.showUpdateDialog(LoginActivity.this,
							updateInfo);
					break;
				case 1: // has no update
					// Toast.makeText(LoginActivity.this, "没有更新",
					// Toast.LENGTH_SHORT)
					// .show();
					break;
				case 2: // none wifi
					// Toast.makeText(LoginActivity.this, "没有wifi连接， 只在wifi下更新",
					// Toast.LENGTH_SHORT)
					// .show();
					break;
				case 3: // time out
					// Toast.makeText(LoginActivity.this, "超时",
					// Toast.LENGTH_SHORT)
					// .show();
					break;
				case 4: // is updating
					/*
					 * Toast.makeText(LoginActivity.this, "正在下载更新...",
					 * Toast.LENGTH_SHORT) .show();
					 */
					break;
				}
			}
		});
		// UmengUpdateAgent.setDownloadListener(new UmengDownloadListener(){
		//
		// @Override
		// public void OnDownloadStart() {
		// Toast.makeText(LoginActivity.this, "download start" ,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void OnDownloadUpdate(int progress) {
		// Toast.makeText(LoginActivity.this, "download progress : " + progress
		// + "%" , Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void OnDownloadEnd(int result, String file) {
		// //Toast.makeText(LoginActivity.this, "download result : " + result ,
		// Toast.LENGTH_SHORT).show();
		// Toast.makeText(LoginActivity.this, "download file path : " + file ,
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		// UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
		//
		// @Override
		// public void onClick(int status) {
		// switch (status) {
		// case UpdateStatus.Update:
		// Toast.makeText(LoginActivity.this, "User chooses update." ,
		// Toast.LENGTH_SHORT).show();
		// break;
		// case UpdateStatus.Ignore:
		// Toast.makeText(LoginActivity.this, "User chooses ignore." ,
		// Toast.LENGTH_SHORT).show();
		// break;
		// case UpdateStatus.NotNow:
		// Toast.makeText(LoginActivity.this, "User chooses cancel." ,
		// Toast.LENGTH_SHORT).show();
		// break;
		// }
		// }
		// });
		UmengUpdateAgent.forceUpdate(LoginActivity.this);
	}

}