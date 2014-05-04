package com.asktun.mg.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.asktun.mg.R;
import com.asktun.mg.bean.CheckVersion;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.service.ApkItem;
import com.chen.jmvc.util.StrUtil;
import com.nostra13.universalimageloader.utils.L;

public class Util {

	public static final int GETDATA_NO_UPDATE = -30;
	public static final int GETDATA_FAIL = -11;
	public static final int GETDATA_SUCCESS = -12;

	private static final String[] dxList = { "����", "����", "����", "ʦ��", "PK����",
			"����" };

	public static void getPurposeIcon(String name, TextView tv) {
		int iconId = R.drawable.icon_purpose1;
		String typeName = "��";
		if (StrUtil.isEmpty(name)) {
			iconId = R.drawable.icon_purpose1;
			typeName = "��";
		}
		if (name.equals(dxList[0])) {
			iconId = R.drawable.icon_purpose1;
			typeName = "��";
		} else if (name.equals(dxList[1])) {
			iconId = R.drawable.icon_purpose2;
			typeName = "��";
		} else if (name.equals(dxList[2])) {
			iconId = R.drawable.icon_purpose3;
			typeName = "��";
		} else if (name.equals(dxList[3])) {
			iconId = R.drawable.icon_purpose4;
			typeName = "ʦ";
		} else if (name.equals(dxList[4])) {
			iconId = R.drawable.icon_purpose5;
			typeName = "PK";
		} else if (name.equals(dxList[5])) {
			iconId = R.drawable.icon_purpose6;
			typeName = "��";
		}
		tv.setBackgroundResource(iconId);
		tv.setText(typeName);
	}

