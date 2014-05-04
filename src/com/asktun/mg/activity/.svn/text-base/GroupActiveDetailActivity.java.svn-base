package com.asktun.mg.activity;

import java.util.HashMap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.DeleteActive;
import com.asktun.mg.bean.GroupZone.ZoneInfo;
import com.chen.jmvc.util.IResponseListener;

/**
 * 群组活动详情
 * 
 * @author Dean
 */
public class GroupActiveDetailActivity extends BaseActivity {

	private View dialogView;
	private Button btn_dialog1;
	private Button btn_dialog2;
	private Button btn_dialog3;
	private Button btn_dialog_cancel;

	@ViewInject(id = R.id.tv_time)
	private TextView tv_time;
	@ViewInject(id = R.id.tv_info)
	private TextView tv_info;

	private ZoneInfo zoneInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_groupactive_detail);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		addRightButtonView(R.drawable.button_delete_selector)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (zoneInfo != null)
							deleteActive(zoneInfo.zone_id);
					}
				});
		setTitleText("活动详情");
		zoneInfo = (ZoneInfo) getIntent().getSerializableExtra("zoneInfo");
		if (zoneInfo != null) {
			tv_time.setText(zoneInfo.add_time);
			tv_info.setText(zoneInfo.info);
		}
		init();
	}

	private void init() {
		// dialogView = mInflater.inflate(R.layout.dialog_groupspace_edit,
		// null);
		// btn_dialog1 = (Button) dialogView.findViewById(R.id.btn_dialog1);
		// btn_dialog2 = (Button) dialogView.findViewById(R.id.btn_dialog2);
		// btn_dialog3 = (Button) dialogView.findViewById(R.id.btn_dialog3);
		// btn_dialog_cancel = (Button)
		// dialogView.findViewById(R.id.btn_cancel);
		// btn_dialog2.setText("编辑活动");
		// btn_dialog3.setText("取消活动");
		// btn_dialog_cancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// removeDialog(Constant.DIALOGBOTTOM);
		// }
		// });
	}

	private void deleteActive(String zoneId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("zone_id", zoneId);
		UIDataProcess.reqData(DeleteActive.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						DeleteActive data = (DeleteActive) arg0;
						if (data.flg == 1) {
							showToast("成功取消活动");
							setResult(RESULT_OK);
							finish();
						} else if (data.flg == -3) {
							showToast("没有权限");
						} else {
							showToast("请求失败");
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
						showToast("获取数据失败");
					}
				});
	}

}