package com.kubeiwu.commontool.volley.cache.memory;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-8
 */
public class LruMemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mLruCache;

    public LruMemoryCache() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 100;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
            protected int sizeOf(String key, Bitmap bitmap) {
            	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            		return bitmap.getByteCount() / 1024;
            	}else {
            		return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            	}
            }
        };

    }

    @Override
    public Bitmap getBitmap(String key) {
        return mLruCache.get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
            mLruCache.put(key, bitmap);
    }

	public void invalidateBitmap(String url) {
		mLruCache.remove(url);
	}

	public void clear() {
		mLruCache.evictAll();
	}
}