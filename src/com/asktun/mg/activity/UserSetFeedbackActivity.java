package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.FeedbackBean;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;

public class UserSetFeedbackActivity extends BaseActivity {
	
	@ViewInject (id = R.id.feedback_et)
	private EditText feedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_set_feedback);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		Button btn = new Button(this);
//		btn.setText("提交");
		btn.setBackgroundResource(R.drawable.ok_hook_btn_selector);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				feedback();
			}
		});
		addRightView(btn);
		setTitleText("意见反馈");
		init();
	}

	private void init(){
		
	}
	
	private void feedback(){
		String content = feedback.getText().toString();
		if(content.length() == 0){
			showToast("写点什么吧");
			return;
		}
		Util.hideKeyboard(this);

		setFeedback(content);
	}
	
	private void setFeedback(String content) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("content", content);
		UIDataProcess.reqData(FeedbackBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						FeedbackBean data = (FeedbackBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								showToast("反馈成功");
								finish();
							} else if (data.getFlg() == 0) {
								showToast("反馈失败");
							}
						} else {
							showToast("操作失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
						showToast("操作失败");
					}
				});
	}
}
