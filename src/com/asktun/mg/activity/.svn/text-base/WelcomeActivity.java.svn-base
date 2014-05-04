package com.asktun.mg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.chen.jmvc.util.PreferenceOperateUtils;
import com.chen.jmvc.util.StrUtil;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 * @author Dean
 */
public class WelcomeActivity extends BaseActivity {

	private PreferenceOperateUtils pou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		pou = new PreferenceOperateUtils(this);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				
				String gameIds = pou.getString("gameIds", "");
				//final int version = Util.getAppVersionCode(WelcomeActivity.this);
				if (StrUtil.isEmpty(gameIds)) {
					redirectTo(ChooseGameActivity.class);
				} else {
					redirectTo(LoginActivity.class);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

	}

	/**
	 * 跳转到...
	 */
	private void redirectTo(Class classname) {
		Intent intent = new Intent(this, classname);
		startActivity(intent);
		finish();
	}

}