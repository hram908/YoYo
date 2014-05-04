package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.CommentListBean;
import com.asktun.mg.bean.CommentListBean.CommentInfo;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.IResponseListener;

public class UserCommentActivity extends BaseActivity {
	
	@ViewInject(id = R.id.comment_list)
	private XListView listView;
	
	private CommentAdapter adapter;
	
	private List<CommentInfo> ciList = new ArrayList<CommentInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_comment);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		setTitleText("我的评价");
		init();
		getCommentList();
	}

	private void init(){
		adapter = new CommentAdapter();
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
	}
	
	private class CommentAdapter extends BaseAdapter {

		// private List<ActiveListItem> list = new ArrayList<ActiveListItem>();
		//
		// public void add(ActiveListItem o) {
		// list.add(o);
		// }
		//
		// public void clear() {
		// list.clear();
		// }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ciList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return ciList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_comment, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.item_comment_icon);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.item_comment_name);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.item_comment_date);
				holder.tv_comment = (TextView) convertView
						.findViewById(R.id.item_comment_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CommentInfo ci = ciList.get(position);
			if(ci != null){
				imageLoader.displayImage(ci.getGame_ico(),holder.iv, options_round10);
				holder.tv_name.setText(ci.getGame_name());
				holder.tv_date.setText(ci.getAdd_time());
				holder.tv_comment.setText(ci.getAppraise());
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
			TextView tv_date;
			TextView tv_comment;
		}

	}
	
	private void getCommentList(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		UIDataProcess.reqData(CommentListBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						CommentListBean data = (CommentListBean) arg0;
						if (data != null) {
							if(data.getFlg() == 1){
								ciList = data.getData();
								adapter.notifyDataSetChanged();
							}
						} else {
							showToast("无法获取数据");
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
						showToast("无法获取数据");
					}
				});
	}
}
