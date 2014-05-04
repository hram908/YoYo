package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.asktun.mg.bean.MembersNear.MembersNearItem.GameInfo;
import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class FriendsList implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public LinkedList<FriendsListItem> data;

	@Override
	public String getInterface() {
		return "friends/myFriendsList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<FriendsList>() {
		}.getType();
	}

	public class FriendsListItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String friend_id;
		@Expose
		public String user_ico;
		@Expose
		public String user_name;
		@Expose
		public String user_age;
		@Expose
		public String distance;
		@Expose
		public String last_active_time;
		@Expose
		public int user_gender;
		@Expose
		public String signature; // ¸öÐÔÇ©Ãû
		@Expose
		public String purpose = "";
		@Expose
		public LinkedList<GameInfo> game_info;
	}

}