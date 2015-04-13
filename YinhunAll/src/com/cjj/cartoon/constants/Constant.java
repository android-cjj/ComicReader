package com.cjj.cartoon.constants;

import android.graphics.Color;
import android.os.Build;

import com.cjj.cartoon.R;

public class Constant {
	public static final String SEEKBAR_PAGE = "seekbar_page";
	
	public  static final int[] ITEM_ARC = { R.drawable.lixiangxiazhai, R.drawable.huidaodingbu,
		R.drawable.manhuajianjie};
	
	public static int K = 0;
	public static final int[] COLORS_SELECT = {
		Color.argb(200, 112, 149, 72),
		Color.argb(200, 72, 114, 115),
		Color.argb(200, 287, 137, 38),
		Color.argb(200, 102, 22, 10),
		Color.argb(200, 182, 52,9),
		Color.argb(200, 143,48, 48),
		Color.argb(200, 230, 26, 95),
		Color.argb(200, 51, 70, 141),
		Color.argb(200, 92, 172, 238),
	};
	
	
	public static boolean checkVersionForShimmerEnable() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return false;
		}
		return true;
	}
	/**************************** url ********************************************/
	public static final String HEADVIEW_URL = "http://www.ishuhui.com/";
	
	


	

}
