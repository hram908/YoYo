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
 * 创建交友群组 或 游戏群组
 * 
 * @author Dean
 */
public class Add2GroupActivity extends BaseActivity {

	@ViewInject(id = R.id.rl_1, click = "btnClick")
	private RelativeLayout rl_1;
	@ViewInject(id = R.id.rl_2, click = "btnClick")
	private RelativeLayout rl_2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_creat_2group);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("创建群");
	}

	public void btnClick(View v) {
		Intent intent = new Intent(this, AddGorFSub2Activity.class);
		switch (v.getId()) {
		case R.id.rl_1:
			break;
		case R.id.rl_2: // 游戏群
			intent.putExtra("groupType", 2);
			break;
		default:
			break;
		}
		startActivityForResult(intent, 0);

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