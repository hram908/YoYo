package com.asktun.mg.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.asktun.mg.MyApp;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class DataManager {
	private static DataManager dataManager;
	
	
	private DataManager() {
		super();
	}

	public static DataManager newInstance() {
		if (dataManager == null) {
			dataManager = new DataManager();
		}
		return dataManager;
	}
	
	/**
	 * 请求软件更新列表数据
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public String initUpdateJson(Context context){
		List<String[]> list = initRequestUpdateList(context);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			String[] arr = list.get(i);
			sb.append("{\"version\":\"").append(arr[0]).append("\",\"game_name\":\"").append(arr[1]).append("\"}");
			if (i < list.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		System.out.println("sb json=" + sb.toString());
		return sb.toString();
	}

	
	/**
	 * 初始化请求更新数�?
	 * @param context
	 * @return
	 */
	
	private List<String[]> initRequestUpdateList(Context context){
		List<String[]> items = new ArrayList<String[]>();
		PackageManager pManager = context.getPackageManager();
		// 获取手机内所有应�?
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
        	PackageInfo pak = packlist.get(i);
        	 // if()里的值如�?=0则为自己装的程序，否则为系统工程自带
        	if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
        		// 添加自己已经安装的应用程�?
                String appName = pManager.getApplicationLabel(pak.applicationInfo).toString();
                if(MyApp.gnList.contains(appName)){
                	String[] arr = new String[2];
    				arr[1] = appName;
    				arr[0] = String.valueOf(pak.versionCode);
    				items.add(arr);
                }
        	}
        }
        System.out.println("=============initRequestUpdateList:" + items.size());
        return items;
	}
}
