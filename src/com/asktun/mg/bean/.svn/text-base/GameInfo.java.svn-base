/**   
 * @Title: Download.java
 * @Package com.cloud.coupon.domain
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 陈红建
 * @date 2013-7-3 下午5:52:46
 * @version V1.0
 */
package com.asktun.mg.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
import android.graphics.Bitmap;

import com.asktun.mg.download.DownloadFile;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Download
 * @Description:下载对象的封装 , 包括下载信息,和电影信息,用于下载列表中
 * @date 2013-7-3 下午5:52:46
 * 
 */
@Table(name="downloadtask")//用于FinalDb指定的表名
public class GameInfo implements Serializable
{

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 12L;
	
	@Expose
	private String game_id;
	@Expose
	private String cate_name;
	@Expose
	private String game_ico;
	@Expose
	private String game_name;
	@Expose
	private String score;
	@Expose
	private String size;
	@Expose
	private String user_num;
	@Expose
	private String url;
	@Expose
	private int versionCode;
	@Expose
	private int status;
	
	private String id;// 电影的ID
	private Long progressCount = (long) 0; // 总大小
	private Long currentProgress = (long) 0;// 当前进度
	private Integer downloadState = 0; // 下载状态
	private boolean editState;// 是否是编辑状态
	private Bitmap movieHeadImage;
	private String movieHeadImagePath;//电影图片的路径
	private String fileSize;// 电影大小
	private String movieName;// 电影名称
	private String downloadUrl; // 下载地址
	private String setCount;// 为第几集
	private DownloadFile downloadFile; // 下载控制器
	private String percentage = "%0"; // 下载百分比的字符串
	private Long uuid; // 下载任务的标识
	private String filePath; // 存储路径
	private boolean isSelected; // 选中状态
	private String movieId;
	private int position;//标识了这个任务所在ListView中的位置
//	private boolean existDwonloadQueue;//是否身在下载队列中
	
	
	public Long getProgressCount()
	{
		return progressCount;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getCate_name() {
		return cate_name;
	}

	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}

	public String getGame_ico() {
		return game_ico;
	}

	public void setGame_ico(String game_ico) {
		this.game_ico = game_ico;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUser_num() {
		return user_num;
	}

	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}

	public void setProgressCount(Long progressCount)
	{
		this.progressCount = progressCount;
	}

	public Long getCurrentProgress()
	{
		return currentProgress;
	}

	public void setCurrentProgress(Long currentProgress)
	{
		this.currentProgress = currentProgress;
	}

	public Integer getDownloadState()
	{
		return downloadState;
	}

	public void setDownloadState(Integer downloadState)
	{
		this.downloadState = downloadState;
	}

	public boolean isEditState()
	{
		return editState;
	}

	public void setEditState(boolean editState)
	{
		this.editState = editState;
	}

	public Bitmap getMovieHeadImage()
	{
		return movieHeadImage;
	}

	public void setMovieHeadImage(Bitmap movieHeadImage)
	{
		this.movieHeadImage = movieHeadImage;
	}

	public String getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(String fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getMovieName()
	{
		return movieName;
	}

	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	

	@Override
	public String toString() {
		return "GameInfo [game_id=" + game_id + ", cate_name=" + cate_name
				+ ", game_ico=" + game_ico + ", game_name=" + game_name
				+ ", score=" + score + ", size=" + size + ", user_num="
				+ user_num + ", url=" + url + ", id=" + id + ", progressCount="
				+ progressCount + ", currentProgress=" + currentProgress
				+ ", downloadState=" + downloadState + ", editState="
				+ editState + ", movieHeadImage=" + movieHeadImage
				+ ", movieHeadImagePath=" + movieHeadImagePath + ", fileSize="
				+ fileSize + ", movieName=" + movieName + ", downloadUrl="
				+ downloadUrl + ", setCount=" + setCount + ", downloadFile="
				+ downloadFile + ", percentage=" + percentage + ", uuid="
				+ uuid + ", filePath=" + filePath + ", isSelected="
				+ isSelected + ", movieId=" + movieId + ", position="
				+ position + "]";
	}

	public String getDownloadUrl()
	{
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}

	public DownloadFile getDownloadFile()
	{
		return downloadFile;
	}

	public void setDownloadFile(DownloadFile downloadFile)
	{
		this.downloadFile = downloadFile;
	}

	public String getPercentage()
	{
		return percentage;
	}

	public void setPercentage(String percentage)
	{
		this.percentage = percentage;
	}

	public Long getUuid()
	{
		return uuid;
	}

	public void setUuid(Long uuid)
	{
		this.uuid = uuid;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSetCount()
	{
		return setCount;
	}

	public void setSetCount(String setCount)
	{
		this.setCount = setCount;
	}

	public String getMovieHeadImagePath()
	{
		return movieHeadImagePath;
	}

	public void setMovieHeadImagePath(String movieHeadImagePath)
	{
		this.movieHeadImagePath = movieHeadImagePath;
	}

	public String getMovieId()
	{
		return movieId;
	}

	public void setMovieId(String movieId)
	{
		this.movieId = movieId;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}


//	public boolean isExistDwonloadQueue()
//	{
//		return existDwonloadQueue;
//	}
//
//	public void setExistDwonloadQueue(boolean existDwonloadQueue)
//	{
//		this.existDwonloadQueue = existDwonloadQueue;
//	}

}
