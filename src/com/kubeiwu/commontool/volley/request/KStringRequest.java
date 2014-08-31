package com.kubeiwu.commontool.volley.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-11
 */
public class KStringRequest extends StringRequest {
	private final Map<String, String> headers;
	private final Map<String, String> params;

	public KStringRequest(String url, Listener<String> listener) {
		this(url, null, listener, null);
	}

	public KStringRequest(String url, Map<String, String> headers, Listener<String> listener, ErrorListener errorListener) {
		this(Method.GET, url, headers, null, listener, errorListener);
	}

	public KStringRequest(int method, String url, Map<String, String> params, Listener<String> listener) {
		this(method, url, null, params, listener, null);

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params != null ? params : super.getParams();
	}

	public KStringRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		this.headers = headers;
		this.params = params;
	}
}
