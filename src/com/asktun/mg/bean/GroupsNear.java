package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GroupsNear implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public int count;
	@Expose
	public LinkedList<GroupsNearItem> data;

	public int obj;

	@Override
	public String getInterface() {
		if (obj == 1) {
			return "UserGroups/myGroup";
		}
		if(obj ==2){
			return "members/groupTypeList";
		}
		return "UserGroups/groupsNear";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GroupsNear>() {
		}.getType();
	}

	public class GroupsNearItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String user_id;
		@Expose
		public String group_id;
		@Expose
		public int is_host;
		@Expose
		public int is_admin;
		@Expose
		public String group_name;
		@Expose
		public String group_ico;
		@Expose
		public String last_active_time;
		/**
		 * 容许最大成员数
		 */
		@Expose
		public String user_num;
		/**
		 * 现有成员数
		 */
		@Expose
		public String mem_ids;
		@Expose
		public String description; // 描述
		@Expose
		public String admin_id; 
		@Expose
		public String longitude; // 经度
		@Expose
		public String latitude; // 纬度
		@Expose
		public String distance; // 距离
		@Expose
		public String is_members; // 距离
		@Expose
		public int is_mem; // 是否成员 1 群主 2 管理员 3 会员

	}

}