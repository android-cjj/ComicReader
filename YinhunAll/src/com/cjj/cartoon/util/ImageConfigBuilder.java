package com.cjj.cartoon.util;

import android.graphics.Bitmap;

import com.cjj.cartoon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class ImageConfigBuilder {

	private static final int EMPTY_PHOTO = R.drawable.pic_bg;
	private static final int EMPTY_PHOTO_WIDTH = R.drawable.empty_photo_by_width;

	/**高清头像模式 - 不使用动画效果*/
	public static final DisplayImageOptions USER_HEAD_HD_OPTIONS = new DisplayImageOptions.Builder()
	.showImageOnLoading(EMPTY_PHOTO)
	.showImageForEmptyUri(EMPTY_PHOTO)
	.showImageOnFail(EMPTY_PHOTO)
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.bitmapConfig(Bitmap.Config.ARGB_8888)
	.build();
	
	
	/**高清头像模式 - 不使用动画效果*/
	public static final DisplayImageOptions NORMAL_IMAGE = new DisplayImageOptions.Builder()
	.showImageOnLoading(EMPTY_PHOTO_WIDTH)
	.showImageForEmptyUri(EMPTY_PHOTO_WIDTH)
	.showImageOnFail(EMPTY_PHOTO_WIDTH)
	.showStubImage(EMPTY_PHOTO_WIDTH)
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new RoundedBitmapDisplayer(10))
	.bitmapConfig(Bitmap.Config.ARGB_8888)
	.build();
	
	
	/**不显示默认图片模式*/
	public static final DisplayImageOptions TRANSPARENT_IMAGE = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.bitmapConfig(Bitmap.Config.RGB_565) 
	.build();
}
