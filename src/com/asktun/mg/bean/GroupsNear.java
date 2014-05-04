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
		 * ��������Ա��
		 */
		@Expose
		public String user_num;
		/**
		 * ���г�Ա��
		 */
		@Expose
		public String mem_ids;
		@Expose
		public String description; // ����
		@Expose
		public String admin_id; 
		@Expose
		public String longitude; // ����
		@Expose
		public String latitude; // γ��
		@Expose
		public String distance; // ����
		@Expose
		public String is_members; // ����
		@Expose
		public int is_mem; // �Ƿ��Ա 1 Ⱥ�� 2 ����Ա 3 ��Ա

	}

}