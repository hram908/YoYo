package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.utils.ExitApplication;

public class UserSetActivity extends BaseActivity {

	@ViewInject(id = R.id.user_set_msg, click = "btnClick")
	private LinearLayout setMsg;
	@ViewInject(id = R.id.user_set_passwd, click = "btnClick")
	private LinearLayout setPasswd;
	@ViewInject(id = R.id.user_set_feedback, click = "btnClick")
	private LinearLayout setFeedback;
	@ViewInject(id = R.id.user_set_about, click = "btnClick")
	private LinearLayout setAbout;
	@ViewInject(id = R.id.user_close, click = "btnClick")
	private ImageView userClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_set);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("∆‰À˚…Ë÷√");
		init();
	}

	private void init() {

	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.user_set_msg:
			startActivity(UserSetMsgActivity.class);
			break;
		case R.id.user_set_passwd:
			startActivity(UserSetPasswdActivity.class);
			break;
		case R.id.user_set_feedback:
			startActivity(UserSetFeedbackActivity.class);
			break;
		case R.id.user_set_about:
			startActivity(UserSetAboutActivity.class);
			break;
		case R.id.user_close:
			ExitApplication.getInstance().exit();
			break;
		default:
			break;
		}
	}
}
