package com.asktun.mg.activity;

import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.SearchBean;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.StrUtil;

/**
 * 附近筛选
 * 
 * @author Dean
 */
public class FilterNearActivity extends BaseActivity {

	@ViewInject(id = R.id.rg_sex)
	private RadioGroup rg_sex;
	@ViewInject(id = R.id.rg_time)
	private RadioGroup rg_time;
	@ViewInject(id = R.id.spinner_xingzuo)
	private Spinner spinner_xingzuo;
	@ViewInject(id = R.id.spinner_age)
	private Spinner spinner_age;
	@ViewInject(id = R.id.spinner_job)
	private Spinner spinner_job;
	@ViewInject(id = R.id.btn_xingzuo, click = "btnClick")
	private LinearLayout btn_xingzuo;
	@ViewInject(id = R.id.btn_age, click = "btnClick")
	private LinearLayout btn_age;
	@ViewInject(id = R.id.btn_job, click = "btnClick")
	private LinearLayout btn_job;
	@ViewInject(id = R.id.ll_game, click = "btnClick")
	private LinearLayout ll_game;
	@ViewInject(id = R.id.tv_game)
	private TextView tv_game;

	/******* 筛选用 *******/
	private String sex; // 筛选用
	private String age;
	private String job;
	private String game_name;
	private String constellate; // 星座
	private String last_active_time = "1"; // 最后活动时间

	FilterNearActivity act;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();

		setAbContentView(R.layout.activity_filternear);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		addRightButtonView(R.drawable.button_gou_selector).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getData();
						Intent intent = new Intent();
						intent.putExtra("sex", sex);
						intent.putExtra("age", age);
						intent.putExtra("job", job);
						intent.putExtra("game_name", game_name);
						intent.putExtra("constellate", constellate);
						intent.putExtra("last_active_time", last_active_time);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
		setTitleText("自定义筛选");
		act = this;
		init();
		initDialog();
	}

	private void getData() {
		age = spinner_age.getSelectedItemPosition() + "";
		if ("0".equals(age)) {
			age = null;
		}
		job = (String) spinner_job.getSelectedItem();
		if ("不限".equals(job)) {
			job = null;
		}
		constellate = (String) spinner_xingzuo.getSelectedItem();
		if ("不限".equals(constellate)) {
			constellate = null;
		}
		game_name = tv_game.getText().toString();
		if ("不限".equals(game_name)) {
			game_name = null;
		}
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_xingzuo:
			spinner_xingzuo.performClick();
			break;
		case R.id.btn_age:
			spinner_age.performClick();
			break;
		case R.id.btn_job:
			spinner_job.performClick();
			break;
		case R.id.ll_game:
			dialog.show();
			break;
		default:
			break;
		}
	}

	private AutoCompleteTextView et_game;
	private ArrayAdapter<String> adapter_tip;
	private String[] autoStrs = new String[] {};
	private View dialogView;
	private Button btn_config;
	private Button btn_cancel;
	private Dialog dialog;

	private void initDialog() {
		
		dialogView = mInflater.inflate(R.layout.dialog_searchgame, null);
		LinearLayout ll = (LinearLayout) dialogView.findViewById(R.id.dialog);
		et_game = (AutoCompleteTextView) ll.findViewById(R.id.etname);
		btn_config = (Button) dialogView.findViewById(R.id.btn_config);
		btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btn_config.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = et_game.getText().toString().trim();
				if (!StrUtil.isEmpty(text)) {
					tv_game.setText(text);
				}
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		et_game.setText("");
		et_game.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.toString() != null && s.length() > 0) {
					search(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		adapter_tip = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, autoStrs);
		et_game.setThreshold(1);
		et_game.setAdapter(adapter_tip);
		
		
		dialog = new Dialog(this, R.style.bubble_dialog);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		dialog.setContentView(dialogView, new LayoutParams(
				diaplayWidth * 4 / 5,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	private void search(String name) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("game_name", name);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(SearchBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						SearchBean data = (SearchBean) arg0;
						if (data.getFlg() == 1) {
							List<GameInfo> list = data.getData();
							autoStrs = new String[list.size()];
							for (int i = 0; i < list.size(); i++) {
								autoStrs[i] = list.get(i).getGame_name();
							}
							adapter_tip = new ArrayAdapter<String>(
									FilterNearActivity.this,
									android.R.layout.simple_dropdown_item_1line,
									autoStrs);
							et_game.setThreshold(1);
							et_game.setAdapter(adapter_tip);
						}
					}

					@Override
					public void onRuning(Object arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onReqStart() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFailure(Object arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	private static final String[] list_xingzuo = new String[] { "不限", "白羊座",
			"金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座",
			"水瓶座", "双鱼座" };
	private static final String[] list_age = new String[] { "不限", "18-25",
			"26-35", "36-45", "46以上" };
	private static final String[] list_job = new String[] { "不限", "计算机/互联网/通信",
			"生产/工艺/制作", "商业/服务业/个体经营", "金融/银行/投资/保险", "文化/广告/传媒", "娱乐/艺术/表演",
			"医疗/护理/制药" };

	private void init() {
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.item_spinner, list_xingzuo);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.item_spinner, list_age);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				R.layout.item_spinner, list_job);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_xingzuo.setAdapter(adapter1);
		spinner_age.setAdapter(adapter2);
		spinner_job.setAdapter(adapter3);

		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_sex_1: // 全部
					sex = null;
					break;
				case R.id.rb_sex_2: // 男
					sex = "1";
					break;
				case R.id.rb_sex_3: // 女
					sex = "2";
					break;

				default:
					break;
				}
			}
		});
		rg_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_time_1: // 15分钟
					last_active_time = "1";
					break;
				case R.id.rb_time_2: // 1小时
					last_active_time = "2";
					break;
				case R.id.rb_time_3: // 1天
					last_active_time = "3";
					break;
				case R.id.rb_time_4: // 3天
					last_active_time = "4";
					break;
				default:
					break;
				}
			}
		});

	}

}