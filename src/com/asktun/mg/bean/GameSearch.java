package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GameSearch implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;
	@Expose
	public LinkedList<GameSearchItem> data;

	@Override
	public String getInterface() {
		return "userGroups/gameSearch";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GameSearch>() {
		}.getType();
	}

	public class GameSearchItem {
		@Expose
		public String game_name;
	}

}