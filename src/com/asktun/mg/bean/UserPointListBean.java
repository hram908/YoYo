package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class UserPointListBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private LinkedList<PointListInfo> data;
	
	@Override
	public String getInterface() {
		return "members/getPointList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<UserPointListBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<PointListInfo> getData() {
		return data;
	}

	public void setData(LinkedList<PointListInfo> data) {
		this.data = data;
	}

	public class PointListInfo {
		@Expose
		private String ly;
		@Expose
		private String point;
		@Expose
		private String total;
		public String getLy() {
			return ly;
		}
		public void setLy(String ly) {
			this.ly = ly;
		}
		public String getPoint() {
			return point;
		}
		public void setPoint(String point) {
			this.point = point;
		}
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
		}
		
		

	}
	
}
