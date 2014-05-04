/**
 * 
 */
package com.asktun.mg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Administrator
 *
 */
public class NetWorkState
{
	public static boolean getNetwork(Context context) {
		//Context context = context.getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		if(activeNetInfo == null) {
			
			return false ;
		}else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			
			return true;
		}else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			
			return true;
		}else {
			return false;
		}
	}

	
}
