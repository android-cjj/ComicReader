package com.cjj.volley.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
/**
 * 检测输出类
 */
public class LogUtil {
	/**输出标志*/
	public static final String TAG ="cjj";
	
	/**输出info的信息*/
	public static void LogMsg_I(String str)
	{
		Log.i(TAG, str);
	}
	
	/**输出警告的信息*/
	public static void LOgMsg_W(String str)
	{
		Log.w(TAG, str);
	}
	
	/**输出错误信息*/
	public static void LogMsg_E(String str){
		Log.e(TAG, str);
	}
	
	/**弹出info的信息*/
	public void toastMsg_I(Context context,String str)
	{
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	/**弹出警告的信息*/
	public void toastMsg_W(Context context,String str)
	{
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	/**弹出错误的信息*/
	public void toastMsg_E(Context context,String str)
	{
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
}
