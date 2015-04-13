package com.cjj.cartoon.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cjj.cartoon.R;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.model.ImageAndTitle;
import com.cjj.cartoon.util.ImageConfigBuilder;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class TwoListDataAdapter extends BaseAdapter{
	private Context mContext;
	private  ArrayList<ImageAndTitle> mList;
	private Shimmer  shimmer;
	private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();
	
	public  TwoListDataAdapter(Context mContext, ArrayList<ImageAndTitle> mList)
	{
		this.mContext = mContext;
		this.mList = mList;
	}
	
	public void updateData(ArrayList<ImageAndTitle> mList)
	{
		this.mList = mList;
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public ImageAndTitle getItem(int position) {
		return mList==null?null:mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null)
		{
			convertView = View.inflate(mContext,R.layout.item_favorite, null);
			viewHolder = new ViewHolder();
			viewHolder.newsImageView = (ImageView) convertView.findViewById(R.id.list_item_image);
			viewHolder.newsTitleView = (ShimmerTextView) convertView.findViewById(R.id.list_item_title);
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder =  (ViewHolder) convertView.getTag();
		}
		
		if(Constant.checkVersionForShimmerEnable())
		{
			shimmer = new Shimmer();
			shimmer.start(viewHolder.newsTitleView);
		}
		ImageAndTitle model = mList.get(position);
		ImageLoader.getInstance().displayImage(model.imageUrl, viewHolder.newsImageView, ImageConfigBuilder.NORMAL_IMAGE, mAnimateFirstListener);
		viewHolder.newsTitleView.setText(model.title);
		
		TypefaceHelper.typeface(convertView);
		return convertView;
	}
	
	public class ViewHolder
	{
		private ShimmerTextView newsTitleView ;
		private ImageView newsImageView;
	}
	
	private static class AnimateFirstDisplayListener extends
	SimpleImageLoadingListener {

static final List<String> displayedImages = Collections
		.synchronizedList(new LinkedList<String>());

@Override
public void onLoadingComplete(String imageUri, View view,
		Bitmap loadedImage) {
	if (loadedImage != null) {
		ImageView imageView = (ImageView) view;
		boolean firstDisplay = !displayedImages.contains(imageUri);
		if (firstDisplay) {
			FadeInBitmapDisplayer.animate(imageView, 500);
			displayedImages.add(imageUri);
		}
	}
}
}

}
