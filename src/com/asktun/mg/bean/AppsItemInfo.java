package com.asktun.mg.bean;

import android.graphics.drawable.Drawable;

public class AppsItemInfo {
	
	private Drawable icon; // ���ͼƬ
	private String label; // ���Ӧ�ó�����
	private String packageName; // ���Ӧ�ó������
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
