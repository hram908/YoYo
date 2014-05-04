package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;

public class UserSetAboutActivity extends BaseActivity {

	@ViewInject(id = R.id.tv_version)
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_set_about);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("¹ØÓÚ");
		init();
	}

	private void init() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (info != null) {
			String curVersionName = info.versionName;
			tv_version.setText("°æ±¾ºÅ:    " + curVersionName);
		}
	}
}
