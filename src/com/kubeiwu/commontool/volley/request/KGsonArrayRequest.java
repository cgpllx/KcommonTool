package com.kubeiwu.commontool.volley.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class KGsonArrayRequest<T> extends KRequest<List<T>> {
	private final Gson gson = new Gson();

	public KGsonArrayRequest(String url, Map<String, String> headers, Listener<List<T>> listener, ErrorListener errorListener) {
		this(Method.GET, url, headers, headers, listener, errorListener);
	}

	public KGsonArrayRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Listener<List<T>> listener,
			ErrorListener errorListener) {
		super(method, url, headers, params, listener, errorListener);
	}      

	@Override
	protected Response<List<T>> parseNetworkResponse(NetworkResponse response) {
		List<T> list = new ArrayList<T>();
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			list = gson.fromJson(json, new TypeToken<List<T>>() {
			}.getType());
			return Response.success(list, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

}