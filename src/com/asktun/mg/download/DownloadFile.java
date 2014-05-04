/**   
* @Title: DownloadFile.java
* @Package com.cloud.coupon.utils
* @Description: TODO(用一句话描述该文件做�?��)
* @date 2013-6-26 下午5:31:23
* @version V1.0
*/ 
package com.asktun.mg.download;

import java.io.File;
import java.io.Serializable;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

/** 
 * @ClassName: DownloadFile
 * @Description: 下载文件
 * @date 2013-6-26 下午5:31:23
 * 
 */
public class DownloadFile implements Serializable
{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示�?��)
	*/ 
	private static final long serialVersionUID = 1L;
	private boolean isStop;
	private HttpHandler<File> mHttpHandler;
	
	
	/**
	 * @Title: downloadFileByUrl
	 * @Description: 通过URL地址下载�?��文件 将文件存储到 toPath�?
	 * @param url 下载地址
	 * @param toPath 存储的路�?
	 * @param downCallBack 下载过程的回�?下载进度,与成功之后的回调
	 * @return void
	 */
	public  DownloadFile startDownloadFileByUrl(String url , String toPath , AjaxCallBack<File> downCallBack) { 
		
		
		if(downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		}else {
			FinalHttp down = new FinalHttp();
			//支持断点续传
			setmHttpHandler(down.download(url, toPath,true, downCallBack));
//			mHttpHandler.
		}
		return this;
	}
	public  DownloadFile startDownloadFileByUrlNoCatch(String url , String toPath , AjaxCallBack<File> downCallBack) {
		
		
		if(downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		}else {
			FinalHttp down = new FinalHttp();
			//支持断点续传
			setmHttpHandler(down.download(url, toPath,true, downCallBack));
//			mHttpHandler.
		}
		return this;
	}
	public void stopDownload() {
		if(getmHttpHandler() != null) {
			getmHttpHandler().stop();
			getmHttpHandler().cancel(true);
			if(!getmHttpHandler().isStop()) {
				getmHttpHandler().stop();
				getmHttpHandler().cancel(true);
			}
		}
	}
	public boolean isStop()
	{
		isStop = getmHttpHandler().isStop();
		return isStop;
	}
	public void setStop(boolean isStop)
	{
		this.isStop = isStop;
	}
	public HttpHandler<File> getmHttpHandler()
	{
		return mHttpHandler;
	}
	public void setmHttpHandler(HttpHandler<File> mHttpHandler)
	{
		this.mHttpHandler = mHttpHandler;
	}
}

