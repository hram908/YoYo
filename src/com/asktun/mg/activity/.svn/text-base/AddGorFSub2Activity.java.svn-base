package com.asktun.mg.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.GameSearch;
import com.asktun.mg.bean.GameSearch.GameSearchItem;
import com.asktun.mg.bean.RegisterBean;
import com.asktun.mg.utils.UploadUtil;
import com.asktun.mg.utils.UploadUtil.OnUploadProcessListener;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.StrUtil;
import com.google.gson.GsonBuilder;

/**
 * 创建群组
 * 
 * @author Dean
 */
public class AddGorFSub2Activity extends BaseActivity {

	private String protraitPath;
	private File protraitFile;
	private Bitmap bitmap;

	@ViewInject(id = R.id.sub2_icon_rl, click = "btnClick")
	private RelativeLayout imageRL;
	@ViewInject(id = R.id.sub2_name)
	private AutoCompleteTextView name;
	@ViewInject(id = R.id.sub2_info)
	private EditText info;
	@ViewInject(id = R.id.sub2_icon)
	private ImageView image;
	@ViewInject(id = R.id.tv_qunmingcheng)
	private TextView tv_qunname;

	private int groupType;

	private ArrayAdapter<String> adapter_tip;
	private String[] autoStrs = new String[] {};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_addfriendorgroup_sub2);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.ok_hook_btn_selector);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitGroup();
			}
		});
		addRightView(btn);
		groupType = getIntent().getIntExtra("groupType", 1);
		if (groupType == 1) {
			setTitleText("创建聊天交友群");
		} else {
			setTitleText("创建游戏交友群");
			tv_qunname.setText("游戏名称");
//			name.setHintTextColor(getResources().getColor(R.color.red));
//			name.setHint("请输入完整的游戏名,方便游戏玩家找到");
			name.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					if (s.toString() != null && s.length() > 0) {
						search(s.toString());
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
			adapter_tip = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, autoStrs);
			name.setThreshold(1);
			name.setAdapter(adapter_tip);
		}

	}

	private void search(String game_name) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("game_name", game_name);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(GameSearch.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						GameSearch data = (GameSearch) arg0;
						if (data != null) {
							if (data.flg == 1) {
								List<GameSearchItem> list = data.data;
								autoStrs = new String[list.size()];
								for (int i = 0; i < list.size(); i++) {
									autoStrs[i] = list.get(i).game_name;
								}
								adapter_tip = new ArrayAdapter<String>(
										AddGorFSub2Activity.this,
										android.R.layout.simple_dropdown_item_1line,
										autoStrs);
								name.setThreshold(1);
								name.setAdapter(adapter_tip);
							}
						} else {
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

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.sub2_icon_rl:
			defaultstartActivityForResult(new Intent(AddGorFSub2Activity.this,
					SelectPicActivity.class), 2);
			break;
		default:
			break;
		}

	}

	private void submitGroup() {
		if (StrUtil.isEmpty(protraitPath) || !protraitFile.exists()) {
			showToast("请选择头像");
			return;
		}
		String groupName = name.getText().toString();
		
		int length = groupName.length();
		if(length>16){
			showToastInThread("群名称长度不能大于16");
			return;
		}
		String description = info.getText().toString();
		if (StrUtil.isEmpty(groupName) || StrUtil.isEmpty(description)) {
			showToast("请填写完整信息");
			return;
		}
		Util.hideKeyboard(this);

		String fileKey = "img_dir";
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
						RegisterBean o = null;
						JsonReqUtil.GsonObj gsonObj = null;
						try {
							gsonObj = RegisterBean.class.newInstance();
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
							if (o.flg == 2) {

							} else if (o.flg == 0) {
								showToast("失败");
							} else if (o.flg == -1) {
								// showToast("缺少参数");
							} else {
								showToast("创建成功");
								setResult(RESULT_OK);
								finish();
							}

						} else {
							showToast("创建失败");
						}
					}
				});

			}

			@Override
			public void initUpload(int fileSize) {
				// TODO Auto-generated method stub
				showProgressDialog();
			}
		}); // 设置监听器监听上传状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("type", groupType + ""); // 1 为普通群 ， 2为游戏群
		params.put("long", mApplication.mLocation.getLongitude() + "");
		params.put("lati", mApplication.mLocation.getLatitude() + "");
		params.put("group_name", groupName);
		params.put("description", description);
		uploadUtil.uploadFile(protraitPath, fileKey, JsonReqUtil.getURL()
				+ "UserGroups/createGroup", params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && null != data) {
			Bundle extras = data.getExtras();
			protraitPath = extras.getString("path");
			bitmap = BitmapFactory.decodeFile(protraitPath);
			protraitFile = new File(protraitPath);
			image.setImageBitmap(bitmap);

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}