package com.cjj.cartoon.model;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class MousePaitedApplication extends Application {
	/** Multiple custom typefaces support */
	private TypefaceCollection mRobotoTypeface;
	public static MousePaitedApplication instance;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		initImageLoader(getApplicationContext());
		initFont();
	}

	private void initFont() {
		// Multiple custom typefaces support
		mRobotoTypeface = new TypefaceCollection.Builder()
				.set(Typeface.NORMAL,
						Typeface.createFromAsset(getAssets(),
								"fonts/Roboto-Thin.ttf"))
				.set(Typeface.BOLD,
						Typeface.createFromAsset(getAssets(),
								"fonts/Roboto-Bold.ttf")).create();
		
		TypefaceHelper.init(mRobotoTypeface);
		
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	/** Multiple custom typefaces support */
	public TypefaceCollection getRobotoTypeface() {
		return mRobotoTypeface;
	}

}
