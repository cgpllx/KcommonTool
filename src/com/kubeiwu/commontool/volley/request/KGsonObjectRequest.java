package com.kubeiwu.commontool.volley.request;

import java.io.UnsupportedEncodingException;
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

public class KGsonObjectRequest<T> extends KRequest<T> {
	private final Gson gson = new Gson();

	public KGsonObjectRequest(String url, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, headers, headers, listener, errorListener);
	}

	public KGsonObjectRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, headers, params, listener, errorListener);
	}


	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			T t = gson.fromJson(json, new TypeToken<T>() {
			}.getType());
			return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}