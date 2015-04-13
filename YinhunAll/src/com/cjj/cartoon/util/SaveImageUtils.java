package com.cjj.cartoon.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * save picture
 * 
 * @author cjj
 * 
 */
public class SaveImageUtils {
	
	private static String fileName ;
	public static Bitmap getBitmapScreen(Activity activity)
	{
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		int w = display.getWidth();  
		int h = display.getHeight();  
		Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );      
		//获取屏幕  
		View decorview = activity.getWindow().getDecorView();   
		decorview.setDrawingCacheEnabled(true);   
		Bmp = decorview.getDrawingCache();  
		return Bmp;
	}
	
	@SuppressLint("NewApi") 
	public static File getDiskCacheDir(Context context, String uniqueName) {  
	    String cachePath;  
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	            || !Environment.isExternalStorageRemovable()) {  
	        cachePath = context.getExternalCacheDir().getPath();  
	    } else {  
	        cachePath = context.getCacheDir().getPath();  
	    }  
	    return new File(cachePath + File.separator + uniqueName);  
	}  

	public static File saveImageToFile(Bitmap bmp,Context context) throws Exception{
		File appDir = getDiskCacheDir(context, "Gintama-image");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		 fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		return file;
	}

	public static void saveImageToGallery(Activity activity,Context context) throws Exception{
		// 首先保存图片
		
		File file = saveImageToFile(getBitmapScreen(activity), context);

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + file.toString())));
	}
}
