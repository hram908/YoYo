package com.asktun.mg.service;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class FileService {

	private static FileServiceInterface mFileServerBySDCard;
	private static FileServiceInterface mFileServerByMemory;

	/**
	 * 如果要将图片存在sd card，则在调用getBitmap() 之前，必须调用本方法�?	 */
	public static void loadFileToMap() {
		if (DJMarketUtils.isSdcardExists()) {
			mFileServerBySDCard = new FileServiceBySDCard();// 存在SD card
			mFileServerBySDCard.loadFileToMap();
		}
		mFileServerByMemory = new FileServiceByMemoary();
	}

	/**
	 * 通过URL获取图片，图片可能保存在 memory或�?sd card上，
	 * 注意:调用本方法之前，�?��要先调用FileService.loadFileToMap();
	 * 
	 * @param url
	 *            图片的URL[类型:String]
	 * @param imageView
	 *            �?��set图片的ImageView[类型:ImageView]
	 * @param defaultBitmap
	 *            如果不能通过网络或�?本地cache获取图片，则使用这张Bitmap作为ImageView的背景[类型:Bitmap]
	 * @param scrollState
	 *            判断ListView的滚动状态，默认是不滚动。默认�?可以这样获取PicturesHandlerUtils.
	 *            SCROLL_STATUS_STOP[类型:Integer]
	 */
	public static void getBitmap(String url, ImageView imageView, Bitmap defaultBitmap, Integer scrollState) {
		if (DJMarketUtils.isSdcardExists() && mFileServerBySDCard != null) {
			mFileServerBySDCard.getBitmap(url, imageView, defaultBitmap, scrollState);
		} else {
			if (mFileServerByMemory != null)
				mFileServerByMemory.getBitmap(url, imageView, defaultBitmap, scrollState);
		}
	}

	/**
	 * 通过URL获取图片，图片可能保存在 memory或�?sd card上，
	 * 注意:调用本方法之前，�?��要先调用FileService.loadFileToMap();
	 * 
	 * @param url
	 *            图片的URL[类型:String]
	 * @param imageView
	 *            �?��set图片的ImageView[类型:ImageView]
	 * @param defaultBitmap
	 *            如果不能通过网络或�?本地cache获取图片，则使用这张Bitmap作为ImageView的背景[类型:Bitmap]
	 * @param isRemote
	 *            判断是否允许获取网络图片
	 * 
	 */
	public static void getBitmap(String url, ImageView imageView, Bitmap defaultBitmap, boolean isRemote) {
		if (DJMarketUtils.isSdcardExists() && mFileServerBySDCard != null) {
			mFileServerBySDCard.getBitmap(url, imageView, defaultBitmap, isRemote);
		} else {
			if (mFileServerByMemory != null)
				mFileServerByMemory.getBitmap(url, imageView, defaultBitmap, isRemote);
		}
	}

	/**
	 * 获取缓存在cache里面的图片�?�?	 * 
	 * @return
	 */
	public static int getCurrentCacheBitmapNumbers() {
		return mFileServerBySDCard.getCurrentCacheBitmapNumbers();
	}

	/**
	 * 清除掉目前缓存中的所有图片，返回true表示清除成功，false表示清除失败
	 */
	public static boolean clearCacheBitmaps() {
		return mFileServerBySDCard.clearCacheBitmaps();
	}
}