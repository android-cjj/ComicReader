package com.cjj.volley.custom.request;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.cjj.volley.NetworkResponse;
import com.cjj.volley.ParseError;
import com.cjj.volley.Request;
import com.cjj.volley.Response;
import com.cjj.volley.Response.ErrorListener;
import com.cjj.volley.Response.Listener;
import com.cjj.volley.toolbox.HttpHeaderParser;
/**
 * 自定义XMLRequest
 * @author cjj
 *
 */
public class XMLRequest extends Request<XmlPullParser> {

	private final Listener<XmlPullParser> mListener;

	public XMLRequest(int method, String url, Listener<XmlPullParser> listener,
			ErrorListener errorListener) 
	{
		super(method, url, errorListener);
		mListener = listener;
	}

	public XMLRequest(String url, Listener<XmlPullParser> listener,
			ErrorListener errorListener)
	{
		this(Method.GET, url, listener, errorListener);
	}

	@Override
	protected Response<XmlPullParser> parseNetworkResponse(
			NetworkResponse response) 
	{
		try {
			String xmlString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlString));
			return Response.success(xmlPullParser,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (XmlPullParserException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(XmlPullParser response) 
	{
		mListener.onResponse(response);
	}

}