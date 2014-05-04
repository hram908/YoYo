package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.asktun.mg.bean.UserInfoBean.LifePhotoResult;
import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * 群主信息
 * 
 * @Description
 * @author Dean
 * 
 */
public class HostInfo implements GsonObj, Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public String isfriend; // 好友关系：null陌生人；0，发出了好友申请；1，好友
	@Expose
	public HostInfoItem hostinfo;
	@Expose
	public LinkedList<GameItem> data;
	@Expose
	public LinkedList<LifePhotoResult> userLifePhotos;
	@Expose
	public LinkedList<LifePhotoResult> userGamePhotos;

	@Override
	public String getInterface() {
		return "UserGroups/host";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<HostInfo>() {
		}.getType();
	}

	// {"flg":1,
	// "isfriend":"",
	// "hostinfo":{"user_id":80001,"user_name":"tangtao","user_age":"26",
	// "user_gender":"1","user_ico":"http:\/\/192.168.1.116\/
	// public\/upload\/user_ico\/1\/20131128\/300_300_201311281509295296ec297fcd8.jpg",
	// "user_address":"\u6b66\u6c49\u5e02\u5149\u8c37\u521b\u4e1a\u8857",
	// "signature":"\u8fd9\u4e2a\u5bb6\u4f19
	// \u5f88\u806a\u660e\u4ec0\u4e48\u4e5f\u6ca1\u7559\u4e0b",
	// "job":"\u4e92\u8054\u7f51","school":"\u534e\u4e2d\u79d1\u6280\u5927\u5b66",
	// "interest":"\u6e38\u6cf3"}}

	public class HostInfoItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String user_id;
		@Expose
		public String user_name;
		@Expose
		public String user_age;
		@Expose
		public String user_gender;
		@Expose
		public String user_ico;
		@Expose
		public String user_address;
		@Expose
		public String signature; // 签名
		@Expose
		public String job;
		@Expose
		public String interest;
		@Expose
		public String distance;
		@Expose
		public String purpose;
		@Expose
		public String user_constellate;
		@Expose
		public String last_active_time;

	}

	public class GameItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Expose
		public String gt_id;
		@Expose
		public String game_id;
		@Expose
		public String game_name;
		@Expose
		public String game_ico;

	}
}