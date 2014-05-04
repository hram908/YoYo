package com.asktun.mg.activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.bean.RegisterBean;
import com.asktun.mg.utils.FileUtils;
import com.asktun.mg.utils.ImageUtils;
import com.asktun.mg.utils.UploadUtil;
import com.asktun.mg.utils.Util;
import com.asktun.mg.utils.UploadUtil.OnUploadProcessListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.StrUtil;
import com.google.gson.GsonBuilder;

/**
 * 注册 头像
 * 
 * @author Dean
 */
public class RegisterIconActivity extends BaseActivity {
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

	@ViewInject(id = R.id.btn_pickphoto, click = "btnClick")
	private Button btn_pickphoto;
	@ViewInject(id = R.id.btn_takephoto, click = "btnClick")
	private Button btn_takephoto;
	@ViewInject(id = R.id.image)
	private ImageView image;

	private String birthday;
	private String phone;
	private String pwd;
	private String username;
	private int sex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_register_icon);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		phone = getIntent().getStringExtra("phone");
		pwd = getIntent().getStringExtra("pwd");
		username = getIntent().getStringExtra("username");
		birthday = getIntent().getStringExtra("birthday");
		sex = getIntent().getIntExtra("sex", 0);

		addRightButton("完成").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReqRegister();
			}
		});
		setTitleText("设置头像(5/5)");
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pickphoto:
			pickPhoto();
			break;
		case R.id.btn_takephoto:
			takePhoto();
			break;
		default:
			break;
		}
	}

	private void ReqRegister() {
		if (StrUtil.isEmpty(protraitPath) || !protraitFile.exists()) {
			showToast("请选择头像");
			return;
		}

		String fileKey = "user_ico";
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
								showToast("已存在该用户");
							} else if (o.flg == 0) {
								showToast("注册失败");
							} else if (o.flg == -1) {
								showToast("缺少参数");
							} else {
								showToast("注册成功");
								mApplication.clearActivity();
								finish();
							}

						} else {
							showToast("获取数据失败");
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
		params.put("user_name", username);
		params.put("user_passwd", pwd);
		params.put("phone", phone);
		params.put("user_birth", birthday);
		params.put("imei", Util.getIMEI(this));
		params.put("user_gender", sex + "");
		uploadUtil.uploadFile(protraitPath, fileKey, JsonReqUtil.getURL()
				+ "members/register", params);
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			// 创建拍摄照片保存的文件夹及图片文件名
			protraitPath = imageCameraPath + getPhotoFileName();
			protraitFile = new File(protraitPath);
			cropUri = Uri.fromFile(protraitFile);
			this.origUri = this.cropUri;
			if (!protraitFile.exists()) {
				try {
					protraitFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 下面这句指定调用相机拍照后的照片存储的路径
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(protraitFile));
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "SD卡不存在", Toast.LENGTH_LONG).show();
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
			// 如果是调用相机拍照时
			case SELECT_PIC_BY_TACK_PHOTO:
				startActionCrop(origUri);
				break;
			case SELECT_PHOTO_ALREADY:
				if (data != null) {
					bitmap = BitmapFactory.decodeFile(protraitPath);
					image.setImageBitmap(bitmap);
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

	/**
	 * 根据时间来命名图片名字
	 * 
	 * @return
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

}