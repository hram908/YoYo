package com.asktun.mg.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.view.SwitchButton;
import com.chen.jmvc.util.PreferenceOperateUtils;

public class UserSetMsgActivity extends BaseActivity {
	@ViewInject (id = R.id.msg_sb)
	private SwitchButton msgBtn;
	@ViewInject (id = R.id.msg_group_sb)
	private SwitchButton msgGroupBtn;
	@ViewInject (id = R.id.msg_activity_sb)
	private SwitchButton msgActivityBtn;
	@ViewInject (id = R.id.msg_sound_sb)
	private SwitchButton msgSoundBtn;
	@ViewInject (id = R.id.msg_vibrate_sb)
	private SwitchButton msgVibrateBtn;

	private Vibrator vibrator=null;
	
	private PreferenceOperateUtils pou;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_set_msg);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("消息通知");
		init();
	}

	private void init(){
		pou = new PreferenceOperateUtils(UserSetMsgActivity.this);
		msgSoundBtn.setChecked(pou.getBoolean("soundSP", true));
		msgVibrateBtn.setChecked(pou.getBoolean("vibrateSP", false));
		//创建vibrator对象       
		vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		msgSoundBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					MyApp.isSoundEnable = true;
					pou.setBoolean("soundSP", true);
				}else{
					MyApp.isSoundEnable = false;
					pou.setBoolean("soundSP", false);
				}
			}
		} );
		msgVibrateBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
//					vibrator.vibrate(1000);
					MyApp.isVibrateEnable = true;
					pou.setBoolean("vibrateSP", true);
				}else {
					MyApp.isVibrateEnable = false;
					pou.setBoolean("vibrateSP", false);
					//关闭震动                   
//					vibrator.cancel();
				}
			}
		});
	}
}
