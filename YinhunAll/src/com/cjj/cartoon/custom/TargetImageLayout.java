package com.cjj.cartoon.custom;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.cartoon.ComicImageViewActivity;
import com.cjj.cartoon.MainActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.model.HeadViewDataModel;
import com.cjj.cartoon.util.ImageConfigBuilder;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

/**
 * @author cjj
 */
public class TargetImageLayout extends FrameLayout{
	private LinearLayout ll_view;
	private RelativeLayout ll_over;
	private ImageView mSelectImage,mSelectImage2;
	private int DEFAULT_COUNT = 5;
	private int mImageCount = DEFAULT_COUNT;
	private int mImageHeght,mImageWidth;
	private int AllImageWidth ;
	private ImageView[] mImageView;
	private int index = 0;
	private List<HeadViewDataModel> list;
	private ShimmerTextView tv_title;
	
	public TargetImageLayout(Context context) {
		super(context);
	}

	public TargetImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	/**
	 * set all image url
	 * @param imageUrl
	 */
//	public void setListImageData(String[] imageUrl)
//	{
//		this.imageUrl = imageUrl;
//		init();
//	}

	/**
	 * set all image url
	 * @param imageUrl
	 */
	private Activity activity;
	public void setListImageData(Activity activity,List<HeadViewDataModel> list)
	{
		this.list = list;
		this.activity = activity;
		init();
	}

	
	/**
	 * 初始化
	 */
	private void init() {
		mImageView = new ImageView[mImageCount];
		ll_view = (LinearLayout) inflaterView(R.layout.ll_view);
		ll_over = (RelativeLayout) inflaterView(R.layout.over_view);
		tv_title = (ShimmerTextView) ll_over.findViewById(R.id.tv_title);
		if(Constant.checkVersionForShimmerEnable())
		{
			(new Shimmer()).start(tv_title);
		}
		mSelectImage = (ImageView) ll_over.findViewById(R.id.iv_target);
		mSelectImage2 = (ImageView) ll_over.findViewById(R.id.iv_target2);
		AllImageWidth  = (getScreenWidth()- getResources().getDimensionPixelSize(R.dimen.stgv_margin)*2)/2*3;
		mImageWidth = AllImageWidth/mImageCount;
		mImageHeght = mImageWidth;
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(mImageWidth, mImageWidth);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mImageWidth, mImageWidth);	
		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);	
		lp2.setMargins(-mImageWidth/3, 0, 0, 0);
		for(int i = 0; i<mImageCount; i++)
		{
			index = i;
			if(i==0)
			{
				mImageView[i] = new ImageView(getContext());
				mImageView[i].setLayoutParams(lp1);
				
			}else
			{
				mImageView[i] = new ImageView(getContext());
				mImageView[i].setLayoutParams(lp2);
			}
			
			mImageView[i].setPadding(1, 0, 1, 0);
			mImageView[i].setBackgroundColor(Color.rgb(245, 245, 245));
			mImageView[i].setScaleType(ScaleType.FIT_XY);
			if(list.size()>0)
			ImageLoader.getInstance().displayImage(list.get(i).url, mImageView[i],ImageConfigBuilder.USER_HEAD_HD_OPTIONS);
			mImageView[i].setClickable(true);
			mImageView[i].setTag(i);
//			mImageView[i].setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					switch(event.getAction())
//					{
//					case MotionEvent.ACTION_DOWN:
//						Toast.makeText(getContext(), "down", 1).show();
//						break;
//					case MotionEvent.ACTION_MOVE:
//						Toast.makeText(getContext(), "move", 1).show();
//						break;
//					case MotionEvent.ACTION_UP:
//						Toast.makeText(getContext(), "up", 1).show();
//						break;
//					}
//					return false;
//				}
//			});
			mImageView[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					
					setLayoutTargerImage(index);
				}
			});
			ll_view.addView(mImageView[i]);
		}
		
		
		tv_title.setLayoutParams(new RelativeLayout.LayoutParams(mImageWidth, LayoutParams.WRAP_CONTENT));
		mSelectImage2.setLayoutParams(lp3);
		mSelectImage2.setClickable(true);
		mSelectImage.setLayoutParams(lp3);
		mSelectImage.setClickable(true);
		targetOnclick(0);
		tv_title.setText(list.get(0).title);
		ImageLoader.getInstance().displayImage(list.get(0).url, mSelectImage2);
		this.addView(ll_view);
		this.addView(ll_over);
		
	}
	
	
	
	private boolean isChange = false;
	private Animation anim_in;
	private Animation anim_out,anim_out2;
	protected void setLayoutTargerImage(int index) {
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mImageWidth, LayoutParams.WRAP_CONTENT);
		if(index == mImageCount-1)
		{
			lp1.setMargins(index*mImageWidth*2/3-mImageHeght/3, 0, 0, 0);
			
			lp2.setMargins(index*mImageWidth*2/3-mImageHeght/3, 0, 0, 0);
		}else
		{
			lp1.setMargins(index*mImageWidth*2/3, 0, 0, 0);	
			
			lp2.setMargins(index*mImageWidth*2/3, 0, 0, 0);	
		}
		
		mSelectImage2.setVisibility(View.VISIBLE);
		anim_in = AnimationUtils.loadAnimation(getContext(), R.anim.image_fade_in);
		anim_out = AnimationUtils.loadAnimation(getContext(), R.anim.image_fade_out);
		tv_title.setLayoutParams(lp2);
		tv_title.setText(list.get(index).title);
		tv_title.startAnimation(anim_in);
		if(isChange)
		{
			if(list.size()>0)
				ImageLoader.getInstance().displayImage(list.get(index).url, mSelectImage2);
			mSelectImage2.startAnimation(anim_in);
			mSelectImage.startAnimation(anim_out);
			anim_out.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mSelectImage.setImageResource(R.drawable.tm);
					mSelectImage.setVisibility(View.GONE);
				}
			});
			mSelectImage2.setLayoutParams(lp1);
			isChange = false;
//			mSelectImage.setImageResource(R.drawable.tm);
			
		}else
		{
			if(list.size()>0)
				ImageLoader.getInstance().displayImage(list.get(index).url, mSelectImage);	
			mSelectImage.startAnimation(anim_in);
			anim_out2 = anim_out;
			mSelectImage.setVisibility(View.VISIBLE);
			anim_out2.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mSelectImage2.setImageResource(R.drawable.tm);		
					mSelectImage2.setVisibility(View.GONE);
				}
			});
			mSelectImage2.startAnimation(anim_out2);
			mSelectImage.setLayoutParams(lp1);
			isChange = true;
//			mSelectImage2.setImageResource(R.drawable.tm);
		};
		
		
		targetOnclick(index);
	
	}

	private void targetOnclick(final int index2) {
		mSelectImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sloveTargetOnclick(index2);
			}
		});
		mSelectImage2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sloveTargetOnclick(index2);
			}
		});
	}
	
	

	protected void sloveTargetOnclick(int index2) {
//		Intent intent = new Intent(getContext(), MainActivity.class);
//		intent.putExtra("link", list.get(index2).link);
//		getContext().startActivity(intent);
		
		Intent intent = new Intent(getContext(), ComicImageViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", list.get(index2).link);
		bundle.putString("title", list.get(index2).title);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.right, R.anim.hold);
	}

	/**
	 *装载器
	 * @param layoutId
	 * @return
	 */
	private View inflaterView(int layoutId)
	{
	   return LayoutInflater.from(getContext()).inflate(layoutId,null);	
	}
	
	
	/**
	 * 获得屏幕的宽度
	 * @return
	 */
	public int getScreenWidth()
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
	
	

}
