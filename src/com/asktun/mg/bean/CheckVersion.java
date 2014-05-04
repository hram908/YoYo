package com.asktun.mg.bean;

import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class CheckVersion implements GsonObj{
		
//	����		˵��						����
//	app_id	appӦ��ID				��
//	version	��ǰ�汾�ţ���ʽ��1.2.0	��
	
	
	@Expose
	private String flg ;  // 0��ʧ�� 1���ɹ� -1��ȱ�ٲ���   2 �Ѿ����۹�
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
		private String version;		// ���°汾��
		@Expose
		private String update_description;	// ����˵��
		@Expose
		private String download_url;		// ���ص�ַ
		
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
