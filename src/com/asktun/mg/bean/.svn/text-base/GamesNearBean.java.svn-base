package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GamesNearBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private int count;
	@Expose
	private LinkedList<GameInfo> data;
	
	@Override
	public String getInterface() {
		return "game/playGameNear";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GamesNearBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public LinkedList<GameInfo> getData() {
		return data;
	}

	public void setData(LinkedList<GameInfo> data) {
		this.data = data;
	}


}
