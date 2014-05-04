package com.asktun.mg.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.activity.NearPersonActivity;
import com.asktun.mg.activity.UserInfoActivity;
import com.asktun.mg.bean.MessageItem;
import com.chen.jmvc.util.StrUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MessageAdapter extends BaseAdapter {

	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	// public static final Pattern EMOTION_URL =
	// Pattern.compile("\\/[\u4e00-\u9fa5]{1,3}");
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private Context mContext;
	private LayoutInflater mInflater;
	private List<MessageItem> mMsgList;

	public MessageAdapter(Context context, List<MessageItem> msgList) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mMsgList = msgList;
		mInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.moren_img)
				.showImageForEmptyUri(R.drawable.moren_img)
				.showImageOnFail(R.drawable.moren_img).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(8))
				.build();
	}

	public void removeHeadMsg() {
		if (mMsgList.size() - 10 > 10) {
			for (int i = 0; i < 10; i++) {
				mMsgList.remove(i);
			}
			notifyDataSetChanged();
		}
	}

	public void setMessageList(List<MessageItem> msgList) {
		mMsgList = msgList;
		notifyDataSetChanged();
	}

	public void upDateMsg(MessageItem msg) {
		mMsgList.add(msg);
		notifyDataSetChanged();
	}

	public void updateUnsent(int position, int sentType) {
		mMsgList.get(position).sentType = sentType;
		notifyDataSetChanged();
	}

	public void updateAudioPath(int position, String path) {
		mMsgList.get(position).audioPath = path;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final MessageItem item = mMsgList.get(position);
		final boolean isComMsg = item.isComMeg;
		ViewHolder holder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + position) == null) {
			holder = new ViewHolder();
			if (isComMsg) {
				convertView = mInflater.inflate(R.layout.chat_item_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chat_item_right, null);
			}
			holder.iv_fail = (ImageView) convertView.findViewById(R.id.iv_fail);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.time = (TextView) convertView.findViewById(R.id.datetime);
			holder.msg = (TextView) convertView.findViewById(R.id.textView2);
			holder.second = (TextView) convertView.findViewById(R.id.tv_sec);
			holder.ivContent = (ImageView) convertView
					.findViewById(R.id.iv_content);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			convertView.setTag(R.drawable.ic_launcher + position);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ position);
		}
		holder.time.setText(item.time);
		holder.time.setVisibility(View.VISIBLE);
		if (!StrUtil.isEmpty(item.image)) {
			imageLoader.displayImage(item.image, holder.icon, options);
		} else {
			holder.icon.setImageResource(R.drawable.moren_img);
		}
		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isComMsg) {
					Intent intent = new Intent(mContext,
							NearPersonActivity.class);
					intent.putExtra("userId", item.userId);
					mContext.startActivity(intent);
				} else {
					mContext.startActivity(new Intent(mContext,
							UserInfoActivity.class));
				}
			}
		});

		if (!isComMsg) {
			switch (item.sentType) {
			case 0:
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.iv_fail.setVisibility(View.GONE);
				break;
			case 1:
				holder.progressBar.setVisibility(View.GONE);
				holder.iv_fail.setVisibility(View.GONE);
				break;
			case -1:
				holder.iv_fail.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}

		switch (item.entityType) {
		case ET_TEXT:
			holder.msg.setText(
					convertNormalStringToSpannableString(item.message),
					BufferType.SPANNABLE);
			holder.ivContent.setImageBitmap(null);
			holder.second.setText(null);
			break;
		case ET_AUDIO:
			if (isComMsg) {
				holder.ivContent
						.setImageResource(R.drawable.chatfrom_voice_playing);
			} else {
				holder.ivContent
						.setImageResource(R.drawable.chatto_voice_playing);
			}
			holder.second.setText(item.message + "\"");
			holder.msg.setText(null);
			break;
		}

		holder.ivContent.setBackgroundResource(0);
		AnimationDrawable animationDrawable = (AnimationDrawable) holder.ivContent
				.getBackground();
		if (animationDrawable != null) {
			animationDrawable.stop();
		}

		holder.msg.setTag(item);
		holder.ivContent.setTag(position);

		return convertView;
	}

	/**
	 * 另外一种方法解析表情
	 * 
	 * @param message
	 *            传入的需要处理的String
	 * @return
	 */
	private CharSequence convertNormalStringToSpannableString(String message) {
		// TODO Auto-generated method stub
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (MyApp.getInstance().getFaceMap().containsKey(str2)) {
					int face = MyApp.getInstance().getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(
							mContext.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(mContext,
								bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

	static class ViewHolder {
		ImageView icon;
		ImageView ivContent;
		TextView time;
		TextView msg;
		TextView second;
		ProgressBar progressBar;
		ImageView iv_fail;

	}
}