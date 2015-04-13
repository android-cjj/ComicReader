package com.cjj.volley.custom.request;


import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.cjj.volley.AuthFailureError;
import com.cjj.volley.NetworkResponse;
import com.cjj.volley.ParseError;
import com.cjj.volley.Request;
import com.cjj.volley.Response;
import com.cjj.volley.Response.ErrorListener;
import com.cjj.volley.Response.Listener;
import com.cjj.volley.callback.CallBackStringDataListener;
import com.cjj.volley.toolbox.HttpHeaderParser;
import com.cjj.volley.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
/**
 * 
 * 自定义GsonRequest
 * @author cjj
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Listener<T> mListener;
	private final Map<String, String> mHeaders;
	private Map<String, String> mMap;
	private  CallBackStringDataListener callBackStringDataListener;
	
	public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.mClazz = clazz;
		this.mHeaders = headers;
		this.mListener = listener;
	}

	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener)
	{
		this(Method.GET, url, clazz, null, listener, errorListener);
	}
	
	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener,CallBackStringDataListener callBackStringDataListener)
	{
		this(Method.GET, url, clazz, null, listener, errorListener);
		this.callBackStringDataListener = callBackStringDataListener;
	}
	
	
	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener,Map<String, String> map)
	{
		this(Method.POST, url,clazz,null,listener, errorListener);
		this.mMap = map;
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError 
	{
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response)
	{
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			LogUtil.LogMsg_I("json="+json);
			if(callBackStringDataListener!=null)
			{
				callBackStringDataListener.callbackStringData(json);
			}
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
	
}
