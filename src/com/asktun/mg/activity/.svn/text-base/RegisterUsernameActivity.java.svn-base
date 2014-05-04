package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.chen.jmvc.util.StrUtil;

/**
 * 注册用户名
 * 
 * @author Dean
 */
public class RegisterUsernameActivity extends BaseActivity {

	@ViewInject(id = R.id.username)
	private EditText et_username;
	@ViewInject(id = R.id.rg_sex)
	private RadioGroup rg_sex;

	private String phone;
	private String pwd;
	private int sex = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register_username);
		mApplication.addActivity(this);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		phone = getIntent().getStringExtra("phone");
		pwd = getIntent().getStringExtra("pwd");

		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radioButton1:
					sex = 1;
					break;
				case R.id.radioButton2:
					sex = 2;
					break;

				default:
					break;
				}
			}
		});
		addRightButton("下一步").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = et_username.getText().toString();
				if (StrUtil.isEmpty(username)) {
					showToast("请输入用户名");
					return;
				}
				Intent intent = new Intent(RegisterUsernameActivity.this,
						RegisterBirthdayActivity.class);
				intent.putExtra("phone", phone);
				intent.putExtra("pwd", pwd);
				intent.putExtra("username", username);
				intent.putExtra("sex", sex);
				startActivity(intent);
			}
		});
		setTitleText("设置用户名(3/5)");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mApplication.removeActivity(this);
	}

}