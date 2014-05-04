package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.CheckphoneBean;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * ע�� ��֤�ֻ�
 * 
 * @author Dean
 */
public class RegisterPhoneActivity extends BaseActivity {

	@ViewInject(id = R.id.et_phone)
	private EditText et_phone;
	@ViewInject(id = R.id.et_code)
	private EditText et_code;
	@ViewInject(id = R.id.btn_code, click = "btnClick")
	private Button btn_code;

	private String code;
	private String phone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register_phone);
		mApplication.addActivity(this);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		
		addRightButton("��һ��").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone = et_phone.getText().toString().trim();
				String code = et_code.getText().toString();
				if (StrUtil.isEmpty(phone)) {
					showToast("�������ֻ�����");
					return;
				}
				if (!StrUtil.isMobileNo(phone)) {
					showToast("��������ȷ�ֻ�����");
					return;
				}
				if (StrUtil.isEmpty(code)) {
					showToast("��������֤��");
					return;
				}
				if (!phone.equals(RegisterPhoneActivity.this.phone)) {
					showToast("��������ȷ�ֻ�����");
					return;
				}
				if (!code.equals(RegisterPhoneActivity.this.code)) {
					showToast("��֤�벻��ȷ������������");
					return;
				}
				Intent intent = new Intent(RegisterPhoneActivity.this,
						RegisterPasswordActivity.class);
				intent.putExtra("phone", phone);
				startActivity(intent);
			}
		});
		setTitleText("��֤�ֻ�(1/5)");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mApplication.removeActivity(this);
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_code:
			String phone = et_phone.getText().toString();
			if (StrUtil.isEmpty(phone)) {
				showToast("�������ֻ�����");
				return;
			}
			if (!StrUtil.isMobileNo(phone)) {
				showToast("��������ȷ�ֻ�����");
				return;
			}
			getCode(phone);
			break;
		default:
			break;
		}
	}

	private void getCode(final String phone) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phone);
		UIDataProcess.reqData(CheckphoneBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						CheckphoneBean data = (CheckphoneBean) arg0;
						if (data != null && data.flg == 1) {
							showToast("�����ͳɹ�");
							code = data.data.code;
							RegisterPhoneActivity.this.phone = phone;
						} else {
							showToast("��������ʧ��");
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
						showToast("��������ʧ��");
					}
				});
	}

}