package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CommentOfGameBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private LinkedList<CommentOfGame> data;
	
	@Override
	public String getInterface() {
		return "game/gameAppraise";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<CommentOfGameBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<CommentOfGame> getData() {
		return data;
	}

	public void setData(LinkedList<CommentOfGame> data) {
		this.data = data;
	}



	public class CommentOfGame {
		@Expose
		private String id;
		@Expose
		private String user_name;
		@Expose
		private String user_id;
		@Expose
		private String user_ico;
		@Expose
		private String add_time;
		@Expose
		private String appraise; // content
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getUser_ico() {
			return user_ico;
		}
		public void setUser_ico(String user_ico) {
			this.user_ico = user_ico;
		}
		public String getAdd_time() {
			return add_time;
		}
		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}
		public String getAppraise() {
			return appraise;
		}
		public void setAppraise(String appraise) {
			this.appraise = appraise;
		}
		

	}
}
