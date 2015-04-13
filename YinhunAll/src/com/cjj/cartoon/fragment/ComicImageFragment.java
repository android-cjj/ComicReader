package com.cjj.cartoon.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.cartoon.R;
import com.cjj.cartoon.adapter.ComicPagerAdapter;
import com.cjj.cartoon.callback.SeekbarImagePageCallback;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.custom.HackyViewPager;
import com.cjj.cartoon.dao.YinhunDao;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class ComicImageFragment extends Fragment implements OnPageChangeListener{
	String link = null;
	String title = null;
	private HackyViewPager vp_comic;
	private ComicPagerAdapter mAdapter;
	private static final String ISLOCKED_ARG = "isLocked";
	private BatteryBroadcastReceiver batteryBroadcastReceiver;
	private ImageView iv_battery;
	private TextView tv_time,tv_title;
	private RelativeLayout rl_time_battery , rl_tile;
	public static  ComicImageFragment instance;
	private ImageView iv_shaonv;
	private AnimationDrawable animationDrawable;
	private ComicDataAsyncTask task;
	private boolean timeFlag = true;
	private LinearLayout ll_shaonv;
	private Shimmer  shimmer;
//	private ListView lv_comic;
	private boolean isListView = false;
	
	public static ComicImageFragment newInstance(String url,String title) {
		ComicImageFragment fragment = new ComicImageFragment();
		Bundle args = new Bundle();
		args.putString("url", url);
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		instance = this;
		task = new ComicDataAsyncTask();
		if (savedInstanceState != null&&vp_comic!=null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) vp_comic).setLocked(isLocked);
		}
		Bundle bundle = getArguments();
		if (bundle != null)
		{
			link = bundle.getString("url");
			title = bundle.getString("title");
		}
		initScreen();
		
		
		registerVpReceiver();
		
		registerBatteryReceiver();
		
		super.onCreate(savedInstanceState);
	}
	

	private void registerBatteryReceiver() {
		  //注册一个接受广播类型
		batteryBroadcastReceiver = new BatteryBroadcastReceiver();
        getActivity().registerReceiver(batteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	private void registerVpReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constant.SEEKBAR_PAGE);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);  
	}

	private int screenWidth, screenHeight;
	private void initScreen() {
		DisplayMetrics dm = new DisplayMetrics();

		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenWidth = dm.widthPixels;

		screenHeight = dm.heightPixels;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.fragment_comic_vertical,null);
		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		vp_comic = (HackyViewPager) view.findViewById(R.id.vp_comic);
		vp_comic.setOnPageChangeListener(this);
		vp_comic.setSeekbarViewPager(new SeekbarImagePageCallback() {
			@Override
			public void imagePage(int page) {
				vp_comic.setCurrentItem(page);
			}
		});
		
