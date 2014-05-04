package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CheckphoneBean implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public CheckphoneItem data;

	@Override
	public String getInterface() {
		return "members/checkphone";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<CheckphoneBean>() {
		}.getType();
	}

	public class CheckphoneItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2807163297273135460L;
		@Expose
		public String code;

	}

}