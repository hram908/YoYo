package com.asktun.mg.bean;

import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CheckVersion implements GsonObj{
		
//	参数		说明						必须
//	app_id	app应用ID				是
//	version	当前版本号，格式：1.2.0	是
	
	
	@Expose
	private String flg ;  // 0—失败 1—成功 -1—缺少参数   2 已经评价过
	@Expose
	private CheckVersionData data; 
	
	
	@Override
	public String getInterface() {
		return "members/checkVersion";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<CheckVersion>(){}.getType();
	}
	
	

	public CheckVersionData getData() {
		return data;
	}

	public void setData(CheckVersionData data) {
		this.data = data;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	
	public class CheckVersionData {
		@Expose
		private String version;		// 最新版本号
		@Expose
		private String update_description;	// 更新说明
		@Expose
		private String download_url;		// 下载地址
		
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getUpdate_description() {
			return update_description;
		}
		public void setUpdate_description(String update_description) {
			this.update_description = update_description;
		}
		public String getDownload_url() {
			return download_url;
		}
		public void setDownload_url(String download_url) {
			this.download_url = download_url;
		}
		
		
		
	}
	
	
}
