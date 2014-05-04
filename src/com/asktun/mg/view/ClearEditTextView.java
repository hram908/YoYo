package com.asktun.mg.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearEditTextView extends EditText {
	private Drawable dRight;
	private Rect rBounds;

	// ������
	public ClearEditTextView(Context paramContext) {
		super(paramContext);
		initEditText();
	}

	public ClearEditTextView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initEditText();
	}

	public ClearEditTextView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		initEditText();
	}

	// ��ʼ��edittext �ؼ�
	private void initEditText() {
		setEditTextDrawable();
		addTextChangedListener(new TextWatcher() { // ���ı����ݸı���м���
			public void afterTextChanged(Editable paramEditable) {
			}

			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
				ClearEditTextView.this.setEditTextDrawable();
			}
		});
	}

	// ����ͼƬ����ʾ
	private void setEditTextDrawable() {
		if (getText().toString().length() == 0) {
			setCompoundDrawables(null, null, null, null);
		} else {
			setCompoundDrawables(null, null, this.dRight, null);
		}
	}

	protected void finalize() throws Throwable {
		super.finalize();
		this.dRight = null;
		this.rBounds = null;
	}

	// ��Ӵ����¼�
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
			this.rBounds = this.dRight.getBounds();
			int i = (int) paramMotionEvent.getX();
			if (i > getRight() - 3 * this.rBounds.width()) {
				setText("");
				paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	// ������ʾ��ͼƬ��Դ
	public void setCompoundDrawables(Drawable paramDrawable1,
			Drawable paramDrawable2, Drawable paramDrawable3,
			Drawable paramDrawable4) {
		if (paramDrawable3 != null)
			this.dRight = paramDrawable3;
		super.setCompoundDrawables(paramDrawable1, paramDrawable2,
				paramDrawable3, paramDrawable4);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// ��ý��㣬�ж��Ƿ�������
		if (this.hasFocus() == true) {
			// ���û�����ݣ��򱣻���ʾ��հ�ť���������ʾ
			if (getText().toString().length() == 0) {
				setCompoundDrawables(null, null, null, null);
			} else {
				setCompoundDrawables(null, null, this.dRight, null);
			}
		} else {
			// ʧȥ���㣬���ذ�ť
			setCompoundDrawables(null, null, null, null);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

}
