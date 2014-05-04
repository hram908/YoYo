package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * Ï²°®µÄÓÎÏ·
 * 
 * @Description
 * @author Dean
 * 
 */
public class MemberLikes implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public LinkedList<MemberLikesItem> data;

	@Override
	public String getInterface() {
		return "members/memberLikes";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<MemberLikes>() {
		}.getType();
	}

	public class MemberLikesItem implements Serializable {
		@Expose
		public String gt_id;
		@Expose
		public String game_name;
		@Expose
		public String game_ico;
		@Expose
		public String user_num
;
	}

}