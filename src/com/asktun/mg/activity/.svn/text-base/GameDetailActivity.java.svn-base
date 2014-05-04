package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.CheckGameDownload;
import com.asktun.mg.bean.CommentOfGameBean;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.CommentOfGameBean.CommentOfGame;
import com.asktun.mg.fragment.GameDetailFragment;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.IResponseListener;

/**
 * 游戏详情
 * 
 * @author Dean
 */
public class GameDetailActivity extends BaseActivity {

	@ViewInject(id = R.id.rgroup)
	private RadioGroup rg;
	@ViewInject(id = R.id.game_detail_ll)
	private LinearLayout detailLayout;
	@ViewInject(id = R.id.game_detail_comments)
	private XListView commentLV;

	private List<CommentOfGame> ciList = new ArrayList<CommentOfGame>();
	private CommentAdapter adapter;

	private String gameId = null;
	private String gameName = null;
	private GameInfo gi;
	
	boolean isDownload = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_gamedetail);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		// setTitleText("三国杀");
		final Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.button_edit_selector);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkGameDownload();
			}
		});
		addRightView(btn);
		init();
//		showLoadingView();
		getCommentList("1");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		if (resultCode == RESULT_OK) {
			getCommentList("1");
		}
	}

	private void init() {
		Intent intent = getIntent();
		if (intent != null) {
			gameId = intent.getStringExtra("gameId");
			gameName = intent.getStringExtra("gameName");
			gi = (GameInfo) intent.getSerializableExtra("gameInfo");
			setTitleText(gameName);
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_1) {
					detailLayout.setVisibility(View.VISIBLE);
					commentLV.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_2) {
					commentLV.setVisibility(View.VISIBLE);
					detailLayout.setVisibility(View.GONE);
				}
			}
		});

		detailFragment = new GameDetailFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.game_detail_ll, detailFragment).commit();

		adapter = new CommentAdapter();
		commentLV.setAdapter(adapter);
		commentLV.setPullLoadEnable(false);
		commentLV.setPullRefreshEnable(false);

	}
	
	private GameDetailFragment detailFragment;

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
			return ciList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return ciList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_comment, null,
						false);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.item_comment_icon);
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
			CommentOfGame ci = ciList.get(position);
			if (ci != null) {
				imageLoader.displayImage(ci.getUser_ico(), holder.iv,
						options_round10);
				holder.tv_name.setText(ci.getUser_name());
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

	public String getGameId() {
		return gameId;
	}

	public GameInfo getGameInfo() {
		return gi;
	}

	private void getCommentList(String page) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("page", page);
		params.put("game_id", gameId);
		UIDataProcess.reqData(CommentOfGameBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						CommentOfGameBean data = (CommentOfGameBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								ciList = data.getData();
								adapter.notifyDataSetChanged();
							}
						} else {
							
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Object arg0) {
					}
				});
	}
	
	private void checkGameDownload() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("game_id", gameId);
		UIDataProcess.reqData(CheckGameDownload.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						CheckGameDownload data = (CheckGameDownload) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								Intent intent = new Intent(GameDetailActivity.this,
										GameCommentActivity.class);
								intent.putExtra("gameId", gameId);
								startActivityForResult(intent, 0);
							}else{
								showToast("下载后才能评论！");
							}
						} else {
							
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Object arg0) {
					}
				});
	}
	
	@Override
	public void RequestData() {
		super.RequestData();
		detailFragment.getGameInfo();
	}

}