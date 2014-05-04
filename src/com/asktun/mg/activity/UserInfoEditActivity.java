package com.asktun.mg.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.DelLoveGameBean;
import com.asktun.mg.bean.HostInfo;
import com.asktun.mg.bean.HostInfo.GameItem;
import com.asktun.mg.bean.ModifyIconBean;
import com.asktun.mg.bean.PhotoBean;
import com.asktun.mg.bean.PhotoBean.PhotoInfo;
import com.asktun.mg.bean.PicDeleteBean;
import com.asktun.mg.bean.RegisterBean;
import com.asktun.mg.bean.UserInfoBean;
import com.asktun.mg.bean.UserInfoBean.UserInfo;
import com.asktun.mg.utils.FileUtils;
import com.asktun.mg.utils.ImageUtils;
import com.asktun.mg.utils.UploadUtil;
import com.asktun.mg.utils.UploadUtil.OnUploadProcessListener;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.Constant;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.StrUtil;
import com.google.gson.GsonBuilder;

public class UserInfoEditActivity extends BaseActivity {
	/******* 拍照保存的路劲 *******/
	public static final String imageCameraPath = "mnt/sdcard/DCIM/Camera/";
	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	/***
	 * 选择好照片后
	 */
	public static final int SELECT_PHOTO_ALREADY = 3;
	private Uri origUri;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/mobilegame/Portrait/";
	private String protraitPath;
	private File protraitFile;
	private Uri cropUri;
	private Bitmap bitmap;

	// @ViewInject (id = R.id.user_info_icon)
	// private ImageView icon;
	@ViewInject(id = R.id.user_info_name)
	private EditText userName;
	@ViewInject(id = R.id.user_info_gender)
	private TextView userGender;
	@ViewInject(id = R.id.user_info_gender_iv)
	private ImageView userGenderIv;
	@ViewInject(id = R.id.user_info_age)
	private TextView userAge;
	@ViewInject(id = R.id.user_info_signature)
	private EditText userSignature;
	@ViewInject(id = R.id.user_info_address)
	private EditText userAddress;
	@ViewInject(id = R.id.user_info_job)
	private EditText userJob;
	@ViewInject(id = R.id.user_info_school)
	private EditText userSchool;
	@ViewInject(id = R.id.user_info_interest)
	private EditText userInterest;
	@ViewInject(id = R.id.user_info_company)
	private EditText userCompany;
	@ViewInject(id = R.id.user_info_gender_layout)
	private LinearLayout genderLL;
	@ViewInject(id = R.id.edit_info_job)
	private LinearLayout jobLL;
	@ViewInject(id = R.id.edit_info_find)
	private LinearLayout findLL;
	@ViewInject(id = R.id.user_info_edit_age)
	private LinearLayout ageLL;
	@ViewInject(id = R.id.spinner_job)
	private Spinner spinner_job;
	@ViewInject(id = R.id.spinner_find)
	private Spinner spinner_find;
	@ViewInject(id = R.id.gv)
	private GridView gv;
	@ViewInject(id = R.id.game_pic_gv)
	private GridView gamePicGV;
	@ViewInject(id = R.id.lovegame_pic_gv)
	private GridView loveGameGV;

	private GVAdapter adapter;
	private GameGVAdapter gameAdapter;
	private GvLoveAdapter adapter_love;

	private View mTimeView1 = null; // 年月日view

	private UserInfo info;

