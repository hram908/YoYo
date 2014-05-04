package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CategoryListBean implements GsonObj, Serializable{

	@Expose
	private int flg;
	@Expose
	private int count;
	@Expose
	private LinkedList<GameInfo> data;
	
	@Override
	public String getInterface() {
		return "game/categoryList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<CategoryListBean>() {
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
