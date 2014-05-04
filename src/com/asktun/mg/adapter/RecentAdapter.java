package com.asktun.mg.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asktun.mg.R;
import com.asktun.mg.swipelistview.SwipeListView;

public class RecentAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private LinkedList<String> mData;
	private SwipeListView mListView;
	private Context mContext;

	public RecentAdapter(Context context, LinkedList<String> data,
			SwipeListView listview) {
		mContext = context;
		this.mInflater = LayoutInflater.from(context);
		mData = data;
		this.mListView = listview;
	}

	public void remove(int position) {
		if (position < mData.size()) {
			mData.remove(position);
			notifyDataSetChanged();
		}
	}

	// public void remove(RecentItem item) {
	// if (mData.contains(item)) {
	// mData.remove(item);
	// notifyDataSetChanged();
	// }
	// }
	//
	// public void addFirst(RecentItem item) {
	// if (mData.contains(item)) {
	// mData.remove(item);
	// }
	// mData.addFirst(item);
	// L.i("addFirst: " + item);
	// notifyDataSetChanged();
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_msglist, null);
		}
		TextView nickTV = (TextView) convertView
				.findViewById(R.id.recent_list_item_name);
		TextView msgTV = (TextView) convertView
				.findViewById(R.id.recent_list_item_msg);
		TextView numTV = (TextView) convertView.findViewById(R.id.unreadmsg);
		TextView timeTV = (TextView) convertView
				.findViewById(R.id.recent_list_item_time);
		ImageView headIV = (ImageView) convertView.findViewById(R.id.icon);
		Button deleteBtn = (Button) convertView
				.findViewById(R.id.recent_del_btn);
//		nickTV.setText(item.getName());
//		msgTV.setText(convertNormalStringToSpannableString(item.getMessage()),
//				BufferType.SPANNABLE);
//		timeTV.setText(TimeUtil.getChatTime(item.getTime()));
//		headIV.setImageResource(PushApplication.heads[item.getHeadImg()]);
//		int num = mMessageDB.getNewCount(item.getUserId());
//		if (num > 0) {
//			numTV.setVisibility(View.VISIBLE);
//			numTV.setText(num + "");
//		} else {
//			numTV.setVisibility(View.GONE);
//		}
		numTV.setVisibility(View.GONE);
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mData.remove(position);
				// mRecentDB.delRecent(item.getUserId());
				notifyDataSetChanged();
				if (mListView != null)
					mListView.closeOpenedItems();
			}
		});
		return convertView;
	}

	/**
	 * ����һ�ַ�����������
	 * 
	 * @param message
	 *            �������Ҫ�����String
	 * @return
	 */
//	private CharSequence convertNormalStringToSpannableString(String message) {
//		// TODO Auto-generated method stub
//		String hackTxt;
//		if (message.startsWith("[") && message.endsWith("]")) {
//			hackTxt = message + " ";
//		} else {
//			hackTxt = message;
//		}
//		SpannableString value = SpannableString.valueOf(hackTxt);
//
//		Matcher localMatcher = MessageAdapter.EMOTION_URL.matcher(value);
//		while (localMatcher.find()) {
//			String str2 = localMatcher.group(0);
//			int k = localMatcher.start();
//			int m = localMatcher.end();
//			// k = str2.lastIndexOf("[");
//			// Log.i("way", "str2.length = "+str2.length()+", k = " + k);
//			// str2 = str2.substring(k, m);
//			if (m - k < 8) {
//				if (PushApplication.getInstance().getFaceMap()
//						.containsKey(str2)) {
//					int face = PushApplication.getInstance().getFaceMap()
//							.get(str2);
//					Bitmap bitmap = BitmapFactory.decodeResource(
//							mContext.getResources(), face);
//					if (bitmap != null) {
//						int rawHeigh = bitmap.getHeight();
//						int rawWidth = bitmap.getHeight();
//						int newHeight = 30;
//						int newWidth = 30;
//						// ������������
//						float heightScale = ((float) newHeight) / rawHeigh;
//						float widthScale = ((float) newWidth) / rawWidth;
//						// �½�������
//						Matrix matrix = new Matrix();
//						matrix.postScale(heightScale, widthScale);
//						// ����ͼƬ����ת�Ƕ�
//						// matrix.postRotate(-30);
//						// ����ͼƬ����б
//						// matrix.postSkew(0.1f, 0.1f);
//						// ��ͼƬ��Сѹ��
//						// ѹ����ͼƬ�Ŀ�͸��Լ�kB��С����仯
//						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//								rawWidth, rawHeigh, matrix, true);
//						ImageSpan localImageSpan = new ImageSpan(mContext,
//								newBitmap, ImageSpan.ALIGN_BASELINE);
//						value.setSpan(localImageSpan, k, m,
//								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//					}
//				}
//			}
//		}
//		return value;
//	}
}
