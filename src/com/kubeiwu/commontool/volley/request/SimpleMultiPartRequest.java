package com.kubeiwu.commontool.volley.request;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kubeiwu.commontool.volley.request.params.MultipartRequestParams;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-13
 */
public class SimpleMultiPartRequest extends MultiPartRequest<String> {

	public SimpleMultiPartRequest(int method, String url, Map<String, String> headers, MultipartRequestParams params, ErrorListener errorListener,
			Listener<String> listener) {
		super(method, url, headers, params, errorListener, listener);
	}

	public SimpleMultiPartRequest(String url, MultipartRequestParams params) {
		this(Method.POST, url, null, params, null, null);
	}
     
	public SimpleMultiPartRequest(String url, MultipartRequestParams params, ErrorListener errorListener, Listener<String> listene) {
		this(Method.POST, url, null, params, errorListener, listene);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(response));
		} catch (Exception je) {
			return Response.error(new ParseError(response));
		}
	}

}