package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import android.os.Bundle;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;

public class UserFavorActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_favor);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("Œ“µƒ ’≤ÿ");
		init();
	}

	private void init(){
		
	}
}
