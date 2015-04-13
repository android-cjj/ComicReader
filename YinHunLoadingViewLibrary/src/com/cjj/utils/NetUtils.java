package com.cjj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 网络工具类
 * @author cjj
 *
 */
public class NetUtils {
	
	/**
	 * 检验网络连接 并toast提示
	 * 
	 * @return
	 */
	public static  boolean isNetworkConnected(Context context) 
	{
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) 
		{
			// 当前网络不可用
			Toast.makeText(context.getApplicationContext(), "没有可用网络",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		return true;

	}

	

}