package com.cjj.cartoon;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.provider.SyncStateContract.Constants;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.cartoon.callback.SeekbarImagePageCallback;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.fragment.ComicImageFragment;
import com.cjj.cartoon.fragment.ComicImageFragment.ImageNumberFragmentCallBack;
import com.cjj.cartoon.util.SaveImageUtils;
import com.cjj.cartoon.util.SettingUtils;
import com.cjj.cartoon.util.ShareViewTask;

public class ComicImageViewActivity extends ActionBarActivity implements ImageNumberFragmentCallBack{
	private Toolbar toolbar ;
	private RelativeLayout bottom_view;
	public static ComicImageViewActivity comicImageViewActivity;
	public View ll_right_tip;
	private SeekBar seekBar;
	private TextView tv_tip_page;
	private int size = 0;
	private SeekbarImagePageCallback seekbarImagePageCallback;
	private TextView tv_tip_right,tv_light,tv_pic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		/**
		  * 设置为横屏
		  */
//		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		 }
		setContentView(R.layout.activity_comic);
		comicImageViewActivity = this;
		init();
		logic();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.fl_comic,
							ComicImageFragment.newInstance(bundle
									.getString("url"),bundle.getString("title"))).commit();
		
		LightShow();

	}

	
	private void LightShow() {
		// preview brightness changes at this window
		// get the current window attributes
		LayoutParams layoutpars = getWindow().getAttributes();
		// set the brightness of this window
		int light = 100;
		try {
			light = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			layoutpars.screenBrightness = SettingUtils.getSetting(ComicImageViewActivity.this, "light", light) / (float) 255;
			// apply attribute changes to this window
			getWindow().setAttributes(layoutpars);		
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}


	private void init() {
		initToorbar();
		initbottomView();
		initTipView();
	}

	private void initTipView() {
		ll_right_tip = this.findViewById(R.id.ll_right);
		ll_right_tip.setVisibility(View.GONE);
		ll_right_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_right_tip.setVisibility(View.GONE);
				ComicImageFragment.instance.showBatteryTip();
			}
		});
	}

	private void initbottomView() {
		bottom_view = (RelativeLayout) this.findViewById(R.id.bottom_view);
		tv_tip_right = (TextView) this.findViewById(R.id.tv_tip);
		tv_tip_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ll_right_tip.setVisibility(View.VISIBLE);
				hideMenuView();
			}
		});
		bottom_view.setVisibility(View.GONE);
		
		/**亮度*/
		tv_light = (TextView) this.findViewById(R.id.tv_light);
		tv_light.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogLightSeeting("调节系统亮度");
			}
		});
		
		
		/**截图*/
		tv_pic = (TextView) this.findViewById(R.id.tv_pic);
		tv_pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogPicSelect();
			}
		});
		
	}

	/**
	 * 图片截图框
	 */
	protected void showDialogPicSelect() {
		  final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                  this,
                  R.layout.item_text
          );
		  arrayAdapter.add("分享图片");
          arrayAdapter.add("保存图片至图册");
          arrayAdapter.add("保存图片至文件夹");
          ListView listView = new ListView(this);
          float scale = getResources().getDisplayMetrics().density;
          int dpAsPixels = (int) (8 * scale + 0.5f);
          listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
          listView.setDividerHeight(0);
          listView.setAdapter(arrayAdapter);
        

          final MaterialDialog alert = new MaterialDialog(this)
              .setContentView(listView);

          listView.setOnItemClickListener(new OnItemClickListener() {
  			@Override
  			public void onItemClick(AdapterView<?> parent, View view,
  					int position, long id) {
  				alert.dismiss();
  				switch(position)
  				{
  				case 0:
  					sharePic();
  					break;
  				case 1:
  					savePicInGarrery();
  					break;
  				case 2:
  					savePicInSdCard();
  					break;
  				}
  			}
  		});
          alert.show();
	}

	
	/**
	 * 保存图片到sdcard
	 */
	protected void savePicInSdCard() {
		hideMenuView();
		try {
			File path = SaveImageUtils.saveImageToFile(SaveImageUtils.getBitmapScreen(ComicImageViewActivity.this), ComicImageViewActivity.this);
			Toast.makeText(this, "保存至"+path, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, "保存失败,内存不足", 1000).show();
			e.printStackTrace();
		}
		showMenuView();
	}


	/**
	 * 保存图片至相册
	 */
	protected void savePicInGarrery() {
		hideMenuView();
		try {
			SaveImageUtils.saveImageToGallery(ComicImageViewActivity.this, ComicImageViewActivity.this);
			Toast.makeText(this, "保存手机图册成功", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, "保存失败,内存不足", 1000).show();
			e.printStackTrace();
		}
		showMenuView();
	}


	


	/**
	 * 亮度框
	 * @param string
	 */
	protected void showDialogLightSeeting(String string) {
		final MaterialDialog dialog = new MaterialDialog(this);
		dialog.setTitle(string);
		dialog.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			  	old_brightness = brightness;
            	SettingUtils.setSetting(ComicImageViewActivity.this, "light", brightness);
			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			 	LayoutParams layoutpars = getWindow().getAttributes();
				// set the brightness of this window
				layoutpars.screenBrightness = old_brightness / (float) 255;
				// apply attribute changes to this window
				getWindow().setAttributes(layoutpars);
				
			}
		});
		View customView = getLayoutInflater().inflate(R.layout.seekbar_light, null);
		createSeekBar(customView);	
		dialog.setContentView(customView);
		dialog.show();
	}


