package com.cjj.cartoon.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import com.cjj.cartoon.MainActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.model.LocalImageModel;
import com.cjj.staggeredgridview.STGVImageView;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class HomeComicListAdapter extends SimpleBaseAdapter<LocalImageModel>{
	private Shimmer  shimmer;
	private STGVImageView iv_home_comic;
	private ShimmerTextView tv_title;
	public HomeComicListAdapter(Context context, ArrayList<LocalImageModel> list) {
		super(context, list);
	}

	@Override
	public int getItemResourceId() {
		return R.layout.item_comic_list;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.cjj.cartoon.adapter.SimpleBaseAdapter.ViewHolder holder) {
		TypefaceHelper.typeface(convertView);
		iv_home_comic = (STGVImageView) holder.getView(R.id.iv_home_comic);
		 tv_title = (ShimmerTextView) holder.getView(R.id.tv_title_home);
		if(checkVersionForShimmerEnable())
		{
			shimmer = new Shimmer();
			shimmer.start(tv_title);
		}
		tv_title.setText(mDataList.get(position).title);
		tv_title.setBackgroundColor(Constant.COLORS_SELECT[Constant.K++%9]);
		iv_home_comic.mWidth = mDataList.get(position).width;
		iv_home_comic.mHeight = mDataList.get(position).height;
		ImageLoader.getInstance().displayImage("assets://"+mDataList.get(position).url,  iv_home_comic);
//		
//		iv_home_comic.setImageBitmap(getImageFromAssetsFile(mDataList.get(position).url));
		
		/**
         * StaggeredGridView has bugs dealing with child TouchEvent
         * You must deal TouchEvent in the child view itself
         **/
		iv_home_comic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	sloveOnclick(position);
            }
        });

		tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	sloveOnclick(position);
            }
        });

		return convertView;
	}
	
	protected void sloveOnclick(int position) {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("link", mDataList.get(position).link);
		intent.putExtra("title", mDataList.get(position).title);
		mContext.startActivity(intent);
	}

	/*  
	   * 从Assets中读取图片  
	   */  
	  private Bitmap getImageFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      AssetManager am = mContext.getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	  
	      return image;  
	  
	  }  
	  
	    public boolean checkVersionForShimmerEnable()
	    {
	    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	    		return false;
			}
			return true;
	    }
	    
	/** 回收资源 */
	public void recycleAdapterResource() {
		if(checkVersionForShimmerEnable())
		{
			shimmer.cancel();
			shimmer = null;
		}
		
		iv_home_comic = null;
		tv_title = null;
		mContext = null;
		mDataList = null;
		
	}

}