	private boolean isGamePic = false;
	private View dialogView;
	private String genderStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_info_edit);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		final Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.ok_hook_btn_selector);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// uploadIconGo();
				modifyUserInfo();
			}
		});
		addRightView(btn);
		setTitleText("个人信息");
		init();
		getUserPic();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getUserInfo();
	}

	private static final String[] list_job = new String[] { "计算机/互联网/通信",
			"生产/工艺/制作", "商业/服务业/个体经营", "金融/银行/投资/保险", "文化/广告/传媒", "娱乐/艺术/表演",
			"医疗/护理/制药" };
	private static final String[] list_find = new String[] { "队友", "基友", "情侣",
			"师傅", "PK对手", "闺蜜" };

	private void init() {
		adapter_love = new GvLoveAdapter();
		loveGameGV.setAdapter(adapter_love);
		adapter = new GVAdapter();
		gv.setAdapter(adapter);
		gameAdapter = new GameGVAdapter();
		gamePicGV.setAdapter(gameAdapter);
		initDialog();
		setEditable(true);

		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				R.layout.item_spinner, list_job);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_job.setAdapter(adapter3);
		jobLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				spinner_job.performClick();
			}
		});

		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
				R.layout.item_spinner, list_find);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_find.setAdapter(adapter4);
		findLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				spinner_find.performClick();
			}
		});

		mTimeView1 = mInflater.inflate(R.layout.choose_three, null);
		initWheelDate(mTimeView1, userAge);
		userAge.setText("");
		ageLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Constant.DIALOGBOTTOM, mTimeView1, 40);
			}
		});
		// genderLL.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// showDialog(Constant.DIALOGCENTER, dialogView, 40);
		// }
		// });
		// icon.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(canEdit){
		// pickPhoto();
		// }
		// }
		// });
	}

	private void initDialog() {
		dialogView = mInflater.inflate(R.layout.dialog_gender, null);
		Button btn_dialog2 = (Button) dialogView.findViewById(R.id.btn_dialog2);
		Button btn_dialog3 = (Button) dialogView.findViewById(R.id.btn_dialog3);
		btn_dialog2.setOnClickListener(new OnClickListener() { // 男

					@Override
					public void onClick(View v) {
						userGenderIv.setBackgroundResource(R.drawable.sex_nan);
						userGender.setText("男");
						genderStr = "1";
						removeDialog(Constant.DIALOGCENTER);
					}
				});
		btn_dialog3.setOnClickListener(new OnClickListener() { // 女

					@Override
					public void onClick(View v) {
						userGenderIv.setBackgroundResource(R.drawable.sex_nv);
						userGender.setText("女");
						genderStr = "2";
						removeDialog(Constant.DIALOGCENTER);
					}
				});
	}

	private void setData() {
		// imageLoader.displayImage(info.getUser_ico(), icon);
		userName.setText(info.getUser_name());
		if (info.getUser_gender().equals("1")) {
			userGenderIv.setBackgroundResource(R.drawable.sex_nan);
			userGender.setText("男");
			genderStr = "1";
		} else {
			userGenderIv.setBackgroundResource(R.drawable.sex_nv);
			userGender.setText("女");
			genderStr = "2";
		}
		userAge.setText(info.getUser_birth());
		userAddress.setText(info.getUser_address());
		userSignature.setText(info.getSignature());
		userJob.setText(info.getJob());
		userSchool.setText(info.getSchool());
		userInterest.setText(info.getInterest());
		userCompany.setText(info.getCompany());
	}

	private void setEditable(boolean b) {
		// userName.setEnabled(b);
		userAge.setEnabled(b);
		userAddress.setEnabled(b);
		userSignature.setEnabled(b);
		userJob.setEnabled(b);
		userSchool.setEnabled(b);
		userInterest.setEnabled(b);
	}

	private class GvLoveAdapter extends BaseAdapter {
		GameItem item = null;
		private List<GameItem> list = new ArrayList<HostInfo.GameItem>();

		public void addAll(List<GameItem> data) {
			list.addAll(data);
			notifyDataSetChanged();
		}

		public void delete(int position) {
			list.remove(position);
		}

		public void clear() {
			list.clear();
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			convertView = mInflater.inflate(
					R.layout.userinfo_icon_lovegamegv_item, null, false);
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
			ImageView ivdel = (ImageView) convertView.findViewById(R.id.iv_del);
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			if (position == list.size()) {
				iv.setImageResource(R.drawable.icon_add);
				iv.setVisibility(View.VISIBLE);
				ivdel.setVisibility(View.GONE);
				tv_name.setVisibility(View.INVISIBLE);
				if (list.size() > 9) {
					iv.setVisibility(View.GONE);
				}
			} else if (position < list.size()) {
				item = list.get(position);
				tv_name.setText(item.game_name);
				String image = item.game_ico;
				if (!StrUtil.isEmpty(image)) {
					imageLoader.displayImage(image, iv, options_round10);
				} else {
					iv.setImageResource(R.drawable.moren_img);
				}
			}
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == list.size()) {
						// 选择喜欢的游戏
						startActivity(ChooseGame2Activity.class);
					}
				}
			});
			ivdel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position < list.size()) {
						item = list.get(position);
						// 删除
						dialogOpen("提示", "确定删除?",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										adapter_love.delete(position);
										adapter_love.notifyDataSetChanged();
										if (adapter.getCount() < 6) {
											LayoutParams params = new LayoutParams(
													ViewGroup.LayoutParams.MATCH_PARENT,
													ViewGroup.LayoutParams.WRAP_CONTENT);
											loveGameGV.setLayoutParams(params);
										}
										deleteLoveGame(item.gt_id);
									}
								});
					}
				}
			});

			return convertView;
		}

	}

	class GVAdapter extends BaseAdapter {
		private List<PhotoInfo> list = new ArrayList<PhotoInfo>();
		PhotoInfo pi = null;

		public void add(PhotoInfo b) {
			list.add(b);
		}

		public void delete(int position) {
			list.remove(position);
		}

		public void clear() {
			list.clear();
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = LayoutInflater.from(UserInfoEditActivity.this)
					.inflate(R.layout.userinfo_icon_gv_item, null);
			ImageView iv = (ImageView) convertView.findViewById(R.id.image);
			ImageView ivdel = (ImageView) convertView.findViewById(R.id.iv_del);
			if (position == list.size()) {
				iv.setImageResource(R.drawable.icon_add);
				iv.setVisibility(View.VISIBLE);
				ivdel.setVisibility(View.GONE);
			} else if (position < list.size()) {
				pi = list.get(position);
				// Bitmap bitmap = BitmapFactory.decodeFile(pi.getPhoto());
				// iv.setImageBitmap(bitmap);
				imageLoader.displayImage(pi.getPhoto(), iv, options);
			}
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == list.size()) {
						if (list.size() > 7) {
							showToast("最多只能添加8张！");
						} else {
							defaultstartActivityForResult(new Intent(
									UserInfoEditActivity.this,
									SelectPicActivity.class), 2);
							isGamePic = false;
						}

					}
				}
			});
			ivdel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position < list.size()) {
						pi = list.get(position);
						// 删除已选图片
						dialogOpen("提示", "确定删除?",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (list.size() > 1) {
											adapter.delete(position);
											adapter.notifyDataSetChanged();
											if (adapter.getCount() < 5) {
												LayoutParams params = new LayoutParams(
														ViewGroup.LayoutParams.MATCH_PARENT,
														ViewGroup.LayoutParams.WRAP_CONTENT);
												gv.setLayoutParams(params);
											}
											deletePic(pi.getPhoto_id());
										} else {
											showToast("请至少保留一张");
										}
									}
								});
					}
				}
			});
			return convertView;
		}

	}

	class GameGVAdapter extends BaseAdapter {
		private List<PhotoInfo> list = new ArrayList<PhotoInfo>();
		PhotoInfo pi = null;

		public void add(PhotoInfo b) {
			list.add(b);
		}

		public void delete(int position) {
			list.remove(position);
		}

		public void clear() {
			list.clear();
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = LayoutInflater.from(UserInfoEditActivity.this)
					.inflate(R.layout.userinfo_icon_gamegv_item, null);
			ImageView iv = (ImageView) convertView.findViewById(R.id.image);
			ImageView ivdel = (ImageView) convertView.findViewById(R.id.iv_del);

			if (position == list.size()) {
				iv.setBackgroundResource(R.drawable.icon_add);
				iv.setVisibility(View.VISIBLE);
				ivdel.setVisibility(View.GONE);
			} else if (position < list.size()) {
				pi = list.get(position);
				// Bitmap bitmap = BitmapFactory.decodeFile(pi.getPhoto());
				// iv.setImageBitmap(bitmap);
				imageLoader.displayImage(pi.getPhoto(), iv, options_round);
				System.out.println("gameAdapter -pi.getPhoto_id()--"
						+ pi.getPhoto_id());
			}

			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == list.size()) {
						if (list.size() > 7) {
							showToast("最多只能添加8张！");
						} else {
							defaultstartActivityForResult(new Intent(
									UserInfoEditActivity.this,
									SelectPicActivity.class), 2);
							// pickPhoto();
							isGamePic = true;
						}
					}
				}
			});
			ivdel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position < list.size()) {
						pi = list.get(position);
						System.out.println("delete -pi.getPhoto_id()--"
								+ pi.getPhoto_id() + " position=" + position);
						// 删除已选图片
						dialogOpen("提示", "确定删除?",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (list.size() > 1) {
											gameAdapter.delete(position);
											gameAdapter.notifyDataSetChanged();
											if (gameAdapter.getCount() < 5) {
												LayoutParams params = new LayoutParams(
														ViewGroup.LayoutParams.MATCH_PARENT,
														ViewGroup.LayoutParams.WRAP_CONTENT);
												gamePicGV
														.setLayoutParams(params);
											}

											deletePic(pi.getPhoto_id());
										} else {
											showToast("请至少保留一张");
										}
									}
								});
					}
				}
			});
			return convertView;
		}

	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && null != data) {
			// switch (requestCode) {
			// // 如果是直接从相册获取
			// case SELECT_PIC_BY_PICK_PHOTO:
			// startActionCrop(data.getData());
			// break;
			// case SELECT_PHOTO_ALREADY:
			// bitmap = BitmapFactory.decodeFile(protraitPath);
			// // icon.setImageBitmap(bitmap);
			// if(!isGamePic){
			// // adapter.add(protraitPath);
			// adapter.notifyDataSetChanged();
			// uploadIcon(protraitPath,"1");
			// }else{
			// // gameAdapter.add(protraitPath);
			// gameAdapter.notifyDataSetChanged();
			// uploadIcon(protraitPath,"2");
			// }
			// break;
			// }

			Bundle extras = data.getExtras();
			protraitPath = extras.getString("path");
			bitmap = BitmapFactory.decodeFile(protraitPath);
			if (!isGamePic) {
				// adapter.add(protraitPath);
				adapter.notifyDataSetChanged();
				uploadIcon(protraitPath, "1");
			} else {
				// gameAdapter.add(protraitPath);
				gameAdapter.notifyDataSetChanged();
				uploadIcon(protraitPath, "2");
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);// 输出图片大小
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// intent.putExtra("return-data", true);
		startActivityForResult(intent, SELECT_PHOTO_ALREADY);
	}

	// 裁剪头像的绝对路径
	private Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			showToast("无法保存上传的头像，请检查SD卡是否挂载");
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (StrUtil.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(this, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = StrUtil.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName = "mg_crop_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	private void getUserInfo() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(UserInfoBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						UserInfoBean data = (UserInfoBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								info = data.getData();
								if (data.loveGame != null) {
									adapter_love.clear();
									adapter_love.addAll(data.loveGame);
								}
								setData();
							} else if (data.getFlg() == -1) {
								// showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("无法获取信息");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("无法获取信息");
					}
				});
	}

	private String getConstellation(String str) {
		int month = 0, day = 0;
		try {
			String[] age = str.split("-");
			month = Integer.parseInt(age[1]);
			day = Integer.parseInt(age[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.getAstro(month, day);
	}

	private void modifyUserInfo() {
		// String name = userName.getText().toString();
		String signature = userSignature.getText().toString();
		String age = userAge.getText().toString();
		String constellation = getConstellation(age);
		String address = userAddress.getText().toString();
		String job = spinner_job.getSelectedItem().toString();
		String find = spinner_find.getSelectedItem().toString();
		String school = userSchool.getText().toString();
		String interest = userInterest.getText().toString();
		String company = userCompany.getText().toString();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("constellate", constellation);
		params.put("signature", signature);
		params.put("user_gender", "");
		params.put("user_age", age);
		params.put("user_address", address);
		params.put("job", job);
		params.put("purpose", find);
		params.put("school", school);
		params.put("interest", interest);
		params.put("company", company);

		params.put("user_ico", "");
		UIDataProcess.reqData(ModifyIconBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						ModifyIconBean data = (ModifyIconBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								showToast("修改成功");
								finish();
							} else if (data.getFlg() == -1) {
								// showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							} else if (data.getFlg() == 0) {
								// showToast("没有修改");
							}
						} else {
							showToast("操作失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("操作失败");
					}
				});
	}

	private void uploadIcon(String iconPath, String type) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("type", type);

		String fileKey = "photo";
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(new OnUploadProcessListener() {

			@Override
			public void onUploadProcess(int uploadSize) {

			}

			@Override
			public void onUploadDone(int responseCode, final String obj) {
				removeProgressDialog();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						RegisterBean o = null;
						JsonReqUtil.GsonObj gsonObj = null;
						try {
							gsonObj = RegisterBean.class.newInstance();
						} catch (InstantiationException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
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
								// showToast("失败");
							} else if (o.flg == -1) {
								// showToast("缺少参数");
							} else {
								// showToast("pic修改成功");
								adapter.clear();
								gameAdapter.clear();
								getUserPic();
							}

						} else {
							showToast("操作失败");
						}
					}
				});

			}

			@Override
			public void initUpload(int fileSize) {
				showProgressDialog();
			}
		}); // 设置监听器监听上传状态

		uploadUtil.uploadFile(iconPath, fileKey, JsonReqUtil.getURL()
				+ "members/addMemberPhoto", params);
	}

	private void getUserPic() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		UIDataProcess.reqData(PhotoBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						PhotoBean data = (PhotoBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								List<PhotoInfo> lifePicList = data.getData();
								List<PhotoInfo> gamePicList = data
										.getUserGamePhotos();
								if (lifePicList != null) {
									for (PhotoInfo pi : lifePicList) {
										// String picName =
										// Util.getNameFromUrl(pi.getPhoto());
										// String picPath =
										// AbFileUtil.downFileToSD(pi.getPhoto(),
										// picName);
										// adapter.add(picPath);
										adapter.add(pi);
										System.out.println("life   pi id====="
												+ pi.getPhoto_id());
									}
								}
								if (gamePicList != null) {
									for (PhotoInfo pi : gamePicList) {
										// String picName =
										// Util.getNameFromUrl(pi.getPhoto());
										// String picPath =
										// AbFileUtil.downFileToSD(pi.getPhoto(),
										// picName);
										// gameAdapter.add(picPath);
										gameAdapter.add(pi);
										System.out.println("game   pi id====="
												+ pi.getPhoto_id());
									}
								}
								if (adapter.getCount() > 4) {
									LayoutParams params = new LayoutParams(
											ViewGroup.LayoutParams.MATCH_PARENT,
											140);
									gv.setLayoutParams(params);
								}
								if (gameAdapter.getCount() > 4) {
									LayoutParams params = new LayoutParams(
											ViewGroup.LayoutParams.MATCH_PARENT,
											140);
									gamePicGV.setLayoutParams(params);
								}
								adapter.notifyDataSetChanged();
								gameAdapter.notifyDataSetChanged();
							} else if (data.getFlg() == -1) {
								// showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("无法获取信息");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("无法获取信息");
					}
				});
	}

	private void deletePic(String photoId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("photo_id", photoId);
		UIDataProcess.reqData(PicDeleteBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						PicDeleteBean data = (PicDeleteBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								// getUserPic();
								// showToast("del success");
							} else if (data.getFlg() == -1) {
								showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("操作失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("操作失败");
					}
				});
	}

	private void deleteLoveGame(String gameId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("gt_id", gameId);
		UIDataProcess.reqData(DelLoveGameBean.class, params, null,
				new IResponseListener() {

					@Override
					public void onSuccess(Object arg0) {
						DelLoveGameBean data = (DelLoveGameBean) arg0;
						if (data != null) {
							if (data.getFlg() == 1) {
								// showToast("del success");
							} else if (data.getFlg() == -1) {
								showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("操作失败");
						}
					}

					@Override
					public void onRuning(Object arg0) {

					}

					@Override
					public void onReqStart() {
						showProgressDialog();
					}

					@Override
					public void onFinish() {
						removeProgressDialog();
					}

					@Override
					public void onFailure(Object arg0) {
						showToast("操作失败");
					}
				});
	}
}
