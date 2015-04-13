package com.cjj.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.callback.ReLoadCallbackListener;
import com.cjj.loadingviewlibrary.R;
import com.cjj.utils.NetUtils;
/**
 * 
 * @author cjj
 *
 */
public class LoadingCjjLayout extends FrameLayout implements OnClickListener{
	
	private View contentView;
	private View failView;
	private View emptyView;
	private View reloadView;
	private View loadingView;
	private TextView tv_reload;
	private TextView tv_loading_doc;
	private TextView tv_not_data;
	private ImageView iv_reloading;
	private Animation rotate;
	private ReLoadCallbackListener callbackListener;
	
	public LoadingCjjLayout(Context context,View contentView) {
		super(context);
		this.contentView = contentView;
		initView();
		handleView();
	}

	public LoadingCjjLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoadingCjjLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	

	private void initView() 
	{
		
		if(failView == null)
		{
			failView = inflaterView( R.layout.view_fail);
			failView.setClickable(true);
			tv_reload = (TextView) failView.findViewById(R.id.tv_reload);
			tv_reload.setOnClickListener(this);
		}
		
		if(emptyView == null)
		{
			emptyView = inflaterView( R.layout.view_empty);
			tv_not_data = (TextView) emptyView.findViewById(R.id.tv_empty);
		}
		
		if(loadingView==null)
		{
		   loadingView = inflaterView( R.layout.view_loading);
		   loadingView.setClickable(true);
//		   tv_loading_doc = (TextView) loadingView.findViewById(R.id.tv_loading_dot);
		}
		
		if(reloadView == null)
		{
			reloadView = inflaterView(R.layout.view_reloading);
			 iv_reloading = (ImageView) reloadView.findViewById(R.id.iv_reloading);
			 rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);  
			LinearInterpolator lin = new LinearInterpolator();  
//			AccelerateInterpolator ain = new AccelerateInterpolator();
			rotate.setInterpolator(lin);
			reloadView.setClickable(true);
		}
		
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
	
	

	private void handleView() 
	{
		add_AllView();
		hide_AllView();
	}
	
	private void add_AllView()
	{
	
		this.addView(emptyView);
		
		this.addView(contentView);
		
		this.addView(failView);
		
		this.addView(loadingView);
		
		this.addView(reloadView);
	}
	
	private void hide_AllView()
	{
		if(failView != null)
		{
			failView.setVisibility(INVISIBLE);
		}
		if(loadingView != null)
		{
			loadingView.setVisibility(INVISIBLE);
		}
		if(emptyView != null)
		{
			emptyView.setVisibility(INVISIBLE);
		}
		if(contentView != null)
		{
			contentView.setVisibility(INVISIBLE);
		}
		if(reloadView != null)
		{
			reloadView.setVisibility(INVISIBLE);
		}
	}
	
	public void show_ContentView()
	{
		if(contentView != null)
		{
			contentView.setVisibility(VISIBLE);
		}
		if(failView != null)
		{
			failView.setVisibility(INVISIBLE);
		}
		if(loadingView != null)
		{
			loadingView.setVisibility(INVISIBLE);
		}
		if(emptyView != null)
		{
			emptyView.setVisibility(INVISIBLE);
		}
		if(reloadView !=null)
		{
			reloadView.setVisibility(INVISIBLE);
		}
		
	}
	
	public void show_FailView()
	{
		if(failView != null)
		{
			failView.setVisibility(VISIBLE);
		}
		if(loadingView != null)
		{
			loadingView.setVisibility(INVISIBLE);
		}
		if(emptyView != null)
		{
			emptyView.setVisibility(INVISIBLE);
		}
		if(contentView != null)
		{
			contentView.setVisibility(VISIBLE);
		}
		if(reloadView !=null)
		{
			reloadView.setVisibility(INVISIBLE);
		}
	}
	
	public void show_EmptyView()
	{
		if(emptyView != null)
		{
			emptyView.setVisibility(VISIBLE);
		}
		
		if(failView != null)
		{
			failView.setVisibility(INVISIBLE);
		}
		if(loadingView != null)
		{
			loadingView.setVisibility(INVISIBLE);
		}
		
		if(contentView != null)
		{
			contentView.setVisibility(INVISIBLE);
		}
		if(reloadView !=null)
		{
			reloadView.setVisibility(INVISIBLE);
		}
	}
	
	public void show_LoadingView()
	{
		if(loadingView != null)
		{
			loadingView.setVisibility(VISIBLE);
		}
		if(failView != null)
		{
			failView.setVisibility(INVISIBLE);
		}
		if(emptyView != null)
		{
			emptyView.setVisibility(INVISIBLE);
		}
		if(contentView != null)
		{
			contentView.setVisibility(VISIBLE);
		}
		if(reloadView !=null)
		{
			reloadView.setVisibility(INVISIBLE);
		}
	}
	
	public void show_reloadingView()
	{
		if(reloadView !=null)
		{
			reloadView.setVisibility(VISIBLE);
			iv_reloading.startAnimation(rotate);
		}
		if(loadingView != null)
		{
			loadingView.setVisibility(INVISIBLE);
		}
		if(failView != null)
		{
			failView.setVisibility(INVISIBLE);
		}
		if(emptyView != null)
		{
			emptyView.setVisibility(INVISIBLE);
		}
	}
	
	
	
	public void setReLoadCallbackListener(ReLoadCallbackListener callbackListener)
	{
		this.callbackListener = callbackListener;
	}
	

	@Override
	public void onClick(View v) {
			solveReLoad();
	}
	
	/**
	 * 处理重新点击事件
	 */
	private void solveReLoad() 
	{
		if(NetUtils.isNetworkConnected(getContext()))
		{
			if(callbackListener != null)
			{
				this.callbackListener.onReLoadCallback();
			}else{
				throw new RuntimeException("You must be set setReLoadCallbackListener");
			}
			
		}
	}

	
	

}
