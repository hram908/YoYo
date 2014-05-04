package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * ×ªÈÃÈº×é
 * @Description
 * @author Dean 
 *
 */
public class TransferGroup implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;
	@Expose
	public int flg;

	@Override
	public String getInterface() {
		return "UserGroups/transferGroup";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<TransferGroup>() {
		}.getType();
	}
}