//		lv_comic = (ListView) view.findViewById(R.id.list_comic);
//		lv_comic.setVerticalScrollBarEnabled(false);
		
		
		rl_tile = (RelativeLayout) view.findViewById(R.id.rl_title);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		rl_time_battery = (RelativeLayout) view.findViewById(R.id.rl_time_battery);
		iv_battery = (ImageView) view.findViewById(R.id.iv_battery);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		
		iv_shaonv = (ImageView) view.findViewById(R.id.imageView_load);
		animationDrawable = (AnimationDrawable) iv_shaonv.getBackground();
		animationDrawable.start();
		ShimmerTextView  tv_wait = (ShimmerTextView) view.findViewById(R.id.shimmer_tv); 
		
		if(checkVersionForShimmerEnable())
		{
			shimmer = new Shimmer();
			shimmer.start(tv_wait);
		}
		ll_shaonv = (LinearLayout) view.findViewById(R.id.ll_shaonv);
		
		super.onViewCreated(view, savedInstanceState);
	}
	

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		task.execute(link);
		new TimeThread().start();
		showTitle();
		super.onActivityCreated(savedInstanceState);
	}
	
	public  HackyViewPager getViewpager()
	{
		if(vp_comic!=null)
		{
			return vp_comic;
		}
		return null;
	}

	private void showTitle() {
		tv_title.setText(title);
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(mBroadcastReceiver);
		getActivity().unregisterReceiver(batteryBroadcastReceiver);
		if(animationDrawable!=null&&animationDrawable.isRunning())
		{
			animationDrawable.stop();
			animationDrawable = null;
		}
		timeFlag = false;
		if(checkVersionForShimmerEnable())
		{
			shimmer.cancel();
		}
		
		super.onDestroyView();
	}
	
	// 当该Fragment被添加,显示到Activity时调用该方法
	// 在此判断显示到的Activity是否已经实现了接口
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof ImageNumberFragmentCallBack)) {
			throw new IllegalStateException(
					"ComicImageFragment所在的Activity必须实现ImageNumberFragmentCallBack接口");
		}
		imageNumberFragmentCallBack = (ImageNumberFragmentCallBack) activity;
	}
	
	// 定义一个业务接口
	// 该Fragment所在Activity需要实现该接口
	// 该Fragment将通过此接口与它所在的Activity交互
	private ImageNumberFragmentCallBack imageNumberFragmentCallBack;
	public interface ImageNumberFragmentCallBack {
		public void Size(int size);
		public void currentPage(int page);
	}

	  

	private class ComicDataAsyncTask extends
			AsyncTask<String, Integer, ArrayList<String>> {

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			if(result!=null&&result.size()>0)
			{
				if(mAdapter == null)
				{
					if(!isListView)
					{
						if(imageNumberFragmentCallBack!=null)
							imageNumberFragmentCallBack.Size(result.size());
							mAdapter = new ComicPagerAdapter(getActivity(), result,screenWidth,screenHeight,vp_comic,handler);
							vp_comic.setAdapter(mAdapter);
							
					}else
					{
						
//						lv_comic.setAdapter(new ComicListViewAdapter(getActivity(), result,lv_comic,screenWidth));
					}
					ll_shaonv.setVisibility(View.GONE);
					
				}else 
				{
					
				}
			}
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> all = null;
			try {
				all = YinhunDao.getInstance().getDetailComicImage(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			publishProgress(90);
			return all;
		}

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) vp_comic).isLocked());
    	}
		super.onSaveInstanceState(outState);
	}
	
	private boolean isViewPagerActive() {
    	return (vp_comic != null && vp_comic instanceof HackyViewPager);
    }
	
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		if(imageNumberFragmentCallBack!=null)
		{
			imageNumberFragmentCallBack.currentPage(arg0);
		}
	}
	
	/**
	 * 监听页数广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction(); 
           int item =  intent.getIntExtra("pro",0);
            if(action.equals(Constant.SEEKBAR_PAGE)){ 
            	vp_comic.setCurrentItem(item-1);
            }  
        }  
          
    };  
    
    /**接受电量改变广播*/
    class BatteryBroadcastReceiver extends BroadcastReceiver{
    	
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		
    		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
    			
			    int level = intent.getIntExtra("level", 0);
  				int scale = intent.getIntExtra("scale", 100);
   				int curPower = (level * 100 / scale)/25;
   				switch (curPower) {
				case 0:
					iv_battery.setImageResource(R.drawable.battery_4);
					break;
				case 1:
					iv_battery.setImageResource(R.drawable.battery_25);
					break;
				case 2:
					iv_battery.setImageResource(R.drawable.battery_50);
					break;
				case 3:
					iv_battery.setImageResource(R.drawable.battery_75);
					break;
				case 4:
					iv_battery.setImageResource(R.drawable.battery_100);
					break;
				}
    		}
    	}

    }
    
    public class TimeThread extends Thread
    {
    	 @Override
    	    public void run() {
    	        // TODO Auto-generated method stub
    	        try {
    	            while(timeFlag){
    	                SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    	                String str=sdf.format(new Date());
    	                handler.sendMessage(handler.obtainMessage(100,str));
    	                Thread.sleep(1000);
    	            }
    	        } catch (InterruptedException e) {
    	            // TODO Auto-generated catch block
    	            e.printStackTrace();
    	        }
    	    }
    }
    
    
    public Handler handler = new Handler()
    {
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what)
    		{
    		case 100:
    			tv_time.setText(String.valueOf(msg.obj));
    			break;
    		case 101:
    			break;
    		}
    	};
    };
    
    
    public void showBatteryTip()
    {
    	if(rl_time_battery!=null)
    	{
    		rl_time_battery.setVisibility(View.VISIBLE);
        	rl_tile.setVisibility(View.VISIBLE);
        	rl_time_battery.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_time));
        	rl_tile.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_title));
    	}
    }
    
    public void hideBatteryTip()
    {
    	if(rl_time_battery!=null)
    	{
    		rl_time_battery.setVisibility(View.GONE);
        	rl_tile.setVisibility(View.GONE);
        	rl_time_battery.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_time));
        	rl_tile.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_title));
    	}
    }
    
   
    public boolean checkVersionForShimmerEnable()
    {
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    		return false;
		}
		return true;
    }
	
}
