package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.asktun.mg.bean.HostInfo.GameItem;
import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class UserInfoBean implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private int flg;
	@Expose
	private UserInfo data;
	@Expose
	public LinkedList<GameItem> recentPlay;
	@Expose
	public LinkedList<GameItem> loveGame ;
	@Expose
	public LinkedList<LifePhotoResult> lifePhotoResult;
	@Expose
	public LinkedList<LifePhotoResult> gamePhotoResult;

	@Override
	public String getInterface() {
		return "members/getUsersInfo";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<UserInfoBean>() {
		}.getType();
	}

	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}

	public class RecentPlay implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Expose
		public String game_id;
		@Expose
		public String game_ico;
		@Expose
		public String game_name;

	}

	public class LifePhotoResult implements Serializable {
		@Expose
		public String photo_id;
		@Expose
		public String photo;

	}

	public class UserInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Expose
		private String user_id;
		@Expose
		private String user_name;
		@Expose
		private String user_ico;
		@Expose
		private String user_gender;
		@Expose
		private String user_age;
		@Expose
		private String is_vip;
		@Expose
		private String user_address;
		@Expose
		private String signature;
		@Expose
		private String job;
		@Expose
		private String school;
		@Expose
		private String interest;
		@Expose
		private String user_num;
		@Expose
		private String purpose;
		@Expose
		private String user_constellate = "";
		@Expose
		private String last_active_time;
		@Expose
		private String distance = "";
		@Expose
		private String user_birth;
		@Expose
		private String is_friend = "";
		@Expose
		private String company;

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getLast_active_time() {
			return last_active_time;
		}

		public void setLast_active_time(String last_active_time) {
			this.last_active_time = last_active_time;
		}

		public String getIs_friend() {
			return is_friend;
		}

		public void setIs_friend(String is_friend) {
			this.is_friend = is_friend;
		}

		public String getUser_num() {
			return user_num;
		}

		public void setUser_num(String user_num) {
			this.user_num = user_num;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public String getUser_age() {
			return user_age;
		}

		public void setUser_age(String user_age) {
			this.user_age = user_age;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUser_ico() {
			return user_ico;
		}

		public void setUser_ico(String user_ico) {
			this.user_ico = user_ico;
		}

		public String getUser_gender() {
			return user_gender;
		}

		public void setUser_gender(String user_gender) {
			this.user_gender = user_gender;
		}

		public String getIs_vip() {
			return is_vip;
		}

		public void setIs_vip(String is_vip) {
			this.is_vip = is_vip;
		}

		public String getUser_address() {
			return user_address;
		}

		public void setUser_address(String user_address) {
			this.user_address = user_address;
		}

		public String getJob() {
			return job;
		}

		public void setJob(String job) {
			this.job = job;
		}

		public String getSchool() {
			return school;
		}

		public void setSchool(String school) {
			this.school = school;
		}

		public String getInterest() {
			return interest;
		}

		public void setInterest(String interest) {
			this.interest = interest;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getUser_constellate() {
			return user_constellate;
		}

		public void setUser_constellate(String user_constellate) {
			this.user_constellate = user_constellate;
		}

		public String getUser_birth() {
			return user_birth;
		}

		public void setUser_birth(String user_birth) {
			this.user_birth = user_birth;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getPurpose() {
			return purpose;
		}

		public void setPurpose(String purpose) {
			this.purpose = purpose;
		}

	}
}
