/**
 * 
 */
package com.asktun.mg.view;



import com.asktun.mg.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * @author Administrator
 *
 */
public class MyProgressDialog extends Dialog {

	private Button dialog_dismissBtn;
	private TextView dialog_message;
	
	public MyProgressDialog(Context context)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setContentView(R.layout.custom_progressdialog);
		
		dialog_message = (TextView) findViewById(R.id.dialog_message);
		
//		dialog_dismissBtn = (Button) findViewById(R.id.dialog_dismissBtn);
//		dialog_dismissBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
//		if(keyCode==KeyEvent.KEYCODE_BACK)
//			return false;
		return super.onKeyDown(keyCode, event);
	}

	
	
	
	
	
	public void setMessage(String message) {
		dialog_message.setText(message);
	}
	
	
	public void hideCloseBtn() {
		dialog_dismissBtn.setVisibility(View.GONE);
	}
	
	
	
}
