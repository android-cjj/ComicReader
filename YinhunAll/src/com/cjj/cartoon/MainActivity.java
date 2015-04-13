package com.cjj.cartoon;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.cjj.cartoon.fragment.MainFragment;
import com.cjj.cartoon.model.MousePaitedApplication;
import com.norbsoft.typefacehelper.ActionBarHelper;
import com.norbsoft.typefacehelper.TypefaceHelper;


public class MainActivity extends ActionBarActivity {
	private String link;
	private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        getIntentData();
       
        intiToolBar();
        
    	Fragment fragment = MainFragment.newInstance(link,title);
    	
		getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        
    }
    
    
    

	private void getIntentData() {
		Intent intent = getIntent();
		if(intent!=null)
		{
			link = intent.getStringExtra("link");
			title = intent.getStringExtra("title");
		}
	}




	private void intiToolBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(title);
//		toolbar.setLogo(R.drawable.app_in);
        setSupportActionBar(toolbar);
     // Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.setNavigationIcon(R.drawable.adward_navigationbar_icon_back_highlighted);
    	TypefaceHelper.typeface(toolbar);
	}



	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		  @Override
		  public boolean onMenuItemClick(MenuItem menuItem) {
		    String msg = "";
//		    switch (menuItem.getItemId()) {
//		      case R.id.action_edit:
//		        msg += "Click edit";
//		        break;
//		      case R.id.action_share:
//		        msg += "Click share";
//		        shareApp();
//		        break;
//		      case R.id.action_settings:
//		        msg += "Click setting";
//		        break;
//		    }
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
		    if(!msg.equals("")) {
		      Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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
			"cjjtitle2");
		startActivity(chooser);
	}
		
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
    	switch(item.getItemId())
    	{
    	case android.R.id.home:
    		this.finish();
    		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    		break;
    	}
        return super.onOptionsItemSelected(item);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	       return true;
	   }
	   return super.onKeyDown(keyCode, event);
	}
}
