package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * 发送消息
 * 
 * @Description
 * @author Dean
 * 
 */
public class FriendMessage implements GsonObj, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034838654151599403L;

	@Expose
	public int flg;
	@Expose
	public LinkedList<FriendMessageItem> data;
	@Expose
	public MessageUserInfo user_info;
	@Expose
	public MessageUserInfo friend_info;

	public int obj;
	
	@Override
	public String getInterface() {
		if(obj == 1){
			return "UserGroups/groupMessageList";
		}
		return "members/friendMessage";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<FriendMessage>() {
		}.getType();
	}

	// "user_id": "3",
	// "friend_id": "3",
	// "message": "烦烦烦",
	// "add_time": "1386674583",
	// "attach_name": ""
	public class FriendMessageItem implements Serializable {
		@Expose
		public String user_ico;
		@Expose
		public String user_id;
		@Expose
		public String friend_id;
		@Expose
		public String message;
		@Expose
		public String add_time;
		@Expose
		public String voice_name;
		@Expose
		public String attach_name; // 判断是否为文件 不为空时
	}

	public class MessageUserInfo implements Serializable {
		@Expose
		public String user_name;
		@Expose
		public String user_ico;
	}

}