/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.cjj.cartoon;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.cjj.cartoon.custom.HackyViewPager;

/**
 * Lock/Unlock button is added to the ActionBar.
 * Use it to temporarily disable ViewPager navigation in order to correctly interact with ImageView by gestures.
 * Lock/Unlock state of ViewPager is saved and restored on configuration changes.
 * 
 * Julia Zudikova
 */

public class ViewPagerActivity extends Activity {

	private static final String ISLOCKED_ARG = "isLocked";
	
	private ViewPager mViewPager;
	private MenuItem menuLockItem;
	private static int screenWidth;

	private static int screenHeight;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_comic_vertical);
        mViewPager = (HackyViewPager) findViewById(R.id.vp_comic);
//		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter());
		
//		if (savedInstanceState != null) {
//			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
//			((HackyViewPager) mViewPager).setLocked(isLocked);
//		}
//		WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        screenWidth = display.getWidth();
//        screenHeight = display.getHeight();
        
        DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
        
               screenWidth = dm.widthPixels;
      
               screenHeight = dm.heightPixels;
	}

	static class SamplePagerAdapter extends PagerAdapter {

		private static final int[] sDrawables = {R.drawable.ic_launcher,R.drawable.ic_launcher};

		@Override
		public int getCount() {
			return sDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageResource(sDrawables[position]);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
			   mAttacher.setOnViewTapListener(new ViewTapListener());
			return photoView;
		}
		
	    private class PhotoTapListener implements OnPhotoTapListener {

	        @Override
	        public void onPhotoTap(View view, float x, float y) {
	            float xPercentage = x * 100f;
	            float yPercentage = y * 100f;
	            
	            Log.i("cjj", "x="+x);
	        }
	    }
	    
	    private class ViewTapListener implements OnViewTapListener{

			@Override
			public void onViewTap(View view, float x, float y) {
				 Log.i("cjj", "xview="+x);
				 Log.i("cjj", "yview="+y);
				 Log.i("cjj", "xw="+screenWidth);
				 Log.i("cjj", "yh="+screenHeight);
			}
	    	
	    }


		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}


    
   
    
    

 
    
}
