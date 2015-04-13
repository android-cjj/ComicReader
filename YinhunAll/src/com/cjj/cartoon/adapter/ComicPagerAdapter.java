package com.cjj.cartoon.adapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.cartoon.ComicImageViewActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.fragment.ComicImageFragment;
import com.cjj.cartoon.util.ImageConfigBuilder;
import com.cjj.volley.utils.LogUtil;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ComicPagerAdapter extends PagerAdapter {
	private Context context;
	private List<String> list;
	private boolean isShow = false;
	private int w, h;
	private ViewPager vp;
	private int index_now = 0;
	private ListView lv;
	private boolean isListView = false;
	TextView tv_number;
	private TextView tv_numbers;

	public ComicPagerAdapter(Context context, List<String> list, int w, int h,
			ViewPager vp,Handler handler) {
		this.list = list;
		this.context = context;
		this.w = w;
		this.h = h;
		this.vp = vp;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (View) arg1;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		String url = list.get(position);
		View v = LayoutInflater.from(context).inflate(
				R.layout.comic_image_view, null);
		PhotoView photoView = (PhotoView) v.findViewById(R.id.iv_photo);
		final View ll_view = v.findViewById(R.id.ll_loading);
		
		final ArcProgress arcProgress = (ArcProgress) v.findViewById(R.id.arc_progress);
		TextView tv_page = (TextView) v.findViewById(R.id.tv_page);
		tv_page.setText(String.valueOf(position + 1));
		index_now = position;
		ImageLoader.getInstance().displayImage(url, photoView,
				ImageConfigBuilder.TRANSPARENT_IMAGE,
				new SimpleImageLoadingListener()
				{
					/**start*/
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						super.onLoadingStarted(imageUri, view);
						ll_view.setVisibility(View.VISIBLE);
						arcProgress.setProgress(0);
					}
					
					/**failed*/
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						// 加载失败的时候执行
						LogUtil.LOgMsg_W("shibai");
						ll_view.setVisibility(View.GONE);
					}
					
					/**complete*/
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						// 加载成功的时候执行
						ll_view.setVisibility(View.GONE);
					}
					
					/**cancel*/
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
					}
					
				},new ImageLoadingProgressListener()
				{

					@Override
					public void onProgressUpdate(String imageUri,
							View view, int current, int total) {
						
						  arcProgress.setProgress((Math.round(100.0f* current / total)));
					}
					
				});

		container.addView(v);
		photoView.mAttacher.setOnViewTapListener(new ViewTapListener());
		return v;
	}
	
	public interface PersentListener {
		public void onPersent(int cur, int total);
	}

	private PersentListener persentListener;

	public void setPersentListener(PersentListener persentListener) {
		this.persentListener = persentListener;
	}

	public void setTextPersent(int cur, int total) {
		tv_number.setText(cur / total * 100 + "%");
		notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	private class ViewTapListener implements OnViewTapListener {

		@Override
		public void onViewTap(View view, float x, float y) {
			index_now = vp.getCurrentItem();
			if (x < (w / 3)) {
				showPrePage();

			} else if (x > (w / 3 * 2)) {
				showNextPage();
			} else {
				if (y < h / 6) {
					showNextPage();
				} else if (y > h / 6 * 5) {
					showNextPage();
				} else {
					showOrHideMenu();
				}
			}
		}

	}

	public void showOrHideMenu() {
		if (isShow) {
			ComicImageViewActivity.comicImageViewActivity.hideMenuView();
			ComicImageFragment.instance.showBatteryTip();
			isShow = false;
		} else {
			ComicImageViewActivity.comicImageViewActivity.showMenuView();
			ComicImageFragment.instance.hideBatteryTip();
			isShow = true;
		}
	}

	public void hideMenu() {
		if (isShow) {
			ComicImageViewActivity.comicImageViewActivity.hideMenuView();
			ComicImageFragment.instance.showBatteryTip();
			isShow = false;
		}
	}

	public void toastMsg(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void showPrePage() {
		if (index_now == 0) {
			toastMsg("当前是第一页");
		} else {
			vp.setCurrentItem(index_now - 1);
		}
	}

	public void showNextPage() {
		if (index_now == list.size() - 1) {
			toastMsg("当前是最后一页");
		} else {
			vp.setCurrentItem(index_now + 1);
		}
	}
}
