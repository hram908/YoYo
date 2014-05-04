package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.asktun.mg.bean.UserInfoBean.LifePhotoResult;
import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GroupInfo implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public GroupInfoItem data;
	@Expose
	public LinkedList<UserItem> userIco;
	@Expose
	public LinkedList<LifePhotoResult> groupLifePhotos;

	@Override
	public String getInterface() {
		return "UserGroups/groupInfo";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GroupInfo>() {
		}.getType();
	}

	// {"flg":1,"data":
	// {"group_name":"\u6211\u53ebmt\u53d1\u70e7\u7fa4","img_dir":"","group_id":"1",
	// "host_id":"2",
	// "description":"\u73a9mt\u7684\u8fc7\u6765\u6211\u4eec\u7fa4",
	// "zone_id":"1",
	// "info":"80\u540e\u7684\u5e74\u4ee3\u600e\u80fd\u4f1a\u4e0d\u6f47\u6d12\uff0c\u518d\u4e0d\u75af\u72c2\u6211\u4eec\u5c31\u8001\u4e86",
	// "mem_ids":"2,3,4,5"},"users_ico":[{"user_id":"2","user_ico":"http:\/\/192.168.1.116\/public\/upload\/20131128\/300_300_201311281509295296ec297fcd8.jpg"},{"user_id":"3","user_ico":"http:\/\/192.168.1.116\/public\/upload\/20131128\/300_300_201311281509295296ec297fcd8.jpg"},{"user_id":"4","user_ico":"http:\/\/192.168.1.116\/public\/upload\/20131128\/300_300_201311281509295296ec297fcd8.jpg"},{"user_id":"5","user_ico":"http:\/\/192.168.1.116\/public\/upload\/20131128\/300_300_201311281509295296ec297fcd8.jpg"}]}

	public class GroupInfoItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String group_name;
		@Expose
		public String group_id;
		@Expose
		public String host_id;
		@Expose
		public String group_ico;
		@Expose
		public String zone_id;
		/**
		 * 群空间信息
		 */
		@Expose
		public String info;
		@Expose
		public String user_name;
		@Expose
		public String user_ico;
		@Expose
		public String description; // 描述
		@Expose
		public String mem_ids;
		@Expose
		public String distance = "";

	}

	public class UserItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Expose
		public String user_id;
		@Expose
		public String user_ico;
		@Expose
		public String user_name;

	}
}