//	protected void showDialogLightSeeting(String title,String okbtn,String message) {
//		CustomDialog.Builder builder = new CustomDialog.Builder(ComicImageViewActivity.this, title, okbtn);
//		// Now we can any of the following methods.
////		builder.content(message);
//		builder.darkTheme(false);
//		builder.titleColor(this.getResources().getColor(R.color.tab_color_pre));
////		builder.contentColor(this.getResources().getColor(R.color.tab_color_pre));
//		builder.positiveColor(this.getResources().getColor(R.color.tab_color_pre));
//		builder.titleTextSize(15);
//		builder.negativeText("取消");
//		builder.negativeColor(this.getResources().getColor(R.color.tab_color_pre));
//		// Now we can build the dialog.
//		CustomDialog customDialog = builder.build();
//		View customView = getLayoutInflater().inflate(R.layout.seekbar_light, null);
//		createSeekBar(customView);	
//		customDialog.setCustomView(customView);
////		// Show the dialog.
//		customDialog.setClickListener(new CustomDialog.ClickListener() {
//            @Override
//            public void onConfirmClick() {
//            	old_brightness = brightness;
//            	SettingUtils.setSetting(ComicImageViewActivity.this, "light", brightness);
//            }
//
//            @Override
//            public void onCancelClick() {
//            	LayoutParams layoutpars = getWindow().getAttributes();
//				// set the brightness of this window
//				layoutpars.screenBrightness = old_brightness / (float) 255;
//				// apply attribute changes to this window
//				getWindow().setAttributes(layoutpars);
//            }
//        });
//		customDialog.show();
//	}
	
	int brightness;
	int old_brightness = 100;
	public void createSeekBar(View customView)
	{
		SeekBar brightbar = (SeekBar) customView.findViewById(R.id.seekBar_light);
		brightbar.setMax(255);
		brightbar.setProgress(200);
		try {
			// get the current system brightness
			 brightness = System.getInt(this.getContentResolver(),
					System.SCREEN_BRIGHTNESS);
			 brightbar.setProgress(SettingUtils.getSetting(ComicImageViewActivity.this, "light", 125));
		} catch (SettingNotFoundException e) {
			Toast.makeText(this, "不能获取系统亮度", Toast.LENGTH_SHORT).show();
			return;
		}
		// register OnSeekBarChangeListener, so it can actually change values
		brightbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// set the system brightness using the brightness variable value
				// if(Settings.System.getInt(cResolver,
				// Settings.System.SCREEN_BRIGHTNESS_MODE) ==
				// Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
				// System.putInt(cResolver, System.SCREEN_BRIGHTNESS,
				// brightness);

				// preview brightness changes at this window
				// get the current window attributes
				LayoutParams layoutpars = getWindow().getAttributes();
				// set the brightness of this window
				layoutpars.screenBrightness = brightness / (float) 255;
				// apply attribute changes to this window
				getWindow().setAttributes(layoutpars);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// sets the minimal brightness level
				// if seek bar is 20 or any value below
				if (progress <= 10) {
					// set the brightness to 20
					brightness = 10;
				} else // brightness is greater than 20
				{
					// sets brightness variable based on the progress bar
					brightness = progress;
				}
			}
		});
		
	}

	

	private void initToorbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar2);
		toolbar.setTitle("");
		toolbar.setVisibility(View.GONE);
		setSupportActionBar(toolbar);
		// Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
		toolbar.setNavigationIcon(R.drawable.adward_navigationbar_icon_back);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComicImageViewActivity.this.finish();
				overridePendingTransition(R.anim.hold, R.anim.left);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if (keyCode == KeyEvent.KEYCODE_BACK) {
			ComicImageViewActivity.this.finish();
			overridePendingTransition(R.anim.hold, R.anim.left);
	       return true;
	   }
	   return super.onKeyDown(keyCode, event);
	}
	
	
	
	private void logic() {
		tv_tip_page = (TextView) this.findViewById(R.id.tv_page_tip);
		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * 拖动条停止拖动的时候调用
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent(Constant.SEEKBAR_PAGE);
				int pro = seekBar.getProgress();
				intent.putExtra("pro", seekBar.getProgress());
				ComicImageViewActivity.this.sendBroadcast(intent);
			}

			/**
			 * 拖动条开始拖动的时候调用
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			/**
			 * 拖动条进度改变的时候调用
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int pro = progress;
				tv_tip_page.setText(pro+"/"+size);
			}
		});
	}
	
	
	public void setVpNumberListener(SeekbarImagePageCallback seekbarImagePageCallback)
	{
		this.seekbarImagePageCallback = seekbarImagePageCallback;
	}
	
	public Toolbar getToolbar()
	{
		if(toolbar!=null)
		{
			return toolbar;
		}
		return null;
	} 
	
	public void hideToolbar()
	{
		if(toolbar!=null)
		{
			toolbar.setVisibility(View.GONE);
			toolbar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
		}
	}
	
	public void showToolbar()
	{
		if(toolbar!=null)
		{
			toolbar.setVisibility(View.VISIBLE);
			toolbar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
		}
	}
	
	public void hideBottomView()
	{
		if(bottom_view!=null)
		{
			bottom_view.setVisibility(View.GONE);
			bottom_view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_bottom));
		}
	}
	
	public void showBottomView()
	{
		if(bottom_view!=null)
		{
			bottom_view.setVisibility(View.VISIBLE);
			bottom_view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_bottom));
		}
	}
	
	/**
	 * 显示toolbar和底部view
	 */
	public void showMenuView()
	{
		showToolbar();
		showBottomView();
	}
	
	public void hideMenuView()
	{
		hideToolbar();
		hideBottomView();
	}
	
	
	

	//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			String msg = "";
			switch (menuItem.getItemId()) {
			case R.id.action_favorite:
				msg += "Click favorite";
				break;
			case R.id.action_share:
				msg += "Click share";
				shareApp();
				break;
			}

			return true;
		}
	};
	
	private void shareApp() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"cjjtitle");
		intent.putExtra(Intent.EXTRA_TEXT,
				"cjjtext");
		Intent chooser = Intent.createChooser(intent,
			"银魂");
		startActivity(chooser);
	}
	
	/**
	 * 分享图片
	 */
	protected void sharePic() {
		ShareViewTask task = new ShareViewTask(this);
		task.setChooserTitle("分享图片");
		task.setExtraSubject("银魂'");
		task.setExtraText("cjj");
		task.setName("cjj2");
		task.execute(ComicImageFragment.instance.getViewpager());
	}


	@Override
	public void Size(int size) {
		this.size = size;
		seekBar.setMax(size);
		tv_tip_page.setText("1/"+size);
	}


	@Override
	public void currentPage(int page) {
		if(page != size)
		{
			tv_tip_page.setText(page+1+"/"+size);
			seekBar.setProgress(page+1);
		}
	}
	
}
