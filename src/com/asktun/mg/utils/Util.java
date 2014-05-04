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

	private static final String[] dxList = { "队友", "基友", "闺蜜", "师傅", "PK对手",
			"情侣" };

	public static void getPurposeIcon(String name, TextView tv) {
		int iconId = R.drawable.icon_purpose1;
		String typeName = "队";
		if (StrUtil.isEmpty(name)) {
			iconId = R.drawable.icon_purpose1;
			typeName = "队";
		}
		if (name.equals(dxList[0])) {
			iconId = R.drawable.icon_purpose1;
			typeName = "队";
		} else if (name.equals(dxList[1])) {
			iconId = R.drawable.icon_purpose2;
			typeName = "基";
		} else if (name.equals(dxList[2])) {
			iconId = R.drawable.icon_purpose3;
			typeName = "闺";
		} else if (name.equals(dxList[3])) {
			iconId = R.drawable.icon_purpose4;
			typeName = "师";
		} else if (name.equals(dxList[4])) {
			iconId = R.drawable.icon_purpose5;
			typeName = "PK";
		} else if (name.equals(dxList[5])) {
			iconId = R.drawable.icon_purpose6;
			typeName = "情";
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
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
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
	 * 将字符串转位日期类型
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
	 * 以友好的方式显示时间
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

		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
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
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = "10天前";
		}
		return ftime;
	}

	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			// String name = info.getTypeName();
			// L.i("当前网络名称：" + name);
			isNetConnected = true;
		} else {
			L.i("没有可用网络");
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
			// 如果不能转换,肯定是错误格式
			return false;
		}
		String s1 = df.format(d);
		// 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
		// "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
		// 逻辑上不对.
		return date.equals(s1);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	/**
	 * 
	 * 描述：px转换为dip
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
	 * @return 根据包名判断 应用是否安装
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
			System.out.println("没有安装");
			return false;
		} else {
			System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * @param context
	 * @param pName
	 *            根据包名启动应用
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
	 * @return 根据apk文件 路径 获得包名
	 */
	public static String getPnameByApk(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(filePath,
				PackageManager.GET_ACTIVITIES);
		String packageName = null;
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			// String appName = pm.getApplicationLabel(appInfo).toString();
			packageName = appInfo.packageName; // 得到安装包名称
			// String version= info.versionName; //得到版本信息
			// Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
			System.out.println("getPnameByApk" + packageName);
		}
		return packageName;
	}

	/**
	 * 安装apk
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
	 * 比较两个时间（hh:mm:ss）大小
	 * 
	 * @param date1
	 * @param date2
	 * @return true 前者大 false 后者大
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
		String[] astro = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
				"双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
		int[] arr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };// 两个星座分割日
		int index = month;
		// 所查询日期在分割日之前，索引-1，否则不变
		if (day < arr[month - 1]) {
			index = index - 1;
		}
		// 返回索引指向的星座string
		return astro[index];
	}

	/*
	 * 检查更新
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
	 * 获取屏幕尺寸
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
