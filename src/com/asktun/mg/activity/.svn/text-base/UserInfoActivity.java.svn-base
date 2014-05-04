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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.UIDataProcess;
import com.asktun.mg.bean.HostInfo;
import com.asktun.mg.bean.HostInfo.GameItem;
import com.asktun.mg.bean.ModifyIconBean;
import com.asktun.mg.bean.PhotoBean;
import com.asktun.mg.bean.PhotoBean.PhotoInfo;
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

public class UserInfoActivity extends BaseActivity {
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
			+ "/moblieGame/Portrait/";
	private String protraitPath;
	private File protraitFile;
	private Uri cropUri;
	private Bitmap bitmap;

	// @ViewInject (id = R.id.user_info_icon)
	// private ImageView icon;
	@ViewInject(id = R.id.gridview_love)
	private GridView gridView_love;
	@ViewInject(id = R.id.user_info_account)
	private TextView userAccount;
	@ViewInject(id = R.id.user_info_age)
	private TextView userAgeTV;
	@ViewInject(id = R.id.user_info_constellation)
	private TextView userConstellation;
	@ViewInject(id = R.id.user_info_find)
	private TextView userFind;
	@ViewInject(id = R.id.iv_purpose)
	private TextView iv_purpose;
	@ViewInject(id = R.id.user_info_gender_iv)
	private ImageView userGenderIV;
	@ViewInject(id = R.id.user_info_name)
	private EditText userName;
	@ViewInject(id = R.id.user_info_gender)
	private TextView userGender;
	@ViewInject(id = R.id.user_info_gender_iv)
	private ImageView userGenderIv;
	@ViewInject(id = R.id.user_info_age)
	private EditText userAge;
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
	private TextView userCompany;
	@ViewInject(id = R.id.user_info_gender_layout)
	private LinearLayout genderLL;
	@ViewInject(id = R.id.gv)
	private GridView gv;
	@ViewInject(id = R.id.game_pic_gv)
	private GridView gamePicGV;

	private GVAdapter adapter;
	private GameGVAdapter gameAdapter;
	private GvLoveAdapter adapter_love;

	private UserInfo info;
	private PhotoBean photoBean;
	private List<PhotoInfo> lifePicList = new ArrayList<PhotoInfo>();
	private List<PhotoInfo> gamePicList = new ArrayList<PhotoInfo>();

	private boolean canEdit = false;
	private View dialogView;
	private String genderStr;

