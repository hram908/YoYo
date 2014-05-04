package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * Èº¿Õ¼ä
 * 
 * @Description
 * @author Dean
 * 
 */
public class GroupZone implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public LinkedList<ZoneInfo> zoneInfo;
	@Expose
	public GroupZoneItem data;

	@Override
	public String getInterface() {
		return "UserGroups/groupZone";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GroupZone>() {
		}.getType();
	}

	public class GroupZoneItem implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String group_id;
		@Expose
		public String user_id;
		@Expose
		public String group_ico;
		@Expose
		public String distance;
	}

	public class ZoneInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2724567532517032398L;
		@Expose
		public String zone_id;
		@Expose
		public String add_time;
		@Expose
		public String info;
	}

}