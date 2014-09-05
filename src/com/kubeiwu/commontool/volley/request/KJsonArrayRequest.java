package com.kubeiwu.commontool.volley.request;

import java.util.Map;

import org.json.JSONArray;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class KJsonArrayRequest extends JsonArrayRequest {

	private final Map<String, String> headers;
	private final Map<String, String> params;

	public KJsonArrayRequest(String url) {
		this(url, null, null, null, null);
	}

	public KJsonArrayRequest(String url, Listener<JSONArray> listener) {
		this(url, null, null, listener, null);
	}

	public KJsonArrayRequest(String url, Map<String, String> params) {
		this(url, null, params, null, null);
	}

	public KJsonArrayRequest(String url, Map<String, String> headers, Map<String, String> params, Listener<JSONArray> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
		this.headers = headers;
		this.params = params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params != null ? params : super.getParams();
	}
}
