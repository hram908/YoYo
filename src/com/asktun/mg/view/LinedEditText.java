package com.asktun.mg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends EditText {
	// private Paint mPaint = new Paint();

	public LinedEditText(Context context) {
		super(context);
		initPaint();
	}

	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}

	private void initPaint() {
		// mPaint.setStyle(Paint.Style.STROKE);
		// // mPaint.setColor(0x80000000);
		// mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setColor(R.color.dashed_line_color);
		// PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
		// mPaint.setPathEffect(effects);
	}

//	@SuppressLint("NewApi")
//	@Override
//	protected void onDraw(Canvas canvas) {
//		Paint mPaint = new Paint();
//		// mPaint.setColor(0x80000000);
//		mPaint.setStyle(Paint.Style.STROKE);
//		mPaint.setColor(Color.LTGRAY);
//		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
//		mPaint.setPathEffect(effects);
//
//		int left = getLeft();
//		int right = getRight();
//		int paddingTop = getPaddingTop();
//		int paddingBottom = getPaddingBottom();
//		int paddingLeft = getPaddingLeft();
//		int paddingRight = getPaddingRight();
//		int height = getHeight();
//		int lineHeight = getLineHeight();
////		int spcingHeight = (int) getLineSpacingExtra();
//		int spcingHeight = (int) 10;
//		int count = (height - paddingTop - paddingBottom) / lineHeight;
//
//		for (int i = 0; i < count; i++) {
//			int baseline = lineHeight * (i + 1) + paddingTop - spcingHeight / 2;
//			canvas.drawLine(left + paddingLeft, baseline, right - paddingRight,
//					baseline, mPaint);
//		}
//
//		super.onDraw(canvas);
//	}
}
