package com.cjj.cartoon.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cjj.cartoon.MainActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.model.LocalImageModel;
import com.cjj.staggeredgridview.STGVImageView;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class HomeListComicAdapter extends BaseAdapter{
	private Context mContext;
	private List<LocalImageModel> mList;
	
	public HomeListComicAdapter(Context mContext,List<LocalImageModel> mList)
	{
		this.mContext = mContext;
		this.mList = mList;
	}
	
	@Override
	public int getCount() {
		return mList == null ?0:mList.size();
	}

	@Override
	public LocalImageModel getItem(int position) {
		return mList == null?null:mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		 View view = null;
	        final LocalImageModel item = mList.get(position);


	        if (convertView == null) {
	            ViewHolder holder = new ViewHolder();
	            view = View.inflate(mContext, R.layout.item_comic_list, null);
	            holder.iv_home_comic = (STGVImageView) view.findViewById(R.id.iv_home_comic);
	            holder.tv_title = (ShimmerTextView) view.findViewById(R.id.tv_title_home);

	            view.setTag(holder);
	        } else {
	            view = convertView;
	        }

	        ViewHolder holder = (ViewHolder) view.getTag();

	        /**
	         * StaggeredGridView has bugs dealing with child TouchEvent
	         * You must deal TouchEvent in the child view itself
	         **/
	        holder.iv_home_comic.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            }
	        });

	        holder.tv_title.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            }
	        });


	        holder.iv_home_comic.mHeight = item.height;
	        holder.iv_home_comic.mWidth = item.width;

//	        Picasso.with(mContext).load(url).into(holder.iv_home_comic);
	        if(Constant.checkVersionForShimmerEnable())
			{
				shimmer = new Shimmer();
				shimmer.start(holder.tv_title);
			}
			holder.tv_title.setText(mList.get(position).title);
			holder.tv_title.setBackgroundColor(Constant.COLORS_SELECT[Constant.K++%9]);
	    	ImageLoader.getInstance().displayImage("assets://"+mList.get(position).url,  holder.iv_home_comic);
	    	/**
	         * StaggeredGridView has bugs dealing with child TouchEvent
	         * You must deal TouchEvent in the child view itself
	         **/
			holder.iv_home_comic.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	sloveOnclick(position);
	            }
	        });

			holder.tv_title.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	sloveOnclick(position);
	            }
	        });
			TypefaceHelper.typeface(view);
	        return view;
	}
	
	protected void sloveOnclick(int position) {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("link", mList.get(position).link);
		intent.putExtra("title", mList.get(position).title);
		mContext.startActivity(intent);
	}
	
	private Shimmer  shimmer;
	public class ViewHolder 
	{
		private STGVImageView iv_home_comic;
		private ShimmerTextView tv_title;
	}
	
	/** 回收资源 */
	public void recycleAdapterResource() {
		if(Constant.checkVersionForShimmerEnable())
		{
			shimmer.cancel();
			shimmer = null;
		}
		
		mContext = null;
		mList = null;
	}
		

}
