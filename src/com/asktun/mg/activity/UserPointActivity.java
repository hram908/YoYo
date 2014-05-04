package com.asktun.mg.activity;

import java.util.HashMap;
import java.util.List;

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
import com.asktun.mg.bean.UserPointListBean;
import com.asktun.mg.bean.UserPointListBean.PointListInfo;
import com.chen.jmvc.util.IResponseListener;

public class UserPointActivity extends BaseActivity {
	@ViewInject(id = R.id.point_total)
	private TextView totalPoint;
	@ViewInject(id = R.id.point_download)
	private TextView downloadPoint;
	@ViewInject(id = R.id.point_comment)
	private TextView commentPoint;
	@ViewInject(id = R.id.point_share)
	private TextView sharePoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_point);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		Button btn = new Button(this);
//		btn.setText("积分兑换");
		btn.setBackgroundResource(R.drawable.user_point_exchangebtn_selector);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showToast("积分商城敬请期待！");
			}
		});
//		addRightView(btn);
		setTitleText("我的积分");
		init();
		getUserPoint();
	}

	private void init(){
		
	}
	
	private void getUserPoint(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(UserPointListBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						UserPointListBean data = (UserPointListBean) arg0;
						if (data != null) {
							if(data.getFlg() == 0){
								//showToast("没有数据");
							}else if(data.getFlg() == 1){
								List<PointListInfo> piList = data.getData();
								for(PointListInfo pi : piList){
									if(pi.getLy().equals("1")){
										downloadPoint.setText(pi.getPoint());
										totalPoint.setText(pi.getTotal());
									}else if(pi.getLy().equals("3")){
										commentPoint.setText(pi.getPoint());
										totalPoint.setText(pi.getTotal());
									}else if(pi.getLy().equals("4")){
										sharePoint.setText(pi.getPoint());
										totalPoint.setText(pi.getTotal());
									}
								}
							}else if(data.getFlg() == -1){
//								showToast("缺少参数");
							}
						} else {
//							showToast("无法获取数据");
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
//						showToast("无法获取数据");
					}
				});
	}
}