	public static int getAppVersionCode(Context context) {
		int versionName = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	private final static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static boolean checkSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * �жϸ����ַ����Ƿ�հ״��� �հ״���ָ�ɿո��Ʊ�����س��������з���ɵ��ַ��� �������ַ���Ϊnull����ַ���������true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * ���ַ���תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		if (isEmpty(sdate))
			return null;
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * ���Ѻõķ�ʽ��ʾʱ��
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// �ж��Ƿ���ͬһ��
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
		} else if (days == 1) {
			ftime = "����";
		} else if (days == 2) {
			ftime = "ǰ��";
		} else if (days > 2 && days <= 10) {
			ftime = days + "��ǰ";
		} else if (days > 10) {
			ftime = "10��ǰ";
		}
		return ftime;
	}

	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// ����������ӷ���
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			// String name = info.getTypeName();
			// L.i("��ǰ�������ƣ�" + name);
			isNetConnected = true;
		} else {
			L.i("û�п�������");
			isNetConnected = false;
		}
		return isNetConnected;
	}

	public static boolean isDate(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = df.parse(date);
		} catch (Exception e) {
			// �������ת��,�϶��Ǵ����ʽ
			return false;
		}
		String s1 = df.format(d);
		// ת�����������ת����String,�������,�߼�����.��formatΪ"yyyy-MM-dd",dateΪ
		// "2006-02-31",ת��Ϊ���ں���ת�����ַ���Ϊ"2006-03-03",˵����ʽ��Ȼ��,������
		// �߼��ϲ���.
		return date.equals(s1);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	/**
	 * 
	 * ������pxת��Ϊdip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 * @throws
	 */
	public static int px2dip(Resources resources, float pxValue) {
		final float scale = resources.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	public static String getNameFromUrl(String url) {
		if (url == null) {
			return "";
		}
		String name = null;
		String[] strs = url.split("/");
		name = strs[strs.length - 1];
		return name;
	}

	private final static double EARTH_RADIUS = 6378137.0;

	public static double gps2m(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * @param context
	 * @param pName
	 * @return ���ݰ����ж� Ӧ���Ƿ�װ
	 */
	public static boolean isAppInstalled(Context context, String pName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(pName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			System.out.println("û�а�װ");
			return false;
		} else {
			System.out.println("�Ѿ���װ");
			return true;
		}
	}

	/**
	 * @param context
	 * @param pName
	 *            ���ݰ�������Ӧ��
	 */
	public static void startAppByPname(Context context, String pName) {
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent();
		intent = pm.getLaunchIntentForPackage(pName);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * @param context
	 * @param filePath
	 * @return ����apk�ļ� ·�� ��ð���
	 */
	public static String getPnameByApk(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(filePath,
				PackageManager.GET_ACTIVITIES);
		String packageName = null;
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			// String appName = pm.getApplicationLabel(appInfo).toString();
			packageName = appInfo.packageName; // �õ���װ������
			// String version= info.versionName; //�õ��汾��Ϣ
			// Drawable icon = pm.getApplicationIcon(appInfo);//�õ�ͼ����Ϣ
			System.out.println("getPnameByApk" + packageName);
		}
		return packageName;
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	public static void installApk(Context context, String apkFilePath) {
		System.out.println("installApk````" + apkFilePath);
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	public static void uninstallApk(Context context, String packageName) {
		Uri packageUri = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
		context.startActivity(uninstallIntent);
	}

	/**
	 * �Ƚ�����ʱ�䣨hh:mm:ss����С
	 * 
	 * @param date1
	 * @param date2
	 * @return true ǰ�ߴ� false ���ߴ�
	 */
	public static boolean compare2Date(String date1, String date2) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		if (!StrUtil.isEmpty(date1) && StrUtil.isEmpty(date2)) {
			return true;
		}
		try {
			d1 = sdf.parse(date1 + ":00");
			d2 = sdf.parse(date2 + ":00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (d1.getTime() > d2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public static void hideKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// List dd = Arrays.asList(ddd);
	}

	public static ApkItem GameInfoToAI(GameInfo gi) {
		ApkItem item = new ApkItem();
		item.apkSize = gi.getSize();
		item.apkUrl = gi.getUrl();
		item.appIconUrl = gi.getGame_ico();
		item.appId = Integer.parseInt(gi.getGame_id());
		item.appName = gi.getGame_name();
		item.score = gi.getScore();
		item.cate_name = gi.getCate_name();
		item.playcount = gi.getUser_num();
		item.status = gi.getStatus();
		return item;
	}

	public static double round(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

	public static String getAstro(int month, int day) {
		String[] astro = new String[] { "Ħ����", "ˮƿ��", "˫����", "������", "��ţ��",
				"˫����", "��з��", "ʨ����", "��Ů��", "�����", "��Ы��", "������", "Ħ����" };
		int[] arr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };// ���������ָ���
		int index = month;
		// ����ѯ�����ڷָ���֮ǰ������-1�����򲻱�
		if (day < arr[month - 1]) {
			index = index - 1;
		}
		// ��������ָ�������string
		return astro[index];
	}

	/*
	 * ������
	 */
	public static void checkUpdate(Context context, final Handler handler,
			final Dialog progressDialog, String version) {
		if (progressDialog != null) {
			progressDialog.show();
		}

		JsonReqUtil jru = new JsonReqUtil(context);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app_id", Util.getAppId(context));
		params.put("version", version);
		jru.setRequstType(JsonReqUtil.REQUEST_GET);

		jru.request(new CheckVersion(), params, new JsonCallBack2() {
			@Override
			public void handler(Object obj, Exception ex) {

				if (progressDialog != null && !progressDialog.isShowing()) {
					return;
				}

				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				if (obj != null) {
					CheckVersion obj2 = (CheckVersion) obj;

					switch (Integer.parseInt(obj2.getFlg())) {
					case 0:
						handler.sendEmptyMessage(GETDATA_NO_UPDATE);
						break;
					case 1:
						Message msg = new Message();
						msg.obj = obj2;
						msg.what = GETDATA_SUCCESS;
						handler.sendMessage(msg);

						break;
					case -1:
						handler.sendEmptyMessage(GETDATA_FAIL);
						break;
					default:
						break;
					}

				} else {
					handler.sendEmptyMessage(GETDATA_FAIL);
				}
			}
		});
	}

	/**
	 * 
	 * @param cxt
	 * @return
	 */
	public static String getUrl(Context cxt) {
		InputStream fis = null;
		try {
			fis = cxt.getResources().getAssets().open("pro.properties");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Properties pro = new Properties();

		try {
			pro.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pro.getProperty("com.asktun.json.api.url");
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppId(Context context) {
		return getMetaData(context, "appID");
	}

	public static String getMetaData(Context cxt, String key) {
		ApplicationInfo appInfo = null;
		try {
			appInfo = cxt.getPackageManager().getApplicationInfo(
					cxt.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Bundle b = appInfo.metaData;
		String str = b.getString(key);
		if (str == null) {
			return b.getInt(key) + "";
		} else {
			return str;
		}
	}

	/**
	 * ��ȡ��Ļ�ߴ�
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
}
