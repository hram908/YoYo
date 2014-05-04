package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CommentListBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private LinkedList<CommentInfo> data;
	
	@Override
	public String getInterface() {
		return "members/appraiseList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<CommentListBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<CommentInfo> getData() {
		return data;
	}

	public void setData(LinkedList<CommentInfo> data) {
		this.data = data;
	}


	public class CommentInfo {
		@Expose
		private String id;
		@Expose
		private String game_name;
		@Expose
		private String game_ico;
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
		public String getGame_name() {
			return game_name;
		}
		public void setGame_name(String game_name) {
			this.game_name = game_name;
		}
		public String getGame_ico() {
			return game_ico;
		}
		public void setGame_ico(String game_ico) {
			this.game_ico = game_ico;
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
