package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class PhotoBean implements GsonObj, Serializable{

	@Expose
	private int flg;
	@Expose
	private LinkedList<PhotoInfo> data;
	@Expose
	private LinkedList<PhotoInfo> userGamePhotos;
	
	@Override
	public String getInterface() {
		return "members/memberPhotoList";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<PhotoBean>() {
		}.getType();
	}
	
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	public LinkedList<PhotoInfo> getData() {
		return data;
	}

	public void setData(LinkedList<PhotoInfo> data) {
		this.data = data;
	}

	public LinkedList<PhotoInfo> getUserGamePhotos() {
		return userGamePhotos;
	}

	public void setUserGamePhotos(LinkedList<PhotoInfo> userGamePhotos) {
		this.userGamePhotos = userGamePhotos;
	}



	public class PhotoInfo implements Serializable{
		@Expose
		private String photo_id;
		@Expose
		private String photo;
		public String getPhoto_id() {
			return photo_id;
		}
		public void setPhoto_id(String photo_id) {
			this.photo_id = photo_id;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		
		
	}

}
