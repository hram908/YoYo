package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * ·¢ËÍÏûÏ¢
 * 
 * @Description
 * @author Dean
 * 
 */
public class SendMessage implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;

	public int obj;

	@Override
	public String getInterface() {
		if (obj == 1) {
			return "UserGroups/sendGroupMessage";
		}
		return "members/sendMessage";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<SendMessage>() {
		}.getType();
	}
}