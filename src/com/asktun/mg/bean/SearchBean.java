package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class SearchBean implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	private int flg;
	@Expose
	private LinkedList<GameInfo> data;

	@Override
	public String getInterface() {
		return "game/searchGame";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<SearchBean>() {
		}.getType();
	}

	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<GameInfo> getData() {
		return data;
	}

	public void setData(LinkedList<GameInfo> data) {
		this.data = data;
	}
	
}