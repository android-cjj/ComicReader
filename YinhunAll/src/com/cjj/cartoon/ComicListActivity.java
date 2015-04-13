package com.cjj.cartoon;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cjj.callback.ReLoadCallbackListener;
import com.cjj.cartoon.adapter.HomeListComicAdapter;
import com.cjj.cartoon.custom.TargetImageLayout;
import com.cjj.cartoon.dao.YinhunDao;
import com.cjj.cartoon.model.HeadViewDataModel;
import com.cjj.cartoon.model.LocalImageModel;
import com.cjj.cartoon.model.MousePaitedApplication;
import com.cjj.loading.LoadingCjjLayout;
import com.cjj.staggeredgridview.StaggeredGridView;
import com.cjj.volley.utils.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshStaggeredGridView;
import com.norbsoft.typefacehelper.ActionBarHelper;
import com.norbsoft.typefacehelper.TypefaceHelper;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class ComicListActivity extends ActionBarActivity implements ReLoadCallbackListener{
	private PullToRefreshListView mPullToRefreshListView;
	private ListView lv;
	private PullToRefreshStaggeredGridView ptrstgv;
	private TargetImageLayout headView;
	private HomeListComicAdapter mAdapter;
	private ArrayList<LocalImageModel> list;
	private StaggeredGridView staggeredGridView;
	private View contentView;
	private LoadingCjjLayout loadingView;
	private boolean isRefresh = false;
	private boolean isReloadData = false;
	private LinearLayout view_line;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**init loadingView*/
		initLoadView();
		
		setContentView(loadingView);
		
		loadingView.show_LoadingView();
		
		intiToolBar();
		
		ActionBarHelper.setTitle(getSupportActionBar(),  TypefaceHelper.typeface(this, R.string.app_name2,MousePaitedApplication.instance.getRobotoTypeface(),Typeface.NORMAL));
	
		view_line = (LinearLayout)findViewById(R.id.view_line);
		ptrstgv = (PullToRefreshStaggeredGridView) findViewById(R.id.ptrstgv);
		staggeredGridView = ptrstgv.getRefreshableView();
		ptrstgv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		headView = new TargetImageLayout(this);
		
		/**
		 * 获取头部信息数据
		 */
		getHeadViewData();
		
		ptrstgv.getRefreshableView().setHeaderView(headView);
		
		
		ptrstgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StaggeredGridView>() {
			@Override
			public void onRefresh(
					PullToRefreshBase<StaggeredGridView> refreshView) {
				refreshData();
			}
		});
		
		
		list = new ArrayList<LocalImageModel>();

		LocalImageModel model = new LocalImageModel();
		model.url = "huoying.jpeg";
		model.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/naruto";
		model.title = "火影忍者\n(Naruto)";
		model.width = 400;
		model.height = 484;
		list.add(model);

	

		LocalImageModel model4 = new LocalImageModel();
		model4.url = "yinshi.jpg";
		model4.title = "银魂\n(Gintama)";
		model4.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/gintama";
		model4.width = 400;
		model4.height = 580;
		list.add(model4);

		LocalImageModel model5 = new LocalImageModel();
		model5.url = "haizeiwang.jpg";
		model5.title = "海贼王\n(One Piece)";
		model5.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/op";
		model5.width = 360;
		model5.height = 480;
		list.add(model5);
		
		LocalImageModel model12 = new LocalImageModel();
		model12.url = "xuejiufayuan.jpg";
		model12.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/fating";
		model12.title = "学纠法院\n(Court)";
		model12.width = 400;
		model12.height = 579;
		list.add(model12);

		
		LocalImageModel model3 = new LocalImageModel();
		model3.url = "heizi.jpg";
		model3.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/%E9%BB%91%E5%AD%90%E7%9A%84%E7%AF%AE%E7%90%83-zaixianmanhua";
		model3.title = "黑子的篮球\n(Kuroko's Basketball)";
		model3.width = 400;
		model3.height = 572;
		list.add(model3);
		
		LocalImageModel model2 = new LocalImageModel();
		model2.url = "dajian.jpg";
		model2.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/cm";
		model2.title = "大剑\n(Great Sword)";
		model2.width = 400;
		model2.height = 372;
		list.add(model2);


		LocalImageModel model6 = new LocalImageModel();
		model6.url = "jinjidejuren.jpg";
		model6.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/aot";
		model6.title = "进击的巨人\n(Attack on Titan)";
		model6.width = 400;
		model6.height = 515;
		list.add(model6);

		LocalImageModel model7 = new LocalImageModel();
		model7.url = "jinjidezhongxue.jpg";
		model7.link ="http://www.ishuhui.com/archives/category/zaixianmanhua/school";
		model7.title = "进击的中学\n(Secondary attack)";
		model7.width = 400;
		model7.height = 320;
		list.add(model7);

		LocalImageModel model8 = new LocalImageModel();
		model8.url = "meishidefulu.jpg";
		model8.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/toriko";
		model8.title = "美食的俘虏\n(Food captives)";
		model8.width = 381;
		model8.height = 500;
		list.add(model8);

		LocalImageModel model9 = new LocalImageModel();
		model9.url = "paiqiu.jpeg";
		model9.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/paiqiu";
		model9.title = "排球少年\n(Junior Volleyball)";
		model9.width = 400;
		model9.height = 590;
		list.add(model9);

		LocalImageModel model10 = new LocalImageModel();
		model10.url = "qizayouxi.jpg";
		model10.title = "欺诈游戏\n(Liar Game)";
		model10.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/liargame";
		model10.width = 400;
		model10.height = 329;
		list.add(model10);

		LocalImageModel model11 = new LocalImageModel();
		model11.url = "quanzhilieren.jpg";
		model11.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/hunter";
		model11.title = "全职猎人\n(HUNTER)";
		model11.width = 400;
		model11.height = 533;
		list.add(model11);

		LocalImageModel model13 = new LocalImageModel();
		model13.url = "yaojingdeweiba.jpg";
		model13.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/ft";
		model13.title = "妖精的尾巴\n(Fairy Tail)";
		model13.width = 327;
		model13.height = 500;
		list.add(model13);

		LocalImageModel model14 = new LocalImageModel();
		model14.url = "yaren.jpg";
		model14.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/yaren";
		model14.title = "亚人\n(Asian people)";
		model14.width = 400;
		model14.height = 537;
		list.add(model14);
		
		LocalImageModel model15 = new LocalImageModel();
		model15.url = "yzdtk.jpg";
		model15.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/area";
		model15.title = "鸭子的天空\n(King Junior Basketball)";
		model15.width = 400;
		model15.height = 600;
		list.add(model15);
		
		
		LocalImageModel model16 = new LocalImageModel();
		model16.url = "chufa.jpg";
		model16.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/wt";
		model16.title = "境界触发者\n(World Trigger)";
		model16.width = 400;
		model16.height = 600;
		list.add(model16);
		
		LocalImageModel model17 = new LocalImageModel();
		model17.url = "wdyxxy.jpg";
		model17.link = "http://www.ishuhui.com/archives/category/zaixianmanhua/hero";
		model17.title = "我的英雄学院\n(My Hero Academy)";
		model17.width = 400;
		model17.height = 400;
		list.add(model17);
		
		
		mAdapter = new HomeListComicAdapter(ComicListActivity.this, list);
		ptrstgv.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	private void initLoadView() {
		contentView = getLayoutInflater().inflate(R.layout.activity_comic_list, null);
		loadingView = new LoadingCjjLayout(this, contentView);
		loadingView.setReLoadCallbackListener(this);
	}

	private void getHeadViewData() {
		if(isReloadData)
		{
			isReloadData = false;
		}else{
			loadingView.show_LoadingView();
		}
		new HeadViewDataAsycTask().execute();
	}
	
	public void refreshData()
	{
		isRefresh = true;
		getHeadViewData();
	}

	private void intiToolBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Gintama");
