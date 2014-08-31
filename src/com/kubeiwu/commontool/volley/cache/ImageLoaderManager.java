//2014-8-7
package com.kubeiwu.commontool.volley.cache;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.kubeiwu.commontool.volley.cache.core.DisplayImageOptions;
import com.kubeiwu.commontool.volley.cache.core.DisplayImageOptions.ImageScaleType;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-7
 */
public class ImageLoaderManager {
	private RequestQueue mQueue;
	private BitmapCache mBitmapCache;
	private ImageLoader mImageLoader;

	public ImageLoaderManager(Context context) {
		mQueue = Volley.newRequestQueue(context);
		mBitmapCache = new BitmapCache(context);
		mImageLoader = new ImageLoader(mQueue, mBitmapCache);
	}

	public void displayImage(String uri, ImageView imageView) {
		this.displayImage(uri, imageView, DisplayImageOptions.createSimple());
	}

	public void displayImage(String uri, ImageView imageView, DisplayImageOptions option) {
		ImageListener listener = ImageLoader.getImageListener(imageView, option.getImageResForEmptyUri(), option.getImageResOnFail());
		switch (option.getImageScaleType()) {
		case ImageScaleType.NONE:
			mImageLoader.get(uri, listener, 0, 0);
			break;
		default:
			mImageLoader.get(uri, listener, imageView.getWidth(), imageView.getHeight());
			break;
		}
	}

	public void loadImage(String uri, ImageListener listener) {
		this.loadImage(uri, listener, 0, 0);
	}

	public void loadImage(String uri, ImageListener listener, int maxWidth, int maxHeight) {
		mImageLoader.get(uri, listener, maxWidth, maxHeight);
	}
}
