package com.asktun.mg;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.download.ContentValue;
import com.asktun.mg.download.FinalDBChen;
import com.asktun.mg.service.AConstDefine;
import com.asktun.mg.service.DJMarketUtils;
import com.asktun.mg.service.DownloadEntity;
import com.asktun.mg.service.DownloadService;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.StrUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BaseFragment extends Fragment implements ContentValue {

	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;

	/** 全局的Application对象，已经完成初始化. */
	public MyApp mApplication = null;

	/** 全局的SharedPreferences对象，已经完成初始化. */
	public SharedPreferences abSharedPreferences = null;

	/**
	 * LinearLayout.LayoutParams，已经初始化为FILL_PARENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsFF = null;

	/**
	 * LinearLayout.LayoutParams，已经初始化为FILL_PARENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsFW = null;

	/**
	 * LinearLayout.LayoutParams，已经初始化为WRAP_CONTENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsWF = null;

	/**
	 * LinearLayout.LayoutParams，已经初始化为WRAP_CONTENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsWW = null;

	/** 左侧的Logo图标View. */
	protected ImageView logoView = null;

	/** 左侧的Logo图标右边的分割线View. */
	protected ImageView logoLineView = null;

	/** 总布�? */
	public RelativeLayout ab_base = null;

	/** 标题栏布�? */
	protected LinearLayout titleLayout = null;

	/** 标题布局. */
	protected LinearLayout titleTextLayout = null;

	/** 标题文本的对齐参�? */
	private LinearLayout.LayoutParams titleTextLayoutParams = null;

	/** 右边布局的的对齐参数. */
	private LinearLayout.LayoutParams rightViewLayoutParams = null;

	private LinearLayout.LayoutParams iconHeightLL;

	/** 标题栏布�?D. */
	private static final int titleLayoutID = 1;

	/** 主内容布�? */
	protected RelativeLayout contentLayout = null;

	/** 显示标题文字的View. */
	protected Button titleTextBtn = null;

	/** 右边的View，可以自定义显示�?��. */
	protected LinearLayout rightLayout = null;

	/** The diaplay width. */
	public int diaplayWidth = 320;

	/** The diaplay height. */
	public int diaplayHeight = 480;

	/** The m window manager. */
	private WindowManager mWindowManager = null;

	/** 标题栏�?明标�? */
	public static final String TITLE_TRANSPARENT_FLAG = "TITLE_TRANSPARENT_FLAG";

	/** 标题栏�?�? */
	public static final int TITLE_TRANSPARENT = 0;

	/** 标题栏不透明. */
	public static final int TITLE_NOTRANSPARENT = 1;
	public static final String SHAREPATH = "app_share";

	protected BaseActivity act;

	private boolean hasActionBar = true;

	private LinearLayout contentView;

	private LinearLayout leftLayout;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected DisplayImageOptions options;
	protected DisplayImageOptions options_round;
	protected DisplayImageOptions options_round10;

	public String mProgressMessage = "Loading...";

	public android.app.ProgressDialog mProgressDialog;

	protected FinalDBChen db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = (BaseActivity) getActivity();
		mInflater = LayoutInflater.from(act);
		mWindowManager = act.getWindowManager();
		mApplication = (MyApp) act.getApplication();
		Display display = mWindowManager.getDefaultDisplay();
		diaplayWidth = display.getWidth();
		diaplayHeight = display.getHeight();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.moren_img)
				.showImageForEmptyUri(R.drawable.moren_img)
				.showImageOnFail(R.drawable.moren_img).cacheInMemory(true)
				.cacheOnDisc(true).build();
		options_round = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.moren_img)
				.showImageForEmptyUri(R.drawable.moren_img)
				.showImageOnFail(R.drawable.moren_img).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(16))
				.build();
		options_round10 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.moren_img)
				.showImageForEmptyUri(R.drawable.moren_img)
				.showImageOnFail(R.drawable.moren_img).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(8))
				.build();

		db = new FinalDBChen(act, DBNAME, new GameInfo(), TABNAME_DOWNLOADTASK,
				null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (hasActionBar) {
			if (contentView == null) {
				initActionBar();
				contentView = new LinearLayout(act);
				contentView.setClickable(true);
				contentLayout.setClickable(true);
				contentView.setLayoutParams(new LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				contentView.addView(ab_base, layoutParamsFF);
				addChildView(contentLayout);
			} else {
				((ViewGroup) contentView.getParent()).removeView(contentView);
			}
			return contentView;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void addChildView(ViewGroup contentLayout) {

	}

	public void initActionBar() {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		layoutParamsFF = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layoutParamsWF.gravity = Gravity.CENTER_VERTICAL;
		layoutParamsWW = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		iconHeightLL = new LinearLayout.LayoutParams(getResources()
				.getDimensionPixelSize(R.dimen.icon_img), getResources()
				.getDimensionPixelSize(R.dimen.icon_img));
		titleTextLayoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		titleTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		rightViewLayoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0);
		rightViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		ab_base = new RelativeLayout(act);
		ab_base.setBackgroundResource(R.drawable.background);

		titleLayout = new LinearLayout(act);
		titleLayout.setId(titleLayoutID);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setPadding(0, 0, 0, 0);

		contentLayout = new RelativeLayout(act);
		contentLayout.setPadding(0, 0, 0, 0);

		titleTextLayout = new LinearLayout(act);
		titleLayout.setMinimumHeight(getResources().getDimensionPixelSize(
				R.dimen.icon_img));
		titleTextLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleTextLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleTextLayout.setPadding(0, 0, 0, 0);

		titleTextBtn = new Button(act);
		titleTextBtn.setEllipsize(TruncateAt.END);
		titleTextBtn.setSingleLine();
		titleTextBtn.setTextColor(Color.rgb(255, 255, 255));
		titleTextBtn.setTextSize(20);
		titleTextBtn.setPadding(5, 0, 5, 0);
		titleTextBtn.setGravity(Gravity.CENTER_VERTICAL);
		titleTextBtn.setBackgroundDrawable(null);
		titleTextLayout.addView(titleTextBtn, layoutParamsWF);

		// 加左边的按钮
		leftLayout = new LinearLayout(act);
		leftLayout.setOrientation(LinearLayout.HORIZONTAL);
		leftLayout.setGravity(Gravity.LEFT);
		leftLayout.setPadding(0, 0, 0, 0);
		leftLayout.setHorizontalGravity(Gravity.LEFT);
		leftLayout.setGravity(Gravity.CENTER_VERTICAL);
		leftLayout.setVisibility(View.GONE);
		titleLayout.addView(leftLayout, rightViewLayoutParams);

		logoView = new ImageView(act);
		logoView.setVisibility(View.GONE);
		logoLineView = new ImageView(act);
		logoLineView.setVisibility(View.GONE);

		layoutParamsWW.leftMargin = 15;
		titleLayout.addView(logoView, layoutParamsWW);
		titleLayout.addView(logoLineView, layoutParamsWW);
		titleLayout.addView(titleTextLayout, titleTextLayoutParams);

		Intent intent = act.getIntent();
		int titleTransparentFlag = intent.getIntExtra(TITLE_TRANSPARENT_FLAG,
				TITLE_NOTRANSPARENT);

		if (titleTransparentFlag == TITLE_TRANSPARENT) {
			ab_base.addView(contentLayout, layoutParamsFW);
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_TOP,
					RelativeLayout.TRUE);
			ab_base.addView(titleLayout, layoutParamsFW2);
		} else {
			ab_base.addView(titleLayout, layoutParamsFW);
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.BELOW, titleLayoutID);
			ab_base.addView(contentLayout, layoutParamsFW1);
		}

		// 加右边的按钮
		rightLayout = new LinearLayout(act);
		rightLayout.setOrientation(LinearLayout.HORIZONTAL);
		rightLayout.setGravity(Gravity.RIGHT);
		rightLayout.setPadding(0, 0, 0, 0);
		rightLayout.setHorizontalGravity(Gravity.RIGHT);
		rightLayout.setGravity(Gravity.CENTER_VERTICAL);
		rightLayout.setVisibility(View.GONE);
		titleLayout.addView(rightLayout, rightViewLayoutParams);

		logoView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (ab_base != null)
			loadDefaultStyle();
	}

	public void loadDefaultStyle() {
		this.setTitleLayoutBackground(R.drawable.top_bg);
		this.setTitleTextColor(getResources().getColor(R.color.white));
		this.setTitleLayoutGravity(Gravity.CENTER, Gravity.CENTER);
	}

	/**
	 * 
	 */
	protected void back() {
		getFragmentManager().popBackStack();
	}

	/**
	 * 标题添加自定义布局view
	 * 
	 * @param resId
	 */
	public void setTitleView(int resId) {
		titleTextLayout.addView(mInflater.inflate(resId, null), layoutParamsWF);
	}

	/**
	 * 标题添加自定义图片view
	 * 
	 * @param resId
	 */
	public void setTitleImageView(int resId) {
		ImageView img = new ImageView(act);
		img.setImageResource(resId);
		titleTextLayout.addView(img, layoutParamsWF);
	}

	/**
	 * 描述：设置标题文本颜�?
	 * 
	 * @param text
	 *            文本
	 */
	public void setTitleTextColor(int color) {
		titleTextBtn.setTextColor(color);
	}

	/**
	 * 描述：设置标题文�?
	 * 
	 * @param text
	 *            文本
	 */
	public void setTitleText(String text) {
		titleTextBtn.setText(text);
	}

	/**
	 * 描述：设置标题文�?
	 * 
	 * @param resId
	 *            文本的资源ID
	 */
	public void setTitleText(int resId) {
		titleTextBtn.setText(resId);
	}

	/**
	 * 描述：设置Logo的背景图.
	 * 
	 * @param drawable
	 *            Logo资源Drawable
	 */
	public void setLogo(Drawable drawable) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundDrawable(drawable);
	}

	/**
	 * 描述：设置Logo的背景资�?
	 * 
	 * @param resId
	 *            Logo资源ID
	 */
	public void setLogo(int resId) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundResource(resId);
	}

	/**
	 * 描述：设置Logo分隔线的背景资源.
	 * 
	 * @param resId
	 *            Logo资源ID
	 */
	public void setLogoLine(int resId) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundResource(resId);
	}

	/**
	 * 描述：设置Logo分隔线的背景�?
	 * 
	 * @param drawable
	 *            Logo资源Drawable
	 */
	public void setLogoLine(Drawable drawable) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundDrawable(drawable);
	}

	/**
	 * 描述：把指定的View填加到标题栏右边.
	 * 
	 * @param rightView
	 *            指定的View
	 */
	public void addRightView(View rightView) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(rightView, layoutParamsWF);
	}

	public void addLeftView(View leftView) {
		leftLayout.setVisibility(View.VISIBLE);
		leftLayout.addView(leftView, layoutParamsWF);
	}

	/**
	 * 描述：把指定资源ID表示的View填加到标题栏右边.
	 * 
	 * @param resId
	 *            指定的View的资源ID
	 */
	public void addRightView(int resId) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(mInflater.inflate(resId, null), layoutParamsWF);
	}

	public View addRightButtonView(int resId) {
		rightLayout.setVisibility(View.VISIBLE);
		ImageView btn = new ImageView(act);
		btn.setBackgroundResource(resId);
		rightLayout.addView(btn, layoutParamsWF);
		return btn;
	}

	public Button addLeftButtonView(int resId) {
		leftLayout.setVisibility(View.VISIBLE);
		Button btn = new Button(act);
		btn.setBackgroundResource(resId);
		btn.setTextColor(getResources().getColor(R.color.white));
		layoutParamsWF.leftMargin = 15;
		leftLayout.addView(btn, layoutParamsWF);
		return btn;
	}

	public View addRightButtonView(String text) {
		rightLayout.setVisibility(View.VISIBLE);
		Button btn = new Button(act);
		btn.setText(text);
		layoutParamsWF.rightMargin = 15;
		rightLayout.addView(btn, layoutParamsWF);
		return btn;
	}

	/**
	 * 描述：清除标题栏右边的View.
	 */
	public void clearRightView() {
		rightLayout.removeAllViews();
	}

	public void clearLeftView() {
		leftLayout.removeAllViews();
	}

	/**
	 * 描述：用指定的View填充主界�?
	 * 
	 * @param contentView
	 *            指定的View
	 */
	public void setAbContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView, layoutParamsFF);
	}

	public void setAbContentView(View contentView, ViewGroup.LayoutParams p) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView, p);
	}

	/**
	 * 描述：用指定资源ID表示的View填充主界�?
	 * 
	 * @param resId
	 *            指定的View的资源ID
	 */
	public void setAbContentView(int resId) {
		contentLayout.removeAllViews();
		contentLayout.addView(mInflater.inflate(resId, null), layoutParamsFF);
	}

	/**
	 * 描述：设置Logo按钮的返回事�?
	 * 
	 * @param mOnClickListener
	 *            指定的返回事�?
	 */
	public void setLogoBackOnClickListener(View.OnClickListener mOnClickListener) {
		logoView.setOnClickListener(mOnClickListener);
	}

	public void showProgressDialog() {
		showProgressDialog(null);
	}

	public void showProgressDialog(String message) {
		if (!StrUtil.isEmpty(message)) {
			mProgressMessage = message;
		}

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setProgressDialog();
				act.showDialog(Constant.DIALOGPROGRESS);
			}
		});
	}

	public void setProgressDialog() {
		if (mProgressDialog == null) {
			if (act.getApplication() instanceof com.chen.jmvc.util.Application) {
				mProgressDialog = ((com.chen.jmvc.util.Application) act
						.getApplication()).getProgressDialog(act);
			} else {
				mProgressDialog = new android.app.ProgressDialog(act);
			}
			mProgressDialog.setMessage(mProgressMessage);
		}
	}

	public void removeProgressDialog() {
		act.removeDialog(Constant.DIALOGPROGRESS);
	}

	public void showToast(final String text) {
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(act, "" + text, Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void showToast(int resId) {
		Toast.makeText(act, "" + this.getResources().getText(resId),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 描述：对话框dialog （确认，取消�?
	 * 
	 * @param title
	 *            对话框标题内�?
	 * @param msg
	 *            对话框提示内�?
	 * @param mOkOnClickListener
	 *            点击确认按钮的事件监�?
	 */
	public void dialogOpen(String title, String msg,
			DialogInterface.OnClickListener mOkOnClickListener) {
		AlertDialog.Builder builder = new Builder(act);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("确认", mOkOnClickListener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 描述：对话框dialog （无按钮�?
	 * 
	 * @param title
	 *            对话框标题内�?
	 * @param msg
	 *            对话框提示内�?
	 */
	public void dialogOpen(String title, String msg) {
		AlertDialog.Builder builder = new Builder(act);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.create().show();
	}

	/**
	 * Gets the title layout.
	 * 
	 * @return the title layout
	 */
	public LinearLayout getTitleLayout() {
		return titleLayout;
	}

	/**
	 * Sets the title layout.
	 * 
	 * @param titleLayout
	 *            the new title layout
	 */
	public void setTitleLayout(LinearLayout titleLayout) {
		this.titleLayout = titleLayout;
	}

	/**
	 * 描述：标题栏的背景图.
	 * 
	 * @param res
	 *            背景图资源ID
	 */
	public void setTitleLayoutBackground(int res) {
		titleLayout.setBackgroundResource(res);
	}

	/**
	 * 描述：标题文字的对齐.
	 * 
	 * @param left
	 *            the left
	 * @param top
	 *            the top
	 * @param right
	 *            the right
	 * @param bottom
	 *            the bottom
	 */
	public void setTitleTextMargin(int left, int top, int right, int bottom) {
		titleTextLayoutParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 描述：标题栏的背景图.
	 * 
	 * @param color
	 *            背景颜色�?
	 */
	public void setTitleLayoutBackgroundColor(int color) {
		titleLayout.setBackgroundColor(color);
	}

	/**
	 * 描述：标题文字字�?
	 * 
	 * @param titleTextSize
	 *            文字字号
	 */
	public void setTitleTextSize(int titleTextSize) {
		this.titleTextBtn.setTextSize(titleTextSize);
	}

	/**
	 * 描述：设置标题文字对齐方�? 根据右边的具体情况判定方向： �?）中间靠�?Gravity.CENTER,Gravity.CENTER
	 * �?）左边居�?右边居右Gravity.LEFT,Gravity.RIGHT
	 * �?）左边居中，右边居右Gravity.CENTER,Gravity.RIGHT
	 * �?）左边居右，右边居右Gravity.RIGHT,Gravity.RIGHT 必须在addRightView(view)方法后设�?
	 * 
	 * @param gravity1
	 *            标题对齐方式
	 * @param gravity2
	 *            右边布局对齐方式
	 */
	public void setTitleLayoutGravity(int gravity1, int gravity2) {
		measureView2(this.rightLayout);
		measureView2(this.logoView);
		measureView2(this.leftLayout);
		int leftWidth = this.logoView.getMeasuredWidth()
				+ this.leftLayout.getMeasuredWidth();
		int rightWidth = this.rightLayout.getMeasuredWidth();
		// Log.d(TAG, "测量布局的宽度："+leftWidth+","+rightWidth);
		this.titleTextLayoutParams.rightMargin = 0;
		this.titleTextLayoutParams.leftMargin = 0;
		// 全部中间�?
		if ((gravity1 == Gravity.CENTER_HORIZONTAL || gravity1 == Gravity.CENTER)) {
			if (leftWidth == 0 && rightWidth == 0) {
				this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			} else {
				if (gravity2 == Gravity.RIGHT) {
					if (rightWidth == 0) {
					} else {
						this.titleTextBtn.setPadding(rightWidth / 3 * 2, 0, 0,
								0);
					}
					this.titleTextBtn.setGravity(Gravity.CENTER);
					this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
				}
				if (gravity2 == Gravity.CENTER
						|| gravity2 == Gravity.CENTER_HORIZONTAL) {
					this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
					this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
					this.titleTextBtn.setGravity(Gravity.CENTER);
					int offset = leftWidth - rightWidth;
					if (offset > 0) {
						this.titleTextLayoutParams.rightMargin = offset;
					} else {
						this.titleTextLayoutParams.leftMargin = Math
								.abs(offset);
					}
				}
			}
			// 左右
		} else if (gravity1 == Gravity.LEFT && gravity2 == Gravity.RIGHT) {
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
			// 全部右靠
		} else if (gravity1 == Gravity.RIGHT && gravity2 == Gravity.RIGHT) {
			this.titleTextLayout.setGravity(Gravity.RIGHT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
		} else if (gravity1 == Gravity.LEFT && gravity2 == Gravity.LEFT) {
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.LEFT);
		}

	}

	/**
	 * 描述：获取标示标题文本的Button.
	 * 
	 * @return the title Button view
	 */
	public Button getTitleTextButton() {
		return titleTextBtn;
	}

	/**
	 * 描述：设置标题字体粗�?
	 * 
	 * @param bold
	 *            the new title text bold
	 */
	public void setTitleTextBold(boolean bold) {
		TextPaint paint = titleTextBtn.getPaint();
		if (bold) {
			// 粗体
			paint.setFakeBoldText(true);
		} else {
			paint.setFakeBoldText(false);
		}

	}

	/**
	 * 描述：设置标题背�?
	 * 
	 * @param resId
	 *            the new title text background resource
	 */
	public void setTitleTextBackgroundResource(int resId) {
		titleTextBtn.setBackgroundResource(resId);
	}

	/**
	 * 描述：设置标题背�?
	 * 
	 * @param d
	 *            背景�?
	 */
	public void setTitleLayoutBackgroundDrawable(Drawable d) {
		titleLayout.setBackgroundDrawable(d);
	}

	/**
	 * 描述：设置标题背�?
	 * 
	 * @param drawable
	 *            the new title text background drawable
	 */
	public void setTitleTextBackgroundDrawable(Drawable drawable) {
		titleTextBtn.setBackgroundDrawable(drawable);
	}

	public static View measureView2(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		return v;
	}

	// public void reqData(@SuppressWarnings("rawtypes") Class cls,
	// Map<String, Object> params, final Object flg,
	// IResponseListener listener) {
	//
	// final UIHelperUtil uhu = UIHelperUtil.getUIHelperUtil(listener);
	// JsonReqUtil jru = new JsonReqUtil();
	// if (!Util.isNetConnected(act)) {
	// act.runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// Toast.makeText(JsonReqUtil.cxt, "当前网络不可用，请检查你的网络设置",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// return;
	// }
	// jru.setRequstType(JsonReqUtil.REQUEST_GET);
	// jru.setParams(params);
	// if (flg != null) {
	// jru.useFieldSetEnable = true;
	// jru.obj = flg;
	// }
	// uhu.sendStartMessage();
	//
	// jru.request(cls, new JsonReqUtil.JsonCallBack() {
	//
	// @Override
	// public void handler(Object jstr, Exception ex) {
	// if (ex != null || jstr == null) {
	// uhu.sendFailureMessage(ex);
	// } else {
	// uhu.sendSuccessMessage(jstr);
	// }
	// uhu.sendFinishMessage();
	// }
	// });
	//
	// }

	/**
	 * 有如下几种情况需要重新设置应用状态：向服务器请求回数据，刷新应用列表，有应用更新数据合并 infos：为本地应用安装信息
	 * downloadlist：为后台应用下载信息 items：为从服务端获取的应用信息
	 * 
	 * @param items
	 * @return
	 */
	protected List<GameInfo> setApkStatus(List<GameInfo> items) {
		if (items != null && items.size() > 0) {
			List<PackageInfo> infos = DJMarketUtils.getInstalledPackages(act);// 获取手机上已安装的应用
			for (int i = 0; i < items.size(); i++) {
				GameInfo item = items.get(i);
				// 与后台下载应用信息作比较
				if (DownloadService.mDownloadService != null) {// 有下载服务在运行
					List<DownloadEntity> downloadList = DownloadService.mDownloadService
							.getAllDownloadList();
					for (int j = 0; j < downloadList.size(); j++) {// 有下载列表
						DownloadEntity entity = downloadList.get(j);
						if (item.getGame_name().equals(entity.appName)
								&& item.getVersionCode() == entity.versionCode) {
							switch (entity.downloadType) {// 重新设置apk的状态
							case AConstDefine.TYPE_OF_DOWNLOAD:// 为正在下载类型的应用
								items.get(i).setStatus(
										AConstDefine.STATUS_APK_INSTALL);// 设置应用为安装状态
								break;
							case AConstDefine.TYPE_OF_UPDATE:// 为可更新类型的应用
								if (entity.getStatus() == AConstDefine.STATUS_OF_INITIAL
										|| entity.getStatus() == AConstDefine.STATUS_OF_IGNORE) {// 过滤掉处于下载初始化或忽略状态
									items.get(i).setStatus(
											AConstDefine.STATUS_APK_UNUPDATE);// 设置应用为未更新状态
								} else {
									items.get(i).setStatus(
											AConstDefine.STATUS_APK_UPDATE);// 设置应用为更新状态
								}
								break;
							}
						} else {
							items.get(i).setStatus(
									AConstDefine.STATUS_APK_UNINSTALL);// 设置应用为未安装
						}
					}
				}
				// 与手机已安装应用信息作比较
				if (infos != null && infos.size() > 0) {
					PackageManager pm = act.getPackageManager();
					for (int k = 0; k < infos.size(); k++) {
						PackageInfo info = infos.get(k);
						if (pm.getApplicationLabel(info.applicationInfo)
								.toString().equals(items.get(i).getGame_name())
								&& info.versionCode >= items.get(i)
										.getVersionCode()) {// 手机已安装此应用且大于等于线上版本
							items.get(i).setStatus(
									AConstDefine.STATUS_APK_INSTALL_DONE);// 设置应用为已安装
							break;
						}
					}
				}
			}
		}
		return items;
	}

	/**
	 * 设置应用详情页应用状态，应用详情页请求数据时，应用详情页onResume()时候
	 * 
	 * @param item
	 * @return
	 */
	protected GameInfo setApkStatus(GameInfo item) {
		if (item != null) {
			// 获取手机上已安装的应用
			List<PackageInfo> infos = DJMarketUtils.getInstalledPackages(act);
			if (DownloadService.mDownloadService != null) {
				List<DownloadEntity> downloadList = DownloadService.mDownloadService
						.getAllDownloadList();// 获取后台下载列表
				for (int j = 0; j < downloadList.size(); j++) {
					DownloadEntity entity = downloadList.get(j);
					if (null != entity && null != item
							&& null != item.getGame_name()
							&& null != entity.appName) {
						if (item.getGame_name().equals(entity.appName)
								&& item.getVersionCode() == entity.versionCode) {// 比较包名和版本号
							switch (entity.downloadType) {
							case AConstDefine.TYPE_OF_DOWNLOAD:
								item.setStatus(AConstDefine.STATUS_APK_INSTALL);
								break;
							case AConstDefine.TYPE_OF_UPDATE:
								if (entity.getStatus() == AConstDefine.STATUS_OF_INITIAL
										|| entity.getStatus() == AConstDefine.STATUS_OF_IGNORE) {
									item.setStatus(AConstDefine.STATUS_APK_UNUPDATE);
								} else {
									item.setStatus(AConstDefine.STATUS_APK_UPDATE);
								}
								break;
							}
							break;
						}
					}
				}
			}
			// 查看手机已安装应用中有无此应用
			if (infos != null && infos.size() > 0) {
				PackageManager pm = act.getPackageManager();
				for (int k = 0; k < infos.size(); k++) {
					PackageInfo info = infos.get(k);
					if (pm.getApplicationLabel(info.applicationInfo).toString()
							.equals(item.getGame_name())
							&& info.versionCode >= item.getVersionCode()) {// 手机已安装此应用且大于等于线上版本
						item.setStatus(AConstDefine.STATUS_APK_INSTALL_DONE);// 设置应用为已安装
						break;
					}
				}
			}
		}
		return item;
	}

}
