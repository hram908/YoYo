package com.asktun.mg.activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.MyApp;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.adapter.FaceAdapter;
import com.asktun.mg.adapter.FacePageAdeapter;
import com.asktun.mg.adapter.MessageAdapter;
import com.asktun.mg.bean.FriendMessage;
import com.asktun.mg.bean.FriendMessage.FriendMessageItem;
import com.asktun.mg.bean.MessageItem;
import com.asktun.mg.bean.MessageItem.EntityType;
import com.asktun.mg.bean.SendMessage;
import com.asktun.mg.utils.RecordUtil;
import com.asktun.mg.utils.UploadUtil;
import com.asktun.mg.utils.UploadUtil.OnUploadProcessListener;
import com.asktun.mg.utils.Util;
import com.asktun.mg.view.CirclePageIndicator;
import com.asktun.mg.view.JazzyViewPager;
import com.asktun.mg.view.JazzyViewPager.TransitionEffect;
import com.asktun.mg.view.XListView;
import com.chen.jmvc.util.DateUtil;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.StrUtil;
import com.google.gson.GsonBuilder;

/**
 * 聊天
 * 
 * @Description
 * @author Dean
 * 
 */
public class ChatActivity extends BaseActivity implements OnTouchListener,
		OnClickListener {
	@ViewInject(id = R.id.send_btn)
	private Button btn_send;
	@ViewInject(id = R.id.face_btn)
	private Button btn_face;
	@ViewInject(id = R.id.msg_et)
	private EditText et_msg;
	@ViewInject(id = R.id.face_ll)
	private LinearLayout faceLinearLayout;
	@ViewInject(id = R.id.face_pager)
	private JazzyViewPager faceViewPager;
	@ViewInject(id = R.id.msg_listView)
	private XListView listView;
	@ViewInject(id = R.id.pushsay_btn)
	private Button btn_pushsay;

	private int currentPage = 0;
	private boolean isFaceShow = false;
	private boolean isSayShow = false;

	private InputMethodManager imm;
	private List<String> keys;
	private MessageAdapter adapter;

	private Dialog recDialog;
	private View recView;
	private ImageView recImg;

	private MediaPlayer mMediaPlayer;
	private RecordUtil mRecordUtil;
	private static final int MAX_TIME = 60;// 最长录音时间
	private static final int MIN_TIME = 1;// 最短录音时间

	private static final int RECORD_NO = 0; // 不在录音
	private static final int RECORD_ING = 1; // 正在录音
	private static final int RECORD_ED = 2; // 完成录音
	private float mRecord_Time;// 录音的时间
	private int mRecord_State = 0; // 录音的状态
	private double mRecord_Volume;// 麦克风获取的音量值
	private boolean mPlayState; // 播放状态
	private String mRecordPath;// 录音的存储位置
	private final static String PATH = Environment
			.getExternalStorageDirectory().getPath() + "/mobilegame/";

	private boolean isGroup;
	private String groupId;
	private String friendId;
	private String name;
	private List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组

	private Thread thread;
	private String lastTime;

	private String userUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// AudioManager am = (AudioManager)
		// getSystemService(Context.AUDIO_SERVICE);
		// am.setStreamVolume(AudioManager.STREAM_MUSIC,
		// am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 4 / 5, 0);

		initActionBar();
		ab_base.setBackgroundResource(R.drawable.background_chat);
		setAbContentView(R.layout.activity_chat);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);

		isGroup = getIntent().getBooleanExtra("isGroup", false);
		groupId = getIntent().getStringExtra("groupId");
		friendId = getIntent().getStringExtra("friendId");
		name = getIntent().getStringExtra("name");
		if (isGroup) {
			setTitleText(name);
			addRightButtonView(R.drawable.button_groupspace_selector)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(ChatActivity.this,
									GroupSpaceActivity.class);
							intent.putExtra("groupId", groupId);
							startActivity(intent);
						}
					});
		} else {
			setTitleText("与" + name + "聊天");
		}
		init();
		initFacePage();
		initVoiceDialog();
		initRecVoice();
		thread = new Thread() {
			@Override
			public void run() {
				Thread.currentThread();
				while (!Thread.interrupted()) {
					getMessage();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					Log.d("thread", "thread running");
				}
			};
		};
		thread.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
		faceLinearLayout.setVisibility(View.GONE);
		isFaceShow = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		thread.interrupt();
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void initVoiceDialog() {
		recDialog = new Dialog(this);
		recView = getLayoutInflater()
				.inflate(R.layout.yuyin_input_dialog, null);
		recImg = (ImageView) recView.findViewById(R.id.imageView1);
		recDialog.getWindow().setContentView(recView);
		recDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
	}

	public void startRecording() {
		if (mRecord_State != RECORD_ING) {
			// 修改录音状态
			mRecord_State = RECORD_ING;
			// 设置录音保存路径
			mRecordPath = UUID.randomUUID().toString() + ".amr";
		}
		mRecordUtil = new RecordUtil(PATH + mRecordPath);
		try {
			// 开始录音
			mRecordUtil.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				btn_pushsay.setText("松开结束");
				recImg.setBackgroundResource(R.drawable.luyin0);
				recDialog.show();
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 初始化录音时间
				mRecord_Time = 0;
				while (mRecord_State == RECORD_ING) {
					// 大于最大录音时间则停止录音
					if (mRecord_Time >= MAX_TIME) {
						mRecordHandler.sendEmptyMessage(0);
					} else {
						try {
							// 每隔200毫秒就获取声音音量并更新界面显示
							Thread.sleep(200);
							mRecord_Time += 0.2;
							if (mRecord_State == RECORD_ING) {
								mRecord_Volume = mRecordUtil.getAmplitude();
								mRecordHandler.sendEmptyMessage(1);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();

	}

	/**
	 * 用来控制录音
	 */
	Handler mRecordHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				stopRecording();
				break;
			case 1:
				// 根据录音声音大小显示效果
				if (mRecord_Volume < 200.0) {
					recImg.setImageResource(R.drawable.luyin0);
				} else if (mRecord_Volume > 200.0 && mRecord_Volume < 7000) {
					recImg.setImageResource(R.drawable.luyin1);
				} else if (mRecord_Volume > 70000.0 && mRecord_Volume < 28000) {
					recImg.setImageResource(R.drawable.luyin2);
				} else if (mRecord_Volume > 28000.0) {
					recImg.setImageResource(R.drawable.luyin3);
				}
				break;
			}
		}

	};

	public void stopRecording() {
		if (mRecord_State == RECORD_ING) {
			// 修改录音状态
			mRecord_State = RECORD_ED;
			// 停止录音
			mRecordUtil.stop();
			// 初始录音音量
			mRecord_Volume = 0;
			// 如果录音时间小于最短时间
			if (mRecord_Time <= MIN_TIME) {
				// 显示提醒
				showToast("录音时间过短");

				new File(PATH + mRecordPath).delete();
				// 修改录音状态
				mRecord_State = RECORD_NO;
				// 修改录音时间
				mRecord_Time = 0;
			} else {
				MessageItem item = new MessageItem();
				item.entityType = EntityType.ET_AUDIO;
				item.name = "gui";
				item.isComMeg = false;
				item.unread = false;
				item.sentType = 0;
				item.time = DateUtil.getStringByFormat(
						System.currentTimeMillis(), DateUtil.dateFormatYMDHMS);
				item.message = (int) mRecord_Time + "";
				item.audioPath = mRecordPath;
				item.image = userUrl;
				adapter.upDateMsg(item);
				listView.setSelection(adapter.getCount() - 1);
				// lastTime = item.time;
				sendMsg(null, "2", mRecordPath);
			}
			btn_pushsay.setText("按住说话");
			if (recDialog != null)
				recDialog.dismiss();
		}

	}

	private void initRecVoice() {
		btn_pushsay.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// 开始录音
				case MotionEvent.ACTION_DOWN:
					if (!Util.checkSDCard()) {
						showToast("sd卡不可用");
					} else {
						startRecording();
					}
					break;
				case MotionEvent.ACTION_UP:
					stopRecording();
					break;
				}
				return false;
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		// 设置当前的最小声音和最大声音值

		Set<String> keySet = MyApp.getInstance().getFaceMap().keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		// 触摸ListView隐藏表情和输入法
		adapter = new MessageAdapter(this, msgList);
		listView.setOnTouchListener(this);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setAdapter(adapter);
		listView.setSelection(adapter.getCount() - 1);
		et_msg.setOnTouchListener(this);
		et_msg.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					btn_send.setBackgroundResource(R.drawable.button_fasong_selector);
				} else {
					btn_send.setBackgroundResource(R.drawable.button_yuyin_selector);
				}
			}
		});
		btn_face.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}

	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < MyApp.NUM_PAGE; ++i) {
			lv.add(getGridView(i));
		}
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(TransitionEffect.Standard);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}
	

	// 初始化表情列表
	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(10);
		gv.setVerticalSpacing(10);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == MyApp.NUM) {// 删除键的位置
					int selection = et_msg.getSelectionStart();
					String text = et_msg.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							et_msg.getText().delete(start, end);
							return;
						}
						et_msg.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * MyApp.NUM + arg2;

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) MyApp.getInstance()
									.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 50;
						int newWidth = 50;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						et_msg.append(spannableString);
					} else {
						String ori = et_msg.getText().toString();
						int index = et_msg.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						et_msg.setText(stringBuilder.toString());
						et_msg.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.face_btn:
			if (!isFaceShow) {
				imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
				faceLinearLayout.setVisibility(View.VISIBLE);
				isFaceShow = true;
				if (isSayShow) {
					btn_pushsay.setVisibility(View.GONE);
					btn_send.setVisibility(View.VISIBLE);
					et_msg.setVisibility(View.VISIBLE);
					et_msg.requestFocus();
					btn_send.setBackgroundResource(R.drawable.button_yuyin_selector);
					isSayShow = false;
				}
			}
			break;
		case R.id.send_btn:// 发送消息
			String msg = et_msg.getText().toString();
			if (!StrUtil.isEmpty(msg)) {
				MessageItem item = new MessageItem();
				item.entityType = EntityType.ET_TEXT;
				item.isComMeg = false;
				item.unread = false;
				item.sentType = 0;
				item.message = msg;
				item.name = "";
				item.time = DateUtil.getStringByFormat(
						System.currentTimeMillis(), DateUtil.dateFormatYMDHMS);
				item.image = userUrl;
				adapter.upDateMsg(item);
				listView.setSelection(adapter.getCount() - 1);
				et_msg.setText("");
				// lastTime = item.time;

				if (isFaceShow) {
					faceLinearLayout.setVisibility(View.GONE);
					isFaceShow = false;
				} else {
					imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
				}

				sendMsg(msg, "1", null);
			} else {
				if (!isSayShow) {
					imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
					faceLinearLayout.setVisibility(View.GONE);
					isSayShow = true;
					isFaceShow = false;
					btn_pushsay.setVisibility(View.VISIBLE);
					btn_send.setBackgroundResource(R.drawable.button_jianpan_selector);
					et_msg.setVisibility(View.GONE);
				} else {
					imm.showSoftInput(et_msg, 0);
					btn_pushsay.setVisibility(View.GONE);
					btn_send.setBackgroundResource(R.drawable.button_yuyin_selector);
					et_msg.setVisibility(View.VISIBLE);
					imm.toggleSoftInputFromWindow(et_msg.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
					et_msg.requestFocus();
					isFaceShow = false;
					isSayShow = false;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.msg_listView:
			imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		case R.id.msg_et:
			imm.showSoftInput(et_msg, 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFaceShow) {
				// 隐藏表情
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	private HttpHandler<?> handler;

	/**
	 * 消息被点击
	 * 
	 * @param view
	 */
	public void onBubbleClick(final View view) {
		final int position = (Integer) view.getTag();
		final MessageItem messageEntity = (MessageItem) adapter
				.getItem(position);
		if (messageEntity.entityType == EntityType.ET_AUDIO) {
			if (!Util.checkSDCard()) {
				showToast("sd卡不可用");
				return;
			}
			String savePAth = null;
			if (StrUtil.isEmpty(messageEntity.audioPath)) {
				savePAth = PATH + UUID.randomUUID().toString() + ".amr";
			} else {
				savePAth = PATH + messageEntity.audioPath;
			}
			if (StrUtil.isEmpty(savePAth)
					|| (!StrUtil.isEmpty(savePAth) && !new File(savePAth)
							.exists())) {
				final String sdpath = getSDPath(savePAth);
				FinalHttp fh = new FinalHttp();
				// 调用download方法开始下载
				handler = fh.download(messageEntity.audioUrl, // 这里是下载的路径
						sdpath, // 这是保存到本地的路径
						false, new AjaxCallBack<File>() {
							@Override
							public void onLoading(long count, long current) {

							}

							@Override
							public void onSuccess(File t) {
								view.setEnabled(true);
								adapter.updateAudioPath(position, sdpath);
								playAudio(sdpath);
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								super.onStart();
								view.setEnabled(false);
								playAudioAnimation(view, messageEntity.isComMeg);

							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// TODO Auto-generated method stub
								super.onFailure(t, errorNo, strMsg);
								view.setEnabled(true);
								adapter.notifyDataSetChanged();
							}

						});
			} else {
				playAudioAnimation(view, messageEntity.isComMeg);
				playAudio(PATH + messageEntity.audioPath);
			}
		}
	}

	public String getSDPath(String savePAth) {
		File directory = new File(savePAth).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			try {
				throw new IOException("Path to file could not be created");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return savePAth;
	}

	private void playAudioAnimation(View view, boolean isComMsg) {
		if (!mPlayState) {
			ImageView imageView = (ImageView) view;
			if (isComMsg) {
				imageView.setBackgroundResource(R.anim.charfrom_anim);
			} else {
				imageView.setBackgroundResource(R.anim.charto_anim);
			}
			AnimationDrawable animationDrawable = (AnimationDrawable) imageView
					.getBackground();
			animationDrawable.setOneShot(false);
			if (animationDrawable.isRunning()) {
				animationDrawable.stop();
			}
			imageView.setImageBitmap(null);
			animationDrawable.start();
		}
	}

	private void playAudio(String path) {
		if (!mPlayState) {
			mMediaPlayer = new MediaPlayer();
			try {
				// 添加录音的路径
				mMediaPlayer.setDataSource(path);
				// 准备
				mMediaPlayer.prepare();
				// 播放
				mMediaPlayer.start();
				mPlayState = true;
				mMediaPlayer
						.setOnCompletionListener(new OnCompletionListener() {
							// 播放结束后调用
							@Override
							public void onCompletion(MediaPlayer mp) {
								// 停止播放
								mMediaPlayer.stop();
								// 修改播放状态
								mPlayState = false;
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										adapter.notifyDataSetChanged();
									}
								});
							}
						});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mPlayState = false;
					mMediaPlayer.stop();
				} else {
					mPlayState = false;
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 发送成功后通知progressbar
	 */
	public void onMessageSendSuccessed() {
		adapter.updateUnsent(adapter.getCount() - 1, 1);
	}

	/**
	 * 发送失败后通知progressbar
	 */
	public void onMessageSendFailed() {
		adapter.updateUnsent(adapter.getCount() - 1, -1);
	}

	private boolean isFirst = true;

	private void sendMsg(String message, String type, String voiceName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		int obj = 0;
		if (isGroup) {
			obj = 1;
			params.put("group_id", groupId);
		} else {
			params.put("friend_id", friendId);
			obj = 0;
		}
		params.put("message", message);
		params.put("type", type); // 1：文字 2：语音 3：图片

		if (type.equals("1")) {
			UIDataProcess.reqData(SendMessage.class, params, obj,
					new IResponseListener() {

						@Override
						public void onSuccess(Object arg0) {
							SendMessage data = (SendMessage) arg0;
							if (data.flg == 1) {
								isFirst = false;
								onMessageSendSuccessed();

							} else {
								onMessageSendFailed();
							}
						}

						@Override
						public void onRuning(Object arg0) {

						}

						@Override
						public void onReqStart() {
							// showProgressDialog();
						}

						@Override
						public void onFinish() {
							// removeProgressDialog();
						}

						@Override
						public void onFailure(Object arg0) {
							onMessageSendFailed();
						}
					});
		} else {
			params.put("voice_time", (int) mRecord_Time + "");
			params.put("voice_name", voiceName);
			sendFileMsg(params);
		}

	}

	private void sendFileMsg(HashMap<String, Object> params) {
		String fileKey = "Attach";
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(new OnUploadProcessListener() {

			@Override
			public void onUploadProcess(int uploadSize) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUploadDone(int responseCode, final String obj) {
				// TODO Auto-generated method stub
				removeProgressDialog();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						SendMessage o = null;
						JsonReqUtil.GsonObj gsonObj = null;
						try {
							gsonObj = SendMessage.class.newInstance();
						} catch (InstantiationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						GsonBuilder builder = new GsonBuilder();
						builder.excludeFieldsWithoutExposeAnnotation();
						Type type = gsonObj.getTypeToken();
						try {
							o = builder.create().fromJson(obj, type);
						} catch (Exception e) {
							Log.e("JsonReqUtil", "得到数据异常");
						}
						if (o != null) {
							if (o.flg == 1) {// 成功
								isFirst = false;
								onMessageSendSuccessed();
							} else {
								onMessageSendFailed();
							}
						} else {
							onMessageSendFailed();
						}
					}
				});

			}

			@Override
			public void initUpload(int fileSize) {
				// TODO Auto-generated method stub

			}
		});
		String url;
		if (isGroup) {
			url = "UserGroups/sendGroupMessage";
		} else {
			url = "members/sendMessage";
		}
		uploadUtil.uploadFile(PATH + mRecordPath, fileKey, JsonReqUtil.getURL()
				+ url, params);
	}

	/**
	 * 获取好友消息记录
	 */
	private void getMessage() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", mApplication.getLoginbean().token);
		params.put("user_id", mApplication.getLoginbean().user_id);
		int obj = 0;
		if (isGroup) {
			obj = 1;
			params.put("group_id", groupId);
		} else {
			obj = 0;
			params.put("friend_id", friendId);

		}
		params.put("lastTime", lastTime);
		UIDataProcess.reqData(FriendMessage.class, params, obj,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						FriendMessage data = (FriendMessage) arg0;
						if (data.flg == 1) {
							if (data.user_info != null) {
								userUrl = data.user_info.user_ico;
							}
							if (null != data.data && data.data.size() > 0) {
								boolean isHaveOther = false;
								for (int i = data.data.size() - 1; i >= 0; i--) {
									if (StrUtil.isEmpty(lastTime)) {
										isHaveOther = true;
										FriendMessageItem info = data.data
												.get(i);
										if (!isFirst
												&& info.user_id.equals(mApplication
														.getLoginbean().user_id)) {
											continue;
										}
										MessageItem item = new MessageItem();
										if (StrUtil.isEmpty(info.attach_name)) {
											item.entityType = EntityType.ET_TEXT;
										} else {
											item.entityType = EntityType.ET_AUDIO;
											item.audioUrl = info.attach_name;
											item.audioPath = info.voice_name;
										}
										item.unread = false;
										item.sentType = 1;
										item.userId = info.user_id;
										item.image = info.user_ico;
										if (info.user_id.equals(mApplication
												.getLoginbean().user_id)) {
											item.isComMeg = false;
											if (data.user_info != null)
												item.image = data.user_info.user_ico;
										} else {
											item.isComMeg = true;
											if (data.friend_info != null)
												item.image = data.friend_info.user_ico;
										}
										item.time = info.add_time;
										item.message = info.message;
										msgList.add(item);
										if (i == 0) {
											lastTime = info.add_time;
										}

									} else {
										FriendMessageItem info = data.data
												.get(i);
										if (isContainSame(info.add_time)) {
											return;
										}
										if (!info.user_id.equals(mApplication
												.getLoginbean().user_id)) {
											isHaveOther = true;
											MessageItem item = new MessageItem();
											if (StrUtil
													.isEmpty(info.attach_name)) {
												item.entityType = EntityType.ET_TEXT;
											} else {
												item.entityType = EntityType.ET_AUDIO;
												item.audioUrl = info.attach_name;
												item.audioPath = info.voice_name;
											}
											item.unread = false;
											item.sentType = 1;
											item.name = "";
											item.image = info.user_ico;
											item.userId = info.user_id;
											if (data.friend_info != null)
												item.image = data.friend_info.user_ico;
											item.time = info.add_time;
											item.message = info.message;
											msgList.add(item);
											lastTime = info.add_time;
										}
										// listView.post(new Runnable() {
										//
										// @Override
										// public void run() {
										// // TODO Auto-generated method
										// // stub
										// adapter.setMessageList(msgList);
										// listView.setSelection(adapter
										// .getCount() - 1);
										// }
										// });
									}

								}
								if (isHaveOther) {
									listView.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub
											adapter.setMessageList(msgList);
											listView.setSelection(adapter
													.getCount() - 1);
										}
									});
								}

							}
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

	private boolean isContainSame(String time) {
		for (MessageItem item : msgList) {
			if (item.isComMeg) {
				if (time.equals(item.time)) {
					return true;
				}
			}
		}
		return false;
	}

}