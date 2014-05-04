package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class DelLoveGameBean implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	private int flg;

	@Override
	public String getInterface() {
		return "members/deleteLoveGame";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<DelLoveGameBean>() {
		}.getType();
	}

	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}
	
}