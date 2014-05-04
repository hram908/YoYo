package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.AddActive;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.DateUtil;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * �����
 * 
 * @author Dean
 */
public class CreatActiveActivity extends BaseActivity {

	@ViewInject(id = R.id.et_time)
	private EditText et_time;
	@ViewInject(id = R.id.et_content)
	private EditText et_content;

	private View mTimeView1 = null; // ������view
	
	private String groupId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_createactivity);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("�����");
		groupId = getIntent().getStringExtra("groupId");
		addRightButtonView(R.drawable.button_gou_selector).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addActive();
			}
		});
		init();
	}

	private void init() {
		// mTimeView1 = mInflater.inflate(R.layout.choose_three, null);
		// initWheelDate(mTimeView1, et_time);
	}

//	public void onClick(View view) {
//		showDialog(Constant.DIALOGBOTTOM, mTimeView1, 40);
//	}
	
	private void addActive(){
		String time = et_time.getText().toString();
		String content = et_content.getText().toString();
		if(StrUtil.isEmpty(time)){
			showToast("������ʱ��");
			return;
		}
		if(StrUtil.isEmpty(content)){
			showToast("����������");
			return;
		}
		if(!Util.isDate(time, "yyyy-MM-dd")){
			showToast("����д��ȷ��ʽʱ��");
			return;
		}
		if(!DateUtil.compare2NowDate(time)){
			showToast("����ڲ���С�ڵ�������");
			return;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("group_id", groupId);
		params.put("end_time", time);
		params.put("info", content);
		UIDataProcess.reqData(AddActive.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						AddActive data = (AddActive) arg0;
						if (data.flg == 1) {
							showToast("�ɹ������");
							setResult(RESULT_OK);
							finish();
						} else if (data.flg == -3) {
							showToast("û��Ȩ��");
						}else {
							showToast("����ʧ��");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("��ȡ����ʧ��");
					}
				});
	}
	

}