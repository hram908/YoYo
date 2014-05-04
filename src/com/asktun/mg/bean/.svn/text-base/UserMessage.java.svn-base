package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * 用户消息
 * 
 * @Description
 * @author Dean
 * 
 */
public class UserMessage implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public LinkedList<FriendMessageInfo> data;
	@Expose
	public LinkedList<FriendMessageInfo> friendMessageInfo;
	@Expose
	public LinkedList<FriendMessageInfo> groupsApplyInfo;
	@Expose
	public LinkedList<FriendMessageInfo> groupMessageInfo;

	@Override
	public String getInterface() {
		return "members/userMessage";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<UserMessage>() {
		}.getType();
	}

	

}