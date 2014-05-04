package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * ¸½½üÈË
 * 
 * @Description
 * @author Dean
 * 
 */
public class MembersNear implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public int count;
	@Expose
	public LinkedList<MembersNearItem> data;

	@Override
	public String getInterface() {
		return "members/membersNear";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<MembersNear>() {
		}.getType();
	}

	public class MembersNearItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String user_id;
		@Expose
		public String user_name;
		@Expose
		public int user_gender;
		@Expose
		public String user_age;
		@Expose
		public String user_ico;
		@Expose
		public String longitude;
		@Expose
		public String latitude;
		@Expose
		public String signature;
		@Expose
		public String distance;
		@Expose
		public String last_active_time;
		@Expose
		public String purpose = "";
		@Expose
		public LinkedList<GameInfo> game_info;

		public class GameInfo implements Serializable {
			@Expose
			public String game_id;
			@Expose
			public String game_name;
			@Expose
			public String game_ico;
		}

	}

}