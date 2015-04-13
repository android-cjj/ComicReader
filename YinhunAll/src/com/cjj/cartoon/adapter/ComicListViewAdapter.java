package com.cjj.cartoon.adapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.cjj.cartoon.ComicImageViewActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.fragment.ComicImageFragment;
import com.cjj.cartoon.util.ImageConfigBuilder;
import com.cjj.volley.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class ComicListViewAdapter extends BaseAdapter{
	private Context context;
	private List<String> list;
	private int w;
	private ListView lv;
	private int index ;
	
	public ComicListViewAdapter(Context context,List<String> list,ListView lv,int w){
		this.context = context;
		this.list = list;
		this.lv = lv;
		this.w = w;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// menu type count
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// current menu type
		return position % 3;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.comic_image_view_listview, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		String item = getItem(position);
		
	    ImageLoader.getInstance().displayImage(item, holder.iv_photo,ImageConfigBuilder.USER_HEAD_HD_OPTIONS,new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				holder.ll_view.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				//加载失败的时候执行  
				holder.ll_view.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				//加载成功的时候执行  
				holder.ll_view.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		},new ImageLoadingProgressListener() {
			
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,int total) {
				LogUtil.LOgMsg_W("curr="+current);
				LogUtil.LogMsg_I("total="+total);
			}
		});
	    holder.iv_photo.mAttacher.setOnViewTapListener(new ViewTapListener());
	    index = position;
		return convertView;
	}
	
	 private class ViewTapListener implements OnViewTapListener{

			@Override
			public void onViewTap(View view, float x, float y) {
					 if(x<(w/3))
					 {
						 if(index!=0)
						 {
							 lv.setSelection(index-1);
						 }else
						 {
							 Toast.makeText(context, "当前是第一页", 1).show();
						 }
					 }else if(x>(w/3*2))
					 {
						 if(index!=getCount())
						 {
							 lv.setSelection(index+1);
						 }else
						 {
							 Toast.makeText(context, "当前是最后一页", 1).show();
						 } 
					 }else{
						
							 showOrHideMenu();	
					 }
				}
				 
	    }
	 
	 private boolean isShow = false;
	 public void showOrHideMenu()
		{
			 if(isShow)
			 {
				 ComicImageViewActivity.comicImageViewActivity.hideMenuView();
				 ComicImageFragment.instance.showBatteryTip();
				 isShow = false;
			 }else
			 {
				 ComicImageViewActivity.comicImageViewActivity.showMenuView();
				 ComicImageFragment.instance.hideBatteryTip();
				 isShow = true;
			 }
		}

	class ViewHolder {
		PhotoView iv_photo;
		LinearLayout ll_view;
		public ViewHolder(View view) {
			iv_photo = (PhotoView) view.findViewById(R.id.iv_photo);
			ll_view = (LinearLayout) view.findViewById(R.id.ll_loading);
			iv_photo.setScaleType(ScaleType.FIT_XY);
			view.setTag(this);
		}
	}
}