//		toolbar.setLogo(R.drawable.app_in);
        setSupportActionBar(toolbar);
     // Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		  @Override
		  public boolean onMenuItemClick(MenuItem menuItem) {
		    switch (menuItem.getItemId()) {
		      case R.id.action_refresh:
		        /**刷新获取数据*/
		    	staggeredGridView.setSelectionToTop();
		        ptrstgv.setRefreshing(true);
		        break;
//		      case R.id.action_share:
//		        msg += "Click share";
//		        shareApp();
//		        break;
//		      case R.id.action_settings:
//		        msg += "Click setting";
//		        break;
		    }
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
//		    if(!msg.equals("aaaa")) {
//		      Toast.makeText(ComicListActivity.this, msg, Toast.LENGTH_SHORT).show();
//		      ptrstgv.setRefreshing(true);
//		    }
		    return true;
		  }
		};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * async get data
	 * @author cjj
	 *
	 */
	private class HeadViewDataAsycTask extends AsyncTask<Void, Void, ArrayList<HeadViewDataModel>>
	{
		ArrayList<HeadViewDataModel> dataHeadView ;
		@Override
		protected ArrayList<HeadViewDataModel> doInBackground(Void... params) {
			try {
				dataHeadView = YinhunDao.getInstance().getHeadViewDataFromNet();
				return dataHeadView;
			} catch (Exception e) {
				LogUtil.LogMsg_I("cjj===>====>"+e);
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<HeadViewDataModel> result) {
			if(dataHeadView!=null)
			{
				LogUtil.LogMsg_I("cjj===>====>1");
				headView.setListImageData(ComicListActivity.this,result);
				loadingView.show_ContentView();
				//初始化  
				Animation translateAnimation = new TranslateAnimation(0.0f,0.0f,-200f,0.0f);  
				//设置动画时间    
				translateAnimation.setDuration(1000);  
				headView.startAnimation(translateAnimation);
				if(isRefresh)
				{
					toastMsg();
				}
			}else
			{
				LogUtil.LogMsg_I("cjj===>====>2");
				loadingView.show_FailView();
			}
			isRefresh = false;
			ptrstgv.onRefreshComplete();
			super.onPostExecute(result);
		}
		
	}


	@Override
	public void onReLoadCallback() {
		isReloadData = true;
		loadingView.show_reloadingView();
		getHeadViewData();
	}
	
	/**
	 *刷新成功提示 
	 */
	public void toastMsg()
	{
		showCustomViewCrouton();
	}
	
	public void showCustomViewCrouton()
	{
		View view = ComicListActivity.this.getLayoutInflater().inflate(
				R.layout.crouton_custom_view, null);
		final Crouton crouton;
//		if (displayOnTop.isChecked()) {
//			crouton = Crouton.make(this, view);
//		} else {
			showLLView();
			crouton = Crouton.make(this, view,
					R.id.view_line);
//		}
		crouton.show();
	}
	
	public void showLLView()
	{
		if(view_line!=null)
		{
//			if(view_line.getVisibility()==View.VISIBLE)
//			{
//				view_line.setVisibility(View.GONE);
//			}else
//			{
				view_line.setVisibility(View.VISIBLE);
//			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mAdapter!=null)
		{
			LogUtil.LOgMsg_W("ondestroy1");
			mAdapter.recycleAdapterResource();
			mAdapter = null;
			System.gc();
		}
		LogUtil.LOgMsg_W("ondestroy");
		super.onDestroy();
	}

}
