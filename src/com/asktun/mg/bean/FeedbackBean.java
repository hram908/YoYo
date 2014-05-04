package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class FeedbackBean implements GsonObj, Serializable{

	@Expose
	private int flg;
	
	@Override
	public String getInterface() {
		return "members/releaseOpinion";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<FeedbackBean>() {
		}.getType();
	}

	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}
	
	
}
