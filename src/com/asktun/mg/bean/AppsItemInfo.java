package com.asktun.mg.bean;

import android.graphics.drawable.Drawable;

public class AppsItemInfo {
	
	private Drawable icon; // 存放图片
	private String label; // 存放应用程序名
	private String packageName; // 存放应用程序包名
	private String size;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	

}
