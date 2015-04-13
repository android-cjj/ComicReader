package com.cjj.volley.callback;

import com.cjj.volley.VolleyError;

/**
 * 数据回调接口
 * @author cjj
 *
 */
public interface CallBackDataListener {
	
	public void callBack(Object data);
	
	public void error(VolleyError error);
}