	private int cHeight;
	private int space10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_user_info);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		final Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.button_edit_selector);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(canEdit){
				// modifyUserInfo();
				// setEditable(false);
				// canEdit = false;
				// btn.setBackgroundResource(R.drawable.modify_default);
				// adapter.notifyDataSetChanged();
				// }else{
				// setEditable(true);
				// canEdit = true;
				// btn.setBackgroundResource(R.drawable.modify_selected);
				// adapter.notifyDataSetChanged();
				// }
				startActivity(UserInfoEditActivity.class);
			}
		});
		addRightView(btn);
		// setTitleText("个人信息");
		init();
	}

	private void init() {
		cHeight = Util.dpToPx(getResources(), 46);
		space10 = Util.dpToPx(getResources(), 10);

		adapter_love = new GvLoveAdapter();
		adapter = new GVAdapter();
		gv.setAdapter(adapter);
		gameAdapter = new GameGVAdapter();
		gamePicGV.setAdapter(gameAdapter);
		// initDialog();
		setEditable(false);
		// genderLL.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(canEdit){
		// showDialog(Constant.DIALOGCENTER, dialogView, 40);
		// }
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

	@Override
	protected void onResume() {
		super.onResume();
		getUserInfo();
		getUserPic();
	}

	private void setData() {
		// imageLoader.displayImage(info.getUser_ico(), icon);
		userAccount.setText(info.getUser_id());
		setTitleText(info.getUser_name());
		userName.setText(info.getUser_name());
		if (info.getUser_gender().equals("1")) {
			userGenderIV.setBackgroundResource(R.drawable.sex_nan);
			userGender.setText("男");
			genderStr = "1";
		} else {
			userGenderIV.setBackgroundResource(R.drawable.sex_nv);
			userGender.setText("女");
			genderStr = "2";
		}
		userAgeTV.setText(info.getUser_age());
		if ("2".equals(info.getUser_gender())) {
			userAgeTV.setBackgroundResource(R.drawable.sex_nv);
		}
		userConstellation.setText(info.getUser_constellate());
		userFind.setText("找" + info.getPurpose());
		Util.getPurposeIcon(info.getPurpose(), iv_purpose);
		userAddress.setText(info.getUser_address());
		userSignature.setText(info.getSignature());
		userJob.setText(info.getJob());
		userSchool.setText(info.getSchool());
		userInterest.setText(info.getInterest());
		userCompany.setText(info.getCompany());

		int ii_love = adapter_love.getCount();
		LayoutParams params_love = new LayoutParams(ii_love
				* (cHeight + space10) , (int) (cHeight + 2.8 * space10));
		gridView_love.setLayoutParams(params_love);
		gridView_love.setColumnWidth(cHeight);
		gridView_love.setHorizontalSpacing(space10);
		gridView_love.setStretchMode(GridView.NO_STRETCH);
		gridView_love.setNumColumns(ii_love);
		gridView_love.setAdapter(adapter_love);
		adapter_love.notifyDataSetChanged();
	}

	private void setEditable(boolean b) {
		userName.setEnabled(b);
		// userAge.setEnabled(b);
		userAddress.setEnabled(b);
		userSignature.setEnabled(b);
		userJob.setEnabled(b);
		userSchool.setEnabled(b);
		userInterest.setEnabled(b);
	}

	class GVAdapter extends BaseAdapter {
		private List<String> list = new ArrayList<String>();

		public void add(String b) {
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
			if (lifePicList == null)
				return 0;
			else
				return lifePicList.size();
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
			convertView = LayoutInflater.from(UserInfoActivity.this).inflate(
					R.layout.item_image, null);
			ImageView iv = (ImageView) convertView.findViewById(R.id.image);
			// iv.setImageBitmap(list.get(position));
			PhotoInfo pi = lifePicList.get(position);
			if (pi != null) {
				imageLoader.displayImage(pi.getPhoto(), iv, options);
			}
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(UserInfoActivity.this,
							ImageBrowserActivity.class);
					intent.putExtra("lifeOrGame", 0);
					intent.putExtra("PhotoBean", photoBean);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	class GameGVAdapter extends BaseAdapter {
		private List<String> list = new ArrayList<String>();

		public void add(String b) {
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
			if (gamePicList == null)
				return 0;
			else
				return gamePicList.size();
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
			convertView = LayoutInflater.from(UserInfoActivity.this).inflate(
					R.layout.item_gameimage, null);
			ImageView iv = (ImageView) convertView.findViewById(R.id.image);
			// iv.setImageBitmap(list.get(position));
			PhotoInfo pi = gamePicList.get(position);
			if (pi != null) {
				imageLoader.displayImage(pi.getPhoto(), iv, options_round);
			}
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(UserInfoActivity.this,
							ImageBrowserActivity.class);
					intent.putExtra("lifeOrGame", 1);
					intent.putExtra("PhotoBean", photoBean);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	private class GvLoveAdapter extends BaseAdapter {

		private List<GameItem> list = new ArrayList<HostInfo.GameItem>();

		public void addAll(List<GameItem> data) {
			list.addAll(data);
			notifyDataSetChanged();
		}

		public void clear() {
			list.clear();
		}

		@Override
		public int getCount() {
			return list.size();
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_nearplay, null,
						false);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GameItem item = list.get(arg0);
			holder.tv_name.setText(item.game_name);
			String image = item.game_ico;
			if (!StrUtil.isEmpty(image)) {
				imageLoader.displayImage(image, holder.iv, options_round10);
			} else {
				holder.iv.setImageResource(R.drawable.moren_img);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv_name;
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
			switch (requestCode) {
			// 如果是直接从相册获取
			case SELECT_PIC_BY_PICK_PHOTO:
				startActionCrop(data.getData());
				break;
			case SELECT_PHOTO_ALREADY:
				if (data != null) {
					bitmap = BitmapFactory.decodeFile(protraitPath);
					// icon.setImageBitmap(bitmap);

				}
				break;
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
									adapter_love.notifyDataSetChanged();
								}

								setData();

							} else if (data.getFlg() == -1) {
								// showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("无法获取数据");
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
						showToast("无法获取数据");
					}
				});
	}

	private void modifyUserInfo() {
		String signature = userSignature.getText().toString();
		String age = userAge.getText().toString();
		String address = userAddress.getText().toString();
		String job = userJob.getText().toString();
		String school = userSchool.getText().toString();
		String interest = userInterest.getText().toString();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mApplication.getLoginbean().user_id);
		params.put("token", mApplication.getLoginbean().token);
		params.put("signature", signature);
		params.put("user_gender", genderStr);
		params.put("user_age", age);
		params.put("user_address", address);
		params.put("job", job);
		params.put("school", school);
		params.put("interest", interest);

		if (protraitPath == null) {
			params.put("user_ico", "");
			UIDataProcess.reqData(ModifyIconBean.class, params, null,
					new IResponseListener() {

						@Override
						public void onSuccess(Object arg0) {
							ModifyIconBean data = (ModifyIconBean) arg0;
							if (data != null) {
								if (data.getFlg() == 1) {
									showToast("修改成功");
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
		} else {
			String fileKey = "user_ico";
			UploadUtil uploadUtil = UploadUtil.getInstance();
			uploadUtil
					.setOnUploadProcessListener(new OnUploadProcessListener() {

						@Override
						public void onUploadProcess(int uploadSize) {

						}

						@Override
						public void onUploadDone(int responseCode,
								final String obj) {
							removeProgressDialog();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									RegisterBean o = null;
									JsonReqUtil.GsonObj gsonObj = null;
									try {
										gsonObj = RegisterBean.class
												.newInstance();
									} catch (InstantiationException e1) {
										e1.printStackTrace();
									} catch (IllegalAccessException e1) {
										e1.printStackTrace();
									}
									GsonBuilder builder = new GsonBuilder();
									builder.excludeFieldsWithoutExposeAnnotation();
									Type type = gsonObj.getTypeToken();
									try {
										o = builder.create()
												.fromJson(obj, type);
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
											showToast("修改成功");
										}

									} else {
										showToast("无法获取数据");
									}
								}
							});

						}

						@Override
						public void initUpload(int fileSize) {
							showProgressDialog();
						}
					}); // 设置监听器监听上传状态

			uploadUtil.uploadFile(protraitPath, fileKey, JsonReqUtil.getURL()
					+ "members/modifyUserInfo", params);
		}
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
								photoBean = data;
								lifePicList = data.getData();
								gamePicList = data.getUserGamePhotos();
								adapter.notifyDataSetChanged();
								gameAdapter.notifyDataSetChanged();
							} else if (data.getFlg() == -1) {
								// showToast("缺少参数");
							} else if (data.getFlg() == -2) {
								showToast("登陆状态出错，请重新登陆");
							}
						} else {
							showToast("无法获取数据");
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
						showToast("无法获取数据");
					}
				});
	}
}
