package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * …Í«Î»Î»∫
 * @Description
 * @author Dean 
 *
 */
public class GroupApply implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;

	@Override
	public String getInterface() {
		return "UserGroups/groupApply";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GroupApply>() {
		}.getType();
	}

}