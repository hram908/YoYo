package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.MemberLikes;
import com.asktun.mg.bean.MemberLikes.MemberLikesItem;
import com.chen.jmvc.util.ExitDoubleClick;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * ѡ��ϲ������Ϸ
 * 
 * @author Dean
 */
public class ChooseGameActivity extends BaseActivity {

	@ViewInject(id = R.id.gridview)
	private GridView gridview;

	private GameAdapter adapter;

	private Button rightView;

	private List<Integer> chooseList = new ArrayList<Integer>();
	private List<MemberLikesItem> gameList = new ArrayList<MemberLikesItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_choicegame);
		FinalActivity.initInjectedView(this);
		rightView = (Button) addRightButton("��һ��");
		rightView.setBackgroundResource(R.drawable.button_top_gray);
//		setBackgroundResource(R.drawable.button_topsmall_selector);
		rightView.setEnabled(false);
		rightView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rightClick();
			}
		});
		setTitleText("ѡ��ϲ������Ϸ");
		showLoadingView();
		init();
	}

	private void rightClick() {
		if (chooseList.size() == 0) {
			leftBack();
		} else {
			String gameId = "";
			for (int position : chooseList) {
				gameId += gameList.get(position).gt_id + ",";
			}
			gameId = gameId.substring(0, gameId.length() - 1);
			Intent intent = new Intent(this, SuggestGroupActivity.class);
			intent.putExtra("gameId", gameId);
			startActivity(intent);
			finish();
		}
	}

	private void init() {
		adapter = new GameAdapter();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (chooseList.contains(arg2)) {
					chooseList.remove((Object) arg2);
				} else {
					chooseList.add(arg2);
				}
				if (chooseList.size() > 0) {
					rightView.setEnabled(true);
					rightView.setBackgroundResource(R.drawable.button_topsmall_selector);
				} else {
					rightView.setEnabled(false);
					rightView.setBackgroundResource(R.drawable.button_top_gray);
				}
				adapter.notifyDataSetChanged();
			}
		});
		loadData();
	}

	public final float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };
	public final float[] BT_SELECTED = new float[] { 1, 0, 0, 0, 99, 0, 1, 0,
			0, 99, 0, 0, 1, 0, 99, 0, 0, 0, 1, 0 };
	public final static float[] BT_SELECTED1 = new float[] { 0.338f, 0.339f,
			0.332f, 0, 0, 0.338f, 0.339f, 0.332f, 0, 0, 0.338f, 0.339f, 0.332f,
			0, 0, 0, 0, 0, 1, 0 };

	private Bitmap bitmap2Gray(Bitmap bmSrc) {
		int width, height;
		height = bmSrc.getHeight();
		width = bmSrc.getWidth();
		Bitmap bmpGray = null;
		bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGray);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmSrc, 0, 0, paint);

		return bmpGray;
	}

	private class GameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gameList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_choosegame, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.gou = (ImageView) convertView.findViewById(R.id.gou);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_count = (TextView) convertView
						.findViewById(R.id.tv_count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final MemberLikesItem item = gameList.get(arg0);
			holder.tv_name.setText(item.game_name);
			holder.tv_count.setText(item.user_num);
			String image = item.game_ico;

			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options_round);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}
			// holder.iv.getDrawable().setColorFilter(
			// new ColorMatrixColorFilter(BT_SELECTED1));

			if (chooseList.contains(arg0)) {
				holder.gou.setVisibility(View.VISIBLE);
				// holder.iv.getDrawable().clearColorFilter();
				// holder.iv.getDrawable().setColorFilter(
				// new ColorMatrixColorFilter(BT_NOT_SELECTED));
			} else {
				// if (!StrUtil.isEmpty(image)) {
				// imageLoader.displayImage(image, holder.iv, options_game);
				// } else {
				// holder.iv.setBackgroundResource(R.drawable.icon_game);
				// }
				// holder.iv.getDrawable().setColorFilter(
				// new ColorMatrixColorFilter(BT_SELECTED1));
				holder.gou.setVisibility(View.GONE);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			ImageView gou;
			TextView tv_name;
			TextView tv_count;
		}

	}

	private void loadData() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		UIDataProcess.reqData(MemberLikes.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						MemberLikes data = (MemberLikes) arg0;
						if (data.flg == 1) {
							if (data.data != null && data.data.size() > 0) {
								gameList.clear();
								gameList.addAll(data.data);
								adapter.notifyDataSetChanged();
								gridview.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										gridview.setAdapter(adapter);
									}
								});
								hideLoadingView();
							}else{
								setErrorMessage();
							}
						} else {
//							showToast("��ȡ����ʧ��");
							setErrorMessage();
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
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
//						showToast("��ȡ����ʧ��");
						setErrorMessage();
					}
				});
	}

	// @Override
	// public void leftBack() {
	// // TODO Auto-generated method stub
	// startActivity(PageFrameActivity.class);
	// mApplication.clearActivity();
	// finish();
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ExitDoubleClick.getInstance(this).doDoubleClick(3000, null);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void RequestData() {
		// TODO Auto-generated method stub
		super.RequestData();
		loadData();
	}
	
}