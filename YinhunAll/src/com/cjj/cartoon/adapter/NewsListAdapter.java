package com.cjj.cartoon.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
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

public class NewsListAdapter extends SimpleBaseAdapter<ImageAndTitle>{
	private Shimmer  shimmer;
	private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();
	public NewsListAdapter(Context context, ArrayList<ImageAndTitle> list) {
		super(context, list);
	}
	
	public NewsListAdapter(Context context, ArrayList<ImageAndTitle> list,boolean isFavorite) {
		super(context, list);
	}

	@Override
	public int getItemResourceId() {
		return R.layout.item_favorite;
	}
	
//	public void updateData(ArrayList<ImageAndTitle> newsList) {
//		this.mDataList = newsList;
//		this.notifyDataSetChanged();
//	}
//	
	
	@Override
	public View getItemView(final int position, View convertView,
		SimpleBaseAdapter.ViewHolder holder) {
		TypefaceHelper.typeface(convertView);
		final ImageView newsImageView = (ImageView) holder.getView(R.id.list_item_image);
		ShimmerTextView newsTitleView = (ShimmerTextView) holder.getView(R.id.list_item_title);
		if(Constant.checkVersionForShimmerEnable())
		{
			shimmer = new Shimmer();
			shimmer.start(newsTitleView);
		}
		ImageAndTitle model = mDataList.get(position);
		ImageLoader.getInstance().displayImage(model.imageUrl, newsImageView, ImageConfigBuilder.NORMAL_IMAGE, mAnimateFirstListener);
		newsTitleView.setText(model.title);
		return convertView;
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
