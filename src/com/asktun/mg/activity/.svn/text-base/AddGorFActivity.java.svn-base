package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;

/**
 * 添加好友群组 或 创建群组
 * 
 * @author Dean
 */
public class AddGorFActivity extends BaseActivity {

	@ViewInject(id = R.id.rl_1, click = "btnClick")
	private RelativeLayout rl_1;
	@ViewInject(id = R.id.rl_2, click = "btnClick")
	private RelativeLayout rl_2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_addfriendorgroup);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("添加");
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.rl_1:
			startActivity(AddGorFSub1Activity.class);
			break;
		case R.id.rl_2:
			startActivityForResult(new Intent(AddGorFActivity.this,
					Add2GroupActivity.class), 0);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if (resultCode == Activity.RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}

	}

}