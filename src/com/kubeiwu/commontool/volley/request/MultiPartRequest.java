package com.kubeiwu.commontool.volley.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kubeiwu.commontool.volley.request.params.MultipartRequestParams;

/**
 * A request for making a Multi Part request
 * @param <T>
 * 
 * @param <T> Response expected
 */
public abstract class MultiPartRequest<T> extends KRequest<T> {

	private MultipartRequestParams params = null;
	private HttpEntity httpEntity = null;

	public MultiPartRequest(int method, String url, Map<String, String> headers, MultipartRequestParams params, ErrorListener errorListener,
			Listener<T> listener) {
		super(method, url, headers, null, listener, errorListener);
		this.params = params;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (params != null) {
			httpEntity = params.getEntity();
			try {
				httpEntity.writeTo(baos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	@Override
	public String getBodyContentType() {
		String str = httpEntity.getContentType().getValue();
		return str;
	}

 

}
