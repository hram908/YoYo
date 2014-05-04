package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.chen.jmvc.util.Md5;
import com.chen.jmvc.util.StrUtil;

/**
 * 注册密码
 * 
 * @author Dean
 */
public class RegisterPasswordActivity extends BaseActivity {

	@ViewInject(id = R.id.password)
	private EditText et_password;
	@ViewInject(id = R.id.repit_password)
	private EditText et_repitpassword;

	private String phone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register_password);
		mApplication.addActivity(this);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		phone = getIntent().getStringExtra("phone");
		addRightButton("下一步").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pwd1 = et_password.getText().toString();
				String pwd2 = et_repitpassword.getText().toString();
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
				Intent intent = new Intent(RegisterPasswordActivity.this,
						RegisterUsernameActivity.class);
				intent.putExtra("phone", phone);
				intent.putExtra("pwd", Md5.MD5(pwd1));
				startActivity(intent);
			}
		});
		setTitleText("设置密码(2/6)");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mApplication.removeActivity(this);
	}

}