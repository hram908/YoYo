package com.asktun.mg.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.utils.FileUtils;
import com.asktun.mg.utils.ImageUtils;
import com.chen.jmvc.util.StrUtil;

/**
 * 说明：主要用于选择文件操作
 * 
 * @author Dean
 * 
 */

public class SelectPicActivity extends BaseActivity implements OnClickListener {

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

	private LinearLayout dialogLayout;
	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;

	private Uri origUri;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/mobilegame/Portrait/";
	private String protraitPath;
	private File protraitFile;
	private Uri cropUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pic);
		initView();
	}

	/**
	 * 初始化加载View
	 */
	private void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo: // 拍照选择
			takePhoto();
			break;
		case R.id.btn_pick_photo: // 相册选择
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
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
			defaultstartActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "SD卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		// Intent intent = new Intent(Intent.ACTION_PICK, null);
		/**
		 * 下面这句话，与其它方式写是一样的效果，如果：
		 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * intent.setType(""image/*");设置数据类型
		 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		 */
		// intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// "image/*");
		// startActivityForResult(Intent.createChooser(intent, "选择图片"),
		// SELECT_PIC_BY_PICK_PHOTO);

		/*****   *****/
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setType("image/*");
		// startActivityForResult(Intent.createChooser(intent, "选择图片"),
		// SELECT_PIC_BY_PICK_PHOTO);

		/*****   *****/
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		defaultstartActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.defaultFinish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// 如果是直接从相册获取
			case SELECT_PIC_BY_PICK_PHOTO:
				if (data != null)
					startActionCrop(data.getData());
				break;
			// 如果是调用相机拍照时
			case SELECT_PIC_BY_TACK_PHOTO:
				startActionCrop(origUri);
				break;
			case SELECT_PHOTO_ALREADY:
				if (data != null) {
					setPicToView(data);
				}
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		Intent intent = getIntent();
		intent.putExtras(extras);
		intent.putExtra("path", protraitPath);
		if (extras != null) {
			setResult(Activity.RESULT_OK, intent);
			this.finish();
		}
	}

	// /**
	// * 选择图片后，获取图片的路径
	// *
	// * @param requestCode
	// * @param data
	// */
	// private void doPhoto(Uri uri) {
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
	// intent.putExtra("crop", "true");
	// // aspectX aspectY 是宽高的比例
	// intent.putExtra("aspectX", 2);
	// intent.putExtra("aspectY", 1);
	// // outputX outputY 是裁剪图片宽高
	// intent.putExtra("outputX", 1000);
	// intent.putExtra("outputY", 500);
	// intent.putExtra("return-data", true);
	// startActivityForResult(intent, SELECT_PHOTO_ALREADY);
	// }

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
		if (this.getUploadTempFile(data) == null) {
			return;
		}
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);// 输出图片大小
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// intent.putExtra("return-data", true);
		defaultstartActivityForResult(intent, SELECT_PHOTO_ALREADY);
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
		String cropFileName = "kaku_crop_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

}
