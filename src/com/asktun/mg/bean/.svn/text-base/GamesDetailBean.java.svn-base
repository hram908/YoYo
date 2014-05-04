package com.asktun.mg.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GamesDetailBean implements GsonObj, Serializable{
	@Expose
	private int flg;
	@Expose
	private GameDetail data;
	@Expose
	private LinkedList<FriendData> friendData;
	@Expose
	private LinkedList<OtherData> otherData;
	
	@Override
	public String getInterface() {
		return "game/gameInfo";
	}

	@Override
	public Type getTypeToken() {
		return new TypeToken<GamesDetailBean>() {
		}.getType();
	}
	
	public int getFlg() {
		return flg;
	}

	public void setFlg(int flg) {
		this.flg = flg;
	}

	
	public GameDetail getData() {
		return data;
	}

	public void setData(GameDetail data) {
		this.data = data;
	}

	public LinkedList<FriendData> getFriendData() {
		return friendData;
	}

	public void setFriendData(LinkedList<FriendData> friendData) {
		this.friendData = friendData;
	}

	public LinkedList<OtherData> getOtherData() {
		return otherData;
	}

	public void setOtherData(LinkedList<OtherData> otherData) {
		this.otherData = otherData;
	}



	public class GameDetail {
		@Expose
		private String game_ico;
		@Expose
		private String isonline;
		@Expose
		private String img_dir;
		@Expose
		private String description;
		@Expose
		private String score;
		@Expose
		private String size;
		@Expose
		private String add_time;
		@Expose
		private String down_times;
		@Expose
		private String point;
		@Expose
		private String game_name;
		@Expose
		private String url;
		
		
		public String getScore() {
			return score;
		}
		public String getGame_ico() {
			return game_ico;
		}
		public void setGame_ico(String game_ico) {
			this.game_ico = game_ico;
		}
		public String getIsonline() {
			return isonline;
		}
		public void setIsonline(String isonline) {
			this.isonline = isonline;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getImg_dir() {
			return img_dir;
		}
		public void setImg_dir(String img_dir) {
			this.img_dir = img_dir;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getAdd_time() {
			return add_time;
		}
		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}
		public String getDown_times() {
			return down_times;
		}
		public void setDown_times(String down_times) {
			this.down_times = down_times;
		}
		public String getPoint() {
			return point;
		}
		public void setPoint(String point) {
			this.point = point;
		}
		public String getGame_name() {
			return game_name;
		}
		public void setGame_name(String game_name) {
			this.game_name = game_name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		
	}
	
	public class FriendData {
		@Expose
		private String user_name;
		@Expose
		private String user_ico;
		@Expose
		private String user_id;
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_ico() {
			return user_ico;
		}
		public void setUser_ico(String user_ico) {
			this.user_ico = user_ico;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		
		
		
	}

	public class OtherData {
		@Expose
		private String game_ico;
		
		private String point;
		@Expose
		private String game_name;
		@Expose
		private String game_id;
		public String getPoint() {
			return point;
		}
		public void setPoint(String point) {
			this.point = point;
		}
		public String getGame_name() {
			return game_name;
		}
		public void setGame_name(String game_name) {
			this.game_name = game_name;
		}
		public String getGame_id() {
			return game_id;
		}
		public void setGame_id(String game_id) {
			this.game_id = game_id;
		}
		public String getGame_ico() {
			return game_ico;
		}
		public void setGame_ico(String game_ico) {
			this.game_ico = game_ico;
		}
		
		
		
	}
}
