package com.asktun.mg.view;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.widget.GridView;

public class MyGridView extends GridView {

	public MyGridView(android.content.Context context) {
		// TODO Auto-generated constructor stub
		super(context);

	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs, R.attr.listViewStyle);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/*
	 * @Override protected void onMeasure(int widthMeasureSpec, int
	 * heightMeasureSpec) { super.onMeasure(widthMeasureSpec,
	 * heightMeasureSpec); if (allHeight > 0) {
	 * setMeasuredDimension(widthMeasureSpec, allHeight); } else if (height > 0)
	 * { int h = (int) (getAdapter().getCount() - 1 * 1.5); h +=
	 * getAdapter().getCount() * height; setMeasuredDimension(widthMeasureSpec,
	 * h); } else { int h = getAdapter().getView(0, null, this).getHeight();
	 * setMeasuredDimension(widthMeasureSpec, getAdapter().getCount() * h); }
	 * 
	 * }
	 */

	View obtainView(int position) {
		View child;

		child = getAdapter().getView(position, null, this);
		if (getCacheColorHint() != 0) {
			child.setDrawingCacheBackgroundColor(getCacheColorHint());
		}
		if (ViewDebug.TRACE_RECYCLER) {
			ViewDebug.trace(child, ViewDebug.RecyclerTraceType.NEW_VIEW,
					position, getChildCount());
		}
		return child;
	}

}
