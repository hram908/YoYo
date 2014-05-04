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
 * ע�� ͷ��
 * 
 * @author Dean
 */
public class RegisterIconActivity extends BaseActivity {
	/******* ���ձ����·�� *******/
	public static final String imageCameraPath = "mnt/sdcard/DCIM/Camera/";
	/***
	 * ʹ����������ջ�ȡͼƬ
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * ʹ������е�ͼƬ
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	/***
	 * ѡ�����Ƭ��
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

		addRightButton("���").setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReqRegister();
			}
		});
		setTitleText("����ͷ��(5/5)");
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
			showToast("��ѡ��ͷ��");
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
							Log.e("JsonReqUtil", "�õ������쳣");
						}
						if (o != null) {
							if (o.flg == 2) {
								showToast("�Ѵ��ڸ��û�");
							} else if (o.flg == 0) {
								showToast("ע��ʧ��");
							} else if (o.flg == -1) {
								showToast("ȱ�ٲ���");
							} else {
								showToast("ע��ɹ�");
								mApplication.clearActivity();
								finish();
							}

						} else {
							showToast("��ȡ����ʧ��");
						}
					}
				});

			}

			@Override
			public void initUpload(int fileSize) {
				// TODO Auto-generated method stub
				showProgressDialog();
			}
		}); // ���ü����������ϴ�״̬
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
	 * ���ջ�ȡͼƬ
	 */
	private void takePhoto() {
		// ִ������ǰ��Ӧ�����ж�SD���Ƿ����
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			// ����������Ƭ������ļ��м�ͼƬ�ļ���
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
			// �������ָ������������պ����Ƭ�洢��·��
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(protraitFile));
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "SD��������", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * �������ȡͼƬ
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
			// �����ֱ�Ӵ�����ȡ
			case SELECT_PIC_BY_PICK_PHOTO:
				startActionCrop(data.getData());
				break;
			// ����ǵ����������ʱ
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
	 * ���պ�ü�
	 * 
	 * @param data
	 *            ԭʼͼƬ
	 * @param output
	 *            �ü���ͼƬ
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// �ü������
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);// ���ͼƬ��С
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);// ȥ�ڱ�
		intent.putExtra("scaleUpIfNeeded", true);// ȥ�ڱ�
		// intent.putExtra("return-data", true);
		startActivityForResult(intent, SELECT_PHOTO_ALREADY);
	}

	// �ü�ͷ��ľ���·��
	private Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			showToast("�޷������ϴ���ͷ������SD���Ƿ����");
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// ����Ǳ�׼Uri
		if (StrUtil.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(this, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = StrUtil.isEmpty(ext) ? "jpg" : ext;
		// ��Ƭ����
		String cropFileName = "mg_crop_" + timeStamp + "." + ext;
		// �ü�ͷ��ľ���·��
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	/**
	 * ����ʱ��������ͼƬ����
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