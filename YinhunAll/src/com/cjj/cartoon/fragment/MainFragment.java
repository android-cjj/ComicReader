package com.cjj.cartoon.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.cjj.cartoon.ComicImageViewActivity;
import com.cjj.cartoon.R;
import com.cjj.cartoon.adapter.TwoListDataAdapter;
import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.custom.GridViewWithHeaderAndFooter;
import com.cjj.cartoon.dao.YinhunDao;
import com.cjj.cartoon.model.ImageAndTitle;
import com.cjj.loading.LoadingCjjLayout;
import com.cjj.volley.utils.LogUtil;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainFragment extends Fragment implements OnScrollListener,OnItemClickListener,OnRefreshListener {
	private GridViewWithHeaderAndFooter gv_data;
	private TwoListDataAdapter mAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private View loadmoreView;
	private int pageIndex = 1;
	private ListDataAsyncTask task;
	private ArcMenu arcMenu;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private String link;
	private String title;
	private LoadingCjjLayout loadingView;
	public static MainFragment newInstance(String link,String title) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putString("link", link);
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(savedInstanceState!=null)
		{
			result = savedInstanceState.getParcelableArrayList("result");
		}
		
		Bundle bundle = getArguments();
		if(bundle!=null)
		{
			link = bundle.getString("link");
			title = bundle.getString("title");
			LogUtil.LogMsg_I("link="+link);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("result", result);
		super.onSaveInstanceState(outState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, null);
		loadmoreView = inflater.inflate(R.layout.view_load_more, null);
		loadingView = new LoadingCjjLayout(getActivity(), v);
		
		return loadingView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		gv_data = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gv_data);
		gv_data.addFooterView(loadmoreView);
		gv_data.setOnScrollListener(this);
		gv_data.setOnItemClickListener(this);
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources( android.R.color.holo_orange_dark);
		
		arcMenu = (ArcMenu) view.findViewById(R.id.arc_menu);
		arcMenu.initSounds(getActivity());
		initArcMenu(arcMenu, Constant.ITEM_ARC);
		super.onViewCreated(view, savedInstanceState);
	}

	private void initArcMenu(final ArcMenu arcMenu2, int[] itemArc) {
		 final int itemCount = itemArc.length;
	        for (int i = 0; i < itemCount; i++) {
	            ImageView item = new ImageView(getActivity());
	            item.setImageResource(itemArc[i]);

	            final int position = i;
	            arcMenu2.addItem(item, new OnClickListener() {

	                @Override
	                public void onClick(View v) {
	                    Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
	                    arcMenu2.playSound(2, 0, getActivity());
	                }
	            });
	        }		
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		loadingView.show_LoadingView();
		task = new ListDataAsyncTask();
		task.execute(pageIndex);
		hideFootView();
		super.onActivityCreated(savedInstanceState);
	}

	 ArrayList<ImageAndTitle> result = null;
	private ArrayList<ImageAndTitle> getDataFromNet(int page) {
		try {
//			result = YinhunDao.getInstance().getComicListData("http://www.ishuhui.com/archives/category/zaixianmanhua/gintama/page/"+page);
		
//			result = YinhunDao.getInstance().getComicListData(link+"/page/"+page);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static ArrayList<ImageAndTitle> list_data;
	private class ListDataAsyncTask extends
			AsyncTask<Integer, Void, ArrayList<ImageAndTitle>> {
		
		ArrayList<ImageAndTitle> results = null;
		@Override
		protected void onPostExecute(ArrayList<ImageAndTitle> result) {
			
			if(result!=null&&result.size()>0)
			{
				
				
				if(mAdapter==null)
				{
					list_data = result;
					mAdapter = new TwoListDataAdapter(getActivity(), list_data);
					gv_data.setAdapter(mAdapter);
				}else
				{
					if(isLoadMore)
					{
						if(result.equals(results))
							return;
						
						results = result;
						
						list_data.addAll(result);
						mAdapter.updateData(list_data);
					}
					
					if(isRefresh)
					{
						list_data = result;
//						Toast.makeText(getActivity(), "刷新成功", 1000).show();
						showCustomViewCrouton();
						mAdapter.updateData(list_data);
					}
					
				}
				
				reSetting();
				loadingView.show_ContentView();
			}else{
				reSetting();
				pageIndex = pageIndex-1;
			}
			
		}

		@Override
		protected ArrayList<ImageAndTitle> doInBackground(Integer... params) {
			ArrayList<ImageAndTitle> result = null;
			try {
				result = YinhunDao.getInstance().getComicListData(link+"/page/"+params[0]);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

	}
	
	public void reSetting()
	{
		hideFootView();
		isRefresh = false;
		isLoadMore = false;
		if(swipeRefreshLayout.isRefreshing())
		{
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		ImageAndTitle model = (ImageAndTitle) mAdapter.getItem(position);
		
		Intent intent = new Intent(getActivity(), ComicImageViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", model.link);
		bundle.putString("title", model.title);
		intent.putExtras(bundle);
		startActivity(intent);
		
		getActivity().overridePendingTransition(R.anim.right,R.anim.hold);//切换Activity的过渡动画  

	}

	private boolean isRefresh = false;
	@Override
	public void onRefresh() {
		
		isRefresh = true;
		mListViewPreLast = 0;
		pageIndex = 1;
		new ListDataAsyncTask().execute(pageIndex);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 处理listView加载更多
	 * @param view
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 * @param totalItemCount
	 */
	private int mListViewPreLast = 0;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		final int lastItem = firstVisibleItem + visibleItemCount;
		
		if (lastItem == totalItemCount) {
			if (mListViewPreLast != lastItem) { /**防止多次调用*/
				getMoreData();
				mListViewPreLast = lastItem;
			}
		}
	}
	
	private boolean isLoadMore = false;
	public void getMoreData()
	{
		showFootView();
		isLoadMore = true;
		pageIndex +=1;
		new ListDataAsyncTask().execute(pageIndex);
	}
	
	/**
	 * show footview
	 */
	private void showFootView()
	{
		if(loadmoreView!=null)
		{
			loadmoreView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * hide footView
	 */
	private void hideFootView()
	{
		if(loadmoreView!=null)
		{
			loadmoreView.setVisibility(View.GONE);
		}
	}
	
	public void showCustomViewCrouton()
	{
		if(getActivity()!=null&&!getActivity().isFinishing()){
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.crouton_custom_view, null);
		 Crouton 
//		if (displayOnTop.isChecked()) {
//			crouton = Crouton.make(this, view);
//		} else {
			crouton = Crouton.make(getActivity(), view,
					R.id.view_line);
//		}
		crouton.show();}
	}
	

}
