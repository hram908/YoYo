package com.asktun.mg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.asktun.mg.R;

public class StarLayout extends LinearLayout {

	private ImageView[] star = new ImageView[5];
	private LinearLayout.LayoutParams lp = null;
	private int count;

	public StarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.starLayout);
		count = a.getInteger(R.styleable.starLayout_count, 0);
		for (int i = 0; i < star.length; i++) {
			star[i] = new ImageView(context);
			star[i].setImageResource(R.drawable.star_gray);
			addView(star[i], lp);
		}
		setStarOfCount();
	}


	/*
	 * 全部清灰
	 */
	private void clearToGray() {
		for (ImageView iv : star) {
			iv.setImageResource(R.drawable.star_gray);
		}
	}

	/**
	 * 获取红星个数
	 * 
	 * @return
	 */
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		clearToGray();
		setStarOfCount();
	}
	
	private void setStarOfCount(){
		for (int i = 0; i < count; i++) {
			star[i].setImageResource(R.drawable.star);
		}
	}

}
