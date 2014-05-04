package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class LoginBean implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public String token;
	@Expose
	public String user_id;
	@Expose
	public String user_name;
	@Expose
	public String is_vip;
	@Expose
	public String now_time;

	@Override
	public String getInterface() {
		return "members/login";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<LoginBean>() {
		}.getType();
	}

}