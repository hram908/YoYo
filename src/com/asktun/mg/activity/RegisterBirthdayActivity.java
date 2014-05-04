package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.StrUtil;

/**
 * 注册用户名
 * 
 * @author Dean
 */
public class RegisterBirthdayActivity extends BaseActivity {

	@ViewInject(id = R.id.ll, click = "btnClick")
	private LinearLayout ll;
	@ViewInject(id = R.id.tv_birth)
	private TextView tv_birth;

	private String birthday;

	private String phone;
	private String pwd;
	private String username;
	private int sex;

	private View mTimeView1 = null; // 年月日view

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register_birthday);
		mApplication.addActivity(this);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		phone = getIntent().getStringExtra("phone");
		pwd = getIntent().getStringExtra("pwd");
		username = getIntent().getStringExtra("username");
		sex = getIntent().getIntExtra("sex", 0);

		addRightButton("下一步").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				birthday = tv_birth.getText().toString();
				if (StrUtil.isEmpty(birthday)) {
					showToast("请选择生日");
					return;
				}
				Intent intent = new Intent(RegisterBirthdayActivity.this,
						RegisterIconActivity.class);
				intent.putExtra("phone", phone);
				intent.putExtra("pwd", pwd);
				intent.putExtra("username", username);
				intent.putExtra("sex", sex);
				intent.putExtra("birthday", birthday);
				startActivity(intent);
			}
		});
		setTitleText("生日(4/5)");
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mApplication.removeActivity(this);
	}

	private void init() {
		mTimeView1 = mInflater.inflate(R.layout.choose_three, null);
		initWheelDate(mTimeView1, tv_birth);
		tv_birth.setText("");
	}

	public void btnClick(View view) {
		showDialog(Constant.DIALOGBOTTOM, mTimeView1, 40);
	}

}