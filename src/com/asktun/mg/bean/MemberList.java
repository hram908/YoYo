package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * 群成员列表
 * 
 * @Description
 * @author Dean
 * 
 */
public class MemberList implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public String user_id;
	@Expose
	public String user_ico;
	@Expose
	public String user_name;
	@Expose
	public String user_age;
	@Expose
	public String admin_id;
	@Expose
	public String mem_ids;
	@Expose
	public int user_gender;
	@Expose
	public LinkedList<MemberListItem> admin_info;
	@Expose
	public LinkedList<MemberListItem> member_info;

	@Override
	public String getInterface() {
		return "UserGroups/memberList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<MemberList>() {
		}.getType();
	}

	public class MemberListItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String user_id;
		@Expose
		public String user_ico;
		@Expose
		public String user_name;
		@Expose
		public String user_age;
		@Expose
		public String distance = "0";
		@Expose
		public String last_active_time;
		@Expose
		public int user_gender;
		@Expose
		public int type; // 1 为群管理员
	}

}