package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GameSortBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private LinkedList<SortInfo> data;
	
	@Override
	public String getInterface() {
		return "game/gameCategory";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GameSortBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<SortInfo> getData() {
		return data;
	}

	public void setData(LinkedList<SortInfo> data) {
		this.data = data;
	}


	public class SortInfo {
		@Expose
		private String cate_id;
		@Expose
		private String cate_name;
		@Expose
		private String category_img;
		
		public String getCate_id() {
			return cate_id;
		}
		public void setCate_id(String cate_id) {
			this.cate_id = cate_id;
		}
		public String getCate_name() {
			return cate_name;
		}
		public void setCate_name(String cate_name) {
			this.cate_name = cate_name;
		}
		public String getCategory_img() {
			return category_img;
		}
		public void setCategory_img(String category_img) {
			this.category_img = category_img;
		}

	}
}
