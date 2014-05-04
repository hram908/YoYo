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
import com.asktun.mg.bean.SendCommentBean;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

public class GameCommentActivity extends BaseActivity {

	private String gameId;

	@ViewInject(id = R.id.et_content)
	private EditText et_content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_applygroup);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		addRightButtonView(R.drawable.button_gou_selector).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gameComment();
					}
				});
		gameId = getIntent().getStringExtra("gameId");
		setTitleText("发表评论");
	}

	private void gameComment() {
		String content = et_content.getText().toString();
		if(StrUtil.isEmpty(content)){
			showToast("写点什么吧");
			return;
		}
		Util.hideKeyboard(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("game_id", gameId);
		params.put("appraise", content);
		UIDataProcess.reqData(SendCommentBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						SendCommentBean data = (SendCommentBean) arg0;
						if (data.flg == 1) {
							showToast("评论成功");
							setResult(RESULT_OK);
							finish();
						} else if (data.flg == -3) {
							showToast("您已经评论过");
						} else {
							showToast("评论失败");
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
						showToast("评论失败");
					}
				});
	}

}