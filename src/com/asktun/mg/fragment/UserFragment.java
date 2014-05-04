package com.asktun.mg.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.activity.UserCommentActivity;
import com.asktun.mg.activity.UserDownloadActivity;
import com.asktun.mg.activity.UserFavorActivity;
import com.asktun.mg.activity.UserInfoActivity;
import com.asktun.mg.activity.UserPointActivity;
import com.asktun.mg.activity.UserSetActivity;

/**
 * 我的
 * 
 * @Description
 * @author Dean
 * 
 */
public class UserFragment extends BaseFragment {

	private View view;
	@ViewInject(id = R.id.user_info, click = "btnClick")
	private LinearLayout userInfo;
	@ViewInject(id = R.id.user_download, click = "btnClick")
	private LinearLayout userDownload;
	@ViewInject(id = R.id.user_point, click = "btnClick")
	private LinearLayout userPoint;
	@ViewInject(id = R.id.user_comment, click = "btnClick")
	private LinearLayout userComment;
	@ViewInject(id = R.id.user_favor, click = "btnClick")
	private LinearLayout userFavor;
	@ViewInject(id = R.id.user_set, click = "btnClick")
	private LinearLayout userSet;
	

	@Override
	public void addChildView(ViewGroup contentLayout) {
		// TODO Auto-generated method stub
		super.addChildView(contentLayout);
		view = mInflater.inflate(R.layout.fragment_user, null);
		FinalActivity.initInjectedView(this, view);
		contentLayout.addView(view, layoutParamsFF);
		setTitleText("用户中心");
		init();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setTitleLayoutBackground(R.drawable.top_bg_1);
	}

	private void init() {

	}

	public void btnClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.user_info:
			intent = new Intent(act, UserInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.user_download:
			intent = new Intent(act, UserDownloadActivity.class);
			startActivity(intent);
			break;
		case R.id.user_point:
			intent = new Intent(act, UserPointActivity.class);
			startActivity(intent);
			break;
		case R.id.user_comment:
			intent = new Intent(act, UserCommentActivity.class);
			startActivity(intent);
			break;
		case R.id.user_favor:
			intent = new Intent(act, UserFavorActivity.class);
			startActivity(intent);
			break;
		case R.id.user_set:
			intent = new Intent(act, UserSetActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
