package com.asktun.mg.bean;

public class MessageItem {

	public String name;// 消息来自
	public String time;// 消息日期
	public String message;// 消息内容
	public byte[] audioBytes;
	public String audioPath; //本地保存地址
	public String audioUrl;  //網絡下載地址
	public boolean isComMeg = true;// 是否为收到的消息
	public int sentType = 0; // 0 发送中 1 成功 -1 失败
	public boolean unread = true;
	public String image;
	public String userId; //用户Id

	public enum EntityType {
		ET_TEXT, ET_AUDIO, ET_IMAGE
	}

	public EntityType entityType;

}
